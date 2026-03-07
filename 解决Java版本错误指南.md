# 解决 "java: 错误: 不支持发行版本 5" 详细指南

## 1. 确认 pom.xml 已修改

我已经修改了 `pom.xml` 文件，添加了 Java 版本配置：
```xml
<properties>
    <java.version>1.8</java.version>
    <mybatis-plus.version>3.5.3.1</mybatis-plus.version>
    <maven.compiler.source>${java.version}</maven.compiler.source>
    <maven.compiler.target>${java.version}</maven.compiler.target>
</properties>
```

## 2. 在 IDEA 中重新加载 Maven 项目

**必须执行此步骤**：
1. 右键点击 `pom.xml` 文件
2. 选择 `Maven` -> `Reload Project`
3. 等待 Maven 重新加载依赖和配置

## 3. 检查 IDEA 项目 JDK 配置

### 步骤 1：检查 Project SDK
1. `File` -> `Project Structure`
2. 在左侧选择 `Project`
3. 确认 `Project SDK` 为 JDK 8 或以上版本
4. 确认 `Project language level` 为 `8 - Lambdas, type annotations etc.`
5. 点击 `Apply` 和 `OK`

### 步骤 2：检查 Module SDK
1. `File` -> `Project Structure`
2. 在左侧选择 `Modules`
3. 选择您的项目模块
4. 切换到 `Dependencies` 标签页
5. 确认 `Module SDK` 为 JDK 8 或以上版本
6. 点击 `Apply` 和 `OK`

## 4. 检查 IDEA 编译器设置

### 步骤 1：打开编译器设置
1. `File` -> `Settings`
2. 在左侧选择 `Build, Execution, Deployment` -> `Compiler` -> `Java Compiler`

### 步骤 2：设置编译版本
1. 在 `Project bytecode version` 选择 `8`
2. 在 `Module per-module bytecode version` 部分：
   - 确保所有模块的 `Target bytecode version` 都设置为 `8`
   - 如果没有显示模块，点击 `Show advanced settings`
3. 点击 `Apply` 和 `OK`

## 5. 清理并重新构建项目

1. `Build` -> `Clean Project`
2. `Build` -> `Rebuild Project`
3. 等待项目构建完成

## 6. 直接使用 Maven 命令指定版本（备选方案）

如果以上步骤仍无效，可以在运行 Maven 命令时直接指定 Java 版本：

```powershell
mvn clean install -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8
mvn spring-boot:run -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8
```

## 7. 检查 JAVA_HOME 环境变量

确保系统的 `JAVA_HOME` 环境变量指向 JDK 8 或以上版本：

1. 打开 `环境变量` 配置
2. 检查 `JAVA_HOME` 变量值是否为 JDK 8 或以上版本的安装目录
3. 例如：`C:\Program Files\Java\jdk1.8.0_391`

## 8. 常见问题排查

### Q1：仍然报错
A：尝试清理 IDEA 缓存：
- `File` -> `Invalidate Caches` -> `Invalidate and Restart`

### Q2：Project SDK 下拉菜单中没有 JDK 8
A：点击 `New` -> `JDK`，选择 JDK 8 安装目录

### Q3：模块编译版本无法修改
A：确保已重新加载 Maven 项目，或手动修改：
- 右键模块 -> `Open Module Settings` -> `Modules` -> 选择模块 -> `Sources` -> `Language level` -> 选择 `8`

## 9. 验证解决方案

1. 重新运行 `HomestayApplication.java`
2. 检查控制台输出，确认不再出现 "不支持发行版本 5" 错误
3. 项目应该能正常编译和运行

## 10. 联系方式

如果按照以上步骤仍无法解决问题，请联系开发团队：
- 邮箱：service@homestay.com
- 电话：400-123-4567
