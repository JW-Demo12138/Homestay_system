<template>
  <div class="home">
    <!-- 1. 顶部透明导航 -->
    <header class="navbar">
      <div class="navbar-container">
        <div class="logo" @click="goHome">
          <h1>民宿预订</h1>
        </div>
        <nav class="nav-links">
          <a href="#" @click="goHome">首页</a>
          <a href="#" @click="goToHomestays">民宿列表</a>
          <a href="#" @click="scrollToCategories">分类</a>
          <a href="#" @click="scrollToRecommend">推荐</a>
        </nav>
        <div class="user-actions">
          <template v-if="userStore.isLoggedIn">
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
            <el-button @click="goToLogin" class="login-btn">登录</el-button>
            <el-button @click="goToRegister" type="primary" class="register-btn">注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <!-- 2. 全屏 Hero + 搜索浮层 -->
    <section class="hero">
      <div class="hero-overlay">
        <div class="hero-content">
          <h1>发现理想的民宿</h1>
          <p>在城市与自然之间，找到属于你的家</p>
          
          <div class="hero-search">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索城市、民宿、关键词"
              size="large"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button @click="handleSearch" type="primary" class="search-btn">探索</el-button>
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </section>

    <!-- 3. 热门城市图片卡片 -->
    <section class="city-section">
      <div class="container">
        <h2 class="section-title">热门目的地</h2>
        <p class="section-subtitle">探索最受欢迎的城市</p>
        
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :md="8" v-for="city in hotCities" :key="city.id">
            <div class="city-card" @click="searchByCity(city.name)">
              <img :src="getCityImage(city.name)" :alt="city.name" />
              <div class="city-name">{{ city.name }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </section>

    <!-- 4. 分类推荐 -->
    <section class="category-section" id="categories">
      <div class="container">
        <h2 class="section-title">特色分类</h2>
        <p class="section-subtitle">根据您的需求找到完美的住宿</p>
        
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :md="8">
            <div class="category-card" @click="goToCategory('海景')">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=beautiful%20seaside%20homestay%20with%20ocean%20view&image_size=landscape_16_9" alt="海景民宿" />
              <div class="category-content">
                <h3>海景民宿</h3>
                <p>面朝大海，春暖花开</p>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8">
            <div class="category-card" @click="goToCategory('亲子')">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=family%20friendly%20homestay%20with%20kids%20area&image_size=landscape_16_9" alt="亲子民宿" />
              <div class="category-content">
                <h3>亲子民宿</h3>
                <p>带娃出行，轻松愉快</p>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8">
            <div class="category-card" @click="goToCategory('情侣')">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=romantic%20homestay%20for%20couples&image_size=landscape_16_9" alt="情侣民宿" />
              <div class="category-content">
                <h3>情侣民宿</h3>
                <p>浪漫时光，难忘回忆</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </section>

    <!-- 5. 推荐民宿 -->
    <section class="recommend-section" id="recommend">
      <div class="container">
        <h2 class="section-title">推荐民宿</h2>
        <p class="section-subtitle">为您精心挑选的优质住宿</p>
        
        <el-row :gutter="24" v-loading="loading">
          <el-col :xs="24" :sm="12" :md="8" v-for="homestay in recommendHomestays" :key="homestay.id">
            <div class="modern-card" @click="goToDetail(homestay.id)">
              <div class="card-image">
                <img :src="getImageUrl((homestay.imageUrl || '').split(',')[0])" :alt="homestay.name" />
              </div>
              
              <div class="card-body">
                <div class="card-top">
                  <span class="title">{{ homestay.name }}</span>
                  <span class="rating">⭐ 4.8</span>
                </div>
                
                <div class="address">{{ homestay.address }}</div>
                
                <div class="price">
                  <span class="amount">¥{{ formatPrice(homestay.price) }}</span>
                  <span class="unit">/晚</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </section>

    <!-- 6. 品牌理念区 -->
    <section class="brand-section">
      <div class="container">
        <div class="brand-content">
          <div class="brand-text">
            <h2>我们的理念</h2>
            <p>民宿不仅仅是一个住宿场所，更是一种生活方式。我们致力于为每一位旅行者提供独特、舒适的住宿体验，让您在旅途中感受到家的温暖。</p>
            <p>无论是城市中心的现代公寓，还是乡村的宁静小屋，我们都精心挑选，确保每一处民宿都能带给您不一样的惊喜。</p>
            <el-button @click="goToHomestays" type="primary" class="explore-btn">探索更多</el-button>
          </div>
          <div class="brand-image">
            <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cozy%20homestay%20living%20room%20with%20warm%20lighting&image_size=landscape_16_9" alt="品牌理念" />
          </div>
        </div>
      </div>
    </section>

    <!-- 7. Footer -->
    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-col">
            <h3>关于我们</h3>
            <p>民宿预订系统致力于为旅行者提供优质的住宿体验，连接房东与房客，创造更多美好的旅行记忆。</p>
          </div>
          <div class="footer-col">
            <h3>快速链接</h3>
            <ul>
              <li><a href="#" @click="goHome">首页</a></li>
              <li><a href="#" @click="goToHomestays">民宿列表</a></li>
              <li><a href="#" @click="scrollToCategories">分类</a></li>
              <li><a href="#" @click="scrollToRecommend">推荐</a></li>
            </ul>
          </div>
          <div class="footer-col">
            <h3>客户服务</h3>
            <ul>
              <li>联系我们</li>
              <li>常见问题</li>
              <li>隐私政策</li>
              <li>用户协议</li>
            </ul>
          </div>
          <div class="footer-col">
            <h3>关注我们</h3>
            <div class="social-links">
              <a href="#">微信</a>
              <a href="#">微博</a>
              <a href="#">抖音</a>
              <a href="#">小红书</a>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2024 民宿预订系统. 保留所有权利.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { homestayAPI } from '@/api/homestay'
import { cityAPI } from '@/api/city'
import { getImageUrl, formatPrice } from '@/utils'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const searchKeyword = ref('')
const hotCities = ref([])
const recommendHomestays = ref([])

// 加载热门城市
const loadHotCities = async () => {
  try {
    hotCities.value = await cityAPI.getHotCities()
  } catch (error) {
    console.error('加载热门城市失败:', error)
    // 如果是403错误，使用空数组，避免页面显示错误
    if (error.response?.status === 403) {
      hotCities.value = []
    }
  }
}

// 加载推荐民宿
const loadRecommendHomestays = async () => {
  loading.value = true
  try {
    recommendHomestays.value = await homestayAPI.getRecommend()
  } catch (error) {
    console.error('加载推荐民宿失败:', error)
    // 如果是403错误，使用空数组，避免页面显示错误
    if (error.response?.status === 403) {
      recommendHomestays.value = []
    }
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/homestays',
      query: { keyword: searchKeyword.value }
    })
  }
}

