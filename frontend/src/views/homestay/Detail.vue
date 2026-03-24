<template>
  <div class="homestay-detail">
    <!-- 导航栏 -->
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
        <!-- 面包屑导航 -->
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item @click="goHome">首页</el-breadcrumb-item>
          <el-breadcrumb-item @click="goToHomestays">民宿列表</el-breadcrumb-item>
          <el-breadcrumb-item>{{ homestay?.name }}</el-breadcrumb-item>
        </el-breadcrumb>
        
        <div v-if="homestay" class="detail-content" v-loading="loading">
          <!-- 图片轮播 -->
          <div class="image-gallery">
            <el-carousel :interval="5000" type="card" height="500px" indicator-position="outside">
              <el-carousel-item v-for="(image, index) in homestayImages" :key="index">
                <img :src="getImageUrl(image, true)" :alt="homestay.name" class="gallery-image" />
              </el-carousel-item>
            </el-carousel>
          </div>
          
          <!-- 基本信息和预订 -->
          <div class="info-booking-section">
            <div class="basic-info">
              <h1 class="homestay-title">{{ homestay.name }}</h1>
              <div class="rating-section">
                <el-rate v-model="rating" disabled show-score score-template="{{ value }}" />
                <span class="review-count">({{ reviewCount }}条评价)</span>
              </div>
              <div class="location-section">
                <el-icon class="location-icon"><Position /></el-icon>
                <span>{{ homestay.address }}</span>
              </div>
              <div class="room-type-section">
                <el-tag size="default" effect="light">{{ homestay.roomType }}</el-tag>
              </div>
            </div>
            
            <!-- 预订卡片 -->
            <div class="booking-card">
              <div class="price-section">
                <span class="price">¥{{ formatPrice(homestay.price) }}</span>
                <span class="price-unit">/晚</span>
              </div>
              <el-form :model="bookingForm" label-position="top">
                <el-row :gutter="10">
                  <el-col :span="12">
                    <el-form-item label="入住">
                      <el-date-picker
                        v-model="bookingForm.checkInDate"
                        type="date"
                        placeholder="选择日期"
                        :disabled-date="disabledDate"
                        @change="calculatePrice"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="退房">
                      <el-date-picker
                        v-model="bookingForm.checkOutDate"
                        type="date"
                        placeholder="选择日期"
                        :disabled-date="disabledCheckOutDate"
                        @change="calculatePrice"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="入住天数">
                  <el-input v-model="days" readonly style="width: 100%" />
                </el-form-item>
                <el-form-item label="总价">
                  <el-input v-model="totalPrice" readonly style="width: 100%">
                    <template #prepend>¥</template>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleBook" :disabled="!canBook" style="width: 100%" size="large">
                    立即预订
                  </el-button>
                </el-form-item>
                <div class="booking-info">
                  <div class="info-item">
                    <el-icon class="info-icon"><Calendar /></el-icon>
                    <span>灵活取消</span>
                  </div>
                  <div class="info-item">
                    <el-icon class="info-icon"><Clock /></el-icon>
                    <span>入住时间: 14:00后</span>
                  </div>
                  <div class="info-item">
                    <el-icon class="info-icon"><Clock /></el-icon>
                    <span>退房时间: 12:00前</span>
                  </div>
                </div>
              </el-form>
            </div>
          </div>
          
          <!-- 设施和描述 -->
          <div class="facilities-description-section">
            <div class="section-card">
              <h2 class="section-title">
                <el-icon class="section-icon"><House /></el-icon>
                房屋设施
              </h2>
              <div class="facilities-grid">
                <div v-for="(facility, index) in homestay.facility?.split(',') || []" :key="index" class="facility-item">
                  <el-icon class="facility-icon"><Check /></el-icon>
                  <span>{{ facility.trim() }}</span>
                </div>
              </div>
            </div>
            
            <div class="section-card">
              <h2 class="section-title">
                <el-icon class="section-icon"><Document /></el-icon>
                房屋描述
              </h2>
              <div class="description-content">
                {{ homestay.description }}
              </div>
            </div>
          </div>
          
          <!-- 位置信息 -->
          <div class="location-section-card section-card">
            <h2 class="section-title">
              <el-icon class="section-icon"><Position /></el-icon>
              位置信息
            </h2>
            <div class="location-info">
              <p class="address">{{ homestay.address }}</p>
            </div>
            
            <!-- 地图容器 -->
            <div v-if="homestay && homestay.longitude && homestay.latitude" class="map-container">
              <div class="map-controls">
                <el-button-group>
                  <el-button :type="mapType === 'standard' ? 'primary' : 'default'" @click="switchMapType('standard')">普通地图</el-button>
                  <el-button :type="mapType === 'satellite' ? 'primary' : 'default'" @click="switchMapType('satellite')">卫星地图</el-button>
                </el-button-group>
              </div>
              
              <div ref="mapContainer" class="map" id="map"></div>
            </div>
            <div v-else class="no-map">
              <el-empty description="暂无位置信息" />
            </div>
          </div>
          
          <!-- 房东信息 -->
          <div class="landlord-section-card section-card">
            <h2 class="section-title">
              <el-icon class="section-icon"><User /></el-icon>
              房东信息
            </h2>
            <div class="landlord-info">
              <el-avatar :size="64" class="landlord-avatar">
                {{ homestay.landlordUsername?.charAt(0).toUpperCase() }}
              </el-avatar>
              <div class="landlord-details">
                <h3>{{ homestay.landlordUsername }}</h3>
                <p class="landlord-phone">{{ homestay.landlordPhone }}</p>
                <p class="landlord-since">成为房东 {{ landlordSince }} 年</p>
              </div>
            </div>
          </div>
          
          <!-- 特色体验部分 -->
          <div v-if="experiences.length > 0" class="experiences-section-card section-card">
            <h2 class="section-title">
              <el-icon class="section-icon"><Star /></el-icon>
              特色体验
            </h2>
            <div class="experiences-list">
              <div v-for="(experience, index) in experiences" :key="index" class="experience-item">
                <div class="experience-image">
                  <img :src="getImageUrl(experience.imageUrl, true)" :alt="experience.name" class="experience-img" />
                </div>
                <div class="experience-info">
                  <h3 class="experience-title">{{ experience.name }}</h3>
                  <div class="experience-type">{{ experience.type }}</div>
                  <div class="experience-price">¥{{ experience.price }}/人</div>
                  <div class="experience-duration">{{ experience.duration }}分钟</div>
                  <div class="experience-time-slots">
                    可预约时段: {{ experience.availableTime }}
                  </div>
                  <el-button type="primary" @click="openExperienceBooking(experience)">
                    预约体验
                  </el-button>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="experiences-section-card section-card">
            <h2 class="section-title">
              <el-icon class="section-icon"><Star /></el-icon>
              特色体验
            </h2>
            <div class="no-experiences">
              <el-empty description="暂无特色体验" />
            </div>
          </div>
          
          <!-- 评论部分 -->
          <div class="reviews-section-card section-card">
            <h2 class="section-title">
              <el-icon class="section-icon"><Star /></el-icon>
              客人评价
            </h2>
            <div v-if="reviews.length > 0" class="reviews-list">
              <div v-for="(review, index) in reviews" :key="index" class="review-item">
                <div class="review-header">
                  <span class="reviewer-name">用户{{ review.userId }}</span>
                  <el-rate v-model="review.rating" disabled :max="5" />
                  <span class="review-date">{{ formatDate(review.createTime) }}</span>
                </div>
                <div class="review-content">{{ review.comment }}</div>
              </div>
            </div>
            <div v-else class="no-reviews">
              <el-empty description="暂无评价" />
            </div>
          </div>
        </div>
        
        <!-- 体验项目预订弹窗 -->
        <el-dialog
          v-model="experienceBookingVisible"
          :title="`预约: ${selectedExperience?.name}`"
          width="500px"
        >
          <el-form :model="experienceBookingForm" label-width="100px">
            <el-form-item label="预订日期">
              <el-date-picker
                v-model="experienceBookingForm.date"
                type="date"
                placeholder="选择日期"
                :disabled-date="disabledDate"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="可预约时段">
              <el-select
                v-model="experienceBookingForm.timeSlot"
                placeholder="选择时段"
                style="width: 100%"
              >
                <el-option
                  v-for="(slot, index) in selectedExperience?.availableTime?.split(',') || []"
                  :key="index"
                  :label="slot.trim()"
                  :value="slot.trim()"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="experienceBookingVisible = false">取消</el-button>
              <el-button type="primary" @click="handleExperienceBook">确认预约</el-button>
            </span>
          </template>
        </el-dialog>

      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { homestayAPI } from '@/api/homestay'
