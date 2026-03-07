# 修复数据库表结构与实体类映射问题

## 问题分析

根据用户提供的数据库表结构（`sql.sql`），发现以下关键问题：

### 1. 数据库名称不匹配
- **配置文件**：`homestay`
- **实际数据库**：`hotel`

### 2. 表结构完全不同
| 代码期望的表 | 实际数据库表 |
|-------------|--------------|
| `order`     | `order_info` |
| `user`      | `user_info`  |

### 3. 字段名称差异
#### 订单表差异
| 代码字段 | 实际数据库字段 |
|----------|----------------|
| `id`     | `order_id`     |
| `userId` | `user_id`      |
| `status` | `order_status` |
| `price`  | `order_cost`   |
| `payMethod` | 不存在      |
| `payTime` | 不存在       |

## 修复方案

### 方案1：修改代码适配现有数据库（推荐）

1. **修改数据库配置**
   - 更新 `application.yml` 中的数据库名称为 `hotel`

2. **修改实体类**
   - 修改 `Order` 实体类：
     - 表名：`@TableName("order_info")`
     - 字段：适配 `order_info` 表结构
   - 修改 `User` 实体类：
     - 表名：`@TableName("user_info")`
     - 字段：适配 `user_info` 表结构

3. **修改Mapper接口**
   - 更新 `UserMapper.xml` 中的表名为 `user_info`

4. **修改Service实现**
   - 更新业务逻辑，适配新的表结构和字段名称

### 方案2：修改数据库适配代码

1. **创建新的数据库**：创建 `homestay` 数据库
2. **创建表结构**：根据实体类创建对应的表结构
3. **导入数据**：如有需要，从 `hotel` 数据库导入数据

## 详细修改计划

### 1. 修改数据库配置
```yaml
# 修改前
url: jdbc:mysql://localhost:3306/homestay?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai

# 修改后
url: jdbc:mysql://localhost:3306/hotel?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
```

### 2. 修改Order实体类
```java
@TableName("order_info")
public class Order {
    @TableId(name = "order_id", type = IdType.AUTO)
    private Long orderId;
    
    @TableField("user_id")
    private Long userId;
    
    // 其他字段适配...
    @TableField("order_status")
    private Integer orderStatus;
    
    @TableField("order_cost")
    private Double orderCost;
    
    // 移除数据库中不存在的字段
    // private String payMethod;
    // private Date payTime;
    
    // Getter和Setter方法适配...
}
```

### 3. 修改User实体类
```java
@TableName("user_info")
public class User {
    @TableId(name = "user_id", type = IdType.AUTO)
    private Long userId;
    
    // 其他字段适配...
    
    // Getter和Setter方法适配...
}
```

### 4. 修改UserMapper.xml
```xml
<select id="selectByUsername" resultType="com.homestay.entity.User">
    SELECT * FROM user_info WHERE username = #{username}
</select>

<select id="selectByPhone" resultType="com.homestay.entity.User">
    SELECT * FROM user_info WHERE phone = #{phone}
</select>

<select id="selectByLoginId" resultType="com.homestay.entity.User">
    SELECT * FROM user_info WHERE username = #{loginId} OR phone = #{loginId}
</select>
```

### 5. 修改Service实现
- 更新 `OrderServiceImpl` 中的业务逻辑
- 更新 `AuthServiceImpl` 中的用户认证逻辑

## 预期效果

修复后，系统将能够正确连接到 `hotel` 数据库，并使用 `order_info` 和 `user_info` 表进行数据操作，不再出现字段不存在的SQL错误。