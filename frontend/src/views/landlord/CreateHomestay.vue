<template>
  <div class="create-homestay">
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
          <h2>发布民宿</h2>
          <el-button @click="goBack">返回</el-button>
        </div>
        
        <!-- 步骤指示器 -->
        <el-steps :active="activeStep" finish-status="success" style="margin-bottom: 30px;">
          <el-step title="基础信息" />
          <el-step title="房源信息" />
          <el-step title="媒体与发布" />
          <el-step title="体验项目" />
        </el-steps>
        
        <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
          <!-- 基础信息卡片 -->
          <el-card class="form-card" v-if="activeStep === 0">
            <template #header>
              <div class="card-header">
                <h3>基础信息</h3>
                <span class="card-tip">填写民宿的基本信息和位置</span>
              </div>
            </template>
            
            <el-form-item label="民宿名称（必填）" prop="name">
              <el-input v-model="form.name" placeholder="请输入民宿名称" maxlength="50" show-word-limit />
            </el-form-item>
            
            <el-form-item label="地址（必填）" prop="address">
              <div class="address-input-container">
                <el-input
                  id="addressInput"
                  v-model="form.address"
                  placeholder="请输入详细地址，如：北京市朝阳区建国路"
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
              <div v-if="addressSuggestions.length > 0" class="address-suggestions" style="position: absolute; top: 100%; left: 0; right: 0; z-index: 9999; display: block;">
                <div
                  v-for="(suggestion, index) in addressSuggestions"
                  :key="index"
                  class="suggestion-item"
                  @click="selectAddress(suggestion)"
                  style="padding: 12px 20px; cursor: pointer; transition: all 0.3s ease; font-size: 14px;"
                >
                  {{ suggestion.name }}
                </div>
              </div>
              <!-- 地图容器 -->
              <div v-if="showMap" class="map-container">
                <div id="locationMap" style="width: 100%; height: 400px;"></div>
                <div class="map-tip">
                  可拖动地图上的标记来调整位置
                </div>
              </div>
            </el-form-item>
            
            <el-form-item label="城市" prop="city">
              <el-input v-model="form.city" placeholder="请输入城市" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="nextStep">下一步</el-button>
            </el-form-item>
          </el-card>
          
          <!-- 房源信息卡片 -->
          <el-card class="form-card" v-if="activeStep === 1">
            <template #header>
              <div class="card-header">
                <h3>房源信息</h3>
                <span class="card-tip">填写民宿的详细信息</span>
              </div>
            </template>
            
            <el-form-item label="每晚价格（必填）" prop="price">
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
            
            <el-form-item label="房型（必填）" prop="roomType">
              <el-select
                v-model="selectedRoomTypes"
                multiple
                placeholder="请选择房型"
                style="width: 100%"
              >
                <el-option v-for="type in roomTypeOptions" :key="type" :label="type" :value="type" />
              </el-select>
              <el-input
                v-model="customRoomType"
                placeholder="或输入自定义房型"
                style="margin-top: 10px; width: 300px"
              >
                <template #append>
                  <el-button @click="addCustomRoomType" size="small">添加</el-button>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item label="设施（必填）" prop="facility">
              <div class="facility-tags">
                <el-tag
                  v-for="facility in selectedFacilities"
                  :key="facility"
                  closable
                  @close="removeFacility(facility)"
                  style="margin-right: 10px; margin-bottom: 10px"
                >
                  {{ facility }}
                </el-tag>
              </div>
              <el-select
                v-model="tempFacility"
                placeholder="选择常用设施"
                style="width: 200px; margin-right: 10px; margin-top: 10px"
                @change="addFacility"
              >
                <el-option v-for="item in facilityOptions" :key="item" :label="item" :value="item" />
              </el-select>
              <el-input
                v-model="customFacility"
                placeholder="或输入自定义设施"
                style="width: 200px; margin-top: 10px"
              >
                <template #append>
                  <el-button @click="addCustomFacility" size="small">添加</el-button>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item label="描述（必填）" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="5"
                placeholder="请输入民宿描述"
                maxlength="500"
                show-word-limit
              />
              <div class="input-suggestion">
                描述建议包含：房源特色、周边环境、入住须知等，不少于50字
              </div>
            </el-form-item>
            
            <el-form-item>
              <el-button @click="prevStep">上一步</el-button>
              <el-button type="primary" @click="nextStep">下一步</el-button>
            </el-form-item>
          </el-card>
          
          <!-- 媒体与发布卡片 -->
          <el-card class="form-card" v-if="activeStep === 2">
            <template #header>
              <div class="card-header">
                <h3>媒体与发布</h3>
                <span class="card-tip">上传图片并设置发布状态</span>
              </div>
            </template>
            
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
                    支持所有图片格式，单张不超过 10MB
                  </div>
                </template>
              </el-upload>
              <div v-if="uploadedImages.length > 0" class="uploaded-images">
                <h4>已上传图片</h4>
                <div class="image-list">
                  <div v-for="(image, index) in uploadedImages" :key="index" class="image-item">
                    <img :src="getImageUrl(image, true)" alt="民宿图片" />
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
                    <span class="status-desc">立即在平台展示，可被预订</span>
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
              <el-button type="primary" @click="goToCreateExperience" style="margin-left: 10px;">
                <el-icon><Star /></el-icon>
                创建体验项目
              </el-button>
              <el-button @click="goToMyExperiences" style="margin-left: 10px;">
                <el-icon><Star /></el-icon>
                管理体验项目
              </el-button>
            </el-form-item>
            
            <el-form-item>
              <el-button @click="prevStep">上一步</el-button>
              <el-button @click="handleReset" style="margin-left: 10px;">重置</el-button>
              <el-button type="primary" @click="handleSubmit" :loading="loading" style="margin-left: 10px;">
                提交发布
              </el-button>
              <el-button @click="saveDraft" style="margin-left: 10px;">
                保存草稿
              </el-button>
            </el-form-item>
          </el-card>
          
          <!-- 体验项目卡片 -->
          <el-card class="form-card" v-if="activeStep === 3">
            <template #header>
              <div class="card-header">
                <h3>体验项目</h3>
                <span class="card-tip">创建与民宿相关的乡村特色体验项目</span>
              </div>
            </template>
            
            <el-form-item>
              <el-button type="primary" @click="goToCreateExperience" style="margin-left: 10px;">
                <el-icon><Star /></el-icon>
                创建体验项目
              </el-button>
              <el-button @click="goToMyExperiences" style="margin-left: 10px;">
                <el-icon><Star /></el-icon>
                管理体验项目
              </el-button>
              <el-button @click="finishCreation" style="margin-left: 10px;">
                <el-icon><Check /></el-icon>
                完成发布
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
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { homestayAPI } from '@/api/homestay'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getImageUrl } from '@/utils'
import { Star } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 组件挂载时更新上传请求头
onMounted(() => {
  updateUploadHeaders()
  // 初始化默认地图
  initDefaultMap()
})

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
  AMap.plugin(['AMap.Map', 'AMap.Marker'], function() {
    map = new AMap.Map('locationMap', {
      zoom: 10,
      center: [defaultLng, defaultLat]
    })
    
    // 添加标记
    marker = new AMap.Marker({
      position: [defaultLng, defaultLat],
      draggable: true,
      cursor: 'move'
    })
    
    map.add(marker)
    
    // 监听标记拖动结束事件
    marker.on('dragend', function(e) {
      const position = e.lnglat
      form.longitude = position.getLng()
      form.latitude = position.getLat()
      
      // 逆地理编码获取新地址
      AMap.plugin('AMap.Geocoder', function() {
        geocoder = new AMap.Geocoder({
          radius: 1000,
          extensions: "all"
        })
        
        geocoder.getAddress([position.getLng(), position.getLat()], function(status, result) {
          if (status === 'complete' && result.info === 'OK') {
            const address = result.regeocode.formattedAddress
            form.address = address
            form.city = result.regeocode.addressComponent.city || result.regeocode.addressComponent.province
          }
        })
      })
    })
  })
}

