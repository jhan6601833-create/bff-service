package com.bjdj;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.GeneratorBuilder;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import java.util.HashMap;
import java.util.Map;

public class MybatisGenerator {
    public static void main(String[] args) {

        String url =
                "jdbc:mysql://rm-0jl364z293k5va54cfo.rwlb.rds.aliyuncs.com:3306/base-info?autoReconnect=true&autoReconnectForPools=true&failOverReadOnly=false&allowMultiQueries=true&useSSL=false";
        String userName = "root";
        String pwd = "xiangyuan@0424";

        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(url, userName, pwd).build();
        GlobalConfig globalConfig = new GlobalConfig.Builder()
                .disableOpenDir()
                .author("bpzhang")
                .dateType(DateType.TIME_PACK)
                .build();
        Map<OutputFile, String> patchInfo = new HashMap<>();
        patchInfo.put(OutputFile.xml, System.getProperty("user.dir") + "/demo-service/src/main/resources/mapper");
        patchInfo.put(
                OutputFile.mapper, System.getProperty("user.dir") + "/demo-service/src/main/java/com/company/mapper");
        patchInfo.put(
                OutputFile.entity, System.getProperty("user.dir") + "/demo-service/src/main/java/com/company/entity");
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent("com.company")
                .pathInfo(patchInfo)
                .build();

        StrategyConfig.Builder strategyConfigBuilder = new StrategyConfig.Builder()
                .enableCapitalMode()
                .enableSkipView()
                .addTablePrefix("tbl_")
                .addInclude("tbl_shop_group_info");
        strategyConfigBuilder
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
                .enableFileOverride()
                .formatFileName("%sEntity")
                .enableChainModel()
                .enableTableFieldAnnotation()
                .enableLombok();
        strategyConfigBuilder
                .mapperBuilder()
                .enableFileOverride()
                .enableFileOverride()
                .enableBaseColumnList()
                .enableBaseResultMap();

        TemplateConfig disable =
                GeneratorBuilder.templateConfig().disable(TemplateType.CONTROLLER, TemplateType.SERVICE);
        AutoGenerator generator = new AutoGenerator(dataSourceConfig);
        generator.global(globalConfig);
        generator.template(disable);
        generator.packageInfo(packageConfig);
        generator.strategy(strategyConfigBuilder.build());
        generator.execute();
    }
}