import { orderAPI } from '@/api/order'
import { ratingAPI } from '@/api/rating'
import { experienceAPI } from '@/api/experience'
import { getImageUrl, formatPrice } from '@/utils'
import { ElMessage } from 'element-plus'
import { ArrowDown, Position, House, Document, User, Star, Calendar, Clock, Check, Picture } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)
const homestay = ref(null)

// 预订相关
const bookingForm = reactive({
  checkInDate: null,
  checkOutDate: null
})

const days = ref(0)
const totalPrice = ref(0)

const canBook = computed(() => {
  return userStore.isLoggedIn && 
         bookingForm.checkInDate && 
         bookingForm.checkOutDate && 
         days.value > 0
})

// 评论相关
const rating = ref(4.5)
const reviewCount = ref(12)
const reviews = ref([])

// 房东相关
const landlordSince = ref(3)

// 体验项目相关
const experiences = ref([])
const selectedExperience = ref(null)
const experienceBookingVisible = ref(false)
const experienceBookingForm = reactive({
  date: null,
  timeSlot: ''
})

// 图片相关
const currentImageIndex = ref(0)

// 地图相关
const mapContainer = ref(null)
const map = ref(null)
const mapType = ref('standard') // standard 或 satellite

// 计算属性：将逗号分隔的图片URL转换为数组
const homestayImages = computed(() => {
  if (!homestay.value?.imageUrl || typeof homestay.value.imageUrl !== 'string') return []
  return homestay.value.imageUrl.split(',').map(url => url.trim()).filter(url => url)
})

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

