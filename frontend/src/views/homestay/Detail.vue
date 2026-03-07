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
        
        <div v-if="homestay" class="detail-content" v-loading="loading">          <!-- 图片轮播 -->
          <div class="image-gallery">
            <el-carousel :interval="5000" type="card" height="500px" indicator-position="outside">
              <el-carousel-item v-for="(image, index) in homestayImages" :key="index">
                <img :src="getImageUrl(image)" :alt="homestay.name" class="gallery-image" />
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
          
          <!-- 位置和路线 -->
          <div class="location-section-card section-card">
            <h2 class="section-title">
              <el-icon class="section-icon"><Position /></el-icon>
              位置信息
            </h2>
            <div class="map-container">
              <div id="map" style="width: 100%; height: 450px; min-width: 300px; min-height: 400px;"></div>
              <div class="map-actions">
                <el-button type="primary" @click="showRoutePlanning">
                  <el-icon><Compass /></el-icon>
                  查看路线
                </el-button>
              </div>
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
              <el-button type="default" plain>
                <el-icon><ChatLineRound /></el-icon>
                联系房东
              </el-button>
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
        
        <!-- 路径规划弹窗 -->
        <el-dialog
          v-model="routeDialogVisible"
          title="路径规划"
          width="550px"
          custom-class="route-dialog"
        >
          <el-form :model="routeForm" label-width="100px">
            <el-form-item label="出发点">
              <el-radio-group v-model="routeForm.startType">
                <el-radio label="current">当前位置</el-radio>
                <el-radio label="custom">自定义位置</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="自定义地址" v-if="routeForm.startType === 'custom'">
              <div class="address-input-container">
                <el-input 
                  v-model="routeForm.startAddress" 
                  placeholder="请输入出发点地址" 
                  @input="handleAddressInput"
                />
                <div v-if="addressSuggestions.length > 0" class="address-suggestions">
                  <div 
                    v-for="(suggestion, index) in addressSuggestions" 
                    :key="index"
                    class="suggestion-item"
                    @click="selectAddress(suggestion)"
                  >
                    {{ suggestion }}
                  </div>
                </div>
              </div>
            </el-form-item>
            <el-form-item label="终点">
              <el-input v-model="routeForm.endAddress" readonly />
            </el-form-item>
          </el-form>
          
          <!-- 路线信息显示区域 -->
          <div v-if="routeInfo.show" class="route-info">
            <h3 class="route-info-title">路线信息</h3>
            <div class="route-info-content">
              <div class="info-item">
                <el-icon class="info-icon"><Timer /></el-icon>
                <span>预估时间: {{ routeInfo.duration }}</span>
              </div>
              <div class="info-item">
                <el-icon class="info-icon"><Location /></el-icon>
                <span>距离: {{ routeInfo.distance }}</span>
              </div>
              <div class="info-item">
                <el-icon class="info-icon"><Money /></el-icon>
                <span>预估费用: {{ routeInfo.cost }}</span>
              </div>
            </div>
          </div>
          
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="routeDialogVisible = false" :disabled="routeLoading">取消</el-button>
              <el-button type="primary" @click="calculateRoute" :loading="routeLoading">开始规划</el-button>
            </span>
          </template>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { homestayAPI } from '@/api/homestay'
import { orderAPI } from '@/api/order'
import { ratingAPI } from '@/api/rating'
import { geocodingAPI } from '@/api/geocoding'
import { getImageUrl, formatPrice } from '@/utils'
import { BASE_URL } from '@/config'
import { ElMessage } from 'element-plus'
import { ArrowDown, Position, House, Document, Compass, User, Star, Calendar, Clock, Check, ChatLineRound, Timer, Location, Money, Close, Search, Van, Guide } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)
const homestay = ref(null)
const map = ref(null)

// 路径规划相关
const routeDialogVisible = ref(false)
const routeForm = reactive({
  startType: 'current',
  startAddress: '',
  endAddress: ''
})
const addressSuggestions = ref([])