// 按城市搜索
const searchByCity = (cityName) => {
  router.push({
    path: '/homestays',
    query: { keyword: cityName }
  })
}

// 页面跳转
const goHome = () => {
  router.push('/')
}

const goToHomestays = () => {
  router.push('/homestays')
}

const goToDetail = (id) => {
  router.push(`/homestays/${id}`)
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

const goToCategory = (category) => {
  router.push({
    path: '/feature/topic',
    query: {
      tag: category
    }
  })
}

// 退出登录
const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}

// 滚动到分类
const scrollToCategories = () => {
  document.getElementById('categories')?.scrollIntoView({ behavior: 'smooth' })
}

// 滚动到推荐
const scrollToRecommend = () => {
  document.getElementById('recommend')?.scrollIntoView({ behavior: 'smooth' })
}

// 获取城市图片
const getCityImage = (cityName) => {
  const cityImages = {
    '杭州': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=hangzhou%20west%20lake%20scenic%20view&image_size=landscape_16_9',
    '上海': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=shanghai%20skyline%20modern%20city&image_size=landscape_16_9',
    '北京': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=beijing%20forbidden%20city%20traditional&image_size=landscape_16_9',
    '广州': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=guangzhou%20canton%20tower%20modern&image_size=landscape_16_9',
    '深圳': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=shenzhen%20modern%20city%20skyline&image_size=landscape_16_9',
    '成都': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chengdu%20traditional%20panda%20city&image_size=landscape_16_9'
  }
  return cityImages[cityName] || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20city%20skyline&image_size=landscape_16_9'
}

// 组件挂载时加载数据
onMounted(() => {
  loadHotCities()
  loadRecommendHomestays()
})
</script>

<style scoped>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 顶部导航 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.logo h1 {
  font-size: 24px;
  font-weight: 700;
  color: #667eea;
  margin: 0;
  cursor: pointer;
}

.nav-links {
  display: flex;
  gap: 32px;
}

.nav-links a {
  text-decoration: none;
  color: #333;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-links a:hover {
  color: #667eea;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.login-btn {
  border: 1px solid #667eea;
  color: #667eea;
}

.register-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  font-weight: 500;
}

/* Hero 区域 */
.hero {
  height: 70vh;
  background: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=beautiful%20homestay%20exterior%20with%20mountain%20view&image_size=landscape_16_9') center/cover no-repeat;
  position: relative;
  border-radius: 0 0 40px 40px;
  margin-bottom: 80px;
}

