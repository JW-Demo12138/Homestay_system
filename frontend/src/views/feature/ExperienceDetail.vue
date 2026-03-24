<template>
  <div class="experience-detail">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <h1>民宿预订系统</h1>
          </div>
          <div class="nav">
            <el-link @click="goHome">首页</el-link>
            <el-link @click="goToHomestays">民宿列表</el-link>
            <el-link @click="goToFeatureTopic">特色体验</el-link>
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
                    <el-dropdown-item
                      v-if="userStore.isAdmin"
                      @click="goToAdmin"
                    >
                      管理员中心
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
        <div class="page-header">
          <h2>体验项目详情</h2>
          <el-button @click="goBack">返回</el-button>
        </div>
        
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="!experience" class="empty">
          <el-empty description="体验项目不存在" />
        </div>
        <div v-else class="experience-content">
          <!-- 图片轮播 -->
          <div class="image-gallery">
            <el-carousel :interval="5000" type="card" height="400px" indicator-position="outside">
              <el-carousel-item v-for="(image, index) in experienceImages" :key="index">
                <img :src="getImageUrl(image, true)" :alt="experience.name" class="gallery-image" />
              </el-carousel-item>
            </el-carousel>
          </div>
          
          <!-- 基本信息 -->
          <div class="info-section">
            <h1 class="experience-title">{{ experience.name }}</h1>
            <div class="experience-meta">
              <el-tag>{{ experience.type }}</el-tag>
              <span class="price">¥{{ experience.price }}/人</span>
              <span class="duration">{{ experience.duration }}分钟</span>
            </div>
            <div class="experience-location">
              <el-icon class="location-icon"><Position /></el-icon>
              <span>{{ experience.location }}</span>
            </div>
            <div class="experience-time">
              <el-icon class="time-icon"><Clock /></el-icon>
              <span>可预约时段: {{ experience.availableTime }}</span>
            </div>
            <div class="experience-description">
              <h3>体验描述</h3>
              <p>{{ experience.description }}</p>
            </div>
            <div class="homestay-info">
              <h3>关联民宿</h3>
              <div v-if="homestay" class="homestay-card" @click="goToHomestayDetail(homestay.id)">
                <img :src="getImageUrl((homestay.imageUrl || '').split(',')[0], true)" :alt="homestay.name" />
                <div class="homestay-info-content">
                  <h4>{{ homestay.name }}</h4>
                  <p>{{ homestay.address }}</p>
                  <p class="homestay-price">¥{{ homestay.price }}/晚</p>
                </div>
              </div>
              <div v-else class="no-homestay">
                <el-empty description="暂无关联民宿" />
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { experienceAPI } from '@/api/experience'
import { homestayAPI } from '@/api/homestay'
import { getImageUrl } from '@/utils'
import { ElMessage } from 'element-plus'
import { ArrowDown, Position, Clock } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(true)
const experience = ref(null)
const homestay = ref(null)

// 计算属性：将逗号分隔的图片URL转换为数组
const experienceImages = computed(() => {
  if (!experience.value?.imageUrl || typeof experience.value.imageUrl !== 'string') return []
  return experience.value.imageUrl.split(',').map(url => url.trim()).filter(url => url)
})

const loadExperienceDetail = async () => {
  loading.value = true
  try {
    const id = route.params.id
    experience.value = await experienceAPI.getDetail(id)
    
    // 加载关联的民宿信息
    if (experience.value.homestayId) {
      try {
        homestay.value = await homestayAPI.getDetail(experience.value.homestayId)
      } catch (error) {
        console.error('加载民宿信息失败:', error)
      }
    }
  } catch (error) {
    console.error('加载体验项目详情失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

const goHome = () => {
  router.push('/')
}

const goToHomestays = () => {
  router.push('/homestays')
}

const goToFeatureTopic = () => {
  router.push('/feature/topic')
}

const goToHomestayDetail = (id) => {
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

const goToOrders = () => {
  router.push('/user/orders')
}

const goToLandlord = () => {
  router.push('/landlord')
}

const goToAdmin = () => {
  router.push('/admin')
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}

onMounted(() => {
  loadExperienceDetail()
})
</script>

<style scoped>
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
  font-size: 18px;
  font-weight: bold;
  color: #5e72e4;
  margin-bottom: 10px;
}

.nav {
  display: flex;
  gap: 30px;
}

.nav .el-link {
  font-size: 18px;
  color: #5e72e4;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav .el-link:hover {
  color: #3f51b5;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h2 {
  margin: 0;
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.loading {
  text-align: center;
  padding: 100px 0;
  font-size: 18px;
  color: #666;
}

.empty {
  text-align: center;
  padding: 100px 0;
}

.experience-content {
  max-width: 1200px;
  margin: 0 auto;
}

.image-gallery {
  margin-bottom: 40px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.gallery-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 12px;
}

.info-section {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.experience-title {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  margin-bottom: 20px;
}

.experience-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.experience-meta .price {
  font-size: 24px;
  font-weight: 600;
  color: #e91e63;
}

.experience-meta .duration {
  font-size: 16px;
  color: #666;
}

.experience-location,
.experience-time {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
  font-size: 16px;
  color: #555;
}

.location-icon,
.time-icon {
  color: #5e72e4;
}

.experience-description {
  margin-top: 30px;
  margin-bottom: 30px;
}

.experience-description h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.experience-description p {
  font-size: 16px;
  line-height: 1.6;
  color: #555;
}

.homestay-info {
  margin-top: 30px;
}

.homestay-info h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.homestay-card {
  display: flex;
  gap: 20px;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.homestay-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.homestay-card img {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
}

.homestay-info-content {
  flex: 1;
}

.homestay-info-content h4 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.homestay-info-content p {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.homestay-price {
  font-size: 16px;
  font-weight: 600;
  color: #e91e63;
}

.no-homestay {
  padding: 40px 0;
  text-align: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f0f4f7;
}

.username {
  font-size: 16px;
  font-weight: 600;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 20px;
    height: 60px;
  }
  
  .nav {
    gap: 20px;
  }
  
  .nav .el-link {
    font-size: 14px;
  }
  
  .page-header h2 {
    font-size: 24px;
  }
  
  .info-section {
    padding: 20px;
  }
  
  .experience-title {
    font-size: 24px;
  }
  
  .homestay-card {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .homestay-card img {
    width: 100%;
    height: 200px;
  }
}
</style>