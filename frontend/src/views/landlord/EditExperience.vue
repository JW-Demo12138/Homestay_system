<template>
  <div class="edit-experience">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <h1>民宿预订系统</h1>
          </div>
          <div class="nav">
            <el-link @click="goHome">首页</el-link>
            <el-link @click="goToHomestays">民宿列表</el-link>
            <el-link @click="goToLandlord">房东中心</el-link>
            <el-link @click="goToUser">个人中心</el-link>
            <el-link @click="handleLogout">退出</el-link>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <div class="page-header">
          <h2>编辑体验项目</h2>
          <el-button @click="goBack">返回</el-button>
        </div>
        
        <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
          <el-card class="form-card">
            <template #header>
              <div class="card-header">
                <h3>体验项目信息</h3>
                <span class="card-tip">编辑乡村特色体验项目的详细信息</span>
              </div>
            </template>
            
            <el-form-item label="体验名称（必填）" prop="name">
              <el-input v-model="form.name" placeholder="请输入体验项目名称" maxlength="50" show-word-limit />
            </el-form-item>
            
            <el-form-item label="体验类型（必填）" prop="type">
              <el-select v-model="form.type" placeholder="请选择体验类型" style="width: 200px">
                <el-option label="民俗手工制作" value="民俗手工制作" />
                <el-option label="农事实践体验" value="农事实践体验" />
                <el-option label="乡村美食烹饪" value="乡村美食烹饪" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="关联民宿" prop="homestayId">
              <el-select v-model="form.homestayId" placeholder="选择关联的民宿" style="width: 300px">
                <el-option v-for="homestay in homestays" :key="homestay.id" :label="homestay.name" :value="homestay.id" />
              </el-select>
              <div class="input-suggestion">
                选择体验项目关联的民宿，系统会自动获取民宿位置并设置7公里范围
              </div>
            </el-form-item>
            
            <el-form-item label="价格（必填）" prop="price">
              <el-input-number
                v-model="form.price"
                :min="0"
                :precision="2"
                :step="10"
                style="width: 200px"
                controls-position="right"
              >
                <template #prefix>¥</template>
              </el-input-number>
            </el-form-item>
            
            <el-form-item label="体验时长（必填）" prop="duration">
              <el-input-number
                v-model="form.duration"
                :min="1"
                :step="30"
                style="width: 200px"
                controls-position="right"
              >
                <template #suffi1x>分钟</template>
              </el-input-number>
            </el-form-item>
            
            <el-form-item label="可预约时段" prop="availableTime">
              <div class="time-slots-container">
                <div class="time-slot" v-for="(slot, index) in timeSlots" :key="index">
                  <el-time-select
                    v-model="slot.start"
                    placeholder="开始时间"
                    start="00:00"
                    step="00:30"
                    end="23:30"
                    format="HH:mm"
                    style="width: 120px; margin-right: 10px"
                  />
                  <span class="time-separator">-</span>
                  <el-time-select
                    v-model="slot.end"
                    placeholder="结束时间"
                    start="00:00"
                    step="00:30"
                    end="23:30"
                    format="HH:mm"
                    style="width: 120px; margin-right: 10px"
                  />
                  <el-button
                    type="danger"
                    size="small"
                    @click="removeTimeSlot(index)"
                    :disabled="timeSlots.length <= 1"
                  >
                    删除
                  </el-button>
                </div>
                <el-button type="primary" size="small" @click="addTimeSlot">
                  添加时段
                </el-button>
              </div>
              <div class="input-suggestion">
                选择体验项目的可预约时段，支持多个时段
              </div>
            </el-form-item>
            
            <el-form-item label="详细描述（必填）" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="5"
                placeholder="请输入体验项目的详细描述"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
            
            <el-form-item label="体验地点（必填）" prop="location">
              <div class="address-input-container">
                <el-input
                  v-model="form.location"
                  placeholder="请输入体验地点，如：北京市朝阳区建国路"
                  maxlength="200"
                  show-word-limit
                  @input="handleAddressInput"
                  @blur="handleAddressBlur"
                />
                <el-button type="primary" @click="getCurrentLocation" style="margin-top: 10px;">
                  获取当前位置
                </el-button>
              </div>
              <!-- 地址搜索建议 -->
              <div v-if="addressSuggestions.length > 0" class="address-suggestions">
                <div
                  v-for="(suggestion, index) in addressSuggestions"
                  :key="index"
                  class="suggestion-item"
                  @click="selectAddress(suggestion)"
                >
                  {{ suggestion.name }}
                </div>
              </div>
              <!-- 地图容器 -->
              <div class="map-container">
                <div id="experienceMap" style="width: 100%; height: 400px;"></div>
                <div class="map-tip">
                  可拖动地图上的标记来调整位置
                </div>
              </div>
              <!-- 位置范围选择（隐藏，固定为7公里） -->
              <div class="range-container" style="display: none;">
                <el-form-item label="位置范围" prop="range">
                  <el-input v-model="form.range" type="hidden" />
                </el-form-item>
              </div>
            </el-form-item>
            
            <el-form-item label="图片上传" prop="images">
              <el-upload
                class="image-uploader"
                :action="uploadUrl"
                :headers="uploadHeaders"
                :on-success="handleImageUpload"
                :on-error="handleUploadError"
                :before-upload="beforeImageUpload"
                :file-list="fileList"
                multiple
                list-type="picture-card"
              >
                <el-icon><Plus /></el-icon>
                <template #tip>
                  <div class="upload-tip">
                    支持 JPG、PNG 格式，单张不超过 2MB
                  </div>
                </template>
              </el-upload>
              <div v-if="uploadedImages.length > 0" class="uploaded-images">
                <h4>已上传图片</h4>
                <div class="image-list">
                  <div v-for="(image, index) in uploadedImages" :key="index" class="image-item">
                    <img :src="getImageUrl(image, true)" alt="体验项目图片" />
                    <div class="image-actions">
                      <el-checkbox v-model="form.coverImage" :label="image">设为封面</el-checkbox>
                      <el-button size="small" type="danger" @click="removeImage(index)">删除</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </el-form-item>
            
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">
                  <div class="status-option">
                    <span class="status-title">上架</span>
                    <span class="status-desc">立即在平台展示，可被预约</span>
                  </div>
                </el-radio>
                <el-radio :label="0">
                  <div class="status-option">
                    <span class="status-title">下架</span>
                    <span class="status-desc">仅房东可见，不对外展示</span>
                  </div>
                </el-radio>
                <el-radio :label="2">
                  <div class="status-option">
                    <span class="status-title">待审核</span>
                    <span class="status-desc">提交后等待平台审核，审核通过后自动上架</span>
                  </div>
                </el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item>
              <el-button @click="handleReset" style="margin-left: 10px;">重置</el-button>
              <el-button type="primary" @click="handleSubmit" :loading="loading" style="margin-left: 10px;">
                保存修改
              </el-button>
            </el-form-item>
          </el-card>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { experienceAPI } from '@/api/experience'
