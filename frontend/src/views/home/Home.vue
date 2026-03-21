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

    <!-- 4. 特色体验 -->
    <section class="category-section" id="categories">
      <div class="container">
        <h2 class="section-title">特色体验</h2>
        <p class="section-subtitle">探索乡村特色，享受独特体验</p>
        
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :md="8">
            <div class="category-card" @click="goToCategory('民俗手工制作')">
              <img src="/image/Special reservation/手工.jpg" alt="民俗手工制作" />
              <div class="category-content">
                <h3>民俗手工制作</h3>
                <p>体验传统工艺，制作特色手工艺品</p>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8">
            <div class="category-card" @click="goToCategory('农事实践体验')">
              <img src="/image/Special reservation/农事.jpg" alt="农事实践体验" />
              <div class="category-content">
                <h3>农事实践体验</h3>
                <p>参与农耕活动，感受乡村生活</p>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8">
            <div class="category-card" @click="goToCategory('乡村美食烹饪')">
              <img src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="乡村美食烹饪" />
              <div class="category-content">
                <h3>乡村美食烹饪</h3>
                <p>学习传统烹饪，品尝乡村特色美食</p>
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
        
        <div class="homestay-grid" v-loading="loading">
          <div class="modern-card" v-for="homestay in recommendHomestays" :key="homestay.id" @click="goToDetail(homestay.id)">
            <div class="card-image">
              <img :src="getImageUrl((homestay.imageUrl || '').split(',')[0])" :alt="homestay.name" />
              <button class="favorite-btn">
                <svg viewBox="0 0 24 24">
                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                </svg>
              </button>
              <div class="card-badge" v-if="homestay.isSuperHost">超赞房东</div>
            </div>
            <div class="card-body">
              <h3 class="title">{{ homestay.name }}</h3>
              <div class="card-header">
                <span class="location">{{ homestay.address?.split(' ')[0] || '未知位置' }}</span>
                <div class="rating">
                  <span class="star-icon">★</span>
                  <span>{{ homestay.rating || '4.8' }}</span>
                </div>
              </div>
              <p class="details">{{ homestay.roomType || '整套公寓' }} · {{ homestay.bedrooms || 1 }}间卧室 · {{ homestay.beds || 1 }}张床 · {{ homestay.bathrooms || 1 }}间浴室</p>
              <div class="price-row">
                <span class="price-label">¥{{ formatPrice(homestay.price) }}</span>
                <span class="price-unit">/晚</span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="load-more">
          <button @click="loadMore">加载更多</button>
        </div>
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
    // 使用硬编码的城市列表，确保显示用户要求的城市
    hotCities.value = [
      { id: 1, name: '深圳' },
      { id: 2, name: '广州' },
      { id: 3, name: '佛山' },
      { id: 4, name: '杭州' },
      { id: 5, name: '上海' },
      { id: 6, name: '北京' },
      { id: 7, name: '成都' },
      { id: 8, name: '厦门' },
      { id: 9, name: '重庆' }
    ]
  } catch (error) {
    console.error('加载热门城市失败:', error)
    hotCities.value = []
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
  // 根据城市名称返回对应的图片路径
  // 用户可以按照城市命名上传图片，比如深圳.jpg、广州.jpg等
  return `/image/hot_cities/${cityName}.jpg`
}

// 组件挂载时加载数据
onMounted(() => {
  loadHotCities()
  loadRecommendHomestays()
})
</script>

<style scoped>
/* ===== Airbnb Design Tokens ===== */
:root {
  /* 品牌色 */
  --airbnb-red: #FF5A5F;
  --airbnb-red-dark: #E00B41;
  --airbnb-black: #000000;
  --airbnb-gray: #717171;
  --airbnb-light-gray: #DDDDDD;
  --airbnb-bg: #F7F7F7;
  
  /* 字体栈 - 匹配 Airbnb 的 Cereal 字体 */
  --font-stack: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "PingFang SC", "Microsoft YaHei", sans-serif;
  
  /* 间距系统（8px 基础） */
  --space-1: 8px;
  --space-2: 16px;
  --space-3: 24px;
  
  /* 圆角 */
  --radius: 12px;
  --radius-lg: 16px;
  
  /* 阴影 */
  --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.08);
  --shadow-md: 0 6px 16px rgba(0, 0, 0, 0.12);
}

