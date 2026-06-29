@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ============================================
echo  OpenPnP - 打包可执行JAR并运行
echo    (生成跨平台独立可执行文件，适合分发部署)
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
    echo [错误] 未找到Maven！
    pause
    exit /b 1
)

echo [1/2] 正在打包项目（mvn package）...
echo 使用Maven: %MVN_CMD%
echo 这可能需要几分钟，请耐心等待...
call "%MVN_CMD%" package -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo [错误] 打包失败！请检查错误信息。
    pause
    exit /b 1
)
echo [1/2] 打包完成 ✓

echo.
echo [2/2] 正在启动OpenPnP（使用可执行JAR）...
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
     -jar target\openpnp-gui-0.0.1-alpha-SNAPSHOT-exec.jar

pause