// 路线信息
const routeInfo = reactive({
  show: false,
  duration: '',
  distance: '',
  cost: ''
})

// 路线规划加载状态
const routeLoading = ref(false)

// ============ 1. 实例缓存与单例管理 ============
// 缓存地图服务实例，避免重复创建
let localSearch = null
let driving = null
let geocoder = null
// 防抖定时器
let debounceTimer = null
// 搜索状态
const searching = ref(false)
// 路线标记
let routeLabel = null
// 路线缓存
const routeCache = new Map()
// 搜索定时器
let searchTimer = null

// 清空路线缓存
const clearRouteCache = () => {
  routeCache.clear()
  ElMessage.success('路线缓存已清空')
}

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

// 评价相关
const rating = ref(4.5)
const reviewCount = ref(12)
const reviews = ref([
  {
    username: '游客1',
    rating: 5,
    date: '2026-02-15',
    content: '房子很干净，位置很好，房东很热情，下次还会再来！'
  },
  {
    username: '游客2',
    rating: 4,
    date: '2026-02-10',
    content: '整体不错，设施齐全，就是隔音效果一般。'
  }
])

// 房东相关
const landlordSince = ref(3)

// 图片相关
const currentImageIndex = ref(0)

// 计算属性：将逗号分隔的图片URL转换为数组
const homestayImages = computed(() => {
  if (!homestay.value?.imageUrl) return []
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
    // 更新终点地址
    if (homestay.value) {
      routeForm.endAddress = homestay.value.address
    }
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
    // 等待 DOM 更新完成后再初始化地图
    await nextTick()
    
    // 检查地图 API 是否加载
    console.log('开始初始化地图');
    console.log('window.BMap:', window.BMap);
    
    // 直接检查百度地图 API 是否加载
    if (!window.BMap) {
      console.error('百度地图 API 未加载或加载失败')
      // 即使地图加载失败，也继续执行，避免阻塞页面
      return
    }
    
    console.log('百度地图 API 已加载')
    
    // 尝试初始化地图
    initMap();
  } catch (error) {
    console.error('加载民宿详情失败:', error)
  } finally {
    loading.value = false
  }
}

// 初始化地图服务（复用实例）
const initMapServices = () => {
  if (!window.BMap) return false
  
  // 复用Geocoder实例
  if (!geocoder) {
    geocoder = new window.BMap.Geocoder()
  }
  
  // 复用DrivingRoute实例
  if (!driving) {
    driving = new window.BMap.DrivingRoute(map.value, {
      renderOptions: {
        map: map.value,
        autoViewport: true,
        enableDragging: true // 允许拖拽路线
      },
      policy: window.BMAP_DRIVING_POLICY_LEAST_TIME, // 优先最短时间
      onSearchComplete: handleRouteComplete,
      onMarkersSet: () => {
        // 标记设置完成回调
      }
    })
  }
  
  return true
}

