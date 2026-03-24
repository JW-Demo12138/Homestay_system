<template>
  <div class="landlord-dashboard">
    <!-- 顶部导航 -->
    <header class="navbar">
      <div class="navbar-container">
        <div class="logo" @click="goHome">
          <h1>民宿管理</h1>
        </div>
        <nav class="nav-links">
          <a href="#" @click="goHome">首页</a>
          <a href="#" @click="goToHomestays">民宿列表</a>
          <a href="#" @click="goToLandlord" class="active">房东中心</a>
          <a href="#" @click="goToMyOrders">我的订单</a>
        </nav>
        <div class="user-actions">
          <NotificationBell />
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
                <el-dropdown-item @click="goToUser">个人中心</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>
    
    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>房东仪表盘</h1>
        <p>欢迎回来，{{ userStore.username }}！这里是您的民宿管理中心</p>
      </div>
      
      <!-- 统计卡片 -->
      <section class="stats-section">
        <div class="stats-grid">
          <div class="stat-card" @click="goToMyHomestays">
            <div class="stat-icon" style="background: linear-gradient(135deg, #ff385c, #ff7a8f);">
              <el-icon><House /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalHomestays }}</div>
              <div class="stat-label">我的民宿</div>
            </div>
          </div>
          
          <div class="stat-card" @click="goToMyOrders">
            <div class="stat-icon" style="background: linear-gradient(135deg, #008489, #59c6c9);">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalOrders }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
          
          <div class="stat-card">
            <div class="stat-icon" style="background: linear-gradient(135deg, #ff9900, #ffc966);">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">¥{{ stats.totalRevenue }}</div>
              <div class="stat-label">总收入</div>
            </div>
          </div>
          
          <div class="stat-card" @click="goToMyOrders">
            <div class="stat-icon" style="background: linear-gradient(135deg, #8a2be2, #c084fc);">
              <el-icon><Timer /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingOrders }}</div>
              <div class="stat-label">待处理订单</div>
            </div>
          </div>
          
          <div class="stat-card" @click="goToMyExperiences">
            <div class="stat-icon" style="background: linear-gradient(135deg, #4caf50, #81c784);">
              <el-icon><Star /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalExperiences }}</div>
              <div class="stat-label">体验项目</div>
            </div>
          </div>
        </div>
      </section>
      
      <!-- 快捷操作和最近订单 -->
      <section class="dashboard-grid">
        <!-- 快捷操作 -->
        <div class="card">
          <div class="card-header">
            <h3>快捷操作</h3>
          </div>
          <div class="card-body">
            <div class="action-grid">
              <el-button @click="goToCreateHomestay" class="action-card primary-action">
                <div class="action-icon create-icon">
                  <el-icon><Plus /></el-icon>
                </div>
                <div class="action-text">发布民宿</div>
              </el-button>
              <el-button @click="goToCreateExperience" class="action-card experience-action">
                <div class="action-icon experience-icon">
                  <el-icon><Star /></el-icon>
                </div>
                <div class="action-text">创建体验项目</div>
              </el-button>
              <el-button @click="goToMyHomestays" class="action-card homestay-action">
                <div class="action-icon homestay-icon">
                  <el-icon><House /></el-icon>
                </div>
                <div class="action-text">我的民宿</div>
              </el-button>
              <el-button @click="goToMyExperiences" class="action-card experience-action">
                <div class="action-icon experience-icon">
                  <el-icon><Star /></el-icon>
                </div>
                <div class="action-text">我的体验项目</div>
              </el-button>
              <el-button @click="goToMyOrders" class="action-card order-action">
                <div class="action-icon order-icon">
                  <el-icon><Document /></el-icon>
                </div>
                <div class="action-text">我的订单</div>
              </el-button>
            </div>
          </div>
        </div>
        
        <!-- 最近订单 -->
        <div class="card">
          <div class="card-header">
            <h3>最近订单</h3>
            <el-link @click="goToMyOrders" class="view-all">查看全部</el-link>
          </div>
          <div class="card-body">
            <div class="orders-list">
              <div v-if="recentOrders.length === 0" class="empty-state">
                <el-icon class="empty-icon"><Document /></el-icon>
                <p>暂无订单</p>
              </div>
              <div v-else class="order-item" v-for="order in recentOrders" :key="order.id">
                <div class="order-info">
                  <h4 class="order-homestay">{{ order.homestayName }}</h4>
                  <p class="order-details">{{ order.checkInDate }} - {{ order.checkOutDate }}</p>
                </div>
                <div class="order-price">¥{{ formatPrice(order.price) }}</div>
                <div class="order-status">
                  <el-tag :type="getStatusType(order.status)">{{ order.status }}</el-tag>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { homestayAPI } from '@/api/homestay'
import { orderAPI } from '@/api/order'
import { experienceAPI } from '@/api/experience'
import { getImageUrl, formatPrice } from '@/utils'
import { ArrowDown, House, Document, Money, Timer, Star } from '@element-plus/icons-vue'
import NotificationBell from '@/components/NotificationBell.vue'

const router = useRouter()
const userStore = useUserStore()

const stats = ref({
  totalHomestays: 0,
  totalOrders: 0,
  totalRevenue: 0,
  pendingOrders: 0,
  totalExperiences: 0
})

const recentOrders = ref([])