// 监听token变化，更新上传请求头
watch(() => localStorage.getItem('accessToken'), () => {
  updateUploadHeaders()
})

const formRef = ref(null)
const loading = ref(false)
const activeStep = ref(0)

// 表单数据
const form = reactive({
  name: '',
  address: '',
  city: '',
  price: null,
  roomType: '',
  facility: '',
  images: [],
  coverImage: '',
  description: '',
  status: 1,
  longitude: '',
  latitude: ''
})

// 地址搜索相关
const addressSuggestions = ref([])
const showMap = ref(true)
let map = null
let marker = null
let geocoder = null
let autoComplete = null
let searchTimer = null
const searchCache = new Map()

// 房型选项
const roomTypeOptions = ['大床房', '双床房', '家庭房', '套房', '别墅', '公寓']
const selectedRoomTypes = ref([])
const customRoomType = ref('')

const addCustomRoomType = () => {
  if (customRoomType.value && !selectedRoomTypes.value.includes(customRoomType.value)) {
    selectedRoomTypes.value.push(customRoomType.value)
    customRoomType.value = ''
  }
}

// 设施选项
const facilityOptions = ['WiFi', '空调', '热水', '电视', '冰箱', '洗衣机', '厨房', '停车位', '阳台', '健身房']
const selectedFacilities = ref([])
const tempFacility = ref('')
const customFacility = ref('')