const initMap = () => {
  try {
    console.log('开始初始化地图')
    
    // 检查民宿数据和坐标是否存在
    if (!homestay.value || !homestay.value.latitude || !homestay.value.longitude) {
      console.log('缺少坐标信息')
      return
    }
    
    // 检查坐标是否为有效数字
    const longitude = parseFloat(homestay.value.longitude)
    const latitude = parseFloat(homestay.value.latitude)
    if (isNaN(longitude) || isNaN(latitude)) {
      console.log('坐标值无效')
      return
    }
    
    console.log('坐标信息:', { longitude, latitude })
    
    // 检查地图容器是否存在
    const mapContainer = document.getElementById('map')
    if (!mapContainer) {
      console.log('地图容器不存在')
      return
    }
    
    console.log('地图容器存在:', mapContainer)
    console.log('地图容器尺寸:', mapContainer.offsetWidth, 'x', mapContainer.offsetHeight)
    
    // 检查百度地图 API 是否加载
    if (!window.BMap) {
      console.error('百度地图 API 未加载或加载失败')
      return
    }
    
    console.log('百度地图 API 已加载')
    
    // 初始化地图
    map.value = new window.BMap.Map('map')
    console.log('地图实例创建成功')
    
    // 设置地图类型为卫星图
    map.value.setMapType(window.BMAP_SATELLITE_MAP)
    console.log('地图类型设置为卫星图')
    
    const point = new window.BMap.Point(longitude, latitude)
    console.log('地图中心点:', point)
    
    // 尝试使用不同的缩放级别
    map.value.centerAndZoom(point, 14)
    console.log('地图中心点和缩放级别设置成功')
    
    // 添加标记
    const marker = new window.BMap.Marker(point)
    map.value.addOverlay(marker)
    console.log('地图标记添加成功')
    
    // 添加信息窗口
    const infoWindow = new window.BMap.InfoWindow(homestay.value.name)
    marker.addEventListener('click', function() {
      this.openInfoWindow(infoWindow)
    })
    console.log('信息窗口添加成功')
    
    // 添加控件
    map.value.addControl(new window.BMap.NavigationControl({
      type: window.BMAP_NAVIGATION_CONTROL_LARGE,
      anchor: window.BMAP_ANCHOR_TOP_LEFT
    }))
    map.value.addControl(new window.BMap.ScaleControl({
      anchor: window.BMAP_ANCHOR_BOTTOM_LEFT
    }))
    // 移除地图类型控件，只显示卫星图
    console.log('地图控件添加成功')
    
    // 尝试设置地图的最小和最大缩放级别
    map.value.setMinZoom(3)
    map.value.setMaxZoom(19)
    console.log('地图缩放级别设置成功')
    
    // 尝试启用滚轮缩放
    map.value.enableScrollWheelZoom(true)
    console.log('滚轮缩放启用成功')
    
    // 尝试启用键盘控制
    map.value.enableKeyboard(true)
    console.log('键盘控制启用成功')
    
    // 尝试启用双击放大
    map.value.enableDoubleClickZoom(true)
    console.log('双击放大启用成功')
    
    // 调整地图视野，确保标记在视野中心
    map.value.setViewport([point])
    console.log('地图视野调整成功')
    
    // 初始化地图服务
    initMapServices()
    
    console.log('地图初始化成功')
  } catch (error) {
    console.error('地图初始化失败:', error)
    // 即使地图初始化失败，也不影响页面其他功能
  }
}

// 显示路径规划弹窗
const showRoutePlanning = () => {
  routeDialogVisible.value = true
}

// ============ 2. 防抖处理地址输入 ============
const handleAddressInput = () => {
  if (!window.BMap) return
  
  // 清除之前的定时器
  clearTimeout(debounceTimer)
  
  // 输入少于2个字符时不搜索
  if (!routeForm.startAddress || routeForm.startAddress.length < 2) {
    addressSuggestions.value = []
    return
  }
  
  // 300ms防抖延迟
  debounceTimer = setTimeout(() => {
    performLocalSearch(routeForm.startAddress)
  }, 300)
}

// 执行本地搜索（抽离逻辑）
const performLocalSearch = (keyword) => {
  if (!localSearch) {
    localSearch = new window.BMap.LocalSearch(map.value, {
      onSearchComplete: handleLocalSearchComplete,
      renderOptions: {
        map: map.value,
        autoViewport: false, // 不自动调整视野，避免频繁重绘
        selectFirstResult: false
      }
    })
  }
  
  localSearch.search(keyword)
}

// 处理搜索结果（简化逻辑）
const handleLocalSearchComplete = (results) => {
  if (!results || typeof results.getCurrentNumPois !== 'function') {
    addressSuggestions.value = []
    return
  }
  
  const count = results.getCurrentNumPois()
  if (count === 0) {
    addressSuggestions.value = []
    return
  }
  
  // 使用Array.from替代for循环，更高效
  addressSuggestions.value = Array.from({ length: count }, (_, i) => {
    const poi = results.getPoi(i)
    return poi ? poi.title : ''
  }).filter(Boolean) // 过滤空值
}

