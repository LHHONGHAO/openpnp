@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ============================================
echo  OpenPnP - 一键构建与运行
echo ============================================
echo.

:: 检测 Maven
set MVN_CMD=
if exist "..\apache-maven-3.9.9\bin\mvn.cmd" (
    set "MVN_CMD=..\apache-maven-3.9.9\bin\mvn.cmd"
) else if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    set "MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
) else (
    where mvn.cmd >nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        set "MVN_CMD=mvn.cmd"
    )
)

if "%MVN_CMD%"=="" (
    echo [错误] 未找到Maven，请安装Maven或将apache-maven-3.9.9放在e:\openpnp\目录下
    echo 运行 'where mvn.cmd' 确认Maven是否在PATH中
    pause
    exit /b 1
)

echo [1/4] 正在编译项目（跳过测试）...
echo 使用Maven: %MVN_CMD%
call "%MVN_CMD%" compile -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo [错误] 编译失败！请检查错误信息。
    pause
    exit /b 1
)
echo [1/4] 编译完成 ✓

echo.
echo [2/4] 正在复制资源文件...
:: 确保资源文件在classes目录下
if exist "src\main\resources\org\openpnp\*.properties" (
    xcopy /Y /Q "src\main\resources\org\openpnp\*.properties" "target\classes\org\openpnp\" >nul
)
echo [2/4] 资源文件复制完成 ✓

echo.
echo [3/4] 正在检查运行环境...
java -version 2>&1 | findstr /i "version" >nul
if %ERRORLEVEL% NEQ 0 (
    echo [错误] 未找到Java运行环境！
    pause
    exit /b 1
)
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do set JAVA_VER=%%i
echo     Java版本: %JAVA_VER%
echo [3/4] 环境检查完成 ✓

echo.
echo [4/4] 正在启动OpenPnP...
echo ============================================
echo  提示: 首次运行请前往 Help ^> Class Title Mapping Editor 启用文本映射
echo ============================================
echo.
java --add-opens=java.base/java.lang=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt.color=ALL-UNNAMED ^
     --add-opens=java.base/java.io=ALL-UNNAMED ^
     --add-opens=java.base/java.util=ALL-UNNAMED ^
     --add-opens=java.desktop/javax.swing=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt.event=ALL-UNNAMED ^
     -cp "target/classes;target/lib/*" ^
     org.openpnp.Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [提示] 如果启动失败，请尝试先运行: mvn package -DskipTests
    echo       然后使用: build-and-run-exec.bat
)
pause
