@echo off
cd /d "%~dp0"
java --add-opens=java.base/java.lang=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt.color=ALL-UNNAMED ^
     --add-opens=java.base/java.io=ALL-UNNAMED ^
     --add-opens=java.base/java.util=ALL-UNNAMED ^
     --add-opens=java.desktop/javax.swing=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt.event=ALL-UNNAMED ^
     -cp "target/classes;target/lib/*" org.openpnp.Main
pause
