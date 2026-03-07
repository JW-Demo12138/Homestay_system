# 民宿预订系统 - 前端开发文档

## 📋 目录

- [项目概述](#项目概述)
- [环境准备](#环境准备)
- [API接口文档](#api接口文档)
- [认证机制](#认证机制)
- [数据格式](#数据格式)
- [前端开发建议](#前端开发建议)
- [示例代码](#示例代码)
- [常见问题](#常见问题)

---

## 项目概述

### 后端服务信息

- **基础URL**: `http://localhost:8080`
- **认证方式**: JWT Token
- **数据格式**: JSON
- **字符编码**: UTF-8

### 技术栈建议

- **框架**: Vue 3 / React / Angular
- **UI库**: Element Plus / Ant Design / Material-UI
- **HTTP客户端**: Axios / Fetch
- **状态管理**: Pinia / Redux / Vuex
- **路由**: Vue Router / React Router

---

## 环境准备

### 1. 确保后端服务已启动

```bash
# 检查后端服务是否运行
curl http://localhost:8080/api/city/hot
```

### 2. 安装HTTP客户端（推荐Axios）

```bash
# npm
npm install axios

# yarn
yarn add axios
```

### 3. 配置API基础地址

```javascript
// src/config/api.js
export const BASE_URL = 'http://localhost:8080'
```

---

## API接口文档

### 🔐 认证模块 (`/api/auth`)

#### 1. 用户登录

```http
POST /api/auth/login
Content-Type: application/json
```

**请求体：**
```json
{
  "username": "admin",        // 用户名（可选）
  "phone": "13800138000",    // 手机号（可选）
  "password": "123456",      // 密码
  "role": "管理员"            // 角色：游客/房东/管理员
}
```

**响应：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "phone": "13800138000",
      "role": "ADMIN",
      "status": 1,
      "createTime": "2024-01-01T00:00:00"
    }
  }
}
```

---

#### 2. 用户注册

```http
POST /api/auth/register
Content-Type: application/json
```

**请求体：**
```json
{
  "username": "newuser",
  "password": "123456",
  "phone": "13900139000",
  "role": "游客"
}
```

**响应：**
```json
{
  "code": 200,
  "message": "注册成功"
}
```

---

#### 3. 刷新令牌

```http
POST /api/auth/refresh
Content-Type: application/json
```

**请求体：**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**响应：**
```json
{
  "code": 200,
  "message": "Token刷新成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

#### 4. 用户登出

```http
POST /api/auth/logout
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "message": "登出成功"
}
```

---

### 👤 用户模块 (`/api/user`)

#### 1. 获取当前用户信息

```http
GET /api/user/info
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "message": "获取用户信息成功",
  "data": {
    "id": 1,
    "username": "admin",
    "phone": "13800138000",
    "role": "ADMIN",
    "status": 1,
    "avatar": "https://picsum.photos/id/1/100/100",
    "createTime": "2024-01-01T00:00:00"
  }
}
```

---

#### 2. 更新用户信息

```http
PUT /api/user/update
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "username": "newusername",
  "phone": "13900139000"
}
```

---

#### 3. 修改密码

```http
POST /api/user/change-password
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

---

#### 4. 上传头像

```http
POST /api/user/upload-avatar
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

**请求体：**
```
avatar: <file>
```

---

#### 5. 获取用户列表（管理员）

```http
GET /api/user/admin/list?page=1&size=10&keyword=admin&role=ADMIN
Authorization: Bearer <token>
```

---

#### 6. 更新用户状态（管理员）

```http
PUT /api/user/admin/status/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "status": 0
}
```

---

### 🏠 民宿模块 (`/api/homestay`)

#### 1. 获取民宿列表

```http
GET /api/homestay/list?page=1&size=10&keyword=杭州&minPrice=100&maxPrice=500&tags=山水风光
```

**响应：**
```json
{
  "code": 200,
  "message": "获取民宿列表成功",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "杭州西湖民宿",
        "address": "杭州市西湖区",
        "latitude": 30.2741,
        "longitude": 120.1551,
        "price": 299.00,
        "roomType": "大床房",
        "facility": "WiFi,空调,热水",
        "imageUrl": "https://picsum.photos/200/150",
        "description": "风景优美的西湖民宿",
        "status": 1,
        "ownerId": 2,
        "landlordUsername": "房东张三",
        "landlordPhone": "13800138001"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

---

#### 2. 获取民宿详情

```http
GET /api/homestay/detail/{id}
```

**响应：**
```json
{
  "code": 200,
  "message": "获取民宿详情成功",
  "data": {
    "id": 1,
    "name": "杭州西湖民宿",
    "address": "杭州市西湖区",
    "latitude": 30.2741,
    "longitude": 120.1551,
    "price": 299.00,
    "roomType": "大床房",
    "facility": "WiFi,空调,热水",
    "imageUrl": "https://picsum.photos/200/150",
    "description": "风景优美的西湖民宿",
    "status": 1,
    "ownerId": 2,
    "landlordUsername": "房东张三",
    "landlordPhone": "13800138001"
  }
}
```

---

#### 3. 创建民宿（房东）

```http
POST /api/homestay/create
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "name": "新民宿",
  "address": "杭州市西湖区",
  "latitude": 30.2741,
  "longitude": 120.1551,
  "price": 299.00,
  "roomType": "大床房",
  "facility": "WiFi,空调,热水",
  "imageUrl": "https://picsum.photos/200/150",
  "description": "风景优美的民宿",
  "status": 1
}
```

---

#### 4. 更新民宿（房东）

```http
PUT /api/homestay/update/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

---

#### 5. 删除民宿（房东）

```http
DELETE /api/homestay/delete/{id}
Authorization: Bearer <token>
```

---

#### 6. 搜索民宿

```http
GET /api/homestay/search?keyword=杭州&minPrice=100&maxPrice=500
```

---

#### 7. 获取推荐民宿

```http
GET /api/homestay/recommend
```

---

#### 8. 获取房东的民宿列表（房东）

```http
GET /api/homestay/landlord/list
Authorization: Bearer <token>
```

---

#### 9. 批量导入民宿（房东）

```http
POST /api/homestay/import
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
[
  {
    "name": "民宿1",
    "address": "地址1",
    "price": 299.00,
    "roomType": "大床房"
  },
  {
    "name": "民宿2",
    "address": "地址2",
    "price": 399.00,
    "roomType": "双床房"
  }
]
```

---

### 📝 订单模块 (`/api/order`)

#### 1. 创建订单

```http
POST /api/order/create
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "userId": 1,
  "homestayId": 1,
  "checkInDate": "2024-02-01",
  "checkOutDate": "2024-02-03",
  "price": 598.00
}
```

**响应：**
```json
{
  "code": 200,
  "message": "创建订单成功",
  "data": {
    "id": 1,
    "userId": 1,
    "homestayId": 1,
    "checkInDate": "2024-02-01",
    "checkOutDate": "2024-02-03",
    "price": 598.00,
    "status": "待支付",
    "createTime": "2024-01-15T10:00:00"
  }
}
```

---

#### 2. 获取用户订单列表

```http
GET /api/order/list
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "message": "获取用户订单成功",
  "data": [
    {
      "id": 1,
      "homestayImage": "https://picsum.photos/200/150",
      "homestayName": "杭州西湖民宿",
      "homestayAddress": "杭州市西湖区",
      "checkInDate": "2024-02-01",
      "checkOutDate": "2024-02-03",
      "guestCount": 1,
      "price": 598.00,
      "status": "待支付",
      "createTime": "2024-01-15T10:00:00"
    }
  ]
}
```

---

#### 3. 获取房东订单列表

```http
GET /api/order/landlord/list
Authorization: Bearer <token>
```

---

#### 4. 获取订单详情

```http
GET /api/order/detail/{id}
Authorization: Bearer <token>
```

---

#### 5. 更新订单状态

```http
PUT /api/order/update/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "status": "已支付"
}
```

---

#### 6. 支付订单

```http
POST /api/order/pay/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "payMethod": "微信支付"
}
```

---

#### 7. 取消订单

```http
POST /api/order/cancel/{id}
Authorization: Bearer <token>
```

---

#### 8. 评价订单

```http
POST /api/order/comment/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "rating": 5,
  "content": "非常满意的住宿体验！"
}
```

---

### ⭐ 评分模块 (`/api/rating`)

#### 1. 创建评分

```http
POST /api/rating/create
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "homestayId": 1,
  "userId": 1,
  "rating": 5,
  "comment": "非常满意的住宿体验！"
}
```

---

#### 2. 获取民宿评分列表

```http
GET /api/rating/homestay/{id}
```

**响应：**
```json
{
  "code": 200,
  "message": "获取评分列表成功",
  "data": [
    {
      "ratingId": 1,
      "homestayId": 1,
      "userId": 1,
      "rating": 5,
      "comment": "非常满意的住宿体验！",
      "createTime": "2024-01-15T10:00:00"
    }
  ]
}
```

---

#### 3. 获取民宿平均评分

```http
GET /api/rating/average/{id}
```

**响应：**
```json
{
  "code": 200,
  "message": "获取平均评分成功",
  "data": 4.5
}
```

---

### 💬 投诉模块 (`/api/complaint`)

#### 1. 获取投诉列表（管理员）

```http
GET /api/complaint/list?page=1&size=10&status=0
Authorization: Bearer <token>
```

---

#### 2. 更新投诉状态（管理员）

```http
PUT /api/complaint/status/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "status": 1
}
```

---

### 🏙️ 城市模块 (`/api/city`)

#### 1. 获取热门城市

```http
GET /api/city/hot
```

**响应：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "杭州",
      "province": "浙江省",
      "latitude": 30.2741,
      "longitude": 120.1551,
      "isHot": 1,
      "sortOrder": 1
    }
  ]
}
```

---

#### 2. 获取所有城市

```http
GET /api/city/all
```

---

#### 3. 根据名称搜索城市

```http
GET /api/city/search?name=杭州
```

---

### 🏷️ 标签模块 (`/api/tag`)

#### 1. 获取所有特色标签

```http
GET /api/tag/feature
```

**响应：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "山水风光",
      "description": "依山傍水，风景优美",
      "status": 1,
      "sortOrder": 1
    }
  ]
}
```

---

#### 2. 获取所有房型

```http
GET /api/tag/room-type
```

---

### 🌟 民宿特色模块 (`/api/feature`)

#### 1. 获取特色内容列表（管理员）

```http
GET /api/feature/list?page=1&size=10&status=0
Authorization: Bearer <token>
```

---

#### 2. 更新特色内容状态（管理员）

```http
PUT /api/feature/status/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

**请求体：**
```json
{
  "status": 1
}
```

---

### 📊 仪表盘模块 (`/api/dashboard`)

#### 1. 获取统计数据（管理员）

```http
GET /api/dashboard/stats
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "message": "获取统计数据成功",
  "data": {
    "totalUsers": 100,
    "totalHomestays": 50,
    "totalOrders": 200,
    "totalRevenue": 50000
  }
}
```

---

#### 2. 获取最近订单（管理员）

```http
GET /api/dashboard/recent-orders
Authorization: Bearer <token>
```

---

#### 3. 获取最近民宿（管理员）

```http
GET /api/dashboard/recent-homestays
Authorization: Bearer <token>
```

---

### 🗺️ 地理编码模块 (`/api/geocoding`)

#### 1. 地址转坐标

```http
GET /api/geocoding/address-to-coords?address=杭州市西湖区&city=杭州
```

**响应：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "address": "杭州市西湖区",
    "longitude": 120.1551,
    "latitude": 30.2741,
    "coordinateSystem": "BD-09"
  }
}
```

---

## 认证机制

### JWT Token 说明

- **访问令牌有效期**: 30天
- **刷新令牌有效期**: 90天
- **令牌类型**: Bearer Token

### 请求头格式

```javascript
headers: {
  'Authorization': 'Bearer ' + token,
  'Content-Type': 'application/json'
}
```

### Token 存储建议

```javascript
// 存储在 localStorage
localStorage.setItem('accessToken', token)
localStorage.setItem('refreshToken', refreshToken)

// 存储在 sessionStorage（关闭浏览器后清除）
sessionStorage.setItem('accessToken', token)

// 存储在 Cookie（更安全，防止XSS攻击）
document.cookie = `accessToken=${token}; path=/; secure; httponly`
```

### Token 刷新流程

```javascript
// 1. 检查token是否过期
function isTokenExpired(token) {
  const payload = JSON.parse(atob(token.split('.')[1]))
  return Date.now() >= payload.exp * 1000
}

// 2. 刷新token
async function refreshToken() {
  const refreshToken = localStorage.getItem('refreshToken')
  const response = await axios.post('/api/auth/refresh', { refreshToken })
  const { accessToken } = response.data.data
  localStorage.setItem('accessToken', accessToken)
  return accessToken
}

// 3. 请求拦截器
axios.interceptors.request.use(async (config) => {
  let token = localStorage.getItem('accessToken')
  
  if (token && isTokenExpired(token)) {
    token = await refreshToken()
  }
  
  config.headers.Authorization = `Bearer ${token}`
  return config
})
```

---

## 数据格式

### 统一响应格式

```json
{
  "code": 200,        // 状态码：200-成功，其他-失败
  "message": "操作成功",  // 消息
  "data": {}          // 数据
}
```

### 分页数据格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [],    // 数据列表
    "total": 100,     // 总记录数
    "size": 10,       // 每页大小
    "current": 1,     // 当前页码
    "pages": 10       // 总页数
  }
}
```

### 日期格式

- **格式**: `yyyy-MM-dd` 或 `yyyy-MM-ddTHH:mm:ss`
- **示例**: `2024-02-01` 或 `2024-02-01T10:00:00`

### 金额格式

- **类型**: `decimal(10,2)`
- **示例**: `299.00`

---

## 前端开发建议

### 1. Axios 配置

```javascript
// src/utils/request.js
import axios from 'axios'
import { BASE_URL } from '@/config/api'

