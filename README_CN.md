# OpenPnP 中文说明文档

## 项目简介

OpenPnP 是一个开源的 SMT（表面贴装技术）贴片机控制软件，用于自动化电子元件的装配过程。

## 环境要求

- **Java JDK**: 11 或更高版本（推荐使用 OpenJDK 17 或更高）
- **Maven**: 3.6 或更高版本
- **操作系统**: Windows / Linux / macOS

## 环境配置

### 1. 安装 Java JDK

确保您已安装 Java 开发工具包 (JDK) 并配置了环境变量。

检查 Java 版本：
```bash
java -version
```

### 2. 安装 Maven

如果您没有安装 Maven，请按以下步骤操作：

**Windows 系统：**
1. 从 [Apache Maven 官网](https://maven.apache.org/download.cgi) 下载 Maven 压缩包
2. 解压到您想要的位置（例如：`e:\openpnp\apache-maven-3.9.9`）
3. 配置环境变量：
   - 新建系统变量 `MAVEN_HOME`，值为 Maven 的安装目录
   - 在 `Path` 变量中添加 `%MAVEN_HOME%\bin`

检查 Maven 安装：
```bash
mvn -version
```

### 3. 配置 Maven 仓库

项目已配置使用 Gitee 镜像仓库以加快依赖下载速度，仓库地址为：
```
https://gitee.com/lhhonghao/openpnp-maven-repo/raw/develop
```

## 快速开始（推荐）

### 一键构建并运行（Windows）

项目提供了两个一键脚本，自动完成编译和启动：

**开发模式（编译后直接运行）：**
```bash
build-and-run.bat
```
此脚本会自动：
1. 检测 Maven 环境
2. 编译项目（跳过测试）
3. 复制资源文件
4. 启动 OpenPnP

**发布模式（打包可执行 JAR 后运行）：**
```bash
build-and-run-exec.bat
```
此脚本会自动：
1. 检测 Maven 环境
2. 执行 `mvn package -DskipTests`
3. 生成独立可执行 JAR 文件
4. 启动 OpenPnP

> 首次运行后将生成 `target/openpnp-gui-0.0.1-alpha-SNAPSHOT-exec.jar`（约 180MB），可复制到其他电脑独立运行。

---

## 分离式构建与运行

### 1. 编译项目

```bash
cd openpnp
mvn clean compile -DskipTests
```

### 2. 打包项目

```bash
mvn clean package -DskipTests
```

打包完成后，会在 `target` 目录下生成：
- `openpnp-gui-0.0.1-alpha-SNAPSHOT.jar` — 普通 jar（需配合 lib/ 目录运行）
- `openpnp-gui-0.0.1-alpha-SNAPSHOT-exec.jar` — 独立可执行 jar（包含所有依赖）

### 方法一：使用批处理脚本运行

```bash
run.bat          # 开发模式运行 (需要先编译)
run-exec.bat     # 独立jar运行 (需要先打包)
```

### 方法二：使用 Java 直接运行

**开发模式（编译后）：**
```bash
java --add-opens=java.base/java.lang=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt.color=ALL-UNNAMED ^
     --add-opens=java.base/java.io=ALL-UNNAMED ^
     --add-opens=java.base/java.util=ALL-UNNAMED ^
     --add-opens=java.desktop/javax.swing=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt.event=ALL-UNNAMED ^
     -cp "target/classes;target/lib/*" ^
     org.openpnp.Main
```

**独立 JAR 模式（打包后）：**
```bash
java --add-opens=java.base/java.lang=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt.color=ALL-UNNAMED ^
     --add-opens=java.base/java.io=ALL-UNNAMED ^
     --add-opens=java.base/java.util=ALL-UNNAMED ^
     --add-opens=java.desktop/javax.swing=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt.event=ALL-UNNAMED ^
     -jar target\openpnp-gui-0.0.1-alpha-SNAPSHOT-exec.jar
```

---

## 文本映射功能（重要）

本项目实现了类名和界面文本的中文显示映射功能，可以将飞达类型、机器组件等以中文名称显示。

### 启用文本映射

文本映射功能**默认关闭**。首次运行后，请按以下步骤启用：

1. 打开菜单栏：**帮助(Help) → 类标题映射编辑器(Class Title Mapping Editor)**
2. 在打开的对话框中，**勾选左上角的复选框**以启用映射
3. 如果界面未自动切换为中文，在对话框中选择 **"Chinese (Simplified)"** 并点击 **"设置语言"**
4. 关闭对话框

启用后，以下界面将以中文显示：
- 飞达类型选择对话框（条状送料器、托盘送料器、拖拽送料器等）
- 飞达配置向导页面标题
- 机器配置界面的组件名称
- 问题与解决方案的描述

### 注意事项

- **每台新电脑首次启动后都需要手动启用映射**
- 映射设置保存在 Java Preferences 中（用户级配置，每台电脑独立）
- 如果界面显示为英文而未切换中文，请检查映射是否已启用

---

## 配置文件

项目包含预配置的配置文件，位于 `.openpnp2_config` 目录下。这些配置文件会在首次运行时自动复制到用户目录下的 `.openpnp2` 文件夹中。

## Git 仓库配置

### 远程仓库地址

```
https://github.com/LHHONGHAO/openpnp.git
```

### 常用 Git 命令

**克隆仓库：**
```bash
git clone https://github.com/LHHONGHAO/openpnp.git
```

**拉取最新代码：**
```bash
git pull origin main
```

**提交更改：**
```bash
git add .
git commit -m "描述您的更改"
git push origin main
```

## 常见问题

### 1. 另一台电脑运行功能不一致/文本映射不生效

**原因：文本映射默认关闭，每台新电脑需要手动启用。**

解决方法：
1. 启动 OpenPnP 后，点击菜单栏 **帮助(Help) → 类标题映射编辑器(Class Title Mapping Editor)**
2. **勾选左上角的复选框**以启用映射
3. 选择 **"Chinese (Simplified)"** 并点击 **"设置语言"**

### 2. 依赖下载失败

如果 Maven 依赖下载失败，请检查网络连接或尝试更换 Maven 镜像源。

### 3. Java 版本不兼容

请确保使用 Java 17 或更高版本。如果使用较新版本的 Java，需要添加 `--add-opens` 参数（`build-and-run.bat` 已自动添加）。

### 4. UI 界面无法显示

确保您的系统支持图形化界面，并且已正确配置 Java 环境。

### 5. 编译后无法运行

请确保先运行过 `mvn compile -DskipTests` 或 `mvn package -DskipTests`。推荐使用 `build-and-run.bat` 一键脚本。

## 更多信息

- [OpenPnP 官方网站](https://openpnp.org/)
- [OpenPnP 官方文档](https://github.com/openpnp/openpnp/wiki)
- [GitHub 仓库](https://github.com/LHHONGHAO/openpnp)

## 许可证

请参考项目根目录下的 LICENSE 文件获取许可证信息。
