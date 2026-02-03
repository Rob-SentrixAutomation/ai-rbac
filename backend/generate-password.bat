@echo off
cd /d %~dp0
echo Generating BCrypt password hash...
echo.

set CLASSPATH=target\classes
for %%f in ("%USERPROFILE%\.m2\repository\org\springframework\security\spring-security-crypto\5.5.8\*.jar") do set CLASSPATH=!CLASSPATH!;%%f

java -cp "%CLASSPATH%" com.example.rbac.PasswordGenerator

pause
