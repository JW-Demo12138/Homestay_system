@echo off

echo 正在检查Java安装...
java -version
if %errorlevel% neq 0 (
    echo 错误：未安装Java或Java环境变量未配置
    pause
    exit /b 1
)

echo.
echo 正在检查Maven安装...
mvn -version
if %errorlevel% neq 0 (
    echo 错误：未安装Maven或Maven环境变量未配置
    echo 请按照backend/README.md中的说明安装Maven
    pause
    exit /b 1
)

echo.
echo 正在进入后端项目目录...
cd "%~dp0backend"

echo.
echo 正在清理并安装项目依赖...
mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo 错误：依赖安装失败
    pause
    exit /b 1
)

echo.
echo 依赖安装成功！
echo 可以使用以下命令运行项目：
echo mvn spring-boot:run
echo 或在IDE中运行HomestayApplication.java主类
pause