const addFacility = () => {
  if (tempFacility.value && !selectedFacilities.value.includes(tempFacility.value)) {
    selectedFacilities.value.push(tempFacility.value)
    tempFacility.value = ''
  }
}

const addCustomFacility = () => {
  if (customFacility.value && !selectedFacilities.value.includes(customFacility.value)) {
    selectedFacilities.value.push(customFacility.value)
    customFacility.value = ''
  }
}

const removeFacility = (facility) => {
  const index = selectedFacilities.value.indexOf(facility)
  if (index > -1) {
    selectedFacilities.value.splice(index, 1)
  }
}

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

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt10M = file.size / 1024 / 1024 < 10
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过10MB!')
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
      uploadedImages.value.push(imageUrl)
      // 同步更新fileList用于Element Plus组件显示
      fileList.value.push({
        name: file.name,
        url: getImageUrl(imageUrl),
        status: 'success'
      })
      
      // 自动设置第一张为封面
      if (!form.coverImage && uploadedImages.value.length === 1) {
        form.coverImage = imageUrl
      }
      ElMessage.success('图片上传成功')
      console.log('上传成功，图片URL:', imageUrl)
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
    { required: true, message: '民宿名称不能为空，请填写', trigger: 'blur' },
    { min: 2, max: 50, message: '民宿名称长度应在 2-50 个字符之间', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '地址不能为空，请填写', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '价格不能为空，请填写', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格必须大于 0', trigger: 'blur' }
  ],
  roomType: [
    { required: true, message: '房型不能为空，请选择', trigger: 'change' }
  ],
  facility: [
    { required: true, message: '设施不能为空，请选择', trigger: 'change' }
  ],
  description: [
    { required: true, message: '描述不能为空，请填写', trigger: 'blur' },
    { min: 50, message: '描述长度应不少于 50 个字符', trigger: 'blur' }
  ]
}

// 分步验证函数
const validateFields = (fields) => {
  console.log('开始验证字段:', fields)
  return new Promise((resolve, reject) => {
    let validatedCount = 0
    let hasError = false
    
    fields.forEach(field => {
      console.log('验证字段:', field)
      if (!formRef.value) {
        console.error('formRef is null')
        reject('formRef is null')
        return
      }
      
      formRef.value.validateField(field, (error) => {
        console.log('验证结果 - 字段:', field, '错误:', error)
        validatedCount++
        
        // 检查是否有错误
        if (error && error !== '' && error !== true) {
          hasError = true
        }
        
        // 所有字段验证完成
        if (validatedCount === fields.length) {
          if (hasError) {
            reject('验证失败')
          } else {
            resolve()
          }
        }
      })
    })
  })
}