/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: var(--font-stack);
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
  font-size: 18px;
  font-weight: 700;
  color: var(--airbnb-red);
  margin: 0 0 10px 0;
  cursor: pointer;
}

.nav-links {
  display: flex;
  gap: 32px;
}

.nav-links a {
  text-decoration: none;
  color: var(--airbnb-black);
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-links a:hover {
  color: var(--airbnb-red);
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.login-btn {
  border: 1px solid var(--airbnb-black);
  color: var(--airbnb-black);
}

.register-btn {
  background: var(--airbnb-red);
  border: none;
  color: rgb(0, 0, 0);
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
  background-color: var(--airbnb-bg);
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
  background: var(--airbnb-red);
  border: none;
  padding: 0 24px;
  color: white;
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
  color: var(--airbnb-black);
  letter-spacing: -0.5px;
}

.section-subtitle {
  font-size: 18px;
  text-align: center;
  margin-bottom: 48px;
  color: var(--airbnb-gray);
  font-weight: 400;
}

/* 城市区域 */
.city-section {
  padding: 80px 0;
}

.city-card {
  border-radius: var(--radius);
  overflow: hidden;
  position: relative;
  cursor: pointer;
  transition: transform 0.3s ease;
  height: 200px;
  box-shadow: var(--shadow-sm);
}

.city-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.city-card:hover {
  transform: scale(1.05);
  box-shadow: var(--shadow-md);
}

.city-card:hover img {
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
  background-color: var(--airbnb-bg);
  margin: 80px 0;
}

.category-card {
  border-radius: var(--radius);
  overflow: hidden;
  position: relative;
  cursor: pointer;
  transition: transform 0.3s ease;
  height: 250px;
  box-shadow: var(--shadow-sm);
}

.category-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.category-card:hover {
  transform: scale(1.03);
  box-shadow: var(--shadow-md);
}

.category-card:hover img {
  transform: scale(1.05);
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
  background-color: white;
}

/* 网格布局 - 桌面端 4 列 */
.homestay-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px 24px;
  margin-bottom: 48px;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}

/* 大屏 3 列 */
@media (min-width: 1600px) {
  .homestay-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 40px 24px;
    max-width: 1400px;
  }
}

/* 小屏 3 列 */
@media (max-width: 1200px) {
  .homestay-grid {
    grid-template-columns: repeat(3, 1fr);
    max-width: 900px;
  }
}

/* 平板 2 列 */
@media (max-width: 768px) {
  .homestay-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 24px 16px;
    max-width: 600px;
  }
}

/* 手机 1 列 */
@media (max-width: 480px) {
  .homestay-grid {
    grid-template-columns: 1fr;
    max-width: 400px;
  }
}

/* Airbnb 卡片样式 */
.modern-card {
  position: relative;
  cursor: pointer;
  /* 无背景色，无大阴影，极简 */
  width: 100%;
}

/* 图片容器 - 3:2 比例（Airbnb 标准） */
.card-image {
  position: relative;
  width: 100%;
  height: 200px;
  border-radius: var(--radius);
  overflow: hidden;
  margin-bottom: var(--space-1);
  background-color: #EBEBEB;
  box-shadow: var(--shadow-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 0;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
  display: block;
  min-width: 100%;
  min-height: 100%;
}

/* 悬停效果：图片轻微放大 */
.modern-card:hover .card-image img {
  transform: scale(1.05);
}

/* 收藏按钮 - 右上角心形 */
.favorite-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 32px;
  height: 32px;
  background: transparent;
  border: none;
  cursor: pointer;
  z-index: 2;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.18));
  transition: transform 0.2s ease;
}

