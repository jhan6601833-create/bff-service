package com.bjdj.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author: yulong.zhang
 * @date: 2025/4/10
 * @description:
 */
public class TraceUtils {
    private static final String SEPARATOR = "-";

    /**
     * 获取traceId
     *
     * @return traceId
     */
    public static String acquire(long batch, ConsumerRecord<String, ?> record) {
        return batch
                + SEPARATOR
                + record.topic()
                + SEPARATOR
                + record.partition()
                + SEPARATOR
                + record.offset()
                + SEPARATOR
                + record.timestamp()
                + SEPARATOR
                + record.key();
    }
}