import { homestayAPI } from '@/api/homestay'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getImageUrl } from '@/utils'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const experienceId = route.params.id

// 加载房东的民宿列表
const homestays = ref([])
const loadHomestays = async () => {
  try {
    console.log('开始加载民宿列表')
    const result = await homestayAPI.getLandlordList()
    console.log('获取民宿列表结果:', result)
    // 响应拦截器直接返回了 data，所以 result 就是 data
    if (Array.isArray(result)) {
      homestays.value = result
      console.log('民宿列表数据:', homestays.value)
    } else {
      // 处理异常情况
      homestays.value = []
    }
  } catch (error) {
    console.error('获取民宿列表失败:', error)
    ElMessage.error('获取民宿列表失败，请检查网络连接')
  }
}

// 组件挂载时更新上传请求头
onMounted(() => {
  updateUploadHeaders()
  loadHomestays()
  getExperienceDetail()
})

const formRef = ref(null)
const loading = ref(false)

// 表单数据
const form = reactive({
  id: '',
  name: '',
  type: '',
  homestayId: '',
  price: null,
  duration: null,
  availableTime: '',
  description: '',
  location: '',
  longitude: '',
  latitude: '',
  range: 100,
  imageUrl: '',
  images: '',
  coverImage: '',
  status: 1
})

// 监听token变化，更新上传请求头
watch(() => localStorage.getItem('accessToken'), () => {
  updateUploadHeaders()
})

