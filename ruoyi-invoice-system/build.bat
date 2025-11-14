@echo off
chcp 65001 >nul
echo ========================================
echo   RuoYi 发票管理系统 - 构建脚本
echo ========================================
echo.

REM 检查Java环境
echo [1/4] 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请先安装JDK 1.8或更高版本
    pause
    exit /b 1
)
echo ✓ Java环境检查通过

REM 检查Gradle环境
echo [2/4] 检查Gradle环境...
gradle -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Gradle，请先安装Gradle 7.0或更高版本
    pause
    exit /b 1
)
echo ✓ Gradle环境检查通过

REM 清理并构建项目
echo [3/4] 清理并构建项目...
call gradle clean build -x test
if %errorlevel% neq 0 (
    echo 错误: 项目构建失败，请检查错误信息
    pause
    exit /b 1
)
echo ✓ 项目构建成功

echo.
echo ========================================
echo  构建完成
echo ========================================
echo 生成的JAR包位置: ruoyi-admin/build/libs/ruoyi-admin.jar
echo 可使用以下命令启动应用:
echo   java -jar ruoyi-admin/build/libs/ruoyi-admin.jar
echo.
echo 或使用启动脚本:
echo   start.bat
pause
