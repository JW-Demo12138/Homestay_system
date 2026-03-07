# IDEA Maven配置指南

## 1. 配置阿里云镜像

### 方法一：全局配置（推荐）
1. 打开IDEA，点击 `File` -> `Settings` -> `Build, Execution, Deployment` -> `Build Tools` -> `Maven`
2. 在右侧找到 `User settings file`，点击右侧的文件夹图标
3. 打开 `settings.xml` 文件（如果不存在，创建一个）
4. 在 `<settings>` 标签内添加以下配置：

```xml
<mirrors>
    <mirror>
        <id>aliyunmaven</id>
        <name>阿里云公共仓库</name>
        <url>https://maven.aliyun.com/repository/public</url>
        <mirrorOf>*</mirrorOf>
    </mirror>
</mirrors>
```

### 方法二：项目级配置
已经在 `pom.xml` 中添加了阿里云仓库配置，无需额外操作。

## 2. 更新项目依赖

1. 在IDEA右侧边栏找到 `Maven` 面板
2. 展开项目节点
3. 右键点击 `Lifecycle` -> `clean`，然后点击 `Run Maven Build`
4. 右键点击 `Lifecycle` -> `install`，然后点击 `Run Maven Build`
5. 等待依赖下载完成（首次运行可能需要几分钟）

## 3. 解决依赖爆红问题

如果依赖仍然爆红：

1. 点击IDEA顶部菜单栏的 `File` -> `Invalidate Caches / Restart...`
2. 选择 `Invalidate and Restart`，等待IDEA重启
3. 重启后，IDEA会自动重新索引依赖

## 4. 运行项目

1. 找到 `HomestayApplication.java` 文件
2. 右键点击该文件
3. 选择 `Run 'HomestayApplication'` 或 `Debug 'HomestayApplication'`

## 5. 常见问题

### 问题1：IDEA无法识别Maven
**解决方法**：
1. 点击 `File` -> `Settings` -> `Build, Execution, Deployment` -> `Build Tools` -> `Maven`
2. 确保 `Maven home directory` 选择了正确的Maven路径（建议使用IDEA内置Maven）

### 问题2：依赖下载缓慢
**解决方法**：
1. 配置阿里云镜像（见步骤1）
2. 检查网络连接
3. 尝试更换其他镜像源

### 问题3：IDEA提示"程序包不存在"
**解决方法**：
1. 确保已经执行了 `mvn clean install`
2. 尝试 `File` -> `Invalidate Caches / Restart...`
3. 检查依赖版本是否正确
