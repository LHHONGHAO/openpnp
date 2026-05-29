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

## 项目构建

### 1. 编译项目

```bash
cd openpnp
mvn clean compile
```

### 2. 打包项目

```bash
mvn clean package -DskipTests
```

打包完成后，会在 `target` 目录下生成可执行的 JAR 文件：
```
openpnp-gui-0.0.1-alpha-SNAPSHOT.jar
```

## 运行项目

### 方法一：使用批处理脚本（Windows）

在项目根目录下运行：
```bash
openpnp.bat
```

### 方法二：使用 Java 直接运行 JAR 文件

```bash
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/java.awt.color=ALL-UNNAMED -jar target\openpnp-gui-0.0.1-alpha-SNAPSHOT.jar
```

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

### 1. 依赖下载失败

如果 Maven 依赖下载失败，请检查网络连接或尝试更换 Maven 镜像源。

### 2. Java 版本不兼容

请确保使用 Java 11 或更高版本。如果您使用的是较新版本的 Java，可能需要添加额外的 JVM 参数。

### 3. UI 界面无法显示

确保您的系统支持图形化界面，并且已正确配置 Java 环境。

## 更多信息

- [OpenPnP 官方网站](https://openpnp.org/)
- [OpenPnP 官方文档](https://github.com/openpnp/openpnp/wiki)
- [GitHub 仓库](https://github.com/LHHONGHAO/openpnp)

## 许可证

请参考项目根目录下的 LICENSE 文件获取许可证信息。
