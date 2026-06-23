
$ErrorActionPreference = "Stop"

$filesToFix = @(
    "src/main/java/org/openpnp/ClassTitleRegistry.java",
    "src/main/java/org/openpnp/Translations.java",
    "src/main/java/org/openpnp/gui/ClassTitleMappingDialog.java"
)

Write-Host "Fixing HTML entities in Java files..."

foreach ($file in $filesToFix) {
    if (-not (Test-Path $file)) {
        Write-Host "File not found: $file"
        continue
    }
    
    Write-Host "Processing $file..."
    $content = [System.IO.File]::ReadAllText($file, [System.Text.Encoding]::UTF8)
    
    # 替换所有 HTML 实体
    $content = $content -replace "&amp;lt;", "&lt;"
    $content = $content -replace "&amp;gt;", "&gt;"
    $content = $content -replace "&amp;amp;", "&amp;"
    $content = $content -replace "&amp;quot;", "`""
    $content = $content -replace "&amp;apos;", "'"
    $content = $content -replace "&lt;", "&lt;"
    $content = $content -replace "&gt;", "&gt;"
    $content = $content -replace "&amp;", "&amp;"
    
    # 保存回去
    [System.IO.File]::WriteAllText($file, $content, [System.Text.Encoding]::UTF8)
    Write-Host "Fixed: $file"
}

Write-Host "All files fixed!"