const request = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    
    if (code === 200) {
      return data
    } else {
      // 处理错误
      console.error(message)
      return Promise.reject(new Error(message))
    }
  },
  error => {
    if (error.response) {
      const { status } = error.response
      
      switch (status) {
        case 401:
          // 未登录或token过期
          localStorage.removeItem('accessToken')
          localStorage.removeItem('refreshToken')
          window.location.href = '/login'
          break
        case 403:
          // 无权限
          console.error('无权限访问')
          break
        case 404:
          // 资源不存在
          console.error('资源不存在')
          break
        case 500:
          // 服务器错误
          console.error('服务器错误')
          break
      }
    }
    
    return Promise.reject(error)
  }
)

export default request
```

---

### 2. API 封装

```javascript
// src/api/auth.js
import request from '@/utils/request'

export const authAPI = {
  // 登录
  login(data) {
    return request({
      url: '/api/auth/login',
      method: 'post',
      data
    })
  },
  
  // 注册
  register(data) {
    return request({
      url: '/api/auth/register',
      method: 'post',
      data
    })
  },
  
  // 刷新token
  refreshToken(data) {
    return request({
      url: '/api/auth/refresh',
      method: 'post',
      data
    })
  },
  
  // 登出
  logout() {
    return request({
      url: '/api/auth/logout',
      method: 'post'
    })
  }
}
```

```javascript
// src/api/homestay.js
import request from '@/utils/request'

