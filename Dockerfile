FROM prod-registry.cn-beijing.cr.aliyuncs.com/base/amazoncorretto:17.0.15


# Set working directory
WORKDIR /app

# Default environment variables
ENV SPRING_PROFILES_ACTIVE=local \
    JAVA_OPTS="" \
    TZ=Asia/Shanghai \
    PROJECT_NAME=demo-service
# Copy the JAR file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar


# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dproject.name=${PROJECT_NAME} -Duser.timezone=${TZ} /app/app.jar"]
