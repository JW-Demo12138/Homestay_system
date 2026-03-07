# 民宿预订系统 - 前端

## 项目介绍

这是一个基于 Vue 3 + Vite + Element Plus 开发的民宿预订系统前端项目。

## 技术栈

- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **地图服务**: 百度地图 API

## 项目结构

```
frontend/
├── src/
│   ├── api/                    # API接口封装
│   │   ├── auth.js             # 认证相关
│   │   ├── user.js             # 用户相关
│   │   ├── homestay.js         # 民宿相关
│   │   ├── order.js            # 订单相关
│   │   ├── rating.js           # 评价相关
│   │   ├── city.js             # 城市相关
│   │   ├── dashboard.js        # 仪表盘相关
│   │   └── geocoding.js       # 地理编码相关
│   ├── assets/                 # 静态资源
│   ├── components/             # 公共组件
│   ├── config/                # 配置文件
│   │   └── index.js           # 全局配置
│   ├── router/                # 路由配置
│   │   └── index.js           # 路由定义和守卫
│   ├── store/                 # 状态管理
│   │   ├── user.js            # 用户状态
│   │   ├── homestay.js        # 民宿状态
│   │   └── order.js           # 订单状态
│   ├── utils/                 # 工具函数
│   │   ├── request.js         # Axios封装
│   │   └── index.js          # 通用工具函数
│   ├── views/                 # 页面组件
│   │   ├── auth/              # 认证页面
│   │   │   ├── Login.vue      # 登录页
│   │   │   └── Register.vue   # 注册页
│   │   ├── home/              # 首页
│   │   │   └── Home.vue      # 首页
│   │   ├── homestay/          # 民宿相关页面
│   │   │   ├── List.vue       # 民宿列表
│   │   │   └── Detail.vue     # 民宿详情
│   │   ├── landlord/           # 房东页面
│   │   │   ├── Dashboard.vue  # 房东仪表盘
│   │   │   ├── MyHomestays.vue # 我的民宿
│   │   │   ├── CreateHomestay.vue # 发布民宿
│   │   │   └── MyOrders.vue   # 房东订单
│   │   └── user/              # 用户页面
│   │       ├── Profile.vue    # 个人中心
│   │       └── MyOrders.vue   # 我的订单
│   ├── App.vue
│   └── main.js
├── public/
├── index.html
├── package.json
├── vite.config.js
└── .gitignore
```

## 功能特性

### 已实现功能

1. **用户认证系统**
   - 用户登录（支持用户名/手机号登录）
   - 用户注册
   - 角色选择（游客/房东/管理员）
   - Token自动刷新
   - 路由守卫

2. **民宿浏览和搜索**
   - 首页展示推荐民宿
   - 民宿列表（支持分页）
   - 民宿详情
   - 多条件搜索（关键词、价格、标签）
   - 热门城市展示

3. **房东功能**
   - 房东仪表盘（统计数据）
   - 发布民宿（支持百度地图坐标获取）
   - 管理民宿（查看、编辑、删除）
   - 查看订单

4. **用户功能**
   - 个人中心
   - 更新个人信息
   - 修改密码
   - 上传头像
   - 我的订单
   - 订单支付、取消、评价

5. **其他功能**
   - 百度地图集成（显示民宿位置）
   - 地理编码（地址转坐标）
   - 响应式设计
   - 统一错误处理
   - 加载状态提示

## 安装和运行

### 1. 安装依赖

```bash
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

项目将在 `http://localhost:3000` 启动

### 3. 构建生产版本

```bash
npm run build
```

### 4. 预览生产版本

```bash
npm run preview
```

## 配置说明

### 后端API地址

在 `src/config/index.js` 中配置后端API地址：

```javascript
export const BASE_URL = 'http://localhost:8080'
```

### 高德地图API

高德地图API已在 `index.html` 中引入：

```html
<script src="https://webapi.amap.com/maps?v=2.0&key=b95e42463f509c7e63a1774fe28aac2f"></script>
```

### Vite代理配置

在 `vite.config.js` 中配置了API代理：

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    },
    '/uploads': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

## 用户角色

系统支持三种用户角色：

1. **游客 (TOURIST)**
   - 浏览民宿
   - 搜索民宿
   - 预订民宿
   - 查看订单
   - 评价订单

2. **房东 (LANDLORD)**
   - 游客所有功能
   - 发布民宿
   - 管理民宿
   - 查看订单

3. **管理员 (ADMIN)**
   - 所有功能
   - 用户管理
   - 数据统计

## API接口说明

详细的API接口文档请参考 `FRONTEND_API_DOCUMENTATION.md`

## 注意事项

1. **后端服务**
   - 确保后端服务已启动（`http://localhost:8080`）
   - 后端数据库需要正确配置

2. **Token管理**
   - Access Token 有效期：2小时
   - Refresh Token 有效期：90天
   - 前端会自动刷新过期的Token

3. **图片上传**
   - 头像上传限制：2MB
   - 支持格式：jpg、jpeg、png、gif

4. **百度地图**
   - 确保网络可以访问百度地图API
   - 地图功能需要有效的AK

## 开发建议

1. **代码规范**
   - 使用 Composition API
   - 组件使用 `<script setup>` 语法
   - 遵循 Vue 3 最佳实践

2. **样式管理**
   - 使用 scoped 样式
   - 保持样式的一致性
   - 参考爱彼迎的设计风格

3. **错误处理**
   - 所有API调用都要有错误处理
   - 使用 Element Plus 的 ElMessage 提示用户
   - 记录错误日志

4. **性能优化**
   - 合理使用 computed 和 watch
   - 避免不必要的重渲染
   - 使用 v-show 替代 v-if（频繁切换）

## 后续开发计划

1. **评价系统**
   - 完善评价功能
   - 评分展示

2. **管理后台**
   - 用户管理
   - 投诉处理
   - 数据统计

3. **高级功能**
   - 民宿收藏
   - 消息通知
   - 搜索历史

## 常见问题

### 1. npm install 失败

尝试使用淘宝镜像：

```bash
npm install --registry=https://registry.npmmirror.com
```

### 2. 跨域问题

确保 Vite 代理配置正确，后端已配置 CORS

### 3. 地图不显示

检查网络连接和百度地图AK是否有效

### 4. Token过期

前端会自动刷新Token，如果失败会跳转到登录页

## 联系方式

如有问题，请联系开发团队。
