import axios from 'axios'
import { ElMessage } from 'element-plus'
import { BASE_URL } from '@/config'
import router from '@/router'

const request = axios.create({
  baseURL: BASE_URL,
  timeout: 10000
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    
    if (code === 200) {
      return data
    } else {
      ElMessage.error(message || '请求失败')
      return Promise.reject(new Error(message))
    }
  },
  error => {
    if (error.response) {
      const { status } = error.response
      
      switch (status) {
        case 401:
          ElMessage.error('未登录或登录已过期')
          localStorage.removeItem('accessToken')
          localStorage.removeItem('refreshToken')
          localStorage.removeItem('userInfo')
          router.push('/login')
          break
        case 403:
          // 对于推荐民宿、热门城市等公开接口，不需要弹出错误提示
          const publicUrls = ['/api/homestay/recommend', '/api/homestay/list', '/api/homestay/detail', '/api/homestay/search', '/api/city/hot', '/api/city/all', '/api/city/search']
          const url = error.config?.url || ''
          const isPublicUrl = publicUrls.some(publicUrl => url.includes(publicUrl))
          if (!isPublicUrl) {
            ElMessage.error('无权限访问')
          }
          break
        case 404:
          ElMessage.error('资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误')
          break
        default:
          ElMessage.error(error.response.data?.message || '请求失败')
      }
    } else if (error.message.includes('timeout')) {
      ElMessage.error('请求超时')
    } else if (error.message.includes('Network')) {
      ElMessage.error('网络错误')
    } else {
      ElMessage.error('请求失败')
    }
    
    return Promise.reject(error)
  }
)

export default request 