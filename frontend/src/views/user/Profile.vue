<template>
  <div class="user-profile">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <h1>民宿预订系统</h1>
          </div>
          <div class="nav">
            <el-link @click="goHome">首页</el-link>
            <el-link @click="goToHomestays">民宿列表</el-link>
            <template v-if="userStore.isLandlord">
              <el-link @click="goToLandlord">房东中心</el-link>
            </template>
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
      </el-header>
      
      <el-main>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="user-card">
              <div class="user-info">
                <el-avatar :size="100" :src="getImageUrl(userStore.userInfo?.avatar)">
                  {{ userStore.username?.charAt(0).toUpperCase() }}
                </el-avatar>
                <h3>{{ userStore.username }}</h3>
                <p>{{ USER_ROLE_MAP[userStore.role] }}</p>
              </div>
              <el-divider />
              <div class="user-stats">
                <div class="stat-item">
                  <div class="stat-value">{{ stats.totalOrders }}</div>
                  <div class="stat-label">订单数</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ stats.completedOrders }}</div>
                  <div class="stat-label">已完成</div>
                </div>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="16">
            <el-card class="info-card">
              <template #header>
                <h3>个人信息</h3>
              </template>
              <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="form.username" />
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="form.name" />
                </el-form-item>
                <el-form-item label="联系电话" prop="phone">
                  <el-input v-model="form.phone" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="form.email" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleUpdate" :loading="loading">
                    更新信息
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
            
            <el-card class="password-card" style="margin-top: 20px;">
              <template #header>
                <h3>修改密码</h3>
              </template>
              <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
                <el-form-item label="旧密码" prop="oldPassword">
                  <el-input v-model="passwordForm.oldPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="passwordForm.newPassword" type="password" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading">
                    修改密码
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
            
            <el-card class="avatar-card" style="margin-top: 20px;">
              <template #header>
                <h3>头像设置</h3>
              </template>
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :before-upload="handleAvatarUpload"
                :auto-upload="false"
                :on-change="handleFileChange"
              >
                <img v-if="userStore.userInfo?.avatar" :src="getImageUrl(userStore.userInfo.avatar)" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { orderAPI } from '@/api/order'
import { userAPI } from '@/api/user'
import { getImageUrl } from '@/utils'
import { USER_ROLE_MAP } from '@/config'
import { ElMessage } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const passwordFormRef = ref(null)
const loading = ref(false)
const passwordLoading = ref(false)

const form = reactive({
  username: userStore.userInfo?.username || '',
  name: userStore.userInfo?.name || '',
  phone: userStore.userInfo?.phone || '',
  email: userStore.userInfo?.email || ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: ''
})

const stats = ref({
  totalOrders: 0,
  completedOrders: 0
})

const uploadHeaders = computed(() => ({
  'Authorization': `Bearer ${userStore.token}`
}))

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const loadStats = async () => {
  try {
    const orders = await orderAPI.getUserOrders()
    stats.value.totalOrders = orders.length || 0
    stats.value.completedOrders = orders.filter(o => o.status === '已完成').length || 0
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const handleUpdate = async () => {
  const valid = await formRef.value.validate()
  if (!valid) return
  
  loading.value = true
  try {
    await userStore.updateUserInfo(form)
    ElMessage.success('更新成功')
  } catch (error) {
    console.error('更新失败:', error)
  } finally {
    loading.value = false
  }
}

const handleChangePassword = async () => {
  const valid = await passwordFormRef.value.validate()
  if (!valid) return
  
  passwordLoading.value = true
  try {
    await userStore.changePassword(passwordForm.oldPassword, passwordForm.newPassword)
    ElMessage.success('密码修改成功')
    passwordFormRef.value.resetFields()
  } catch (error) {
    console.error('修改密码失败:', error)
  } finally {
    passwordLoading.value = false
  }
}

const handleFileChange = (file) => {
  handleAvatarUpload(file.raw)
}

const handleAvatarUpload = async (file) => {
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
  
  try {
    console.log('开始上传头像...')
    const response = await userAPI.uploadAvatar(file)
    console.log('上传头像响应:', response)
    
    ElMessage.success('头像上传成功')
    
    console.log('开始获取用户信息...')
    await userStore.getUserInfo()
    console.log('获取到的用户信息:', userStore.userInfo)
    console.log('用户头像URL:', userStore.userInfo?.avatar)
    
  } catch (error) {
    console.error('上传头像失败:', error)
    // 检查是否是目录不存在的错误
    if (error.message?.includes('NoSuchFileException')) {
      ElMessage.error('上传目录未创建，请联系管理员')
    } else if (error.response?.status === 400) {
      ElMessage.error('上传参数错误')
    } else if (error.response?.status === 500) {
      ElMessage.error('服务器内部错误')
    } else {
      ElMessage.error('上传头像失败')
    }
  }
  
  return false
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

loadStats()
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

.user-card {
  text-align: center;
}

.user-info {
  padding: 20px 0;
}

.user-info h3 {
  margin: 10px 0 5px;
  font-size: 20px;
  color: #333;
}

.user-info p {
  margin: 0;
  color: #666;
}

.user-stats {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.info-card h3,
.password-card h3,
.avatar-card h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.avatar-uploader .avatar {
  width: 150px;
  height: 150px;
  display: block;
  border-radius: 50%;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
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
