# RuoYi 发票管理系统

基于RuoYi框架的发票管理系统，支持双模式发票提交（文件上传和URL链接）。

## 项目概述

本项目将原有的Flask发票管理系统完整迁移到RuoYi框架，保持了所有原有功能，并利用RuoYi的强大权限管理和快速开发能力。

## 功能特性

### 用户角色管理
- **上传者 (uploader)**: 提交发票，查看个人发票记录
- **管理员 (admin)**: 审核发票，管理报销状态，查看统计报表
- **超级管理员 (superadmin)**: 用户管理，系统监控，全量数据访问

### 发票管理功能
- 双模式提交：文件上传和URL链接
- 发票审核工作流：待审核、已通过、已拒绝
- 报销状态管理：未报销、已报销
- 标签管理：支持发票分类标记
- 拒绝理由系统：详细的审核反馈

### 数据统计与筛选
- 多维度数据筛选（时间、状态、上传者等）
- 发票金额统计
- 报销情况汇总
- 导出功能

## 技术栈

### 后端技术
- Spring Boot 2.7.18
- RuoYi 4.7.7
- MyBatis Plus 3.5.3.1
- MySQL 8.0
- Apache Shiro 安全框架
- Druid 连接池

### 前端技术
- Vue 3
- Element Plus
- Vue Router 4
- Pinia 状态管理
- Axios HTTP客户端

## 项目结构

```
ruoyi-invoice-system/
├── ruoyi-admin/                    # 后台管理模块
├── ruoyi-common/                   # 通用工具模块
├── ruoyi-framework/                # 框架核心模块
├── ruoyi-system/                   # 系统模块
│   ├── controller/                 # 控制器层
│   ├── domain/                     # 实体类
│   ├── mapper/                     # 数据访问层
│   ├── service/                    # 服务层
│   └── vo/                         # 视图对象
├── ruoyi-ui/                       # 前端模块
├── sql/                            # 数据库脚本
└── config/                         # 配置文件
```

## 快速开始

### 环境要求
- JDK 8+
- MySQL 8.0+
- Maven 3.6+
- Node.js 16+ (前端)

### 数据库初始化
1. 创建数据库 `ruoyi_invoice`
2. 执行 `sql/init.sql` 脚本

### 后端启动
```bash
cd ruoyi-invoice-system
mvn clean install
cd ruoyi-admin
mvn spring-boot:run
```

### 前端启动
```bash
cd ruoyi-ui
npm install
npm run dev
```

## 默认账户

- 超级管理员: superadmin / superadmin123
- 管理员: admin1 / password123
- 上传者: uploader1 / password123

## API接口

### 用户认证
- POST `/api/login` - 用户登录
- POST `/api/logout` - 用户登出
- GET `/api/user/info` - 获取用户信息

### 发票管理
- POST `/api/invoice/upload` - 上传发票文件
- POST `/api/invoice/submitUrl` - 提交URL链接发票
- GET `/api/invoice/list` - 获取发票列表
- PUT `/api/invoice/updateStatus` - 更新发票状态
- GET `/api/invoice/download/{id}` - 下载发票文件

## 配置说明

### 数据库配置
修改 `config/application-dev.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ruoyi_invoice
    username: your_username
    password: your_password
```

### 文件上传配置
修改 `config/application.yml` 中的文件存储路径：
```yaml
file:
  upload:
    path: /path/to/upload/directory
```

## 部署说明

### 生产环境部署
1. 修改配置文件为生产环境配置
2. 打包项目：`mvn clean package -DskipTests`
3. 部署到服务器
4. 配置反向代理和SSL证书

### 容器化部署
项目支持Docker部署，具体配置参考部署文档。

## 迁移说明

### 从Flask迁移到RuoYi的主要变化
1. **技术栈**: Python Flask → Java Spring Boot
2. **数据库**: SQLite → MySQL
3. **安全框架**: Flask-Login → Apache Shiro
4. **前端**: HTML模板 → Vue3 SPA
5. **权限管理**: 基于角色的细粒度权限控制

### 保持的功能
- 多角色用户管理
- 双模式发票提交
- 发票审核流程
- 数据筛选统计
- 文件上传下载

## 许可证

MIT License

## 联系方式

如有问题，请联系项目维护团队。
