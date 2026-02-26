# BFF 聚合编排服务 



## 项目简介
本服务主要面向小程序 APP等端提供业务聚合能力 

##框架
- **Spring Boot 3.5.7** - 最新的 Spring Boot 框架
- **Apache Dubbo 3.3.6** - 高性能 RPC 框架，使用 Triple 协议
- **Nacos** - 服务注册与配置中心
- **MyBatis Plus** - 增强的 ORM 框架
- **MySQL** - 关系型数据库
- **Redis** - 缓存和分布式会话
- **Kafka** - 消息队列
- **RabbitMQ** - 消息队列
- **Java 17/21** - LTS 版本

## 项目结构

```
mini-bff-service/
├── bff-base   
├── ├── common  -- 通用常量，枚举...等
├── ├── component   -- 组件：mybatisplus封装，reids mq等组件封装...
├── ├── core   -- 工具包 
├── bff-miniapp   -- 提供对C端业务（可能不止针对小程序）
├── bff-admin     -- 提供对B端业务 主要应用于后台管理
├── dubbo-facade   -- dubbo 提供给内部服务调用

```

## 要求

### 命名要求
* Request入参： Params结尾
* Response返回：VO结尾
* 方法传参入参: DTO结尾

### 编写

* BASE模块禁止建POJO - 多模块之间应保持独立性
* 日期变量以LocalDate LocalDateTime LocalTime类型为准
* 对外请求需注意超时时间 避免把自己拉爆 默认2s
* 禁止长接口 编写代码时请理清自己的思路 及时拆分逻辑（步骤一） （步骤二）（步骤三）
* 禁止IFELSE嵌套 

## 技术特性

### 1. 模块化设计
- **demo-facade**: 定义服务接口和 DTO，作为服务消费者的依赖
- **demo-service**: 服务提供者，实现业务逻辑和 Dubbo 服务

### 2. Dubbo 配置
- 使用 **Triple 协议**（Dubbo 3 推荐协议，支持 gRPC 互通）
- 集成 **Nacos** 作为注册中心
- 支持服务预热和优雅启动
- 配置了饥饿加载，避免首次请求超时

### 3. 数据持久化
- 使用 **MyBatis Plus** 简化 CRUD 操作
- 集成 **MySQL** 作为主数据库
- 支持驼峰命名自动转换

### 4. 分布式能力
- **Redis** 缓存支持
- **Kafka** 消息队列
- **RabbitMQ** 消息队列

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- Docker（用于容器化部署）
- MySQL 8.0+
- Redis
- Nacos Server

### 本地开发

1. **克隆项目**
```bash
git clone <repository-url>
cd spring-boot-3x-demo
```

2. **配置数据库和中间件**

编辑 `demo-service/src/main/resources/application-local.yaml`，配置：
- MySQL 数据库连接
- Redis 连接信息
- Nacos 注册中心地址
- RabbitMQ/Kafka 连接（如需使用）

3. **编译项目**
```bash
mvn clean install
```

4. **运行服务**
```bash
cd demo-service
mvn spring-boot:run
```

5. **测试接口**
```bash
curl http://localhost:8081/test
```

## Docker 部署

### 1. 构建镜像

```bash
# 编译项目
mvn clean package -DskipTests

# 构建 Docker 镜像
docker build -t spring-boot-3x-demo:latest .
```

### 2. 运行容器

```bash
docker run -d \
  --name demo-service \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=local \
  -e JAVA_OPTS="-Xms512m -Xmx1024m" \
  spring-boot-3x-demo:latest
```

### 3. 查看日志

```bash
docker logs -f demo-service
```

## 配置说明

### application.yaml
主配置文件，定义活动的 profile：
```yaml
spring:
  profiles:
    active: local
```

### application-local.yaml
本地环境配置，包含：
- 服务端口：8081
- 数据库连接
- Redis 配置
- Dubbo 配置
- Nacos 注册中心

### Dubbo 配置要点

```yaml
dubbo:
  protocol:
    name: tri              # Triple 协议
    port: 50052
    serialization: protobuf
  registry:
    address: nacos://xxx   # Nacos 地址
  consumer:
    lazy: false            # 关闭懒加载
    init: true             # 饥饿加载，提前初始化
```

## API 示例

### REST API
- **GET** `/test` - 测试接口

### Dubbo 服务
```java
public interface TestServiceAFacade {
    TestRes methodTest(TestReq param);
    String methodTest2(String param);
}
```

## 开发指南

### 添加新的 Dubbo 服务

1. 在 `demo-facade` 模块定义接口和 DTO
2. 在 `demo-service` 模块实现接口
3. 使用 `@DubboService` 注解暴露服务
4. 配置相应的 group 和 version

### 添加新的 REST 接口

1. 在 `demo-service/controller` 包下创建 Controller
2. 使用 `@RestController` 注解
3. 定义请求映射和业务逻辑

## 常见问题

### 1. Dubbo 无法创建 .dubbo 目录
确保容器有足够的权限，或挂载外部卷：
```bash
docker run -v dubbo-data:/home/appuser/.dubbo ...
```

### 2. 服务启动后首次调用超时
配置文件中已设置 `lazy: false` 和 `init: true` 解决此问题

### 3. Nacos 注册失败
检查 Nacos 地址是否正确，网络是否可达

## 版本信息

- **Spring Boot**: 3.5.7
- **Dubbo**: 3.3.6
- **MyBatis Plus**: 3.5.12
- **Java**: 17/21