// 监听民宿选择变化
watch(() => form.homestayId, (newHomestayId) => {
  if (newHomestayId) {
    const selectedHomestay = homestays.value.find(h => h.id === newHomestayId)
    if (selectedHomestay && selectedHomestay.longitude && selectedHomestay.latitude) {
      // 设置体验位置为民宿位置
      form.longitude = selectedHomestay.longitude
      form.latitude = selectedHomestay.latitude
      form.location = selectedHomestay.address || ''
      
      // 初始化地图，显示民宿位置
      initMap(selectedHomestay.longitude, selectedHomestay.latitude)
      
      // 设置范围为7公里
      form.range = 7000
      updateRange()
      
      ElMessage.success('已获取民宿位置，您可以在7公里范围内选择体验位置')
    }
  }
})

// 可预约时段
const timeSlots = ref([])

// 初始化timeSlots
const initTimeSlots = () => {
  if (form.availableTime) {
    const slots = form.availableTime.split(',').map(slot => {
      const [start, end] = slot.split('-')
      return { start: start.trim(), end: end.trim() }
    })
    timeSlots.value = slots.length > 0 ? slots : [{ start: '', end: '' }]
  } else {
    timeSlots.value = [{ start: '', end: '' }]
  }
}

// 添加时段
const addTimeSlot = () => {
  timeSlots.value.push({ start: '', end: '' })
}

// 删除时段
const removeTimeSlot = (index) => {
  timeSlots.value.splice(index, 1)
}

// 监听timeSlots变化，更新availableTime
watch(timeSlots, (newSlots) => {
  form.availableTime = newSlots
    .filter(slot => slot.start && slot.end)
    .map(slot => `${slot.start}-${slot.end}`)
    .join(',')
}, { deep: true })

// 地址搜索相关
const addressSuggestions = ref([])
let map = null
let marker = null
let circle = null
let geocoder = null
let autoComplete = null
let searchTimer = null
const searchCache = new Map()

// 图片上传
const uploadUrl = '/api/user/upload-avatar'
const uploadedImages = ref([])
const uploadHeaders = ref({
  Authorization: `Bearer ${localStorage.getItem('accessToken') || ''}`
})

// 文件列表用于Element Plus upload组件
const fileList = ref([])

// 更新上传请求头
const updateUploadHeaders = () => {
  uploadHeaders.value = {
    Authorization: `Bearer ${localStorage.getItem('accessToken') || ''}`
  }
}

// 获取体验项目详情
const getExperienceDetail = async () => {
  try {
    const experience = await experienceAPI.getDetail(experienceId)
    // 响应拦截器直接返回了data，所以experience就是data
    form.id = experience.id
    form.name = experience.name
    form.type = experience.type
    form.homestayId = experience.homestayId || ''
    form.price = experience.price
    form.duration = experience.duration
    form.availableTime = experience.availableTime
    form.description = experience.description
    form.location = experience.location
    form.longitude = experience.longitude
    form.latitude = experience.latitude
    form.range = 7000 // 设置为7公里范围
    form.imageUrl = experience.imageUrl
    form.images = experience.images
    form.coverImage = experience.imageUrl
    form.status = experience.status
    
    // 处理图片
    if (experience.images) {
      // 提取图片文件名
      uploadedImages.value = experience.images.split(',').map(image => image.split('/').pop())
      // 同步更新fileList用于Element Plus组件显示
      fileList.value = uploadedImages.value.map(image => ({
        name: image,
        url: getImageUrl(image, true),
        status: 'success'
      }))
    }
    
    // 初始化地图
    if (form.longitude && form.latitude) {
      initMap(form.longitude, form.latitude)
    } else {
      initDefaultMap()
    }
    
    // 初始化可预约时段
    initTimeSlots()
  } catch (error) {
    console.error('获取体验项目详情失败:', error)
    ElMessage.error('获取体验项目详情失败')
  }
}

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB!')
    return false
  }
  
  return true
}

