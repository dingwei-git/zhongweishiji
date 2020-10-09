#!/bin/bash

# 停止容器
docker stop workorder

# sleep 2s

# 删除容器
docker rm workorder

# 删除镜像
docker rmi workorder:1.0

# 构建镜像 . 号表示在当前目录构建
docker build -t workorder:1.0 .

# 创建容器并启动
docker run -d -p 20000:20000 --net=host --name workorder -v /opt/order/images:/opt/order/images    -v /root/logs:/usr/logs workorder:1.0