// 选择地址
const selectAddress = (address) => {
  routeForm.startAddress = address
  addressSuggestions.value = []
}

// ============ 3. 优化路径计算 ============
const calculateRoute = async () => {
  // 防止重复调用
  if (searching.value) {
    ElMessage.warning('正在规划路线，请稍候...')
    return
  }
  
  const { latitude, longitude } = homestay.value || {}
  if (!latitude || !longitude) {
    ElMessage.error('缺少民宿坐标')
    return
  }

  // 清除之前可能存在的超时定时器
  if (window.routeSearchTimeout) {
    clearTimeout(window.routeSearchTimeout)
    window.routeSearchTimeout = null
  }

  // 清除之前的搜索定时器
  if (searchTimer) {
    clearTimeout(searchTimer)
    searchTimer = null
  }

  try {
    let origin
    let startName

    if (routeForm.startType === 'current') {
      const position = await getCurrentPosition()
      console.log('当前位置:', position.coords)
      origin = `${position.coords.latitude},${position.coords.longitude}`
      startName = '当前位置'
    } else {
      if (!routeForm.startAddress) {
        throw new Error('请输入地址')
      }
      // 先将地址转换为坐标
      try {
        const coords = await geocodingAPI.addressToCoords(routeForm.startAddress)
        origin = `${coords.latitude},${coords.longitude}`
        startName = routeForm.startAddress
        console.log('地址转换结果:', coords)
      } catch (addrError) {
        console.error('地址转换错误:', addrError)
        throw new Error('地址转换失败，请检查地址是否正确')
      }
    }

    const dest = `${latitude},${longitude}`
    const mode = 'driving'
    const force = false // 默认为false，使用缓存

    searching.value = true
    routeLoading.value = true
    
    // 延迟关闭弹窗，避免阻塞UI
    requestAnimationFrame(() => {
      routeDialogVisible.value = false
    })

    clearRoute()

    console.log('路线规划参数:', { origin, dest, mode })
    console.log('请求URL:', `${BASE_URL}/api/map/route?origin=${origin}&destination=${dest}&mode=${mode}&refresh=${force}`)

    // 调用后端API获取路线信息
    const routeData = await geocodingAPI.searchRoute(origin, dest, mode, force)
    console.log('路线规划结果:', routeData)

    // 更新路线信息
    updateRouteInfo(routeData.distance, routeData.duration)
    
    // 清除状态
    searching.value = false
    routeLoading.value = false
    
  } catch (error) {
    // 清除超时定时器
    if (window.routeSearchTimeout) {
      clearTimeout(window.routeSearchTimeout)
      window.routeSearchTimeout = null
    }
    
    searching.value = false
    routeLoading.value = false
    console.error('路径规划错误:', error)
    console.error('错误详情:', error.response)
    ElMessage.error(error.message || '路径规划失败')
  }
}



// 封装获取当前位置为Promise
const getCurrentPosition = () => {
  return new Promise((resolve, reject) => {
    navigator.geolocation.getCurrentPosition(resolve, () => {
      reject(new Error('获取当前位置失败'))
    })
  })
}

// 封装地址解析为Promise（复用geocoder实例）
const geocodeAddress = (address) => {
  return new Promise((resolve, reject) => {
    console.log('开始解析地址:', address)
    geocoder.getPoint(address, (point) => {
      if (point) {
        console.log('地址解析成功:', address, '->', point)
        resolve(point)
      } else {
        console.error('地址解析失败:', address)
        reject(new Error('地址解析失败'))
      }
    }, {
      // 添加城市参数，提高解析准确性
      city: '' // 留空表示全国范围搜索
    })
  })
}

// 清理旧路线
const clearRoute = () => {
  if (driving) {
    driving.clearResults()
  }
  routeInfo.show = false
}

