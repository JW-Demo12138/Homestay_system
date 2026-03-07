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
                  v-model="form.address"
                  placeholder="请输入详细地址，如：北京市朝阳区建国路"
                  maxlength="200"
                  show-word-limit
                  @input="handleAddressInput"
                  @blur="handleAddressBlur"
                  @focus="handleAddressFocus"
                >
                  <template #append>
                    <el-button @click="getCurrentLocation" type="primary" size="small">
                      <el-icon><Position /></el-icon>
                      当前位置
                    </el-button>
                  </template>
                </el-input>
                <!-- 地址建议下拉列表 -->
                <div v-if="showAddressSuggestions && addressSuggestions.length > 0" class="address-suggestions">
                  <div 
                    v-for="(suggestion, index) in addressSuggestions" 
                    :key="index"
                    class="suggestion-item"
                    @click="selectAddress(suggestion)"
                  >
                    <el-icon class="suggestion-icon"><MapLocation /></el-icon>
                    <span class="suggestion-text">{{ suggestion }}</span>
                  </div>
                </div>
              </div>
            </el-form-item>
            
            <el-form-item label="城市" prop="city">
              <el-input v-model="form.city" placeholder="城市会根据地址自动填充" readonly />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="getCoords" :loading="loadingCoords">
                <el-icon><Location /></el-icon>
                自动获取坐标
              </el-button>
              <span class="tip">点击后会根据地址自动填充经纬度</span>
            </el-form-item>
            
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="经度" readonly />
            </el-form-item>
            
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="纬度" readonly />
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
            
            <el-form-item label="图片上传" prop="images">              <el-upload
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
                  <div v-for="(image, index) in uploadedImages" :key="index" class="image-item">                  <img :src="getImageUrl(image)" alt="民宿图片" />
                    <div class="image-actions">                      <el-checkbox v-model="form.coverImage" :label="image">设为封面</el-checkbox>
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
import { geocodingAPI } from '@/api/geocoding'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Position, Location, MapLocation } from '@element-plus/icons-vue'
import { getImageUrl } from '@/utils'

const router = useRouter()
const userStore = useUserStore()

// 组件挂载时更新上传请求头
onMounted(() => {
  updateUploadHeaders()
})

// 监听token变化，更新上传请求头
watch(() => localStorage.getItem('accessToken'), () => {
  updateUploadHeaders()
})

const formRef = ref(null)
const loading = ref(false)
const loadingCoords = ref(false)
const activeStep = ref(0)

// 地址建议相关
const showAddressSuggestions = ref(false)
const addressSuggestions = ref([])
const addressInputTimer = ref(null)

// 表单数据
const form = reactive({
  name: '',
  address: '',
  city: '',
  longitude: null,
  latitude: null,
  price: null,
  roomType: '',
  facility: '',
  images: [],
  coverImage: '',
  description: '',
  status: 1
})

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

// 地址和坐标
const getAddressSuggestions = async (keyword) => {
  if (!keyword || keyword.length < 2) {
    addressSuggestions.value = []
    return
  }
  
  try {
    if (!window.BMap) {
      ElMessage.error('百度地图API加载失败')
      return
    }
    
    // 使用百度地图的本地搜索服务，不指定城市以搜索全国范围
    const localSearch = new window.BMap.LocalSearch('', {
      onSearchComplete: (results) => {
        if (localSearch.getStatus() === window.BMAP_STATUS_SUCCESS) {
          const suggestions = []
          for (let i = 0; i < results.getCurrentNumPois(); i++) {
            const poi = results.getPoi(i)
            // 显示地点名称和地址，格式：地点名称 (地址)
            const suggestionText = `${poi.title} (${poi.address})`
            suggestions.push(suggestionText)
          }
          addressSuggestions.value = suggestions
          showAddressSuggestions.value = true
        }
      }
    })
    
    localSearch.search(keyword)
  } catch (error) {
    console.error('获取地址建议失败:', error)
    addressSuggestions.value = []
  }
}

// 地址输入处理
const handleAddressInput = (value) => {
  clearTimeout(addressInputTimer.value)
  addressInputTimer.value = setTimeout(() => {
    getAddressSuggestions(value)
  }, 300)
}

// 地址输入框获得焦点
const handleAddressFocus = () => {
  if (form.address) {
    getAddressSuggestions(form.address)
  }
}

// 地址输入框失去焦点
const handleAddressBlur = () => {
  // 延迟隐藏，以便点击建议项
  setTimeout(() => {
    showAddressSuggestions.value = false
  }, 200)
}

