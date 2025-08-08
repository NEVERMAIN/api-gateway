#!/bin/bash
# 普通镜像构建，随系统版本构建 amd/arm
docker build -t system/api-gateway-center-app:1.0.1 -f ./Dockerfile .