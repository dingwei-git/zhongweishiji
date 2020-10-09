#!/bin/bash

# 停止容器
docker stop login

# sleep 2s

# 删除容器
docker rm login

# 删除镜像
docker rmi login:1.0

# 构建镜像 . 号表示在当前目录构建
docker build -t login:1.0 .

# 创建容器并启动
docker run -d -p 20001:20001 --net=host --name login    -v /root/logs:/usr/logs login:1.0
