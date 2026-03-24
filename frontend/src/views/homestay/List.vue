<template>
  <div class="homestay-list">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <h1>民宿预订系统</h1>
          </div>
          <div class="nav">
            <el-link @click="goHome">首页</el-link>
            <el-link @click="goToHomestays">民宿列表</el-link>
            <template v-if="userStore.isLoggedIn">
              <el-link v-if="userStore.isLandlord" @click="goToLandlord">房东中心</el-link>
              <el-dropdown>
                <div class="user-dropdown">
                  <el-avatar :size="32" :src="getImageUrl(userStore.userInfo?.avatar)">
                    {{ userStore.username?.charAt(0).toUpperCase() }}
                  </el-avatar>
                  <span class="username">{{ userStore.username }}</span>
                  <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </div>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="goToUser">
                      个人中心
                    </el-dropdown-item>

                    <!-- 我的订单 -->
                    <el-dropdown-item
                      v-if="userStore.isLoggedIn"
                      @click="goToOrders"
                    >
                      我的订单
                    </el-dropdown-item>

                    <el-dropdown-item
                      v-if="userStore.isLandlord"
                      @click="goToLandlord"
                    >
                      房东中心
                    </el-dropdown-item>

                    <el-dropdown-item divided @click="handleLogout">
                      退出登录
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-link @click="goToLogin">登录</el-link>
              <el-link @click="goToRegister">注册</el-link>
            </template>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <!-- 分类 Banner -->
        <div v-if="currentTag" class="category-banner">
          <h2>{{ currentTag }}民宿</h2>
          <p>为您精选优质 {{ currentTag }} 特色房源</p>
        </div>
        
        <div class="search-section">
          <el-form :inline="true" :model="searchParams">
            <el-form-item label="关键词">
              <el-input v-model="searchParams.keyword" placeholder="民宿名称、地址或描述" clearable />
            </el-form-item>
            <el-form-item label="价格范围">
              <el-input-number v-model="searchParams.minPrice" :min="0" placeholder="最低价" />
              <span>-</span>
              <el-input-number v-model="searchParams.maxPrice" :min="0" placeholder="最高价" />
            </el-form-item>
            <el-form-item label="标签">
              <el-input v-model="searchParams.tags" placeholder="标签，如：山水风光" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        
        <div class="list-section" v-loading="loading">
          <el-empty v-if="homestays.length === 0 && !loading" description="暂无民宿数据" />
          <el-row :gutter="20" v-else>
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="homestay in homestays" :key="homestay.id">
              <el-card class="homestay-card" @click="goToDetail(homestay.id)" shadow="hover">
                <img :src="getImageUrl((homestay.imageUrl || '').split(',')[0], true)" :alt="homestay.name" />
                <div class="homestay-info">
                  <h3>{{ homestay.name }}</h3>
                  <p class="address">{{ homestay.address }}</p>
                  <div class="info-row">
                    <span class="room-type">{{ homestay.roomType }}</span>
                    <span class="price">¥{{ formatPrice(homestay.price) }}/晚</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          
          <el-pagination
            v-if="total > 0"
            v-model:current-page="searchParams.page"
            v-model:page-size="searchParams.size"
            :total="total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSearch"
            @current-change="handleSearch"
            style="margin-top: 30px; text-align: center;"
          />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { homestayAPI } from '@/api/homestay'
import { getImageUrl, formatPrice } from '@/utils'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)
const homestays = ref([])
const total = ref(0)
const currentTag = ref('')

const searchParams = reactive({
  keyword: '',
  minPrice: null,
  maxPrice: null,
  tags: '',
  page: 1,
  size: 10
})