.favorite-btn:hover {
  transform: scale(1.1);
}

.favorite-btn svg {
  width: 24px;
  height: 24px;
  fill: rgba(0,0,0,0.5);
  stroke: white;
  stroke-width: 2;
  transition: fill 0.2s ease;
}

.favorite-btn:hover svg,
.favorite-btn.active svg {
  fill: var(--airbnb-red);
  stroke: var(--airbnb-red);
}

/* 标签 - 左上角（超赞房东等） */
.card-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background: rgba(255, 255, 255, 0.95);
  padding: 6px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  color: var(--airbnb-black);
  z-index: 2;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* 卡片内容 - Airbnb 信息层级 */
.card-body {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 8px 0;
}

/* 第一行：位置 + 评分（两端对齐） */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  color: var(--airbnb-gray);
}

.location {
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.rating {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 15px;
  color: var(--airbnb-black);
  font-weight: 500;
}

.star-icon {
  color: var(--airbnb-black);
  font-size: 12px;
}

/* 第二行：房源标题（加粗） */
.title {
  font-size: 15px;
  font-weight: 500;
  color: var(--airbnb-black);
  line-height: 1.4;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-top: 2px;
}

/* 第三行：距离/日期（灰色） */
.details {
  font-size: 15px;
  color: var(--airbnb-gray);
  margin-top: 2px;
}

/* 第四行：价格 */
.price-row {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-top: 6px;
  font-size: 15px;
}

.price-label {
  font-weight: 600;
  color: var(--airbnb-black);
}

.price-unit {
  color: var(--airbnb-black);
  font-weight: 400;
}

/* 悬停状态优化 */
.modern-card:hover .title {
  color: var(--airbnb-gray);
}

/* 分页/加载更多（Airbnb 风格） */
.load-more {
  margin-top: 48px;
  text-align: center;
}

.load-more button {
  padding: 14px 24px;
  background: transparent;
  border: 1px solid var(--airbnb-black);
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--airbnb-black);
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: var(--font-stack);
}

.load-more button:hover {
  background: var(--airbnb-bg);
  border-color: var(--airbnb-black);
}

/* 品牌理念区 */
.brand-section {
  padding: 80px 0;
  background-color: var(--airbnb-bg);
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
  color: var(--airbnb-black);
  letter-spacing: -0.5px;
}

.brand-text p {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 24px;
  color: var(--airbnb-gray);
}

.explore-btn {
  background: var(--airbnb-red);
  border: none;
  padding: 12px 24px;
  color: white;
}

.brand-image img {
  width: 100%;
  border-radius: var(--radius);
  box-shadow: var(--shadow-md);
}

/* Footer - GitHub Style */
.footer {
  background-color: #161b22;
  color: #f0f6fc;
  padding: 40px 0 24px;
  border-top: 1px solid #30363d;
}

.footer-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 32px;
  margin-bottom: 32px;
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
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #f0f6fc;
}

.footer-col p {
  font-size: 14px;
  line-height: 1.5;
  color: #8b949e;
}

.footer-col ul {
  list-style: none;
}

.footer-col ul li {
  margin-bottom: 10px;
}

.footer-col ul li a {
  color: #8b949e;
  text-decoration: none;
  transition: color 0.2s ease;
}

.footer-col ul li a:hover {
  color: #58a6ff;
  text-decoration: underline;
}

.social-links {
  display: flex;
  gap: 12px;
}

.social-links a {
  color: #8b949e;
  text-decoration: none;
  transition: color 0.2s ease;
}

.social-links a:hover {
  color: #58a6ff;
}

.footer-bottom {
  border-top: 1px solid #30363d;
  padding-top: 20px;
  text-align: center;
  font-size: 12px;
  color: #8b949e;
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

/* 滚动条美化（浏览器端专属） */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: var(--airbnb-light-gray);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--airbnb-gray);
}
</style>