const disabledCheckOutDate = (time) => {
  if (!bookingForm.checkInDate) return false
  return time.getTime() <= bookingForm.checkInDate.getTime()
}

const calculatePrice = () => {
  if (bookingForm.checkInDate && bookingForm.checkOutDate) {
    const diff = bookingForm.checkOutDate.getTime() - bookingForm.checkInDate.getTime()
    days.value = Math.ceil(diff / (1000 * 60 * 60 * 24))
    totalPrice.value = (days.value * homestay.value.price).toFixed(2)
  } else {
    days.value = 0
    totalPrice.value = 0
  }
}

const loadHomestayDetail = async () => {
  loading.value = true
  try {
    const id = route.params.id
    homestay.value = await homestayAPI.getDetail(id)
    // 加载评论数据
    try {
      const reviewData = await ratingAPI.getHomestayRatings(id)
      // 检查返回数据结构
      if (reviewData && reviewData.data) {
        reviews.value = reviewData.data
        reviewCount.value = reviewData.data.length
        if (reviewData.data.length > 0) {
          rating.value = reviewData.data.reduce((sum, review) => sum + review.rating, 0) / reviewData.data.length
        }
      } else {
        // 兼容直接返回列表的情况
        reviews.value = reviewData
        reviewCount.value = reviewData.length
        if (reviewData.length > 0) {
          rating.value = reviewData.reduce((sum, review) => sum + review.rating, 0) / reviewData.length
        }
      }
    } catch (error) {
      console.error('加载评论失败:', error)
    }
    
    // 加载体验项目数据
    try {
      const experienceData = await experienceAPI.getList({ homestayId: id })
      // 检查是否为分页对象（有records属性）
      if (experienceData && experienceData.records) {
        experiences.value = experienceData.records
      } else if (Array.isArray(experienceData)) {
        // 兼容直接返回列表的情况
        experiences.value = experienceData
      }
    } catch (error) {
      console.error('加载体验项目失败:', error)
    }
  } catch (error) {
    console.error('加载民宿详情失败:', error)
  } finally {
    loading.value = false
  }
}