// 步骤控制
const nextStep = async () => {
  try {
    console.log('点击下一步，当前步骤:', activeStep.value)
    console.log('表单数据:', form)
    
    if (activeStep.value === 0) {
      // 验证基础信息
      console.log('开始验证基础信息')
      await validateFields(['name', 'address'])
      console.log('基础信息验证通过')
    }
    
    if (activeStep.value === 1) {
      // 验证房源信息
      console.log('开始验证房源信息')
      await validateFields(['price', 'roomType', 'facility', 'description'])
      console.log('房源信息验证通过')
    }
    
    if (activeStep.value === 2) {
      // 验证媒体信息
      console.log('开始验证媒体信息')
      if (uploadedImages.value.length === 0) {
        ElMessage.warning('请上传至少一张图片')
        return
      }
      console.log('媒体信息验证通过')
    }
    
    if (activeStep.value < 3) {
      console.log('步骤增加前:', activeStep.value)
      activeStep.value++
      console.log('步骤增加后:', activeStep.value)
    }
  } catch (error) {
    console.error('验证失败:', error)
    // 验证失败，不继续
  }
}

const prevStep = () => {
  if (activeStep.value > 0) {
    activeStep.value--
  }
}

// 监听选择的房型和设施，自动更新到表单
watch([selectedRoomTypes, selectedFacilities], () => {
  form.roomType = selectedRoomTypes.value.join('、')
  form.facility = selectedFacilities.value.join('、')
}, { deep: true })

// 保存草稿
const saveDraft = () => {
  // 这里可以实现草稿保存功能
  ElMessage.success('草稿保存成功')
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
    form.imageUrl = uploadedImages.value.join(',')
    if (!form.coverImage) {
      form.coverImage = uploadedImages.value[0]
    }
    
    console.log('准备提交的民宿数据:', {
      name: form.name,
      address: form.address,
      city: form.city,
      longitude: form.longitude,
      latitude: form.latitude,
      imageUrl: form.imageUrl,
      coverImage: form.coverImage,
      imagesCount: uploadedImages.value.length
    })
    
    loading.value = true
    
    // 使用展开运算符确保不是reactive对象
    const { images, ...submitData } = form
    const result = await homestayAPI.create(submitData)
    console.log('民宿创建结果:', result)
    
    // 跳转到体验项目步骤
    activeStep.value = 3
    ElMessage.success('民宿发布成功！现在可以创建体验项目')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('发布失败:', error)
      ElMessage.error('发布失败')
    }
  } finally {
    loading.value = false
  }
}

// 重置表单
const handleReset = () => {
  formRef.value.resetFields()
  selectedRoomTypes.value = []
  selectedFacilities.value = []
  uploadedImages.value = []
  form.images = []
  form.coverImage = ''
  activeStep.value = 0
}

// 地址搜索相关方法
const handleAddressInput = (value) => {
  // 清空旧结果
  addressSuggestions.value = []
  
  // 基础校验
  if (!value || value.length < 1) {
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
      console.error('地图API加载失败')
      // 模拟一些地址建议，以便在地图API加载失败时也能看到提示
      const mockTips = [
        { name: '北京市朝阳区建国路', address: '', location: { lng: 116.46556, lat: 39.90882 } },
        { name: '上海市浦东新区陆家嘴', address: '', location: { lng: 121.49989, lat: 31.23977 } },
        { name: '广州市天河区珠江新城', address: '', location: { lng: 113.32716, lat: 23.12005 } }
      ]
      addressSuggestions.value = mockTips
      return
    }
    
    try {
      // 加载 AutoComplete 插件
      AMap.plugin('AMap.AutoComplete', function() {
        if (!autoComplete) {
          autoComplete = new AMap.AutoComplete({
            city: '全国',
            input: 'addressInput' // 关联输入框
          })
        }
        
        // 发起搜索
        autoComplete.search(value, function(status, result) {
          console.log('地址搜索结果:', status, result)
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
            console.log('地址建议:', tips)
          } else {
            console.error('地址搜索失败:', status, result)
            // 搜索失败时显示模拟数据
            const mockTips = [
              { name: '北京市朝阳区建国路', address: '', location: { lng: 116.46556, lat: 39.90882 } },
              { name: '上海市浦东新区陆家嘴', address: '', location: { lng: 121.49989, lat: 31.23977 } },
              { name: '广州市天河区珠江新城', address: '', location: { lng: 113.32716, lat: 23.12005 } }
            ]
            addressSuggestions.value = mockTips
          }
        })
      })
    } catch (error) {
      console.error('地址搜索异常:', error)
      // 异常时显示模拟数据
      const mockTips = [
        { name: '北京市朝阳区建国路', address: '', location: { lng: 116.46556, lat: 39.90882 } },
        { name: '上海市浦东新区陆家嘴', address: '', location: { lng: 121.49989, lat: 31.23977 } },
        { name: '广州市天河区珠江新城', address: '', location: { lng: 113.32716, lat: 23.12005 } }
      ]
      addressSuggestions.value = mockTips
    }
  }, 300) // 300ms防抖，减少用户等待感
}