const handleImageUpload = (response, file) => {
  if (response.code === 200) {
    // 从响应中获取图片URL - 用户头像上传接口返回用户对象
    let imageUrl = ''
    if (response.data && response.data.avatar) {
      imageUrl = response.data.avatar
    } else if (typeof response.data === 'string') {
      imageUrl = response.data
    }
    
    if (imageUrl) {
      // 提取图片文件名
      const fileName = imageUrl.split('/').pop()
      // 构建本地存储路径
      const localImageUrl = fileName
      uploadedImages.value.push(localImageUrl)
      // 同步更新fileList用于Element Plus组件显示
      fileList.value.push({
        name: file.name,
        url: getImageUrl(localImageUrl, true),
        status: 'success'
      })
      
      // 自动设置第一张为封面
      if (!form.coverImage && uploadedImages.value.length === 1) {
        form.coverImage = localImageUrl
      }
      ElMessage.success('图片上传成功')
      console.log('上传成功，图片URL:', localImageUrl)
    } else {
      ElMessage.error('图片上传失败：未返回图片URL')
    }
  } else {
    ElMessage.error('图片上传失败：' + (response.message || '未知错误'))
  }
}

const handleUploadError = (error) => {
  console.error('上传错误:', error)
  ElMessage.error('图片上传失败')
}

const removeImage = (index) => {
  const removedImage = uploadedImages.value[index]
  uploadedImages.value.splice(index, 1)
  fileList.value.splice(index, 1)
  
  // 如果删除的是封面图，重新设置
  if (form.coverImage === removedImage) {
    form.coverImage = uploadedImages.value.length > 0 ? uploadedImages.value[0] : ''
  }
}

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '体验名称不能为空，请填写', trigger: 'blur' },
    { min: 2, max: 50, message: '体验名称长度应在 2-50 个字符之间', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '体验类型不能为空，请选择', trigger: 'change' }
  ],
  price: [
    { required: true, message: '价格不能为空，请填写', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格必须大于 0', trigger: 'blur' }
  ],
  duration: [
    { required: true, message: '体验时长不能为空，请填写', trigger: 'blur' },
    { type: 'number', min: 1, message: '体验时长必须大于 0', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '描述不能为空，请填写', trigger: 'blur' },
    { min: 50, message: '描述长度应不少于 50 个字符', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '体验地点不能为空，请填写', trigger: 'blur' }
  ]
}

// 地址搜索相关方法
const handleAddressInput = (value) => {
  // 清空旧结果
  addressSuggestions.value = []
  
  // 基础校验
  if (!value || value.length < 2) {
    return
  }
  
  // 检查缓存
  if (searchCache.has(value)) {
    addressSuggestions.value = searchCache.get(value)
    return
  }
  
  // 防抖处理
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    // 确保高德地图 API 加载完成
    if (typeof AMap === 'undefined') {
      ElMessage.error('地图API加载失败')
      return
    }
    
    // 加载 AutoComplete 插件
    AMap.plugin('AMap.AutoComplete', function() {
      if (!autoComplete) {
        autoComplete = new AMap.AutoComplete({
          city: '全国'
        })
      }
      
      // 发起搜索
      autoComplete.search(value, function(status, result) {
        if (status === 'complete' && result.info === 'OK') {
          const tips = result.tips.map(tip => ({
            name: tip.name,
            address: tip.address,
            location: tip.location
          }))
          // 缓存结果
          searchCache.set(value, tips)
          // 限制缓存大小
          if (searchCache.size > 100) {
            const firstKey = searchCache.keys().next().value
            searchCache.delete(firstKey)
          }
          addressSuggestions.value = tips
        }
      })
    })
  }, 500) // 500ms防抖
}

const handleAddressBlur = () => {
  // 延迟隐藏建议列表，以便用户可以点击选择
  setTimeout(() => {
    addressSuggestions.value = []
  }, 200)
}

const selectAddress = (suggestion) => {
  form.location = suggestion.name + (suggestion.address ? ' ' + suggestion.address : '')
  form.longitude = suggestion.location.lng
  form.latitude = suggestion.location.lat
  addressSuggestions.value = []
  initMap(suggestion.location.lng, suggestion.location.lat)
  updateRange()
}

