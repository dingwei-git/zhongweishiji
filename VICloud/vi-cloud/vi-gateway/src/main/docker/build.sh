#!/bin/bash

# 停止容器
docker stop gateway

# sleep 2s

# 删除容器
docker rm gateway

# 删除镜像
docker rmi gateway:1.0

# 构建镜像 . 号表示在当前目录构建
docker build  -t gateway:1.0 .

# 创建容器并启动
docker run -d -p 30000:30000 --net=host --name gateway   -v /root/logs:/usr/logs gateway:1.0
