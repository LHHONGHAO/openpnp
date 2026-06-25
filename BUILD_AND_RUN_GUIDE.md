# OpenPnP 编译运行交接文档

## 项目概述

OpenPnP 是一个开源的贴片机控制软件，本项目进行了多语言架构改造，实现了类名、问题描述、解决方案文本的集中式映射管理。

---

## 环境要求

### 必需软件
- **JDK**: 24.0.1 (Temurin) 或更高版本
  - 路径: `C:\Users\Administrator\.jdks\temurin-24\bin\javac.exe`
- **Maven**: 3.9.9
  - 路径: `e:\openpnp\apache-maven-3.9.9\bin\mvn.cmd`
- **Git**: 用于版本控制

### 可选工具
- **IDE**: Trae IDE 或 IntelliJ IDEA
- **操作系统**: Windows 10/11

---

## 项目结构

```
e:\openpnp\
── apache-maven-3.9.9/          # Maven 安装目录
├── openpnp/                      # 主项目目录
│   ├── src/
│   │   ── main/
│   │       ├── java/
│   │       │   └── org/openpnp/
│   │       │       ├── ClassTitleRegistry.java      # 核心映射注册表
│   │       │       ├── Translations.java            # 多语言翻译类
│   │       │       ├── gui/
│   │       │       │   ├── ClassTitleMappingDialog.java  # 映射表编辑界面
│   │       │       │   └── MachineSetupPanel.java        # 机器配置页面
│   │       │       └── machine/reference/
│   │       │           └── solutions/               # 问题与解决方案模块
│   │       └── resources/
│   │           └── org/openpnp/
│   │               ├── class-mappings.properties    # 类名映射配置
│   │               ├── text-mappings.properties     # 文本映射配置
│   │               ├── text-mapping-patterns.properties  # 动态文本模式映射
│   │               ├── translations.properties      # 英文翻译
│   │               └── translations_zh_CN.properties # 中文翻译
│   ├── pom.xml                    # Maven 项目配置
│   └── openpnp.bat               # Windows 启动脚本
└── apache-maven-3.9.9-bin.zip    # Maven 安装包
```

---

## 编译步骤

### 1. 进入项目目录
```powershell
cd e:\openpnp\openpnp
```

### 2. 编译项目（跳过测试和代码风格检查）
```powershell
& "e:\openpnp\apache-maven-3.9.9\bin\mvn.cmd" compile -DskipTests "-Dcheckstyle.skip=true"
```

### 3. 打包为 JAR 文件
```powershell
& "e:\openpnp\apache-maven-3.9.9\bin\mvn.cmd" package -DskipTests "-Dcheckstyle.skip=true"
```

编译成功后，JAR 文件位于：
```
e:\openpnp\openpnp\target\openpnp-gui-0.0.1-alpha-SNAPSHOT.jar
```

---

## 运行步骤

### 方法一：使用启动脚本
```powershell
cd e:\openpnp\openpnp
.\openpnp.bat
```

### 方法二：直接运行 JAR
```powershell
cd e:\openpnp\openpnp
java --add-opens=java.base/java.lang=ALL-UNNAMED `
     --add-opens=java.desktop/java.awt=ALL-UNNAMED `
     --add-opens=java.desktop/java.awt.color=ALL-UNNAMED `
     -jar target\openpnp-gui-0.0.1-alpha-SNAPSHOT.jar
```

**注意**: 必须添加 `--add-opens` 参数以解决 Java 模块访问限制问题。

---

## 核心功能说明

### 1. 统一映射架构

所有映射（类名、文本、模式）统一存储在 `ClassTitleRegistry` 中，通过 `Mapping` 类管理：

```java
public enum MappingType {
    CLASS,    // 类名映射
    TEXT,     // 静态文本映射
    PATTERN   // 动态模式映射
}

