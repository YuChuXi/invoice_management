# RuoYi发票管理系统 - 环境安装指南

## 系统要求

### 硬件要求
- **内存**: 至少 4GB RAM (推荐8GB)
- **存储**: 至少 2GB 可用空间
- **处理器**: 四核以上

### 软件要求
- **操作系统**: Windows 10/11, macOS 10.15+, Ubuntu 18.04+
- **Java**: JDK 1.8 或更高版本
- **数据库**: MySQL 5.7+ 或 MySQL 8.0
- **构建工具**: Maven 3.6+
- **前端环境**: Node.js 16+, npm 8+

## 环境安装步骤

### 1. 安装Java开发环境

#### Windows系统
1. 访问 [Oracle JDK下载](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) 或 [OpenJDK下载](https://adoptium.net/)
2. 下载 JDK 8 或更高版本
3. 运行安装程序
4. 配置环境变量：
   ```
   JAVA_HOME: C:\Program Files\Java\jdk1.8.0_xxx
   Path: %JAVA_HOME%\bin
   ```

#### macOS系统
```bash
# 使用Homebrew安装
brew install openjdk@8

# 或从官网下载安装包
```

#### Linux系统 (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-8-jdk
```

### 2. 验证Java安装
```bash
java -version
javac -version
```

### 3. 安装MySQL数据库

#### Windows系统
1. 访问 [MySQL官网](https://dev.mysql.com/downloads/mysql/)
2. 下载 MySQL Community Server
3. 运行安装程序，记住root密码

#### macOS系统
```bash
# 使用Homebrew安装
brew install mysql
brew services start mysql
```

#### Linux系统 (Ubuntu/Debian)
```bash
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

### 4. 安装Maven

#### Windows系统
1. 访问 [Maven官网](https://maven.apache.org/download.cgi)
2. 下载 Binary zip archive
3. 解压到指定目录
4. 配置环境变量：
   ```
   M2_HOME: C:\apache-maven-3.8.x
   Path: %M2_HOME%\bin
   ```

#### macOS/Linux系统
```bash
# 使用Homebrew (macOS)
brew install maven

# 或手动安装
wget https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
tar -xzf apache-maven-3.8.6-bin.tar.gz
sudo mv apache-maven-3.8.6 /usr/local/maven
```

### 5. 验证Maven安装
```bash
mvn -version
```

### 6. 安装Node.js和npm

#### Windows系统
1. 访问 [Node.js官网](https://nodejs.org/)
2. 下载 LTS 版本
3. 运行安装程序

#### macOS系统
```bash
# 使用Homebrew安装
brew install node
```

#### Linux系统 (Ubuntu/Debian)
```bash
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt-get install -y nodejs
```

### 7. 验证Node.js安装
```bash
node --version
npm --version
```

## 项目部署步骤

### 1. 下载项目代码
```bash
# 如果使用git
git clone <项目仓库地址>
cd ruoyi-invoice-system

# 或者直接下载ZIP文件并解压
```

### 2. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE ruoyi_invoice DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 导入数据库脚本
```bash
mysql -u root -p ruoyi_invoice < sql/init.sql
```

### 3. 后端配置

#### 修改数据库连接配置
编辑 `ruoyi-admin/src/main/resources/application-druid.yml`：
```yaml
# 数据源配置
spring:
  datasource:
    druid:
      # 主库数据源
      master:
