
# 编译脚本
$ErrorActionPreference = "Stop"
$sourceDir = "src/main/java"
$outputDir = "target/classes"

# 找到编译依赖
$classpath = @()
# 先看看是否有其他依赖的 jar 包在 target 或其他位置
if (Test-Path "target/openpnp-gui-0.0.1-alpha-SNAPSHOT.jar") {
    Write-Host "Found target JAR, extracting classpath..."
    $jarPath = (Resolve-Path "target/openpnp-gui-0.0.1-alpha-SNAPSHOT.jar").Path
    $classpath += $jarPath
}

# 我们需要找到 PMW tinylog 的依赖
if (Test-Path "$env:USERPROFILE/.m2") {
    Write-Host "Looking in Maven local repository..."
    Get-ChildItem "$env:USERPROFILE/.m2" -Recurse -Filter "*.jar" -ErrorAction SilentlyContinue | ForEach-Object {
        if ($_.Name -match "tinylog|junit|swing|awt|commons") {
            $classpath += $_.FullName
        }
    }
}

# 输出当前的 classpath
Write-Host "Classpath entries found: $($classpath.Count)"
$classpath | ForEach-Object { Write-Host "  $_" }

# 编译源文件
Write-Host "Compiling Java files..."
$javacCmd = "javac"
$javacArgs = @()
$javacArgs += "-encoding"
$javacArgs += "UTF-8"
$javacArgs += "-d"
$javacArgs += $outputDir
$javacArgs += "-sourcepath"
$javacArgs += $sourceDir
if ($classpath.Count -gt 0) {
    $javacArgs += "-cp"
    $javacArgs += ($classpath -join ";")
}

# 要编译的文件
$filesToCompile = @(
    "$sourceDir/org/openpnp/ClassTitleRegistry.java",
    "$sourceDir/org/openpnp/Translations.java",
    "$sourceDir/org/openpnp/gui/ClassTitleMappingDialog.java"
)
$javacArgs += $filesToCompile

Write-Host "Executing: $javacCmd $($javacArgs -join " ")"
&amp; $javacCmd @javacArgs

if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed!"
    exit 1
}

Write-Host "Compilation successful!"