public static class Mapping {
    public final String id;
    public final MappingType type;
    public String source;
    public String englishTitle;
    public String chineseTitle;
    public String category;
    public boolean appendName;
    // ...
}
```

### 2. 映射配置文件

#### class-mappings.properties
格式: `PID|fullClassName|simpleName|englishTitle|chineseTitle|appendName|category`

示例:
```
001|org.openpnp.machine.reference.ReferenceMachine|ReferenceMachine|Machine|机器|false|Machine
002|org.openpnp.machine.reference.ReferenceHead|ReferenceHead|Head|贴装头|true|Machine
```

#### text-mappings.properties
格式: `PID|originalText|englishTranslation|chineseTranslation`

示例:
```
200|Axis is not assigned to a driver.|Axis is not assigned to a driver.|轴未分配给驱动器。
201|Assign a driver.|Assign a driver.|分配一个驱动器。
```

#### text-mapping-patterns.properties
格式: `PID|regex|englishTemplate|chineseTemplate`

示例:
```
350|^Set the manual nozzle tip change location for (.*?)\.$|Set the manual nozzle tip change location for {0}.|设置{0}的手动吸嘴尖更换位置。
```

### 3. 映射启用开关

通过 `ClassTitleRegistry.isMappingEnabled()` 控制全局映射开关，状态保存在 Java Preferences 中：
- 启用时：所有类名和文本自动应用映射，显示格式为 `(PID:xxx) 翻译文本`
- 禁用时：显示原始英文文本

### 4. 映射表编辑界面

通过 `ClassTitleMappingDialog` 提供统一的映射表编辑界面：
- 单一表格展示所有映射类型（类名、文本、模式）
- 支持按 PID、源文本、翻译文本、分类等多条件搜索
- 表格列宽可调并持久化保存
- 非原文和 PID 列可编辑
- 界面语言随软件语言设置自动切换

---

## 关键代码位置

### 映射注册表
- **文件**: `src/main/java/org/openpnp/ClassTitleRegistry.java`
- **功能**: 加载、保存、查询所有映射数据

### 映射表界面
- **文件**: `src/main/java/org/openpnp/gui/ClassTitleMappingDialog.java`
- **功能**: 提供映射表的编辑和管理界面

### 多语言翻译
- **文件**: `src/main/java/org/openpnp/Translations.java`
- **功能**: 管理多语言资源包，支持动态语言切换

### 机器配置页面
- **文件**: `src/main/java/org/openpnp/gui/MachineSetupPanel.java`
- **功能**: 显示机器配置树，应用类名映射

### 问题与解决方案
- **目录**: `src/main/java/org/openpnp/machine/reference/solutions/`
- **文件**: AxisSolutions.java, CameraSolutions.java, HeadSolutions.java 等
- **功能**: 实现问题检测与解决方案，应用文本映射

---

## 常见问题

### 1. 编译错误：找不到 javac.exe
**原因**: JDK 路径未正确配置

**解决**: 确认 JDK 安装路径，使用完整路径调用：
```powershell
& "C:\Users\Administrator\.jdks\temurin-24\bin\javac.exe" -version
```

### 2. 运行时异常：InaccessibleObjectException
**原因**: Java 模块访问限制

**解决**: 添加 `--add-opens` 参数：
```powershell
java --add-opens=java.base/java.lang=ALL-UNNAMED `
     --add-opens=java.desktop/java.awt=ALL-UNNAMED `
     --add-opens=java.desktop/java.awt.color=ALL-UNNAMED `
     -jar target\openpnp-gui-0.0.1-alpha-SNAPSHOT.jar
```

### 3. 映射未生效
**原因**: 映射开关未启用

**解决**: 在映射表界面勾选"启用映射"复选框

### 4. 类名显示为"属性表持有者"
**原因**: SimplePropertySheetHolder 被错误映射

**解决**: 已删除 PID 077 的映射，确保 PropertySheetHolder 子类使用各自的标题

### 5. JAR 文件损坏
**原因**: 使用 jar uf 命令更新类文件导致

**解决**: 删除损坏的 JAR，使用 Maven 重新打包：
```powershell
& "e:\openpnp\apache-maven-3.9.9\bin\mvn.cmd" package -DskipTests "-Dcheckstyle.skip=true"
```

---

## Git 工作流

### 查看状态
```powershell
cd e:\openpnp\openpnp
git status
```

### 添加并提交
```powershell
git add <file>
git commit -m "提交信息"
```

### 推送到 GitHub
```powershell
git push
```

### 远程仓库
- **URL**: https://github.com/LHHONGHAO/openpnp.git
- **分支**: main

---

## 修改映射配置

### 添加新的类名映射
编辑 `src/main/resources/org/openpnp/class-mappings.properties`，添加一行：
```
PID|完整类名|简单类名|英文标题|中文标题|是否附加名称|分类
```

### 添加新的文本映射
编辑 `src/main/resources/org/openpnp/text-mappings.properties`，添加一行：
```
PID|原始英文文本|英文翻译|中文翻译
```

### 添加动态文本模式
编辑 `src/main/resources/org/openpnp/text-mapping-patterns.properties`，添加一行：
```
PID|正则表达式|英文模板|中文模板
```

**注意**: 修改配置文件后需要重新编译打包才能生效。

---

## 版本信息

- **当前版本**: 2.6_2026-06-25
- **最后更新**: 2026-06-25
- **提交哈希**: 4ed8d2e377

---

## 联系方式

如有问题，请查看 GitHub 仓库：
https://github.com/LHHONGHAO/openpnp
