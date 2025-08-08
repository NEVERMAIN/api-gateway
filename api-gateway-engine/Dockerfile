# 基础镜像
FROM eclipse-temurin:17-jdk-alpine

# 镜像作者信息（可选）
LABEL maintainer="VOY-R7"

# 环境变量配置
ENV JAVA_OPTS=""

ENV PARAMS=""

ENV TZ=Asia/Shanghai

# 创建应用目录
WORKDIR /app

# 拷贝 JAR 文件到容器中（注意文件名匹配）
COPY api-gateway-engine.jar app.jar

# 暴露端口（根据你的应用监听端口来配置，Spring Boot 默认是 8080）
EXPOSE 8007 7397

# 启动命令，支持额外传入 JVM 参数
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar $PARAMS"]