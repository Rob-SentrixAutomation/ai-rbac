@echo off
echo ============================
echo 编译RBAC权限管理系统
echo ============================
echo.

echo [1/3] 清理项目...
call mvn clean

echo.
echo [2/3] 编译项目...
call mvn compile

echo.
echo [3/3] 打包项目...
call mvn package -DskipTests

echo.
echo ============================
echo 编译完成!
echo ============================
pause