// ============ 4. 简化路线结果处理 ============
const handleRouteComplete = (results) => {
  // 清除超时定时器
  if (window.routeSearchTimeout) {
    clearTimeout(window.routeSearchTimeout)
    window.routeSearchTimeout = null
  }
  
  // 只在开发环境输出日志
  if (process.env.NODE_ENV === 'development') {
    console.log('路线规划完成')
  }

  // 注意：这个回调现在主要用于处理地图上的路线显示
  // 实际的路线信息处理已经在searchRouteWithRetry函数中完成
}

// 封装DrivingRoute的getSearchCompleteCallback方法（如果不存在）
if (window.BMap && !window.BMap.DrivingRoute.prototype.getSearchCompleteCallback) {
  window.BMap.DrivingRoute.prototype.getSearchCompleteCallback = function() {
    return this._onSearchComplete
  }
}

// 封装DrivingRoute的setSearchCompleteCallback方法（如果不存在）
if (window.BMap && !window.BMap.DrivingRoute.prototype.setSearchCompleteCallback) {
  window.BMap.DrivingRoute.prototype.setSearchCompleteCallback = function(callback) {
    this._onSearchComplete = callback
  }
}

// 从结果中提取路线方案
const extractPlanFromResults = (results) => {
  if (results.Vl && Array.isArray(results.Vl) && results.Vl.length > 0) {
    return results.Vl[0]
  }

  if (typeof results.getStatus === 'function' && 
      results.getStatus() === window.BMAP_STATUS_SUCCESS && 
      typeof results.getPlan === 'function') {
    return results.getPlan(0)
  }
  
  return null
}

// 提取距离信息
const extractDistance = (plan) => {
  if (typeof plan.getDistance === 'function') {
    return plan.getDistance(false)
  }
  return plan.distance || 0
}

// 提取时间信息
const extractDuration = (plan) => {
  if (typeof plan.getDuration === 'function') {
    return plan.getDuration(false)
  }
  return plan.duration || 0
}

// 更新路线信息（抽离UI更新逻辑）
const updateRouteInfo = (distance, duration) => {
  // 确保distance和duration是数字
  const distanceNum = Number(distance)
  const durationNum = Number(duration)
  
  const km = distanceNum / 1000
  
  routeInfo.show = true
  routeInfo.distance = distanceNum > 1000 ? `${km.toFixed(1)}公里` : `${distanceNum}米`
  routeInfo.duration = durationNum > 60
    ? `${Math.floor(durationNum / 60)}小时${durationNum % 60}分钟`
    : `${durationNum}分钟`
  routeInfo.cost = calculateCost(km)
}

// 抽离费用计算
const calculateCost = (km) => {
  const basePrice = 10
  const extraPrice = Math.max(km - 3, 0) * 2
  return `¥${(basePrice + extraPrice).toFixed(2)}`
}