const handleBook = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  try {
    await orderAPI.create({
      userId: userStore.userId,
      homestayId: homestay.value.id,
      checkInDate: formatDate(bookingForm.checkInDate),
      checkOutDate: formatDate(bookingForm.checkOutDate),
      price: parseFloat(totalPrice.value),
      guestName: userStore.userInfo?.guestName || userStore.username,
      guestPhone: userStore.userInfo?.phone || '',
      guestEmail: userStore.userInfo?.email || ''
    })
    ElMessage.success('预订成功')
    router.push('/user/orders')
  } catch (error) {
    console.error('预订失败:', error)
  }
}

const formatDate = (date) => {
  if (!date) return '未知日期'
  const d = new Date(date)
  if (isNaN(d.getTime())) return '未知日期'
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
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

const openGallery = (index) => {
  // 这里可以实现图片画廊的打开逻辑
  console.log('打开图片画廊，索引:', index)
}

// 打开体验项目预订弹窗
const openExperienceBooking = (experience) => {
  selectedExperience.value = experience
  experienceBookingForm.date = null
  experienceBookingForm.timeSlot = ''
  experienceBookingVisible.value = true
}

// 处理体验项目预订
const handleExperienceBook = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (!experienceBookingForm.date || !experienceBookingForm.timeSlot) {
    ElMessage.warning('请选择日期和时段')
    return
  }
  
  try {
    // 创建体验项目订单
    const result = await orderAPI.create({
      userId: userStore.userId,
      experienceId: selectedExperience.value.id,
      homestayId: homestay.value.id,
      bookingDate: formatDate(experienceBookingForm.date),
      timeSlot: experienceBookingForm.timeSlot,
      price: selectedExperience.value.price,
      guestName: userStore.userInfo?.guestName || userStore.username,
      guestPhone: userStore.userInfo?.phone || '',
      guestEmail: userStore.userInfo?.email || '',
      type: 'EXPERIENCE' // 标记为体验项目订单
    })
    
    console.log('预约体验项目成功:', result)
    ElMessage.success('预约成功')
    experienceBookingVisible.value = false
    
    // 跳转到订单详情页面进行支付
    if (result.data && result.data.id) {
      router.push(`/user/orders`)
    }
  } catch (error) {
    console.error('预约失败:', error)
    ElMessage.error('预约失败')
  }
}

// 初始化地图
const initMap = () => {
  if (!homestay.value || !homestay.value.longitude || !homestay.value.latitude || !mapContainer.value) return
  
  // 检查是否已加载高德地图API
  if (typeof AMap === 'undefined') {
    ElMessage.error('地图API加载失败')
    return
  } else {
    console.log('开始创建地图...')
    createMap()
  }
}

// 创建地图
const createMap = () => {
  if (!homestay.value || !homestay.value.longitude || !homestay.value.latitude || !mapContainer.value) return
  
  console.log('地图容器:', mapContainer.value)
  console.log('民宿坐标:', homestay.value.longitude, homestay.value.latitude)
  
  // 销毁已存在的地图实例
  if (map.value) {
    map.value.destroy()
    map.value = null
  }
  
  // 加载所需插件
  AMap.plugin(['AMap.Marker', 'AMap.ToolBar', 'AMap.Scale', 'AMap.MapType', 'AMap.TileLayer.Satellite'], function() {
    try {
      // 创建地图实例 - 使用ref引用的DOM元素
      map.value = new AMap.Map(mapContainer.value, {
        center: [homestay.value.longitude, homestay.value.latitude],
        zoom: 15
      })
      
      console.log('地图创建成功:', map.value)
      
      // 添加标记
      new AMap.Marker({
        position: [homestay.value.longitude, homestay.value.latitude],
        title: homestay.value.name,
        map: map.value
      })
      
      // 添加控件
      map.value.addControl(new AMap.ToolBar())
      map.value.addControl(new AMap.Scale())
      
      // 切换到指定地图类型
      if (mapType.value === 'satellite') {
        // 切换到卫星地图
        const satelliteLayer = new AMap.TileLayer.Satellite()
        satelliteLayer.setMap(map.value)
      } else {
        // 切换到标准地图
        // 标准地图是默认类型，不需要额外设置
      }
    } catch (error) {
      console.error('地图创建失败:', error)
      ElMessage.error('地图创建失败，请刷新页面重试')
    }
  })
}

