# 使用现有 Maven 配置指南

## 1. 确认 Maven 目录

根据您提供的信息，您的 Maven 安装目录是：
```
E:\hote\No.364Hotel-management-system-master
```

请先确认该目录下是否包含 `bin`、`conf` 等 Maven 核心目录：
```
E:\hote\No.364Hotel-management-system-master\bin
E:\hote\No.364Hotel-management-system-master\conf
E:\hote\No.364Hotel-management-system-master\lib
```

## 2. 配置 Maven 环境变量

### 步骤 1：打开环境变量配置
1. 右键点击 "此电脑"（或 "计算机"）
2. 选择 "属性"
3. 点击 "高级系统设置"
4. 点击 "环境变量"

### 步骤 2：配置 MAVEN_HOME
1. 在 "系统变量" 部分，点击 "新建"
2. 变量名：`MAVEN_HOME`
3. 变量值：`E:\hote\No.364Hotel-management-system-master`
4. 点击 "确定"

### 步骤 3：配置 Path
1. 在 "系统变量" 部分，找到并选中 `Path` 变量
2. 点击 "编辑"
3. 点击 "新建"
4. 输入：`%MAVEN_HOME%\bin`
5. 点击 "确定"
6. 点击 "确定" 关闭环境变量窗口
7. 点击 "确定" 关闭系统属性窗口

## 3. 验证 Maven 配置

1. 打开新的 PowerShell 窗口（必须新建，否则环境变量不生效）
2. 运行命令：`mvn -version`
3. 如果输出 Maven 版本信息，说明配置成功

**成功示例输出：**
```
Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
Maven home: E:\hote\No.364Hotel-management-system-master
git revision: bc0240f3c744dd6b6ec2920b3cd08dcc295161ae
Java version: 1.8.0_391, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk1.8.0_391\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

## 4. 在 IDEA 中使用现有 Maven

### 步骤 1：打开 IDEA 设置
1. 打开 IDEA
2. 点击顶部菜单栏的 `File` -> `Settings`

### 步骤 2：配置 Maven 路径
1. 在左侧选择 `Build, Execution, Deployment` -> `Build Tools` -> `Maven`
2. 在 `Maven home directory` 选择 `Custom`
3. 点击 `...` 按钮，浏览并选择您的 Maven 目录：`E:\hote\No.364Hotel-management-system-master`
4. 点击 `Apply` 和 `OK` 保存设置

### 步骤 3：刷新 Maven 依赖
1. 右键点击 `pom.xml` 文件
2. 选择 `Maven` -> `Reload Project`
3. 等待 Maven 下载所有依赖

## 5. 直接使用现有 Maven 命令

如果您不想配置环境变量，也可以直接使用完整路径运行 Maven 命令：

```powershell
"E:\hote\No.364Hotel-management-system-master\bin\mvn.cmd" -version
"E:\hote\No.364Hotel-management-system-master\bin\mvn.cmd" clean install
"E:\hote\No.364Hotel-management-system-master\bin\mvn.cmd" spring-boot:run
```

## 6. 配置 Maven 镜像（加速下载）

1. 找到 Maven 配置文件：`E:\hote\No.364Hotel-management-system-master\conf\settings.xml`
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

## 7. 后续步骤

1. 配置完成后，在 IDEA 中重新加载 Maven 项目
2. 等待依赖下载完成
3. 运行 `HomestayApplication.java` 主类
4. 查看控制台输出，确认项目是否成功启动

## 8. 常见问题

### Q1：命令 `mvn -version` 仍无法识别
A：请确保：
- 已正确配置环境变量
- 已打开新的 PowerShell 窗口
- 检查 `MAVEN_HOME` 路径是否正确

### Q2：IDEA 中 Maven 仍然报错
A：尝试：
- 重新导入 Maven 项目
- 清理 IDEA 缓存：`File` -> `Invalidate Caches` -> `Invalidate and Restart`
- 检查 JDK 版本是否与 `pom.xml` 中配置的一致

## 9. 联系方式

如果遇到问题，请联系开发团队：
- 邮箱：service@homestay.com
- 电话：400-123-4567