// 美观的气泡窗口（保留但暂时未使用）
const showAdvancedRouteCard = (point, duration, distance, cost) => {
  // 百度地图的信息窗口实现
  if (routeLabel) {
    map.value.removeOverlay(routeLabel)
  }

  const content = `
    <div class="bubble-window">
      <div class="bubble-header">路线信息</div>
      <div class="bubble-content">
        <div class="info-item">
          <span class="info-label">时间：</span>
          <span class="info-value">${duration}</span>
        </div>
        <div class="info-item">
          <span class="info-label">距离：</span>
          <span class="info-value">${distance}</span>
        </div>
        <div class="info-item">
          <span class="info-label">费用：</span>
          <span class="info-value cost">${cost}</span>
        </div>
      </div>
    </div>
  `

  routeLabel = new window.BMap.InfoWindow(content, {
    width: 220,
    height: 120,
    offset: new window.BMap.Size(-100, -80)
  })

  map.value.openInfoWindow(routeLabel, point)
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

onMounted(() => {
  // 页面加载时清空路线缓存
  clearRouteCache()
  loadHomestayDetail()
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

/* 面包屑导航 */
.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
}

.detail-content {
  padding: 0 0 40px;
}

/* 图片轮播 */
.image-gallery {
  margin-bottom: 40px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.gallery-image {
  width: 100%;
  height: 500px;
  object-fit: cover;
}

/* 基本信息和预订部分 */
.info-booking-section {
  display: flex;
  gap: 40px;
  margin-bottom: 40px;
}

.basic-info {
  flex: 1;
}

.homestay-title {
  font-size: 32px;
  font-weight: bold;
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
  color: #666;
}

.location-section {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  font-size: 16px;
  color: #333;
}

.location-icon {
  color: #f56c6c;
}

.room-type-section {
  margin-bottom: 20px;
}

/* 预订卡片 */
.booking-card {
  width: 350px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 24px;
  background: white;
  position: sticky;
  top: 100px;
}

.price-section {
  display: flex;
  align-items: baseline;
  margin-bottom: 24px;
}

.price {
  color: #f56c6c;
  font-size: 32px;
  font-weight: bold;
  margin-right: 8px;
}

.price-unit {
  font-size: 16px;
  color: #666;
}

.booking-info {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 14px;
  color: #666;
}

.info-icon {
  color: #667eea;
}

/* 设施和描述部分 */
.facilities-description-section {
  display: flex;
  gap: 40px;
  margin-bottom: 40px;
}

.section-card {
  flex: 1;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 32px;
  background: white;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
}

.section-icon {
  color: #667eea;
}

/* 设施网格 */
.facilities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
}

.facility-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #333;
}

.facility-icon {
  color: #67c23a;
}

/* 描述内容 */
.description-content {
  font-size: 16px;
  line-height: 1.6;
  color: #333;
}

/* ========== 爱彼迎风格变量 ========== */
.location-section-card {
  --rausch: #ff5a5f;          /* Airbnb标志性珊瑚红 */
  --rausch-dark: #e04850;     /* 深珊瑚红（悬停） */
  --rausch-light: #fff8f8;    /* 极浅红（背景） */
  --dark: #222222;            /* 主文字 */
  --gray: #717171;            /* 次要文字 */
  --light-gray: #b0b0b0;      /* 占位符 */
  --border: #dddddd;          /* 边框 */
  --hover-border: #aaaaaa;    /* 悬停边框 */
  --bg: #f7f7f7;              /* 页面背景灰 */
  --shadow-sm: 0 1px 2px rgba(0,0,0,0.08), 0 4px 12px rgba(0,0,0,0.05);
  --shadow-md: 0 2px 4px rgba(0,0,0,0.04), 0 6px 16px rgba(0,0,0,0.08);
  --shadow-lg: 0 4px 8px rgba(0,0,0,0.04), 0 12px 24px rgba(0,0,0,0.12);
  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 24px;
  
  margin-bottom: 48px;
  padding: 32px;
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

/* ========== 地图容器 ========== */
.map-container {
  position: relative;
  border-radius: var(--radius-md);
  overflow: hidden;
  width: 100%;
  height: 520px;
  background: var(--bg);
  border: 1px solid var(--border);
}

/* ========== 底部悬浮搜索栏（爱彼迎风格） ========== */
.map-actions {
  position: absolute;
  bottom: 32px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 100;
  width: 90%;
  max-width: 680px;
}

.route-input-container {
  display: flex;
  gap: 16px;
  align-items: center;
  background: white;
  padding: 12px 12px 12px 24px;
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
  border: 1px solid var(--border);
  transition: box-shadow 0.2s ease, transform 0.1s ease;
}

.route-input-container:hover {
  box-shadow: 0 6px 16px rgba(0,0,0,0.12), 0 2px 4px rgba(0,0,0,0.08);
}

.route-input-container .el-input {
  flex: 1;
}

.route-input-container .el-input__inner {
  border: none;
  background: transparent;
  font-size: 16px;
  color: var(--dark);
  padding: 8px 0;
  height: auto;
  line-height: 1.5;
}

.route-input-container .el-input__inner::placeholder {
  color: var(--light-gray);
  font-weight: 400;
}

.route-input-container .el-input__inner:focus {
  box-shadow: none;
}

/* 爱彼迎风格按钮 */
.route-input-container .el-button--primary {
  background: var(--rausch);
  border: none;
  border-radius: var(--radius-md);
  padding: 14px 24px;
  font-size: 16px;
  font-weight: 600;
  color: white;
  transition: all 0.2s ease;
}

.route-input-container .el-button--primary:hover {
  background: var(--rausch-dark);
  transform: scale(1.02);
  box-shadow: 0 2px 4px rgba(255,90,95,0.3);
}

/* ========== 右侧面板 ========== */
.route-panel {
  position: absolute;
  top: 24px;
  right: 24px;
  background: white;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
  padding: 24px;
  width: 320px;
  z-index: 100;
  border: 1px solid var(--border);
  max-height: calc(100% - 48px);
  overflow-y: auto;
}

.route-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebebeb;
}

.route-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--dark);
  letter-spacing: -0.2px;
}