// 切换地图类型
const switchMapType = (type) => {
  mapType.value = type
  // 销毁当前地图实例并重新创建
  if (map.value) {
    map.value.destroy()
    map.value = null
  }
  // 重新初始化地图
  initMap()
}

onMounted(() => {
  loadHomestayDetail()
  
  // 轮询等待高德API加载（防止script标签异步加载未完成）
  const checkAMap = setInterval(() => {
    if (typeof AMap !== 'undefined' && mapContainer.value) {
      clearInterval(checkAMap)
      initMap()
    }
  }, 500)
  
  // 10秒超时保护
  setTimeout(() => clearInterval(checkAMap), 10000)
})
</script>

<style scoped>
 /* General Styles */ 
 body { 
   font-family: 'Helvetica Neue', Arial, sans-serif; 
   color: #333; 
   background-color: #f8f9fa; 
 } 
 
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
 
 /* Breadcrumb */ 
 .breadcrumb { 
   margin-bottom: 30px; 
   font-size: 16px; 
   font-weight: 600; 
 } 
 
 .detail-content { 
   padding: 40px 20px; 
 } 
 
 /* Image Gallery */ 
 .image-gallery { 
   margin-bottom: 40px; 
   border-radius: 15px; 
   overflow: hidden; 
   box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1); 
 } 
 
 .gallery-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 10px;
  display: block;
  min-width: 100%;
  min-height: 100%;
} 
 
 /* Info and Booking Section */ 
 .info-booking-section { 
   display: flex; 
   justify-content: space-between; 
   gap: 40px; 
   margin-bottom: 50px; 
 } 
 
 .basic-info { 
   flex: 1; 
 } 
 
 .homestay-title { 
   font-size: 36px; 
   font-weight: 700; 
   color: #333; 
   margin-bottom: 16px; 
 } 
 
 .rating-section { 
   display: flex; 
   align-items: center; 
   gap: 12px; 
   margin-bottom: 16px; 
 } 
 
 .review-count { 
   font-size: 14px; 
   color: #888; 
 } 
 
 .location-section { 
   display: flex; 
   align-items: center; 
   gap: 8px; 
   margin-bottom: 16px; 
   font-size: 18px; 
   color: #555; 
 } 
 
 .location-icon { 
   color: #e91e63; 
 } 
 
 .room-type-section { 
   margin-bottom: 20px; 
 } 
 
 /* Booking Card */ 
 .booking-card { 
   width: 350px; 
   border-radius: 12px; 
   box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1); 
   padding: 32px; 
   background: white; 
 } 
 
 .price-section { 
   display: flex; 
   align-items: baseline; 
   margin-bottom: 24px; 
 } 
 
 .price { 
   color: #e91e63; 
   font-size: 40px; 
   font-weight: bold; 
   margin-right: 10px; 
 } 
 
 .price-unit { 
   font-size: 18px; 
   color: #888; 
 } 
 
 .booking-info { 
   margin-top: 20px; 
   padding-top: 20px; 
   border-top: 1px solid #f0f0f0; 
 } 
 
 .info-item { 
   display: flex; 
   align-items: center; 
   gap: 12px; 
   margin-bottom: 16px; 
   font-size: 14px; 
   color: #888; 
 } 
 
 .info-icon { 
   color: #5e72e4; 
 } 
 
 /* Facilities and Description */ 
 .facilities-description-section { 
   display: flex; 
   justify-content: space-between; 
   gap: 40px; 
   margin-bottom: 50px; 
 } 
 
 .section-card { 
   flex: 1; 
   border-radius: 12px; 
   box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1); 
   padding: 40px; 
   background: white; 
 } 
 
 .section-title { 
   display: flex; 
   align-items: center; 
   gap: 12px; 
   font-size: 24px; 
   font-weight: 700; 
   color: #333; 
   margin-bottom: 30px; 
 } 
 
 .section-icon { 
   color: #5e72e4; 
 } 
 
 /* Facility Grid */ 
 .facilities-grid { 
   display: grid; 
   grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); 
   gap: 24px; 
 } 
 
 .facility-item { 
   display: flex; 
   align-items: center; 
   gap: 12px; 
   font-size: 16px; 
   color: #555; 
 } 
 
 .facility-icon { 
   color: #67c23a; 
 } 
 
 .description-content { 
   font-size: 16px; 
   line-height: 1.6; 
   color: #555; 
 } 
 
 /* Landlord Information */ 
 .landlord-section-card { 
   margin-bottom: 50px; 
 } 
 
 .landlord-info { 
   display: flex; 
   align-items: center; 
   gap: 24px; 
 } 
 
 .landlord-avatar { 
   background: linear-gradient(135deg, #5e72e4 0%, #764ba2 100%); 
   color: white; 
   font-size: 24px; 
   font-weight: bold; 
 } 
 
 .landlord-details { 
   flex: 1; 
 } 
 
 .landlord-details h3 { 
   font-size: 20px; 
   font-weight: 700; 
   color: #333; 
   margin-bottom: 8px; 
 } 
 
 .landlord-phone { 
   font-size: 14px; 
   color: #777; 
   margin-bottom: 8px; 
 } 
 
 .landlord-since { 
   font-size: 14px; 
   color: #999; 
 } 
 
 /* Reviews Section */ 
 .reviews-section-card { 
   margin-bottom: 50px; 
 } 
 
 .reviews-list { 
   display: flex; 
   flex-direction: column; 
   gap: 24px; 
 } 
 
 .review-item { 
   padding-bottom: 24px; 
   border-bottom: 1px solid #f0f0f0; 
 } 
 
 .review-item:last-child { 
   border-bottom: none; 
 } 
 
 .review-header { 
   display: flex; 
   align-items: center; 
   gap: 16px; 
   margin-bottom: 12px; 
 } 
 
 .reviewer-name { 
   font-weight: bold; 
   color: #333; 
 } 
 
 .review-date { 
   font-size: 14px; 
   color: #999; 
 } 
 
 .review-content { 
   font-size: 16px; 
   line-height: 1.6; 
   color: #555; 
 } 
 
 .no-reviews {
  padding: 40px 0;
  text-align: center;
}

/* 体验项目相关样式 */
.experiences-section-card {
  margin-bottom: 50px;
}

.experiences-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.experience-item {
  display: flex;
  gap: 30px;
  padding: 20px;
  border-radius: 12px;
  background-color: #f8f9fa;
  transition: all 0.3s ease;
}

.experience-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.experience-image {
  width: 200px;
  height: 150px;
  border-radius: 8px;
  overflow: hidden;
}

.experience-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  min-width: 100%;
  min-height: 100%;
}