// 选择地址建议
const selectAddress = (suggestion) => {
  // 解析地址建议，提取地址部分
  const addressMatch = suggestion.match(/\((.*)\)$/)
  if (addressMatch) {
    form.address = addressMatch[1]
  } else {
    form.address = suggestion
  }
  showAddressSuggestions.value = false
  // 自动提取城市
  const cityMatch = form.address.match(/^(\S+[省市自治区])/)
  if (cityMatch) {
    form.city = cityMatch[1]
  }
  // 自动获取坐标
  getCoords()
}

// 获取当前位置
const getCurrentLocation = () => {
  if (navigator.geolocation) {
    loadingCoords.value = true
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords
        form.latitude = latitude
        form.longitude = longitude
        
        try {
          if (!window.BMap) {
            ElMessage.error('百度地图API加载失败')
            loadingCoords.value = false
            return
          }
          
          // 使用百度地图的逆地理编码服务
          const geocoder = new window.BMap.Geocoder()
          const point = new window.BMap.Point(longitude, latitude)
          
          geocoder.getLocation(point, (result) => {
            loadingCoords.value = false
            if (result) {
              form.address = result.address
              form.city = result.addressComponents.city
              ElMessage.success('获取当前位置成功')
            } else {
              ElMessage.error('获取地址信息失败')
            }
          })
        } catch (error) {
          loadingCoords.value = false
          console.error('逆地理编码失败:', error)
          ElMessage.error('获取地址信息失败')
        }
      },
      (error) => {
        loadingCoords.value = false
        console.error('获取位置失败:', error)
        ElMessage.error('获取位置失败，请检查定位权限')
      }
    )
  } else {
    ElMessage.error('您的浏览器不支持地理定位')
  }
}

const getCoords = () => {
  if (!form.address) {
    ElMessage.warning('请先输入地址')
    return
  }
  
  loadingCoords.value = true
  try {
    if (!window.BMap) {
      ElMessage.error('百度地图API加载失败')
      loadingCoords.value = false
      return
    }
    
    // 使用百度地图的地址解析服务
    const geocoder = new window.BMap.Geocoder()
    geocoder.getPoint(form.address, (point) => {
      loadingCoords.value = false
      if (point) {
        form.longitude = point.lng
        form.latitude = point.lat
        ElMessage.success('获取坐标成功')
      } else {
        ElMessage.error('获取坐标失败，请检查地址是否正确')
      }
    }, form.city)
  } catch (error) {
    loadingCoords.value = false
    console.error('获取坐标失败:', error)
    ElMessage.error('获取坐标失败，请检查地址是否正确')
  }
}

// 防抖函数
const debounce = (func, delay) => {
  let timeoutId
  return (...args) => {
    clearTimeout(timeoutId)
    timeoutId = setTimeout(() => func.apply(null, args), delay)
  }
}

// 监听地址变化自动获取坐标
watch(() => form.address, debounce((newAddress) => {
  if (newAddress && newAddress.length > 5) {
    getCoords()
  }
}, 800))

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
      
      if (!form.longitude || !form.latitude) {
        console.log('经纬度为空，显示警告')
        ElMessage.warning('请获取坐标')
        return
      }
      console.log('经纬度已获取:', form.longitude, form.latitude)
    }
    
    if (activeStep.value === 1) {
      // 验证房源信息
      console.log('开始验证房源信息')
      await validateFields(['price', 'roomType', 'facility', 'description'])
      console.log('房源信息验证通过')
    }
    
    if (activeStep.value < 2) {
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
    
    // 检查经纬度
    if (!form.longitude || !form.latitude) {
      ElMessage.warning('请获取坐标')
      return
    }
    
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
      imageUrl: form.imageUrl,
      coverImage: form.coverImage,
      imagesCount: uploadedImages.value.length
    })
    
    loading.value = true
    
    // 使用展开运算符确保不是reactive对象
    const { images, ...submitData } = form
    const result = await homestayAPI.create(submitData)
    console.log('民宿创建结果:', result)
    
    await ElMessageBox.alert(
      '发布成功！',
      '提示',
      {
        confirmButtonText: '查看房源',
        cancelButtonText: '继续发布',
        showCancelButton: true,
        callback: (action) => {
          if (action === 'confirm') {
            router.push('/landlord/homestays')
          } else {
            handleReset()
          }
        }
      }
    )
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
  border-radius: 0 0 4px 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
  max-height: 300px;
  overflow-y: auto;
}

.suggestion-item {
  padding: 12px 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: background-color 0.2s;
}

.suggestion-item:hover {
  background-color: #f5f7fa;
}

.suggestion-icon {
  margin-right: 8px;
  color: #667eea;
}

.suggestion-text {
  flex: 1;
  font-size: 14px;
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

/* 优化步骤指示器 */
.el-steps {
  margin-bottom: 30px;
}

.el-step__head {
  transition: all 0.3s ease;
}

.el-step__head:hover {
  transform: scale(1.1);
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
}
</style>
