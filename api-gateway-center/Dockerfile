# 基础镜像，可以先执行 docker pull openjdk:17-jdk-slim
FROM openjdk:17-jdk-slim

# 作者
LABEL maintainer="VOY-R7"

# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
COPY api-gateway-center-app.jar /api-gateway-center-app.jar

# 声明端口
EXPOSE 8001

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /api-gateway-center-app.jar $PARAMS"]