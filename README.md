# MQTT Simulator

## 项目简介

该项目包含两个主要部分：
1. **数据发送器**：撰写一个模拟数据发送程序，可以向 MQTT Broker 的 topic 发送一批至少 100 条 JSON 消
   息。
   - (1) 消息体含两个关键字段，timestamp 和 type。
   - (2) Timesatmp 值须为发送时刻前后 30 秒的一个随机时间，秒单位；
   - (3) Type 有 A、B、C、D 四种类型。
2. **数据接收器**：撰写一个订阅 MQTT Broker 相应 topic 接收消息的程序
   - (1) 该程序接收消息，并用自行设计的数据结构进行内存存储
   - (2) 该程序提供一个 RESTFUL 的 API 接口
   - - 1 入参为开始时间和结束时间，分钟单位
   - - 2 返回值为一个 json 实体，返回入参时间单位内的 ABCD 条数分别为多少

## 环境要求

- Java 1.8 或更高版本
- Maven
- 安装有emqx
- EMQX MQTT Broker

## 使用说明
### 服务器上安装emqx
1. 下载 emqx-5.0.10-otp24.2.1-1-el7-amd64.rpmSHA256
   wget https://www.emqx.com/zh/downloads/broker/5.0.10/emqx-5.0.10-otp24.2.1-1-el7-amd64.rpm

2. 安装
   sudo yum install emqx-5.0.10-otp24.2.1-1-el7-amd64.rpm

3. 运行
   sudo systemctl start emqx
4. 开发对应的18083和1883端口
5. emqx开启之后可以打开控制台查看具体信息，控制台地址: http://ip:18083

### 1. 启动web服务 `Application` 类
- 启动 Spring Boot 应用程序，自动订阅相应的 topic。

### 2. 编译并运行 `DataPublisher` 类，向指定 topic 发送 100 条消息。

### 3. 统计数据

- 通过 HTTP GET 请求访问 `/api/messages/count` 接口，传入 `startTime` 和 `endTime` 来获取 A、B、C、D 四种类型消息的计数。

### 示例请求
- http://localhost:8080/api/messages/count?startTime=1726638439&endTime=1726638739
