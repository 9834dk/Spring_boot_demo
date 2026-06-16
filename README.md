# Spring Boot 学生管理系统 (Student Management System)

## 📌 项目简介
本项目是一个基于 **Spring Boot** 构建的后端 Web 应用程序，主要提供对“学生（Student）”信息的管理功能，包括增、删、改、查（CRUD）操作。
项目采用了标准的**分层架构（Layered Architecture）**，并通过 **Spring Data JPA** 实现了与数据库的交互。

## 🏗️ 框架与技术栈
- **框架**: Spring Boot 3.x
- **编程语言**: Java 17
- **数据库**: MySQL 
- **ORM / 数据访问**: Spring Data JPA & Hibernate
- **依赖管理**: Maven
- **RESTful API**: Spring Web

## ⚙️ 项目核心功能
该项目暴露了以下 RESTful API 接口，用于和前端或其他客户端进行数据交互，所有的接口统一返回 `Response<T>` 标准格式的数据。

| 功能 | 请求方式 | 接口路径 | 参数说明 |
| --- | --- | --- | --- |
| **查询学生** | `GET` | `/student/{id}` | 路径参数 `id`: 学生的唯一标识 |
| **新增学生** | `POST` | `/student` | 请求体 `StudentDTO`: 包含 name, email 等信息 |
| **删除学生** | `DELETE`| `/student/{id}` | 路径参数 `id`: 待删除学生的标识 |
| **更新学生** | `PUT` | `/student/{id}` | 路径参数 `id`, 以及可选参数 `name`, `email` |

## 📂 项目结构说明
项目采用了 MVC 衍生出来的**经典三层架构**：

- **`controller` (控制层)**
  - `StudentController.java`：负责接收客户端的 HTTP 请求，将请求转发给 Service 层处理，并统一包装返回结果 (`Response`)。
- **`service` (业务逻辑层)**
  - `StudentService.java`：定义了具体的业务接口。
  - `StudentServiceImpl.java`：实现了业务逻辑，比如校验邮箱是否重复、查询数据是否存在等。
- **`dao` (数据访问层 & 实体类)**
  - `Student.java`：数据库表 `student` 对应的实体类 (Entity)。
  - `StudentRepository.java`：基于 Spring Data JPA 提供开箱即用的数据库 CRUD 方法。
- **`dto` (数据传输对象)**
  - `StudentDTO.java`：用于在 Controller 和 Service 之间传递数据，隐藏数据库实体的敏感信息，避免暴露所有字段。
- **`converter` (数据转换器)**
  - `StudentConverter.java`：负责 `Student` (数据库实体) 和 `StudentDTO` (传输对象) 之间的相互转换。
- **`com.Response.java` (统一响应封装)**
  - 提供 `newSuccess` 和 `newFail`，规范前后端分离场景下的返回数据结构。
- **`SpringBootDemoApplication.java` (主启动类)**
  - 应用程序的入口。
- **`application.yaml` (配置文件)**
  - 配置了项目使用的数据库连接信息以及 JPA 相关配置。