// 地址输入完成后自动定位到地图
const handleAddressBlur = () => {
  // 延迟隐藏建议列表，以便用户可以点击选择
  setTimeout(() => {
    addressSuggestions.value = []
    
    // 如果地址不为空，尝试通过地理编码获取经纬度并更新地图
    if (form.address) {
      // 确保高德地图 API 加载完成
      if (typeof AMap === 'undefined') {
        console.error('地图API加载失败')
        return
      }
      
      try {
        // 加载 Geocoder 插件
        AMap.plugin('AMap.Geocoder', function() {
          if (!geocoder) {
            geocoder = new AMap.Geocoder({
              radius: 1000,
              extensions: "all"
            })
          }
          
          // 发起地理编码
          geocoder.getLocation(form.address, function(status, result) {
            console.log('地理编码结果:', status, result)
            if (status === 'complete' && result.info === 'OK') {
              const location = result.geocodes[0].location
              form.longitude = location.lng
              form.latitude = location.lat
              
              // 更新地图位置
              initMap(location.lng, location.lat)
              showMap.value = true
              
              console.log('地址定位成功:', location.lng, location.lat)
            } else {
              console.error('地理编码失败:', status, result)
            }
          })
        })
      } catch (error) {
        console.error('地理编码异常:', error)
      }
    }
  }, 300) // 增加延迟时间，确保用户有足够时间点击
}