// 获取当前位置
const getCurrentLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { longitude, latitude } = position.coords
        form.longitude = longitude
        form.latitude = latitude
        
        // 确保高德地图 API 加载完成
        if (typeof AMap === 'undefined') {
          ElMessage.error('地图API加载失败')
          return
        }
        
        // 动态加载 Geocoder 插件
        AMap.plugin('AMap.Geocoder', function() {
          geocoder = new AMap.Geocoder({
            radius: 1000,
            extensions: "all"
          })
          
          geocoder.getAddress([longitude, latitude], function(status, result) {
            console.log('Geocoder result:', status, result)
            if (status === 'complete' && result.info === 'OK') {
              const address = result.regeocode.formattedAddress
              form.location = address
              initMap(longitude, latitude)
              updateRange()
              ElMessage.success('位置获取成功')
            } else {
              console.error('Geocoder failed:', status, result)
              // 处理不同的错误情况
              if (status === 'error' && result.info === 'INVALID_USER_SCODE') {
                ElMessage.error('API key 配置错误，请检查高德地图 API key')
              } else {
                ElMessage.error('获取地址失败，请手动输入')
              }
              // 即使获取地址失败，也设置经纬度
              initMap(longitude, latitude)
              updateRange()
              // 手动设置一个默认地址，避免表单验证失败
              if (!form.location) {
                form.location = `经度: ${longitude.toFixed(6)}, 纬度: ${latitude.toFixed(6)}`
              }
            }
          })
        })
      },
      (error) => {
        console.error('获取位置失败:', error)
        ElMessage.error('获取位置失败，请检查定位权限')
      }
    )
  } else {
    ElMessage.error('浏览器不支持地理定位')
  }
}

// 初始化默认地图
const initDefaultMap = () => {
  // 确保高德地图 API 加载完成
  if (typeof AMap === 'undefined') {
    // 如果 API 未加载，延迟初始化
    setTimeout(initDefaultMap, 1000)
    return
  }
  
  // 默认位置：北京市
  const defaultLng = 116.404
  const defaultLat = 39.915
  
  // 初始化地图
  AMap.plugin(['AMap.Map', 'AMap.Marker', 'AMap.Circle'], function() {
    map = new AMap.Map('experienceMap', {
      zoom: 15,
      center: [defaultLng, defaultLat]
    })
    
    // 添加标记
    marker = new AMap.Marker({
      position: [defaultLng, defaultLat],
      draggable: true,
      cursor: 'move'
    })
    
    map.add(marker)
    
    // 添加圆形覆盖物
    circle = new AMap.Circle({
      center: [defaultLng, defaultLat],
      radius: form.range,
      strokeColor: "#1890ff",
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: "#1890ff",
      fillOpacity: 0.2
    })
    
    map.add(circle)
    
    // 监听标记拖动结束事件
    marker.on('dragend', function(e) {
      const position = e.lnglat
      form.longitude = position.getLng()
      form.latitude = position.getLat()
      
      // 更新圆形覆盖物位置
      circle.setCenter(position)
      
      // 逆地理编码获取新地址
      AMap.plugin('AMap.Geocoder', function() {
        geocoder = new AMap.Geocoder({
          radius: 1000,
          extensions: "all"
        })
        
        geocoder.getAddress([position.getLng(), position.getLat()], function(status, result) {
          if (status === 'complete' && result.info === 'OK') {
            const address = result.regeocode.formattedAddress
            form.location = address
          }
        })
      })
    })
  })
}

// 初始化地图
const initMap = (lng, lat) => {
  // 确保高德地图 API 加载完成
  if (typeof AMap === 'undefined') {
    ElMessage.error('地图API加载失败')
    return
  }
  
  if (!map) {
    // 动态加载 Map 组件
    AMap.plugin(['AMap.Map', 'AMap.Marker', 'AMap.Circle'], function() {
      map = new AMap.Map('experienceMap', {
        zoom: 15,
        center: [lng, lat]
      })
      
      // 添加标记
      marker = new AMap.Marker({
        position: [lng, lat],
        draggable: true,
        cursor: 'move'
      })
      
      map.add(marker)
      
      // 添加圆形覆盖物
      circle = new AMap.Circle({
        center: [lng, lat],
        radius: form.range,
        strokeColor: "#1890ff",
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: "#1890ff",
        fillOpacity: 0.2
      })
      
      map.add(circle)
      
      // 监听标记拖动结束事件
      marker.on('dragend', function(e) {
        const position = e.lnglat
        form.longitude = position.getLng()
        form.latitude = position.getLat()
        
        // 更新圆形覆盖物位置
        circle.setCenter(position)
        
        // 逆地理编码获取新地址
        AMap.plugin('AMap.Geocoder', function() {
          geocoder = new AMap.Geocoder({
            radius: 1000,
            extensions: "all"
          })
          
          geocoder.getAddress([position.getLng(), position.getLat()], function(status, result) {
            if (status === 'complete' && result.info === 'OK') {
              const address = result.regeocode.formattedAddress
              form.location = address
            }
          })
        })
      })
    })
  } else {
    // 更新地图中心和标记位置
    map.setCenter([lng, lat])
    map.setZoom(15)
    marker.setPosition([lng, lat])
    // 更新圆形覆盖物位置
    circle.setCenter([lng, lat])
  }
}

