# RuoYi 发票管理系统项目结构

## 项目概述
将现有的Flask发票管理系统迁移到RuoYi框架，保持原有功能完整，支持双模式发票提交（文件上传和URL链接）。

## 项目结构

```
ruoyi-invoice-system/
├── ruoyi-admin/                    # 后台管理模块
├── ruoyi-common/                   # 通用工具模块
├── ruoyi-framework/                # 框架核心模块
├── ruoyi-system/                   # 系统模块
│   ├── src/main/java/com/ruoyi/system/
│   │   ├── controller/             # 控制器
│   │   │   ├── SysUserController.java
│   │   │   ├── InvoiceController.java
│   │   │   ├── UploadController.java
│   │   │   └── DownloadController.java
│   │   ├── domain/                 # 实体类
│   │   │   ├── SysUser.java
│   │   │   ├── Invoice.java
│   │   │   └── InvoiceFile.java
│   │   ├── mapper/                 # 数据访问层
│   │   │   ├── SysUserMapper.java
│   │   │   └── InvoiceMapper.java
│   │   ├── service/                # 服务层
│   │   │   ├── ISysUserService.java
│   │   │   ├── SysUserServiceImpl.java
│   │   │   ├── IInvoiceService.java
│   │   │   └── InvoiceServiceImpl.java
│   │   └── vo/                     # 视图对象
│   │       ├── InvoiceVO.java
│   │       └── UploadResultVO.java
│   └── src/main/resources/
│       └── mapper/system/          # MyBatis映射文件
│           ├── SysUserMapper.xml
│           └── InvoiceMapper.xml
├── ruoyi-ui/                       # 前端模块（Vue3 + Element Plus）
│   ├── src/
│   │   ├── api/                    # API接口
│   │   │   ├── user.js
│   │   │   ├── invoice.js
│   │   │   └── upload.js
│   │   ├── views/                  # 页面组件
│   │   │   ├── login/              # 登录页
│   │   │   ├── uploader/           # 上传者页面
│   │   │   │   ├── index.vue       # 上传者主页面
│   │   │   │   ├── upload.vue      # 发票上传组件
│   │   │   │   ├── list.vue        # 发票列表组件
│   │   │   │   └── summary.vue     # 汇总组件
│   │   │   ├── admin/              # 管理员页面
│   │   │   │   ├── index.vue       # 管理员主页面
│   │   │   │   ├── pending.vue     # 待审核
│   │   │   │   ├── approved.vue    # 已通过
│   │   │   │   └── rejected.vue    # 已拒绝
│   │   │   └── superadmin/         # 超级管理员页面
│   │   │       ├── index.vue       # 超级管理员主页面
│   │   │       ├── users.vue       # 用户管理
│   │   │       └── invoices.vue    # 发票管理
│   │   └── router/                 # 路由配置
│   │       └── index.js
├── sql/                            # 数据库脚本
│   ├── init.sql                    # 初始化脚本
│   └── update.sql                  # 更新脚本
└── config/                         # 配置文件
    ├── application.yml
    └── application-dev.yml
```

## 数据库设计

### 用户表 (sys_user)
```sql
CREATE TABLE sys_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    user_name VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    role_type VARCHAR(20) NOT NULL COMMENT '角色类型: uploader,admin,superadmin',
    status CHAR(1) DEFAULT '0' COMMENT '状态:0正常,1停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';
```

### 发票表 (sys_invoice)
```sql
CREATE TABLE sys_invoice (
    invoice_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '发票ID',
    invoice_number VARCHAR(100) NOT NULL COMMENT '发票号码',
    supplier_name VARCHAR(200) NOT NULL COMMENT '供应商名称',
    purchase_date DATE NOT NULL COMMENT '购买日期',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    description TEXT COMMENT '描述信息',
    file_path VARCHAR(500) COMMENT '文件存储路径',
    file_url VARCHAR(500) COMMENT '文件URL链接',
    file_type VARCHAR(10) DEFAULT 'upload' COMMENT '文件类型:upload,url',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态:pending,approved,rejected',
    reimbursement_status VARCHAR(20) DEFAULT 'unreimbursed' COMMENT '报销状态:unreimbursed,reimbursed',
    tags VARCHAR(200) COMMENT '标签',
    rejection_reason TEXT COMMENT '拒绝理由',
    uploader_id BIGINT NOT NULL COMMENT '上传者ID',
    admin_id BIGINT COMMENT '管理员ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (uploader_id) REFERENCES sys_user(user_id),
    FOREIGN KEY (admin_id) REFERENCES sys_user(user_id)
) COMMENT='发票表';
```

## 核心功能模块

### 1. 用户认证与权限管理
- 基于RuoYi的Shiro安全框架
- 角色权限控制：上传者、管理员、超级管理员
- 登录认证和会话管理

### 2. 发票管理模块
- 双模式发票提交：文件上传和URL链接
- 发票审核工作流
- 报销状态管理
- 标签管理
- 拒绝理由系统

### 3. 文件管理模块
- 智能文件上传（支持本地存储和URL链接）
- 文件下载服务
- 文件类型验证
- 存储路径管理

### 4. 数据统计与报表
- 发票状态统计
- 报销情况汇总
- 用户提交统计
- 时间段筛选

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 2.x + RuoYi
- **安全**: Apache Shiro
- **ORM**: MyBatis Plus
- **数据库**: MySQL 8.0
- **文件存储**: 本地文件系统
- **构建工具**: Maven

### 前端技术栈
- **框架**: Vue 3 + Element Plus
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **构建工具**: Vite

## API接口设计

### 用户认证接口
- `POST /api/login` - 用户登录
- `POST /api/logout` - 用户登出
- `GET /api/user/info` - 获取用户信息

### 发票管理接口
- `POST /api/invoice/upload` - 上传发票
- `GET /api/invoice/list` - 获取发票列表
- `PUT /api/invoice/{id}/status` - 更新发票状态
- `GET /api/invoice/{id}/download` - 下载发票文件
- `POST /api/invoice/{id}/resubmit` - 重新提交发票

### 文件管理接口
- `POST /api/upload/file` - 文件上传
- `POST /api/upload/url` - URL链接提交
- `GET /api/download/{fileId}` - 文件下载

## 迁移计划

1. **第一阶段**: 搭建RuoYi基础框架
2. **第二阶段**: 实现用户管理和权限系统
3. **第三阶段**: 实现发票管理核心功能
4. **第四阶段**: 实现文件上传下载功能
5. **第五阶段**: 前端页面开发
6. **第六阶段**: 系统测试和优化

## 部署配置

### 环境要求
- JDK 8+
- MySQL 8.0+
- Maven 3.6+
- Node.js 16+

### 配置文件
- 数据库连接配置
- 文件存储路径配置
- 权限配置
- 日志配置

这个项目结构将现有的Flask发票管理系统完整迁移到RuoYi框架，保持了所有原有功能，并利用RuoYi的强大权限管理和快速开发能力。