/* ========== 路线信息卡片 ========== */
.route-info {
  margin: 20px 0;
  padding: 20px;
  background: var(--bg);
  border-radius: var(--radius-md);
  border: none;
}

.route-info-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--dark);
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--gray);
}

.route-info-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.route-info .info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 16px 12px;
  background: white;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border);
  transition: all 0.2s ease;
}

.route-info .info-item:hover {
  border-color: var(--rausch);
  box-shadow: 0 2px 8px rgba(255,90,95,0.08);
}

.route-info .info-icon {
  color: var(--rausch);
  font-size: 20px;
  margin-bottom: 4px;
}

.route-info .info-item span {
  font-size: 12px;
  color: var(--gray);
  font-weight: 500;
}

.route-info .info-item span:last-child {
  font-weight: 600;
  color: var(--dark);
  font-size: 15px;
}

/* ========== 交通方式切换（ pill 样式） ========== */
.transport-modes {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebebeb;
}

.transport-modes .el-button-group {
  width: 100%;
  display: flex;
  background: var(--bg);
  padding: 4px;
  border-radius: var(--radius-md);
  gap: 4px;
}

.transport-modes .el-button {
  flex: 1;
  padding: 10px;
  font-size: 14px;
  border: none;
  background: transparent;
  color: var(--gray);
  border-radius: var(--radius-sm);
  font-weight: 500;
  transition: all 0.2s;
}

.transport-modes .el-button.active,
.transport-modes .el-button:hover {
  background: white;
  color: var(--dark);
  box-shadow: 0 1px 2px rgba(0,0,0,0.08);
  font-weight: 600;
}

/* 房东信息部分 */
.landlord-section-card {
  margin-bottom: 40px;
}

.landlord-info {
  display: flex;
  align-items: center;
  gap: 24px;
}

.landlord-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 24px;
  font-weight: bold;
}

.landlord-details {
  flex: 1;
}

.landlord-details h3 {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.landlord-phone {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.landlord-since {
  font-size: 14px;
  color: #999;
}

/* 评论部分 */
.reviews-section-card {
  margin-bottom: 40px;
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
  padding-bottom: 0;
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
  font-size: 14px;
  line-height: 1.6;
  color: #333;
}

.no-reviews {
  padding: 40px 0;
  text-align: center;
}

/* 用户下拉菜单 */
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

/* ========== 地址输入建议 ========== */
.address-input-container {
  position: relative;
  width: 100%;
}

.address-suggestions {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  right: 0;
  background: white;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-lg);
  z-index: 1000;
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid var(--border);
  padding: 8px 0;
}

.suggestion-item {
  padding: 16px 24px;
  cursor: pointer;
  font-size: 15px;
  color: var(--dark);
  transition: background 0.15s;
  border-bottom: 1px solid #f7f7f7;
}

.suggestion-item:last-child {
  border-bottom: none;
}

.suggestion-item:hover {
  background: var(--rausch-light);
  color: var(--rausch-dark);
}

/* ========== 路线信息卡片 ========== */
.route-info {
  margin: 20px 0;
  padding: 20px;
  background: var(--bg);
  border-radius: var(--radius-md);
  border: none;
}

.route-info-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--dark);
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--gray);
}

