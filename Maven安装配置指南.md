# Maven 安装配置指南

## 1. 下载 Maven

### 官方下载地址
- Maven 官方网站：https://maven.apache.org/download.cgi
- 推荐下载：`apache-maven-3.9.6-bin.zip`（Windows 版本）

### 下载步骤
1. 打开上述链接
2. 找到 "Files" 部分
3. 下载 `apache-maven-3.9.6-bin.zip` 文件
4. 保存到本地（建议保存到 `D:\` 或 `E:\` 根目录）

## 2. 安装 Maven

1. 解压下载的 `apache-maven-3.9.6-bin.zip` 文件
   - 右键点击文件，选择 "Extract All..."
   - 选择解压目录，建议解压到 `D:\apache-maven-3.9.6`

2. 验证解压是否成功
   - 解压后，目录结构应为：`D:\apache-maven-3.9.6\bin`、`D:\apache-maven-3.9.6\conf` 等

## 3. 配置 Maven 环境变量

### 步骤 1：打开环境变量配置
1. 右键点击 "此电脑"（或 "计算机"）
2. 选择 "属性"
3. 点击 "高级系统设置"
4. 点击 "环境变量"

### 步骤 2：配置 MAVEN_HOME
1. 在 "系统变量" 部分，点击 "新建"
2. 变量名：`MAVEN_HOME`
3. 变量值：Maven 解压目录（如 `D:\apache-maven-3.9.6`）
4. 点击 "确定"

### 步骤 3：配置 Path
1. 在 "系统变量" 部分，找到并选中 `Path` 变量
2. 点击 "编辑"
3. 点击 "新建"
4. 输入：`%MAVEN_HOME%\bin`
5. 点击 "确定"
6. 点击 "确定" 关闭环境变量窗口
7. 点击 "确定" 关闭系统属性窗口

## 4. 验证 Maven 安装

1. 打开新的 PowerShell 窗口（必须新建，否则环境变量不生效）
2. 运行命令：`mvn -version`
3. 如果输出 Maven 版本信息，说明安装成功

**成功示例输出：**
```
Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
Maven home: D:\apache-maven-3.9.6
git revision: bc0240f3c744dd6b6ec2920b3cd08dcc295161ae
Java version: 1.8.0_391, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk1.8.0_391\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

## 5. 使用 IDEA 内置 Maven（备选方案）

如果您不想安装本地 Maven，可以使用 IDEA 内置的 Maven：

1. 打开 IDEA，点击 `File` -> `Settings`
2. 选择 `Build, Execution, Deployment` -> `Build Tools` -> `Maven`
3. 在 `Maven home directory` 选择 `Bundled (Maven 3)`
4. 点击 `Apply` 和 `OK`
5. 右键点击 `pom.xml` -> `Maven` -> `Reload Project`

## 6. 配置 Maven 镜像（加速下载）

1. 找到 Maven 配置文件：`D:\apache-maven-3.9.6\conf\settings.xml`
2. 右键点击，选择 "编辑"
3. 找到 `<mirrors>` 标签
4. 在 `<mirrors>` 标签内添加阿里云镜像：

```xml
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>central</mirrorOf>
    <name>阿里云公共仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

5. 保存文件

## 7. 常见问题

### Q1：命令 `mvn -version` 仍无法识别
A：请确保：
- 已正确配置环境变量
- 已打开新的 PowerShell 窗口
- 检查 `MAVEN_HOME` 路径是否正确
- 检查 `Path` 中是否添加了 `%MAVEN_HOME%\bin`

### Q2：Maven 下载依赖缓慢
A：配置阿里云镜像（参考步骤 6）

### Q3：IDEA 中 Maven 仍然报错
A：尝试：
- 重新导入 Maven 项目
- 清理 IDEA 缓存：`File` -> `Invalidate Caches` -> `Invalidate and Restart`
- 检查 JDK 版本是否与 `pom.xml` 中配置的一致

## 8. 后续步骤

Maven 安装成功后，回到 IDEA 中：
1. 右键点击 `pom.xml` -> `Maven` -> `Reload Project`
2. 等待依赖下载完成
3. 运行 `HomestayApplication.java`

## 9. 联系方式

如果遇到问题，请联系开发团队：
- 邮箱：service@homestay.com
- 电话：400-123-4567
