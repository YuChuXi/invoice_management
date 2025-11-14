@echo off
chcp 65001 >nul
echo ========================================
echo   RuoYi 发票管理系统 - 启动脚本
echo ========================================
echo.

REM 检查Java环境
echo [1/5] 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请先安装JDK 1.8或更高版本
    pause
    exit /b 1
)
echo ✓ Java环境检查通过

REM 检查Gradle环境
echo [2/5] 检查Gradle环境...
gradle -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Gradle，请先安装Gradle 7.0或更高版本
    pause
    exit /b 1
)
echo ✓ Gradle环境检查通过

REM 构建项目
echo [3/5] 构建项目...
call gradle clean build -x test
if %errorlevel% neq 0 (
    echo 错误: 项目构建失败，请检查错误信息
    pause
    exit /b 1
)
echo ✓ 项目构建成功

REM 启动应用
echo [4/5] 启动应用...
echo 启动端口: 8080
echo 应用将在浏览器中自动打开: http://localhost:8080
echo.

cd ruoyi-admin
start "" "http://localhost:8080"
call gradle bootRun

echo [5/5] 应用已启动
echo 如果应用没有自动启动，请手动访问: http://localhost:8080
echo 默认账户:
echo   超级管理员: superadmin / superadmin123
echo   管理员: admin1 / password123
echo   上传者: uploader1 / password123
pause