.hero-overlay {
  background: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0.3),
    rgba(0, 0, 0, 0.6)
  );
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0 0 40px 40px;
}

.hero-content {
  text-align: center;
  color: white;
  max-width: 800px;
  padding: 0 24px;
}

.hero-content h1 {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 16px;
  line-height: 1.2;
}

.hero-content p {
  font-size: 20px;
  margin-bottom: 32px;
  opacity: 0.9;
}

.hero-search {
  max-width: 600px;
  margin: 0 auto;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  padding: 0 24px;
}

/* 通用容器 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

/* 通用标题 */
.section-title {
  font-size: 32px;
  font-weight: 700;
  text-align: center;
  margin-bottom: 12px;
  color: #333;
}

.section-subtitle {
  font-size: 16px;
  text-align: center;
  margin-bottom: 48px;
  color: #666;
}

/* 城市区域 */
.city-section {
  padding: 80px 0;
}

.city-card {
  border-radius: 20px;
  overflow: hidden;
  position: relative;
  cursor: pointer;
  transition: transform 0.3s ease;
  height: 200px;
}

.city-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.city-card:hover {
  transform: scale(1.05);
}

.city-name {
  position: absolute;
  bottom: 16px;
  left: 20px;
  color: white;
  font-size: 20px;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* 分类区域 */
.category-section {
  padding: 80px 0;
  background-color: #f6f7fb;
  margin: 80px 0;
}

.category-card {
  border-radius: 20px;
  overflow: hidden;
  position: relative;
  cursor: pointer;
  transition: transform 0.3s ease;
  height: 250px;
}

.category-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.category-card:hover {
  transform: scale(1.03);
}

.category-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  padding: 32px 20px 20px;
  color: white;
}

.category-content h3 {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;
}

.category-content p {
  font-size: 14px;
  opacity: 0.9;
}

/* 推荐民宿区域 */
.recommend-section {
  padding: 80px 0;
}

.modern-card {
  border-radius: 20px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  background: white;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.modern-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12);
}

.card-image img {
  width: 100%;
  aspect-ratio: 4/3;
  object-fit: cover;
}

.card-body {
  padding: 16px;
}

.card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.title {
  font-weight: 600;
  font-size: 16px;
  color: #333;
}

.rating {
  font-size: 14px;
  color: #ff9800;
}

.address {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
}

.price {
  display: flex;
  align-items: baseline;
}

.amount {
  font-size: 20px;
  font-weight: bold;
  color: #ff5722;
}

.unit {
  font-size: 14px;
  color: #666;
  margin-left: 4px;
}

/* 品牌理念区 */
.brand-section {
  padding: 80px 0;
  background-color: #f6f7fb;
  margin: 80px 0;
}

.brand-content {
  display: flex;
  flex-direction: column;
  gap: 48px;
}

@media (min-width: 768px) {
  .brand-content {
    flex-direction: row;
    align-items: center;
  }
  
  .brand-text {
    flex: 1;
    padding-right: 48px;
  }
  
  .brand-image {
    flex: 1;
  }
}

.brand-text h2 {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 24px;
  color: #333;
}

.brand-text p {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 24px;
  color: #666;
}

.explore-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  padding: 12px 24px;
}

.brand-image img {
  width: 100%;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

/* Footer */
.footer {
  background-color: #333;
  color: white;
  padding: 80px 0 40px;
}

.footer-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 48px;
  margin-bottom: 48px;
}

@media (min-width: 768px) {
  .footer-content {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .footer-content {
    grid-template-columns: repeat(4, 1fr);
  }
}

.footer-col h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
}

.footer-col p {
  font-size: 14px;
  line-height: 1.6;
  color: #ccc;
}

.footer-col ul {
  list-style: none;
}

.footer-col ul li {
  margin-bottom: 12px;
}

.footer-col ul li a {
  color: #ccc;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-col ul li a:hover {
  color: #667eea;
}

.social-links {
  display: flex;
  gap: 16px;
}

.social-links a {
  color: #ccc;
  text-decoration: none;
  transition: color 0.3s ease;
}

.social-links a:hover {
  color: #667eea;
}

.footer-bottom {
  border-top: 1px solid #555;
  padding-top: 24px;
  text-align: center;
  font-size: 14px;
  color: #999;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 36px;
  }
  
  .hero-content p {
    font-size: 16px;
  }
  
  .section-title {
    font-size: 24px;
  }
  
  .city-card, .category-card {
    height: 150px;
  }
  
  .nav-links {
    display: none;
  }
}
</style>