.route-info-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.route-info .info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 16px 12px;
  background: white;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border);
  transition: all 0.2s ease;
}

.route-info .info-item:hover {
  border-color: var(--rausch);
  box-shadow: 0 2px 8px rgba(255,90,95,0.08);
}

.route-info .info-icon {
  color: var(--rausch);
  font-size: 20px;
  margin-bottom: 4px;
}

.route-info .info-item span {
  font-size: 12px;
  color: var(--gray);
  font-weight: 500;
}

.route-info .info-item span:last-child {
  font-weight: 600;
  color: var(--dark);
  font-size: 15px;
}

/* ========== 错误提示 ========== */
.route-error {
  margin-bottom: 20px;
}

.route-error .el-alert {
  border-radius: var(--radius-sm);
  border: 1px solid #ffd1d1;
  background: #fff2f2;
  color: #c13515;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
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

@media (max-width: 768px) {
  .homestay-title {
    font-size: 24px;
  }
  
  .price {
    font-size: 24px;
  }
  
  .section-card {
    padding: 20px;
  }
  
  .facilities-grid {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  }
  
  .landlord-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
}

/* 美观的气泡窗口样式 */
.bubble-window {
  width: 220px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0,0,0,0.2);
  overflow: hidden;
  font-family: Arial, sans-serif;
  animation: bubblePop 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  border: 1px solid rgba(102, 126, 234, 0.2);
}

.bubble-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 12px 18px;
  font-size: 15px;
  font-weight: bold;
  position: relative;
}

.bubble-header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #4facfe 0%, #00f2fe 100%);
}

.bubble-content {
  padding: 16px 18px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
  padding: 6px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  margin-bottom: 0;
  border-bottom: none;
}

.info-label {
  color: #666;
  font-weight: 500;
}

.info-value {
  color: #333;
  font-weight: 600;
}

.info-value.cost {
  color: #ff4d4f;
  font-weight: bold;
  font-size: 15px;
}

@keyframes bubblePop {
  from {
    transform: scale(0.7);
    opacity: 0;
    box-shadow: 0 0 0 rgba(0,0,0,0);
  }
  to {
    transform: scale(1);
    opacity: 1;
    box-shadow: 0 6px 20px rgba(0,0,0,0.2);
  }
}

/* ========== 弹窗（爱彼迎风格） ========== */
.route-dialog {
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.route-dialog .el-dialog__header {
  background: white;
  padding: 24px 32px;
  border-bottom: 1px solid #ebebeb;
  margin-right: 0;
}

.route-dialog .el-dialog__title {
  color: var(--dark);
  font-size: 22px;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.route-dialog .el-dialog__body {
  padding: 32px;
  background: white;
}

.route-dialog .el-dialog__footer {
  padding: 20px 32px;
  background: var(--bg);
  border-top: 1px solid #ebebeb;
  display: flex;
  justify-content: flex-end;
  gap: 16px;
}

.route-dialog .el-button {
  border-radius: var(--radius-md);
  padding: 12px 24px;
  font-size: 15px;
  font-weight: 600;
  border: 1px solid var(--dark);
  background: white;
  color: var(--dark);
  transition: all 0.2s;
}

.route-dialog .el-button:hover {
  border-color: var(--dark);
  background: var(--bg);
}

.route-dialog .el-button--primary {
  background: var(--rausch);
  border-color: var(--rausch);
  color: white;
}

.route-dialog .el-button--primary:hover {
  background: var(--rausch-dark);
  border-color: var(--rausch-dark);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(255,90,95,0.3);
}
</style>
