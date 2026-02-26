package com.bjdj.utils;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 词语相似度匹配系统 - 通用版本
 * 不依赖预定义词汇，使用通用算法处理词序变化
 */
public class WordSimilarityMatcher {

    private static final Pattern NORMALIZE_PATTERN = Pattern.compile("\\p{M}");
    private static final Set<String> STOP_WORDS = Set.of("的", "了", "在", "是", "和", "与", "及", "或");

    /**
     * 相似度结果类
     */
    public record SimilarityResult(double score, String strategy, String details) {

        @Override
        public String toString() {
            return String.format("Score: %.3f, Strategy: %s, Details: %s", score, strategy, details);
        }
    }

    /**
     * 综合相似度计算
     */
    public SimilarityResult calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return new SimilarityResult(0.0, "NULL_CHECK", "输入为空");
        }

        // 预处理
        String normalized1 = normalizeText(text1);
        String normalized2 = normalizeText(text2);

        // 完全匹配
        if (normalized1.equals(normalized2)) {
            return new SimilarityResult(1.0, "EXACT_MATCH", "完全匹配");
        }

        // 计算各种相似度
        double tokenSimilarity = calculateTokenSimilarity(text1, text2);
        if (tokenSimilarity == 1.0) {
            return new SimilarityResult(1.0, "EXACT_TOKEN_MATCH", "词元完全匹配（顺序可变）");
        }
        double editDistance = calculateEditDistanceSimilarity(normalized1, normalized2);
        double jaccardSimilarity = calculateJaccardSimilarity(normalized1, normalized2);
        double structuralSimilarity = calculateStructuralSimilarity(text1, text2);

        // 加权计算最终相似度 - 重点关注词元匹配
        double finalScore = (tokenSimilarity * 0.7)
                + (editDistance * 0.1)
                + (jaccardSimilarity * 0.1)
                + (structuralSimilarity * 0.1);

        String details = String.format(
                "Token:%.3f, Edit:%.3f, Jaccard:%.3f, Struct:%.3f",
                tokenSimilarity, editDistance, jaccardSimilarity, structuralSimilarity);

        return new SimilarityResult(finalScore, "COMPOSITE", details);
    }

    /**
     * 文本标准化
     */
    private String normalizeText(String text) {
        if (text == null) return "";

        // 转换为小写
        text = text.toLowerCase();

        // 移除标点符号和空格
        text = text.replaceAll("[\\p{Punct}\\s]+", "");

        // Unicode标准化
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = NORMALIZE_PATTERN.matcher(text).replaceAll("");

        return text;
    }

    /**
     * 基于词元的相似度计算 - 核心算法
     */
    private double calculateTokenSimilarity(String text1, String text2) {
        List<String> tokens1 = tokenize(text1);
        List<String> tokens2 = tokenize(text2);

        // 移除停用词
        tokens1 = tokens1.stream()
                .filter(t -> !STOP_WORDS.contains(t) && !t.trim().isEmpty())
                .collect(Collectors.toList());
        tokens2 = tokens2.stream()
                .filter(t -> !STOP_WORDS.contains(t) && !t.trim().isEmpty())
                .collect(Collectors.toList());

        if (tokens1.isEmpty() || tokens2.isEmpty()) {
            return 0.0;
        }

        // 转换为集合进行比较
        Set<String> set1 = new HashSet<>(tokens1);
        Set<String> set2 = new HashSet<>(tokens2);

        // 计算交集
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // 如果两个集合完全相同，返回1.0
        if (set1.equals(set2)) {
            return 1.0;
        }
        // 如果排序后列表完全相同，也返回1.0（顺序敏感场景）
        List<String> sorted1 = new ArrayList<>(tokens1);
        List<String> sorted2 = new ArrayList<>(tokens2);
        Collections.sort(sorted1);
        Collections.sort(sorted2);
        if (sorted1.equals(sorted2)) {
            return 1.0;
        }

        // 计算Jaccard相似度作为基础分数
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }

    /**
     * 通用分词算法
     */
    private List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return tokens;
        }

        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);

            if (Character.isDigit(c)) {
                // 提取数字序列
                StringBuilder sb = new StringBuilder();
                while (i < text.length() && Character.isDigit(text.charAt(i))) {
                    sb.append(text.charAt(i));
                    i++;
                }
                tokens.add(sb.toString());
            } else if (Character.isLetter(c)) {
                // 提取字母序列
                StringBuilder sb = new StringBuilder();
                while (i < text.length() && Character.isLetter(text.charAt(i))) {
                    sb.append(text.charAt(i));
                    i++;
                }
                tokens.add(sb.toString());
            } else if (isChinese(c)) {
                // 中文字符处理
                tokens.addAll(extractChineseTokens(text, i));
                // 跳过已处理的中文字符
                while (i < text.length() && isChinese(text.charAt(i))) {
                    i++;
                }
            } else {
                // 跳过标点符号和空格
                i++;
            }
        }

        return tokens;
    }

    /**
     * 判断是否为中文字符
     */
    private boolean isChinese(char c) {
        return c >= 0x4e00 && c <= 0x9fff;
    }

    /**
     * 提取中文词汇
     */
    private List<String> extractChineseTokens(String text, int start) {
        List<String> tokens = new ArrayList<>();

        // 找到连续中文字符的结束位置
        int end = start;
        while (end < text.length() && isChinese(text.charAt(end))) {
            end++;
        }

        String chineseSeq = text.substring(start, end);

        // 使用多种长度的滑动窗口进行分词
        Set<String> candidateTokens = new HashSet<>();

        // 单字符
        for (int i = 0; i < chineseSeq.length(); i++) {
            candidateTokens.add(chineseSeq.substring(i, i + 1));
        }

        // 双字符
        for (int i = 0; i < chineseSeq.length() - 1; i++) {
            candidateTokens.add(chineseSeq.substring(i, i + 2));
        }

        // 三字符（如果长度足够）
        if (chineseSeq.length() >= 3) {
            for (int i = 0; i < chineseSeq.length() - 2; i++) {
                candidateTokens.add(chineseSeq.substring(i, i + 3));
            }
        }

        // 四字符（如果长度足够）
        if (chineseSeq.length() >= 4) {
            for (int i = 0; i < chineseSeq.length() - 3; i++) {
                candidateTokens.add(chineseSeq.substring(i, i + 4));
            }
        }

        // 优先选择较长的词汇
        tokens.addAll(candidateTokens.stream()
                .sorted((a, b) -> Integer.compare(b.length(), a.length()))
                .collect(Collectors.toList()));

        return tokens;
    }

    /**
     * 编辑距离相似度
     */
    private double calculateEditDistanceSimilarity(String s1, String s2) {
        int distance = levenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());
        return maxLength == 0 ? 1.0 : 1.0 - (double) distance / maxLength;
    }

    /**
     * Levenshtein距离计算
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     * Jaccard相似度计算
     */
    private double calculateJaccardSimilarity(String s1, String s2) {
        Set<String> set1 = getNGrams(s1, 2);
        Set<String> set2 = getNGrams(s2, 2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    /**
     * 获取N-gram
     */
    private Set<String> getNGrams(String text, int n) {
        Set<String> ngrams = new HashSet<>();
        if (text.length() >= n) {
            for (int i = 0; i <= text.length() - n; i++) {
                ngrams.add(text.substring(i, i + n));
            }
        }
        return ngrams;
    }

    /**
     * 结构相似度计算
     */
    private double calculateStructuralSimilarity(String s1, String s2) {
        // 提取数字
        String numbers1 = s1.replaceAll("[^0-9]", "");
        String numbers2 = s2.replaceAll("[^0-9]", "");

        // 如果都没有数字，认为结构相似
        if (numbers1.isEmpty() && numbers2.isEmpty()) {
            return 1.0;
        }

        // 如果只有一个有数字，结构不同
        if (numbers1.isEmpty() || numbers2.isEmpty()) {
            return 0.0;
        }

        // 如果数字相同，结构相似
        if (numbers1.equals(numbers2)) {
            return 1.0;
        }

        // 数字不同，结构不同
        return 0.0;
    }

    /**
     * 批量相似度计算
     */
    public List<SimilarityResult> batchCalculate(List<String> group1, List<String> group2) {
        List<SimilarityResult> results = new ArrayList<>();

        for (String text1 : group1) {
            for (String text2 : group2) {
                SimilarityResult result = calculateSimilarity(text1, text2);
                results.add(result);
            }
        }

        return results;
    }

    /**
     * 主方法 - 演示用法
     */
    public static void main(String[] args) {
        WordSimilarityMatcher matcher = new WordSimilarityMatcher();

        // 测试用例
        String[][] testCases = {
            {"牛排啤酒套餐", "【重磅推出】啤酒牛排套餐"},
            {"1", "1"}
        };

        System.out.println("=== 词语相似度匹配测试 ===\n");

        for (String[] testCase : testCases) {
            String text1 = testCase[0];
            String text2 = testCase[1];

            SimilarityResult result = matcher.calculateSimilarity(text1, text2);

            System.out.printf("文本1: %s%n", text1);
            System.out.printf("文本2: %s%n", text2);
            System.out.printf("相似度: %.3f%n", result.score());
            System.out.printf("策略: %s%n", result.strategy());
            System.out.printf("详情: %s%n", result.details());
            System.out.println("判断: " + (result.score() > 0.7 ? "相似" : "不相似"));
            System.out.println("----------------------------------------\n");
        }
    }
}