const loadStats = async () => {
  try {
    const homestays = await homestayAPI.getLandlordList()
    stats.value.totalHomestays = homestays.length || 0
    
    const ordersResponse = await orderAPI.getLandlordOrders()
    const orders = Array.isArray(ordersResponse) ? ordersResponse : (ordersResponse.orders || ordersResponse.data?.orders || [])
    stats.value.totalOrders = orders.length || 0
    stats.value.pendingOrders = orders.filter(o => o.status === 'PENDING').length || 0
    stats.value.totalRevenue = orders
      .filter(o => o.status === 'PAID' || o.status === 'COMPLETED')
      .reduce((sum, o) => sum + (o.price || 0), 0)
      .toFixed(2)
    
    const experiencesResponse = await experienceAPI.getLandlordList()
    const experiences = Array.isArray(experiencesResponse) ? experiencesResponse : (experiencesResponse.data || [])
    stats.value.totalExperiences = experiences.length || 0
    
    recentOrders.value = orders.slice(0, 5)
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const goHome = () => {
  router.push('/')
}

const goToHomestays = () => {
  router.push('/homestays')
}

const goToLandlord = () => {
  router.push('/landlord')
}

const goToUser = () => {
  router.push('/user')
}

const goToCreateHomestay = () => {
  router.push('/landlord/homestays/create')
}

const goToMyHomestays = () => {
  router.push('/landlord/homestays')
}

const goToMyOrders = () => {
  router.push('/landlord/orders')
}

const goToCreateExperience = () => {
  router.push('/landlord/experiences/create')
}

const goToMyExperiences = () => {
  router.push('/landlord/experiences')
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}

const getStatusType = (status) => {
  const typeMap = {
    '待支付': 'warning',
    '已支付': 'success',
    '已完成': 'info',
    '已取消': 'danger'
  }
  return typeMap[status] || 'info'
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
/* 全局变量 */
:root {
  --primary-color: #ff385c;
  --primary-light: #fff1f4;
  --bg-color: #f7f7f7;
  --card-radius: 18px;
  --soft-shadow: 0 8px 30px rgba(0,0,0,0.05);
}

/* 基础样式 */
body {
  background: var(--bg-color);
}

/* 导航栏 */
.navbar {
  background: white;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 32px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo h1 {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 700;
  color: var(--primary-color);
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
  transition: all 0.3s ease;
  padding: 8px 0;
  position: relative;
}

.nav-links a:hover,
.nav-links a.active {
  color: var(--primary-color);
}

.nav-links a.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: var(--primary-color);
  border-radius: 2px;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.user-dropdown:hover {
  background-color: var(--primary-light);
}

.username {
  font-size: 14px;
  font-weight: 500;
}

/* 主内容区 */
.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 32px;
}

/* 页面标题 */
.page-header {
  margin-bottom: 40px;
}

.page-header h1 {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.page-header p {
  font-size: 16px;
  color: #666;
  margin: 0;
}

/* 统计卡片 */
.stats-section {
  margin-bottom: 40px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
}

.stat-card {
  background: white;
  border-radius: 20px;
  padding: 28px;
  box-shadow: var(--soft-shadow);
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 40px rgba(0,0,0,0.08);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 网格布局 */
.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

/* 卡片 */
.card {
  background: white;
  border-radius: 20px;
  box-shadow: var(--soft-shadow);
  overflow: hidden;
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 12px 40px rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 24px 0;
}

.card-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.view-all {
  font-size: 14px;
  color: var(--primary-color);
  transition: color 0.3s ease;
}

.view-all:hover {
  color: #e03153;
}

.card-body {
  padding: 24px;
}

/* 操作按钮网格 */
.action-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.action-card {
  background: white;
  border: 2px solid var(--primary-color);
  border-radius: 16px;
  padding: 24px;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  text-align: center;
  height: 120px;
  color: var(--primary-color);
}

.action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(255, 56, 92, 0.2);
  border-color: var(--primary-color);
}

.primary-action {
  grid-column: 1 / -1;
  background: linear-gradient(135deg, #ff385c, #ff7a8f);
  border-color: #ff385c;
  color: white;
}

.primary-action:hover {
  box-shadow: 0 8px 24px rgba(255, 56, 92, 0.3);
  border-color: #ff385c;
}

.action-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-bottom: 8px;
  background: rgba(255, 56, 92, 0.1);
  color: var(--primary-color);
}

.create-icon {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.experience-icon {
  background: rgba(255, 56, 92, 0.1);
  color: var(--primary-color);
}

.homestay-icon {
  background: rgba(255, 56, 92, 0.1);
  color: var(--primary-color);
}

.order-icon {
  background: rgba(255, 56, 92, 0.1);
  color: var(--primary-color);
}

.primary-action .action-icon {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.action-text {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

/* 订单列表 */
.orders-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.order-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.order-item:hover {
  background: #f5f5f5;
}

.order-info {
  flex: 1;
}

.order-homestay {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin: 0 0 4px;
}

.order-details {
  font-size: 12px;
  color: #666;
  margin: 0;
}

.order-price {
  font-size: 16px;
  font-weight: 600;
  color: var(--primary-color);
  margin: 0 16px;
}

.order-status {
  flex-shrink: 0;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .navbar-container {
    padding: 16px 24px;
  }
  
  .nav-links {
    display: none;
  }
  
  .main-content {
    padding: 32px 24px;
  }
  
  .page-header h1 {
    font-size: 24px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .stat-card {
    padding: 20px;
  }
  
  .stat-value {
    font-size: 24px;
  }
}
</style>