#!/bin/bash

# 停止容器
docker stop thirddata

# sleep 2s

# 删除容器
docker rm thirddata

# 删除镜像
docker rmi thirddata:1.0

# 构建镜像 . 号表示在当前目录构建
docker build -t thirddata:1.0 .

# 创建容器并启动
docker run -d -p 21000:21000 --net=host --name thirddata   -v /root/logs:/usr/logs thirddata:1.0