// 更新范围
const updateRange = () => {
  if (circle) {
    circle.setRadius(form.range)
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    // 验证所有字段
    await formRef.value.validate()
    
    // 检查图片
    if (uploadedImages.value.length === 0) {
      ElMessage.warning('请上传至少一张图片')
      return
    }
    
    // 确保图片提交 - 将图片URL数组转换为逗号分隔字符串
    form.images = uploadedImages.value.join(',')
    if (!form.coverImage) {
      form.coverImage = uploadedImages.value[0]
    }
    form.imageUrl = form.coverImage
    
    console.log('准备提交的体验项目数据:', form)
    
    loading.value = true
    
    // 使用展开运算符确保不是reactive对象
    const result = await experienceAPI.update(form.id, form)
    console.log('体验项目更新结果:', result)
    
    await ElMessageBox.alert(
      '更新成功！',
      '提示',
      {
        confirmButtonText: '查看体验项目',
        cancelButtonText: '继续编辑',
        showCancelButton: true,
        callback: (action) => {
          if (action === 'confirm') {
            router.push('/landlord/experiences')
          }
        }
      }
    )
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新失败:', error)
      ElMessage.error('更新失败')
    }
  } finally {
    loading.value = false
  }
}

// 重置表单
const handleReset = () => {
  getExperienceDetail()
}

// 导航方法
const goBack = () => {
  router.back()
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

const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}
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

.form-card {
  margin-bottom: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.card-tip {
  font-size: 14px;
  color: #999;
}

.input-suggestion {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.image-uploader {
  margin-bottom: 20px;
}

.uploaded-images h4 {
  margin: 20px 0 10px 0;
  font-size: 16px;
  color: #333;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.image-item {
  width: 120px;
  text-align: center;
}

.image-item img {
  width: 100%;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.image-actions {
  font-size: 12px;
}

.status-option {
  display: flex;
  flex-direction: column;
}

.status-title {
  font-weight: bold;
  margin-bottom: 4px;
}

.status-desc {
  font-size: 12px;
  color: #999;
}

.upload-tip {
  font-size: 12px;
  color: #999;
}

/* 地址搜索相关样式 */
.address-input-container {
  position: relative;
}

.address-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
  max-height: 200px;
  overflow-y: auto;
}

.suggestion-item {
  padding: 10px 15px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.suggestion-item:hover {
  background-color: #f5f7fa;
}

/* 地图相关样式 */
.map-container {
  margin-top: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  width: 100%;
  max-width: 800px;
  margin: 20px auto;
  display: block;
}

.map-tip {
  padding: 10px;
  background-color: #f5f7fa;
  font-size: 12px;
  color: #606266;
  text-align: center;
}

.range-container {
  margin-top: 10px;
  text-align: center;
}

/* 优化整体UI */
.form-card {
  margin-bottom: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.form-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

/* 优化按钮样式 */
.el-button {
  transition: all 0.3s ease;
}

.el-button:hover {
  transform: translateY(-1px);
}

/* 优化输入框样式 */
.el-input {
  transition: all 0.3s ease;
}

.el-input:focus-within {
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

/* 可预约时段样式 */
.time-slots-container {
  margin-bottom: 10px;
}

.time-slot {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.time-separator {
  margin: 0 10px;
  color: #999;
}

@media (max-width: 768px) {
  .form-card {
    margin: 0 -20px 20px;
    border-radius: 0;
  }
  
  .address-suggestions {
    left: -10px;
    right: -10px;
  }
  
  .time-slot {
    flex-wrap: wrap;
  }
  
  .time-slot .el-button {
    margin-top: 10px;
  }
}
</style>
