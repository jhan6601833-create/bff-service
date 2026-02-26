# bff-base-component

该模块提供常用中间件的基础自动配置，目标是让业务模块按需接入：

- MySQL / DataSource
- MyBatis Plus 分页拦截器
- Redis 通用 `RedisTemplate<String, Object>`
- RabbitMQ JSON 消息转换与 `RabbitTemplate` 默认行为
- Kafka 生产者监听器

## 开关配置

```yaml
bff:
  component:
    mysql:
      enabled: true
    mybatis:
      enabled: true
    redis:
      enabled: true
    rabbitmq:
      enabled: true
    kafka:
      enabled: true
```

## 说明

- 所有配置均使用 `@ConditionalOnMissingBean`，业务模块可通过自定义 Bean 覆盖默认实现。
- 默认配置偏向通用与安全（例如 RabbitMQ 强制开启 mandatory，Redis 使用 JSON 序列化）。