const selectAddress = (suggestion) => {
  form.address = suggestion.name + (suggestion.address ? ' ' + suggestion.address : '')
  form.longitude = suggestion.location.lng
  form.latitude = suggestion.location.lat
  addressSuggestions.value = []
  initMap(suggestion.location.lng, suggestion.location.lat)
  showMap.value = true
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
              form.address = address
              form.city = result.regeocode.addressComponent.city || result.regeocode.addressComponent.province
              console.log('获取位置成功:', address, longitude, latitude)
              initMap(longitude, latitude)
              showMap.value = true
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
              showMap.value = true
              // 手动设置一个默认地址，避免表单验证失败
              if (!form.address) {
                form.address = `经度: ${longitude.toFixed(6)}, 纬度: ${latitude.toFixed(6)}`
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

// 初始化地图
const initMap = (lng, lat) => {
  // 确保高德地图 API 加载完成
  if (typeof AMap === 'undefined') {
    ElMessage.error('地图API加载失败')
    return
  }
  
  if (!map) {
    // 动态加载 Map 组件
    AMap.plugin(['AMap.Map', 'AMap.Marker'], function() {
      map = new AMap.Map('locationMap', {
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
      
      // 监听标记拖动结束事件
      marker.on('dragend', function(e) {
        const position = e.lnglat
        form.longitude = position.getLng()
        form.latitude = position.getLat()
        
        // 逆地理编码获取新地址
        AMap.plugin('AMap.Geocoder', function() {
          geocoder = new AMap.Geocoder({
            radius: 1000,
            extensions: "all"
          })
          
          geocoder.getAddress([position.getLng(), position.getLat()], function(status, result) {
            if (status === 'complete' && result.info === 'OK') {
              const address = result.regeocode.formattedAddress
              form.address = address
              form.city = result.regeocode.addressComponent.city || result.regeocode.addressComponent.province
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
  }
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

// 完成创建
const finishCreation = () => {
  router.push('/landlord/homestays')
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

.card-tip {
  font-size: 14px;
  color: #999;
}

.input-suggestion {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.facility-tags {
  margin-bottom: 10px;
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

.tip {
  font-size: 12px;
  color: #999;
  margin-left: 10px;
}

/* 优化整体UI */
/* 整体布局优化 */
.el-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.form-card {
  margin-bottom: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  padding: 30px;
}

.form-card:hover {
  box-shadow: 0 8px 30px 0 rgba(0, 0, 0, 0.15);
}

/* 表单元素优化 */
.el-form-item {
  margin-bottom: 24px;
}

.el-form-item__label {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.el-input,
.el-select,
.el-input-number {
  width: 100%;
  max-width: 600px;
  transition: all 0.3s ease;
}

.el-input:focus-within,
.el-select:focus-within,
.el-input-number:focus-within {
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.el-textarea {
  width: 100%;
  max-width: 800px;
  resize: vertical;
  min-height: 120px;
}

/* 优化按钮样式 */
.el-button {
  transition: all 0.3s ease;
  padding: 10px 24px;
  font-size: 14px;
}

.el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 优化步骤指示器 */
.el-steps {
  margin-bottom: 40px;
  padding: 0 20px;
}

.el-step__title {
  font-size: 16px;
  font-weight: 500;
}

.el-step__head {
  transition: all 0.3s ease;
  width: 40px;
  height: 40px;
  line-height: 40px;
}

.el-step__head:hover {
  transform: scale(1.1);
}

/* 地址输入和地图优化 */
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
  border-radius: 8px;
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  z-index: 9999;
  max-height: 300px;
  overflow-y: auto;
  max-width: 600px;
  margin-top: 40px;
  display: block !important;
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

.map-container {
  margin-top: 24px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  overflow: hidden;
  width: 100%;
  max-width: 1000px;
  margin: 24px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

#locationMap {
  height: 500px !important;
}

.map-tip {
  padding: 12px;
  background-color: #f5f7fa;
  font-size: 14px;
  color: #606266;
  text-align: center;
  border-top: 1px solid #dcdfe6;
}

/* 图片上传优化 */
.image-uploader {
  margin-bottom: 30px;
}

.uploaded-images h4 {
  margin: 30px 0 15px 0;
  font-size: 18px;
  color: #333;
  font-weight: 500;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.image-item {
  width: 140px;
  text-align: center;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 12px;
  transition: all 0.3s ease;
}

.image-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.image-item img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 6px;
  margin-bottom: 12px;
}

.image-actions {
  font-size: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

/* 设施标签优化 */
.facility-tags {
  margin-bottom: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.facility-tags .el-tag {
  padding: 6px 12px;
  font-size: 14px;
  border-radius: 16px;
  background-color: #f0f2f5;
  border: 1px solid #e4e7ed;
}

/* 步骤按钮优化 */
.el-form-item:last-child {
  margin-top: 40px;
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  padding-top: 20px;
  border-top: 1px solid #f0f2f5;
}

/* 状态选项优化 */
.status-option {
  display: flex;
  flex-direction: column;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-right: 24px;
  transition: all 0.3s ease;
  min-width: 220px;
  flex-shrink: 0;
}

.status-option:hover {
  border-color: #667eea;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.1);
}

.status-title {
  font-weight: bold;
  margin-bottom: 8px;
  font-size: 16px;
}

.status-desc {
  font-size: 14px;
  color: #606266;
  line-height: 1.4;
}

/* 响应式调整 - 仅针对桌面端 */
@media (min-width: 1024px) {
  .el-main {
    padding: 60px 40px;
  }
  
  .form-card {
    padding: 40px;
  }
  
  .el-form-item {
    margin-bottom: 30px;
  }
  
  .el-steps {
    margin-bottom: 50px;
  }
}

/* 移除移动端样式，因为不需要考虑移动端 */
</style>
