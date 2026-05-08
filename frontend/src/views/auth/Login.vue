<template>
  <div class="login-container">
    <div class="background-overlay"></div>
    <div class="login-box">
      <div class="logo-section">
        <div class="logo-icon">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
            <polyline points="9 22 9 12 15 12 15 22"/>
          </svg>
        </div>
        <h2>民宿预订系统</h2>
        <p>发现美好住宿体验</p>
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="用户名或手机号" 
            class="login-input"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="密码" 
            show-password
            class="login-input"
          />
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="loginForm.role" placeholder="请选择角色" class="login-select">
            <el-option label="游客" value="TOURIST" />
            <el-option label="房东" value="LANDLORD" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" class="login-btn">
            登录
          </el-button>
        </el-form-item>
        <el-form-item class="link-row">
          <el-link type="primary" @click="goToRegister">还没有账号？去注册</el-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  role: 'TOURIST'
})

const rules = {
  username: [
    { required: true, message: '请输入用户名或手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate()
  if (!valid) return
  
  loading.value = true
  try {
    await userStore.login(loginForm)
    ElMessage.success('登录成功')
    
    if (userStore.role === 'LANDLORD') {
      router.push('/landlord')
    } else if (userStore.role === 'ADMIN') {
      router.push('/admin')
    } else {
      router.push('/')
    }
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-image: url('https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=1920&q=80');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
}

.background-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(2px);
}

.login-box {
  position: relative;
  z-index: 10;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 45px;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  width: 420px;
  animation: slideUp 0.5s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.logo-section {
  text-align: center;
  margin-bottom: 35px;
}

.logo-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto 15px;
  color: #5e72e4;
  animation: bounce 2s infinite;
  display: flex;
  justify-content: center;
  align-items: center;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.logo-section h2 {
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 8px;
}

.logo-section p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0;
}

.login-input {
  border-radius: 8px;
  height: 44px;
}

.login-select {
  border-radius: 8px;
  height: 44px;
}

.login-btn {
  width: 100%;
  height: 44px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #5e72e4 0%, #825ee4 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(94, 114, 228, 0.4);
}

.link-row {
  text-align: center;
  margin-top: 20px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-form-item__label {
  display: none;
}

.el-form-item__content {
  margin-left: 0 !important;
}
</style>
