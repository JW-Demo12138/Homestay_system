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
                <img :src="getImageUrl((homestay.imageUrl || '').split(',')[0])" :alt="homestay.name" />
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
    const params = {
      ...searchParams,
      tags: currentTag.value || searchParams.tags
    }
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
.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
}

.logo {
  cursor: pointer;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  color: #667eea;
}

.nav {
  display: flex;
  gap: 20px;
}

.nav .el-link {
  font-size: 16px;
}

.category-banner {
  margin-bottom: 30px;
  padding: 40px;
  border-radius: 20px;
  background: linear-gradient(135deg, #ff385c, #ff7e67);
  color: white;
}

.category-banner h2 {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 8px;
}

.category-banner p {
  opacity: 0.9;
}

.search-section {
  background: white;
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.list-section {
  padding: 20px 0;
}

.homestay-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.homestay-card:hover {
  transform: translateY(-5px);
}

.homestay-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.homestay-info {
  padding: 15px 0 0;
}

.homestay-info h3 {
  margin: 0 0 10px;
  font-size: 18px;
  color: #333;
}

.homestay-info .address {
  margin: 0 0 10px;
  color: #666;
  font-size: 14px;
}

.homestay-info .info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.homestay-info .room-type {
  color: #909399;
  font-size: 14px;
}

.homestay-info .price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  font-weight: 500;
}

.user-dropdown .el-avatar {
  margin-right: 4px;
}
</style>