const loadHomestays = async () => {
  loading.value = true
  try {
    // 构建参数，只包含非 null 的价格范围值
    const params = {
      keyword: searchParams.keyword,
      tags: currentTag.value || searchParams.tags,
      page: searchParams.page,
      size: searchParams.size
    }
    
    // 只添加有效的价格范围参数
    if (searchParams.minPrice !== null) {
      params.minPrice = searchParams.minPrice
    }
    if (searchParams.maxPrice !== null) {
      params.maxPrice = searchParams.maxPrice
    }
    
    console.log('传递给后端的参数:', params)
    
    const result = await homestayAPI.getList(params)
    homestays.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载民宿列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  // 验证价格范围
  if (searchParams.minPrice !== null && searchParams.maxPrice !== null) {
    if (searchParams.minPrice > searchParams.maxPrice) {
      // 交换值，确保最小值不大于最大值
      [searchParams.minPrice, searchParams.maxPrice] = [searchParams.maxPrice, searchParams.minPrice]
    }
  } else if (searchParams.minPrice !== null) {
    // 只输入了最小值，设置最大值为一个合理的默认值
    searchParams.maxPrice = 99999
  } else if (searchParams.maxPrice !== null) {
    // 只输入了最大值，设置最小值为 0
    searchParams.minPrice = 0
  }
  searchParams.page = 1
  loadHomestays()
}

const handleReset = () => {
  searchParams.keyword = ''
  searchParams.minPrice = null
  searchParams.maxPrice = null
  searchParams.tags = ''
  currentTag.value = ''
  searchParams.page = 1
  loadHomestays()
}

const goToDetail = (id) => {
  router.push(`/homestays/${id}`)
}

const goHome = () => {
  router.push('/')
}

const goToHomestays = () => {
  router.push('/homestays')
}

const goToLogin = () => {
  router.push('/login')
}

const goToRegister = () => {
  router.push('/register')
}

const goToUser = () => {
  router.push('/user')
}

const goToLandlord = () => {
  router.push('/landlord')
}

const goToOrders = () => {
  router.push('/user/orders')
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}

onMounted(() => {
  if (route.query.keyword) {
    searchParams.keyword = route.query.keyword
  }
  if (route.query.tags) {
    currentTag.value = route.query.tags
  }
  loadHomestays()
})
</script>

<style scoped>
/* Airbnb Design Tokens */
:root {
  --airbnb-red: #FF5A5F;
  --airbnb-red-dark: #E00B41;
  --airbnb-black: #222222;
  --airbnb-gray: #717171;
  --airbnb-light-gray: #DDDDDD;
  --airbnb-bg: #F7F7F7;
  --radius: 12px;
  --radius-lg: 16px;
  --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.08);
  --shadow-md: 0 6px 16px rgba(0, 0, 0, 0.12);
}

/* Header */
.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 80px;
  padding: 0 40px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  cursor: pointer;
}

.logo h1 {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: bold;
  color: var(--airbnb-red);
}

.nav {
  display: flex;
  gap: 30px;
}

.nav .el-link {
  font-size: 16px;
  color: var(--airbnb-black);
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav .el-link:hover {
  color: var(--airbnb-red);
}

/* Category Banner */
.category-banner {
  margin-bottom: 40px;
  padding: 60px;
  border-radius: var(--radius-lg);
  background: linear-gradient(135deg, var(--airbnb-red), #ff7e67);
  color: white;
  text-align: center;
  box-shadow: var(--shadow-md);
}

.category-banner h2 {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 12px;
  line-height: 1.2;
}

.category-banner p {
  font-size: 18px;
  opacity: 0.9;
  margin: 0;
}

/* Search Section */
.search-section {
  background: white;
  padding: 32px;
  border-radius: var(--radius);
  margin-bottom: 32px;
  box-shadow: var(--shadow-sm);
  max-width: 1400px;
  margin-left: auto;
  margin-right: auto;
}

.search-section .el-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: end;
}

.search-section .el-form-item {
  margin-bottom: 0;
}

.search-section .el-button {
  padding: 0 24px;
  height: 40px;
}

/* List Section */
.list-section {
  padding: 0 0 40px;
  max-width: 1400px;
  margin: 0 auto;
}

/* Airbnb Style Cards */
.homestay-card {
  margin-bottom: 32px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  border-radius: var(--radius);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.homestay-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-md);
}

.homestay-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: var(--radius) var(--radius) 0 0;
  transition: transform 0.3s ease;
  display: block;
  min-width: 100%;
  min-height: 100%;
}

.homestay-card:hover img {
  transform: scale(1.05);
}

/* Card Content */
.homestay-info {
  padding: 16px;
  background: white;
}

.homestay-info h3 {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--airbnb-black);
  line-height: 1.4;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.homestay-info .address {
  margin: 0 0 12px;
  color: var(--airbnb-gray);
  font-size: 14px;
  line-height: 1.4;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.homestay-info .info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.homestay-info .room-type {
  color: var(--airbnb-gray);
  font-size: 14px;
  flex: 1;
}

.homestay-info .price {
  color: var(--airbnb-red);
  font-size: 18px;
  font-weight: 600;
  white-space: nowrap;
}

/* Pagination */
.el-pagination {
  margin-top: 40px;
}

/* User Dropdown */
.user-dropdown {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
  border: 1px solid var(--airbnb-light-gray);
}

.user-dropdown:hover {
  background-color: var(--airbnb-bg);
  border-color: var(--airbnb-gray);
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: var(--airbnb-black);
}

/* Responsive Design */
@media (max-width: 768px) {
  .header-content {
    padding: 0 20px;
    height: 60px;
  }
  
  .logo h1 {
    font-size: 24px;
  }
  
  .nav {
    gap: 20px;
  }
  
  .nav .el-link {
    font-size: 14px;
  }
  
  .category-banner {
    padding: 40px 20px;
  }
  
  .category-banner h2 {
    font-size: 28px;
  }
  
  .search-section {
    padding: 20px;
  }
  
  .search-section .el-form {
    flex-direction: column;
    align-items: stretch;
  }
  
  .homestay-card img {
    height: 200px;
  }
}
</style>
