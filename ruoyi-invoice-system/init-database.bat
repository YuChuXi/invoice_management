@echo off
chcp 65001 >nul
echo ========================================
echo   RuoYi 发票管理系统 - 数据库初始化脚本
echo ========================================
echo.

REM 检查MySQL环境
echo [1/4] 检查MySQL环境...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到MySQL客户端，请先安装MySQL 8.0或更高版本
    echo 或者手动执行SQL脚本: sql\init.sql
    pause
    exit /b 1
)
echo ✓ MySQL环境检查通过

REM 提示用户输入数据库信息
echo [2/4] 请输入数据库连接信息
set /p mysql_host=数据库主机 (默认: localhost): 
if "%mysql_host%"=="" set mysql_host=localhost

set /p mysql_port=数据库端口 (默认: 3306): 
if "%mysql_port%"=="" set mysql_port=3306

set /p mysql_user=数据库用户名 (默认: root): 
if "%mysql_user%"=="" set mysql_user=root

set /p mysql_password=数据库密码: 

set /p mysql_database=数据库名 (默认: ruoyi_invoice): 
if "%mysql_database%"=="" set mysql_database=ruoyi_invoice

REM 测试数据库连接
echo [3/4] 测试数据库连接...
mysql -h %mysql_host% -P %mysql_port% -u %mysql_user% -p%mysql_password% -e "SELECT 1;" >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 数据库连接失败，请检查连接信息
    pause
    exit /b 1
)
echo ✓ 数据库连接成功

REM 执行初始化脚本
echo [4/4] 执行数据库初始化脚本...
mysql -h %mysql_host% -P %mysql_port% -u %mysql_user% -p%mysql_password% < sql\init.sql
if %errorlevel% neq 0 (
    echo 错误: 数据库初始化失败，请检查SQL脚本
    pause
    exit /b 1
)
echo ✓ 数据库初始化成功

echo.
echo ========================================
echo  数据库初始化完成
echo ========================================
echo 数据库名: %mysql_database%
echo 默认账户信息:
echo   超级管理员: superadmin / superadmin123
echo   管理员: admin1 / password123
echo   上传者: uploader1 / password123
echo.
echo 现在可以运行 start.bat 启动应用
pause
