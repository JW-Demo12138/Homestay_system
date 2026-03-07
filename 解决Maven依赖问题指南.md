# 解决 Maven 依赖问题详细指南

## 1. 问题分析

从截图中可以看到，pom.xml 文件显示了多个错误：
- 找不到项目 `org.springframework.boot:spring-boot-starter-parent:2.7.17`
- 找不到依赖项 `mysql:mysql-connector-java:`
- 找不到插件 `org.springframework.boot:spring-boot-maven-plugin:`

这些错误表明 Maven 没有成功下载和解析项目依赖。

## 2. 解决方案

### 步骤 1：检查 Maven 配置

1. 确认已正确配置 Maven 环境（参考之前的配置指南）
2. 确保 `settings.xml` 中配置了正确的镜像源（推荐阿里云镜像）
3. 检查网络连接是否正常

### 步骤 2：强制刷新 Maven 依赖

方法一：使用 IDEA Maven 工具栏
1. 打开 IDEA 右侧的 `Maven` 工具栏
2. 点击 `Lifecycle` 展开生命周期
3. 右键点击 `clean` -> `Run Maven Build`
4. 右键点击 `install` -> `Run Maven Build`
5. 等待 Maven 下载所有依赖

方法二：使用 IDEA 右键菜单
1. 右键点击 `pom.xml` 文件
2. 选择 `Maven` -> `Clean`
3. 然后选择 `Maven` -> `Install`
4. 等待 Maven 下载所有依赖

方法三：使用命令行（推荐）
1. 打开 PowerShell 窗口
2. 进入后端项目目录：`cd E:\Homestay system\backend`
3. 运行命令：
   ```powershell
   mvn clean install -U -DskipTests
   ```
   - `-U`：强制更新快照依赖
   - `-DskipTests`：跳过测试，加速构建

### 步骤 3：配置阿里云镜像（关键）

如果依赖下载缓慢或失败，配置阿里云镜像可以显著改善：

1. 找到 Maven 配置文件：
   - 本地 Maven：`E:\hote\No.364Hotel-management-system-master\conf\settings.xml`
   - IDEA 内置 Maven：通常在 `~/.m2/settings.xml`

2. 在 `<mirrors>` 标签内添加：
   ```xml
   <mirror>
       <id>aliyunmaven</id>
       <mirrorOf>central</mirrorOf>
       <name>阿里云公共仓库</name>
       <url>https://maven.aliyun.com/repository/public</url>
   </mirror>
   <mirror>
       <id>aliyunmaven-spring</id>
       <mirrorOf>spring</mirrorOf>
       <name>阿里云 Spring 仓库</name>
       <url>https://maven.aliyun.com/repository/spring</url>
   </mirror>
   ```

3. 保存文件后，重新执行步骤 2

### 步骤 4：检查 Maven 仓库状态

如果仍然有问题，检查 Maven 本地仓库：
1. 找到本地仓库位置（通常在 `~/.m2/repository`）
2. 删除 `org/springframework/boot` 目录
3. 删除 `mysql` 目录
4. 重新执行步骤 2，让 Maven 重新下载这些依赖

## 3. 解决特定问题

### 问题 1：找不到 spring-boot-starter-parent

确保 `pom.xml` 中的 Spring Boot 版本号正确：
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.17</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

### 问题 2：找不到 mysql-connector-java

MySQL 驱动依赖缺少版本号，添加版本号：
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
    <scope>runtime</scope>
</dependency>
```

### 问题 3：找不到 spring-boot-maven-plugin

Spring Boot Maven 插件缺少版本号，添加版本号：
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>2.7.17</version>
</plugin>
```

## 4. 验证解决方案

1. 执行 `mvn clean install -U -DskipTests` 后，检查控制台输出
2. 确保没有 `ERROR` 级别日志
3. 回到 IDEA，检查 pom.xml 中的错误是否消失
4. 尝试运行 `HomestayApplication.java` 主类

## 5. 常见问题排查

### Q1：Maven 命令执行失败
A：检查：
- Maven 环境变量是否正确配置
- 网络连接是否正常
- 镜像配置是否正确

### Q2：IDEA 中仍显示错误，但命令行构建成功
A：尝试：
- 重新导入 Maven 项目
- 清理 IDEA 缓存：`File` -> `Invalidate Caches` -> `Invalidate and Restart`

### Q3：依赖下载成功，但 IDEA 仍报错
A：检查：
- IDEA 是否使用了正确的 Maven 配置
- 检查 `File` -> `Settings` -> `Build, Execution, Deployment` -> `Build Tools` -> `Maven`

## 6. 后续步骤

解决依赖问题后，按照之前的 Java 版本配置指南，确保 IDEA 和 Maven 的 Java 版本配置一致，然后尝试运行项目。

如果问题仍然存在，请提供完整的错误日志，以便进一步分析。
