<p align="center">
 <img src="https://img.shields.io/badge/VUE-2.5.21-green.svg" alt="Build Status">
  <img src="https://img.shields.io/badge/Spring%20Cloud-Greenwich.SR1-orange.svg" alt="Coverage Status">
  <img src="https://img.shields.io/badge/spring%20Boot-2.1.3.RELEASE-brightgreen.svg" alt="Downloads">
</p>  

# jaws

## 介绍
融视云人脸业务java开发平台

## 软件架构
软件架构说明

### 服务切分

``` lua
jaws
├── jaws-common -- 系统公共模块 
├── jaws-eureka -- 服务注册与发现
├── jaws-modules -- 微服务模块
├    ├── jaws-oas -- 开发平台服务
├    ├── jaws-map -- 地图服务服务
├    └── jaws-frs -- 人脸服务
├── jaws-public-service  -- 公共基础服务 
├    ├── jaws-pss -- 图片服务
├    ├── jaws-gateway -- 公用网管服务（未来规划）
├    ├── jaws-pss-api -- 图片存储服务API
├    ├── jaws-dms-api -- 设备管理API
├    └── jaws-uas-api -- 用户中心服务API
├── jaws-visual  -- 图形化模块 
├    ├── jaws-hystrix-dashboard
├    ├── jaws-monitor
├    ├── jaws-pinpoint
├    └── jaws-zipkin
```
### 功能列表


## 安装教程

### 0. 安装前提条件
     0.1. jdk请安装1.8版本
     0.2. 已部署UAS、DMS服务
     0.3. 已安装mysql数据库 , 并通知开发人员根据当前数据库地址和用户名密码进行程序打包
     0.4. 已安装nginx
     0.5. 已安装rabbitmq , 并通知开发人员根据调整连接的账号密码

### 1.目录结构参考(172.16.33.85环境)
``` lua
/(根目录)
├── opt(一级目录)
├    └── jaws(二级目录)
├         └── faces -- 图像存储路径(可在pss.yml配置文件上修改路径)
├              ├── image --设备上报图片存储路径
├              └── template --frs人员模板图片存储路径
├             eureka --Eureka注册中心
├              ├── jaws-eureka-1.0-SNAPSHOT.jar --eureka服务
├              ├── start.sh --启动脚本
├              ├── stop.sh --停止脚本
├              ├── check.sh --重启脚本
├              └── tpid --记录服务pid号使用(自动生成，无需部署)
├             frs --FRS人脸服务
├              ├── jaws-frs-1.0-SNAPSHOT.jar --frs服务
├              ├── frs.yml --frs配置文件
├              ├── start.sh --启动脚本
├              ├── stop.sh --停止脚本
├              ├── check.sh --重启脚本
├              ├── temp --人员批量上传zip解压目录(可定期清理)
├              └── tpid --记录服务pid号使用(自动生成，无需部署)
├             pss --PSS图片存储服务
├              ├── jaws-pss-1.0-SNAPSHOT.jar --pss服务
├              ├── pss.yml --pss配置文件
├              ├── start.sh --启动脚本
├              ├── stop.sh --停止脚本
├              ├── check.sh --重启脚本
├              └── tpid --记录服务pid号使用(自动生成，无需部署)
├── log  -- 日志目录 
├    └── jaws
├         ├── eureka --eureka注册中心日志(可定期清理)
├         ├── frs    --frs人脸服务日志(可定期清理)
├         └── pss    --pss图片存储服务日志(可定期清理)
```
### 2.人脸服务端口说明
    1.FRS 端口 20700
    2.EUREKA 端口 20701
    3.PSS 端口 20800

### 3.nginx配置项
```log
http {
    ......
    server {
        ......
        location ^~/v1/frs{
            proxy_connect_timeout 200;
            client_max_body_size 100m;
            proxy_pass http://127.0.0.1:20700/v1/frs;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header   X-Real-IP        $remote_addr;
        }
        location /faces/ {
            alias    /opt/jaws/faces/;
        }
        .......
    }
}
```
### 4. mysql部署说明
#### 4.1. mysql数据库增加或修改的配置项(my.conf配置文件)
```log
    ......
    max_binlog_cache_size =500M
    max_binlog_size = 500M
    group_concat_max_len = 18446744073709551615
    ......
```
#### 4.2. mysql建库脚本(请联系开发人员)



公安厅版本sql变化

```sql
#1.人脸识别记录表追加人员类别字段
ALTER TABLE `t_frs_contrast_record` ADD COLUMN `person_type` INT(11) NOT NULL COMMENT '人员类别' AFTER `face_template`;
#2.人员表追加人员类别字段,并默认类别'1'
ALTER TABLE `t_frs_user` ADD COLUMN `type` INT(11) NOT NULL DEFAULT '1' COMMENT '人员类别(依据人员类别表)' AFTER `name`;
#3.人员部门表删除部门编号字段
ALTER TABLE `t_frs_user_org` DROP COLUMN `org_code`;
#4.创建考勤每日统计表
CREATE TABLE `t_frs_check_day_statistic` (`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',`person_id` int(11) NOT NULL COMMENT '人员ID', `check_date` date NOT NULL COMMENT '考勤日期(yyyy-mm-dd)', `first_record_id` int(11) DEFAULT NULL COMMENT '初次考勤记录ID', `last_record_id` int(11) DEFAULT NULL COMMENT '末次考勤记录ID', `first_record_time` datetime(0) DEFAULT NULL COMMENT '初次考勤记录时间', `last_record_time` datetime(0) DEFAULT NULL COMMENT '末次考勤记录时间',  PRIMARY KEY (`id`) USING BTREE) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考勤每日统计表' ROW_FORMAT = Compact;
#5.创建人员类别表
CREATE TABLE `t_frs_person_type`  ( `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键', `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型名称', PRIMARY KEY (`id`) USING BTREE) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人员类别表' ROW_FORMAT = Compact;
#6.添加人员类别默认数据
INSERT INTO `t_frs_person_type` (`id`, `name`) VALUES (1, '默认类别');

```



### 5. rabbitmq创建交换机及队列(可依据172.16.33.85环境 , 或联系开发人员创建)
    1.创建交换机 frs.record.exchange
    2.创建队列 device.manage.frs.queue、device.status.frs.queue

## 使用说明

banner.txt生成地址 : http://patorjk.com/software/taag/#p=display&h=3&v=3&f=Doom&t=jaws-oas