export const homestayAPI = {
  // 获取民宿列表
  getList(params) {
    return request({
      url: '/api/homestay/list',
      method: 'get',
      params
    })
  },
  
  // 获取民宿详情
  getDetail(id) {
    return request({
      url: `/api/homestay/detail/${id}`,
      method: 'get'
    })
  },
  
  // 创建民宿
  create(data) {
    return request({
      url: '/api/homestay/create',
      method: 'post',
      data
    })
  },
  
  // 更新民宿
  update(id, data) {
    return request({
      url: `/api/homestay/update/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除民宿
  delete(id) {
    return request({
      url: `/api/homestay/delete/${id}`,
      method: 'delete'
    })
  }
}
```

---

### 3. 状态管理

```javascript
// src/store/user.js (Pinia示例)
import { defineStore } from 'pinia'
import { authAPI } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('accessToken') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
    userInfo: null
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token
  },
  
  actions: {
    // 登录
    async login(loginData) {
      const { accessToken, refreshToken, user } = await authAPI.login(loginData)
      
      this.token = accessToken
      this.refreshToken = refreshToken
      this.userInfo = user
      
      localStorage.setItem('accessToken', accessToken)
      localStorage.setItem('refreshToken', refreshToken)
      localStorage.setItem('userInfo', JSON.stringify(user))
    },
    
    // 登出
    async logout() {
      await authAPI.logout()
      
      this.token = ''
      this.refreshToken = ''
      this.userInfo = null
      
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('userInfo')
    },
    
    // 刷新token
    async refreshAccessToken() {
      const { accessToken } = await authAPI.refreshToken({
        refreshToken: this.refreshToken
      })
      
      this.token = accessToken
      localStorage.setItem('accessToken', accessToken)
    }
  }
})
```

---

### 4. 路由守卫

```javascript
// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.path === '/login' && userStore.isLoggedIn) {
    next('/home')
  } else {
    next()
  }
})

export default router
```

---

## 示例代码

### Vue 3 示例

```vue
<template>
  <div>
    <h1>民宿列表</h1>
    
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="关键词">
        <el-input v-model="searchForm.keyword" placeholder="请输入关键词" />
      </el-form-item>
      <el-form-item label="价格范围">
        <el-input-number v-model="searchForm.minPrice" :min="0" />
        <span>-</span>
        <el-input-number v-model="searchForm.maxPrice" :min="0" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </el-form-item>
    </el-form>
    
    <!-- 民宿列表 -->
    <el-row :gutter="20">
      <el-col v-for="item in homestayList" :key="item.id" :span="6">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="item.imageUrl" class="image">
          <div style="padding: 14px">
            <h3>{{ item.name }}</h3>
            <p>{{ item.address }}</p>
            <p>¥{{ item.price }}/晚</p>
            <el-button type="primary" @click="handleDetail(item.id)">查看详情</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.size"
      :total="pagination.total"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { homestayAPI } from '@/api/homestay'
import { useRouter } from 'vue-router'

const router = useRouter()

const searchForm = ref({
  keyword: '',
  minPrice: null,
  maxPrice: null
})

const homestayList = ref([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})

const fetchHomestayList = async () => {
  try {
    const { records, total } = await homestayAPI.getList({
      page: pagination.value.page,
      size: pagination.value.size,
      ...searchForm.value
    })
    
    homestayList.value = records
    pagination.value.total = total
  } catch (error) {
    console.error('获取民宿列表失败', error)
  }
}

const handleSearch = () => {
  pagination.value.page = 1
  fetchHomestayList()
}

const handlePageChange = (page) => {
  pagination.value.page = page
  fetchHomestayList()
}

const handleDetail = (id) => {
  router.push(`/homestay/${id}`)
}

onMounted(() => {
  fetchHomestayList()
})
</script>

<style scoped>
.image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
</style>
```

---

### React 示例

```jsx
import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { homestayAPI } from '@/api/homestay'

const HomestayList = () => {
  const navigate = useNavigate()
  
  const [searchForm, setSearchForm] = useState({
    keyword: '',
    minPrice: null,
    maxPrice: null
  })
  
  const [homestayList, setHomestayList] = useState([])
  const [pagination, setPagination] = useState({
    page: 1,
    size: 10,
    total: 0
  })
  
  const fetchHomestayList = async () => {
    try {
      const { records, total } = await homestayAPI.getList({
        page: pagination.page,
        size: pagination.size,
        ...searchForm
      })
      
      setHomestayList(records)
      setPagination(prev => ({ ...prev, total }))
    } catch (error) {
      console.error('获取民宿列表失败', error)
    }
  }
  
  const handleSearch = () => {
    setPagination(prev => ({ ...prev, page: 1 }))
    fetchHomestayList()
  }
  
  const handlePageChange = (page) => {
    setPagination(prev => ({ ...prev, page }))
    fetchHomestayList()
  }
  
  const handleDetail = (id) => {
    navigate(`/homestay/${id}`)
  }
  
  useEffect(() => {
    fetchHomestayList()
  }, [pagination.page])
  
  return (
    <div>
      <h1>民宿列表</h1>
      
      <form>
        <input
          type="text"
          placeholder="请输入关键词"
          value={searchForm.keyword}
          onChange={(e) => setSearchForm({ ...searchForm, keyword: e.target.value })}
        />
        <input
          type="number"
          placeholder="最低价格"
          value={searchForm.minPrice || ''}
          onChange={(e) => setSearchForm({ ...searchForm, minPrice: e.target.value })}
        />
        <input
          type="number"
          placeholder="最高价格"
          value={searchForm.maxPrice || ''}
          onChange={(e) => setSearchForm({ ...searchForm, maxPrice: e.target.value })}
        />
        <button type="button" onClick={handleSearch}>搜索</button>
      </form>
      
      <div>
        {homestayList.map(item => (
          <div key={item.id}>
            <img src={item.imageUrl} alt={item.name} />
            <h3>{item.name}</h3>
            <p>{item.address}</p>
            <p>¥{item.price}/晚</p>
            <button onClick={() => handleDetail(item.id)}>查看详情</button>
          </div>
        ))}
      </div>
      
      <div>
        <button
          disabled={pagination.page === 1}
          onClick={() => handlePageChange(pagination.page - 1)}
        >
          上一页
        </button>
        <span>第 {pagination.page} 页</span>
        <button
          disabled={pagination.page >= Math.ceil(pagination.total / pagination.size)}
          onClick={() => handlePageChange(pagination.page + 1)}
        >
          下一页
        </button>
      </div>
    </div>
  )
}

export default HomestayList
```

---

## 常见问题

### 1. 跨域问题

**问题**: 前端请求后端API时出现跨域错误

**解决方案**:
- 方案1: 配置后端允许跨域（已配置）
- 方案2: 使用开发代理（Vue CLI示例）

```javascript
// vue.config.js
module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}
```

---

### 2. Token过期处理

**问题**: Token过期后请求失败

**解决方案**:
- 在请求拦截器中检查token是否过期
- 过期时自动刷新token
- 刷新失败时跳转到登录页

---

### 3. 图片上传

**问题**: 头像上传功能返回随机图片URL

**说明**: 当前后端头像上传是模拟实现，返回随机图片URL。如需实现真实上传，需要：
1. 配置文件存储服务（如OSS、MinIO）
2. 修改后端上传逻辑
3. 前端使用FormData上传文件

---

### 4. 订单状态映射

**问题**: 订单状态是中文还是英文？

**说明**: 订单状态使用英文枚举值：
- `PENDING` - 待支付
- `PAID` - 已支付
- `CANCELLED` - 已取消
- `COMPLETED` - 已完成

前端需要根据状态显示对应的中文文本。

---

### 5. 角色权限控制

**问题**: 如何根据用户角色显示不同的功能？

**解决方案**:
```javascript
// 获取用户角色
const role = userStore.userInfo.role

// 根据角色判断
if (role === 'LANDLORD') {
  // 显示房东功能
} else if (role === 'ADMIN') {
  // 显示管理员功能
} else {
  // 显示游客功能
}
```

---

### 6. 日期格式化

**问题**: 后端返回的日期格式如何显示？

**解决方案**:
```javascript
// 使用dayjs或moment.js
import dayjs from 'dayjs'

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}
```

---

## 附录

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证或token过期 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 角色说明

| 角色 | 英文标识 | 权限 |
|------|---------|------|
| 游客 | TOURIST | 浏览民宿、创建订单、评分、投诉 |
| 房东 | LANDLORD | 管理自己的民宿、查看订单 |
| 管理员 | ADMIN | 管理所有用户、民宿、订单、投诉 |

### 民宿状态说明

| 状态 | 值 | 说明 |
|------|-----|------|
| 下架 | 0 | 民宿不可见 |
| 上架 | 1 | 民宿可见 |
| 待审核 | 2 | 民宿等待管理员审核 |

### 订单状态说明

| 状态 | 英文标识 | 说明 |
|------|---------|------|
| 待支付 | PENDING | 订单创建，等待支付 |
| 已支付 | PAID | 订单已支付 |
| 已取消 | CANCELLED | 订单已取消 |
| 已完成 | COMPLETED | 订单已完成，可以评价 |

---

## 联系方式

如有问题，请联系后端开发团队。

---

**文档版本**: v1.0  
**最后更新**: 2024-01-15