.experience-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.experience-title {
  font-size: 20px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.experience-type {
  font-size: 14px;
  color: #888;
  margin-bottom: 8px;
}

.experience-price {
  font-size: 18px;
  font-weight: 700;
  color: #e91e63;
  margin-bottom: 8px;
}

.experience-duration {
  font-size: 14px;
  color: #555;
  margin-bottom: 8px;
}

.experience-time-slots {
  font-size: 14px;
  color: #555;
  margin-bottom: 16px;
}

.no-experiences {
  padding: 40px 0;
  text-align: center;
}

/* 地图相关样式 */
.map-container {
  margin-top: 30px;
}

.map-controls {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-start;
}

.map {
  width: 100%;
  height: 400px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.no-map {
  padding: 60px 0;
  text-align: center;
}

/* 路径规划面板样式 */
.route-planning-panel {
  background-color: white;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.route-planning-panel .el-form {
  margin-bottom: 20px;
}

.route-result {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.route-result h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.route-result p {
  margin: 4px 0;
  font-size: 14px;
  color: #606266;
}

/* 地址输入和建议样式 */
.address-input-container {
  position: relative;
  width: 100%;
}

.address-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  z-index: 9999;
  max-height: 300px;
  overflow-y: auto;
  margin-top: 4px;
}

.suggestion-item {
  padding: 12px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.suggestion-item:hover {
  background-color: #f5f7fa;
}

/* User Dropdown */ 
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
 
 /* Responsive Styles - Desktop First */ 
 @media (max-width: 1600px) { 
   .info-booking-section { 
     flex-direction: column; 
   } 
 
   .booking-card { 
     width: 100%; 
     position: static; 
   } 
 
   .facilities-description-section { 
     flex-direction: column; 
   } 
 }
</style>
