import { defineStore } from 'pinia'
import { authAPI } from '@/api/auth'
import { userAPI } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('accessToken') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null')
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    userId: (state) => state.userInfo?.id,
    username: (state) => state.userInfo?.username,
    role: (state) => state.userInfo?.role,
    isLandlord: (state) => state.userInfo?.role === 'LANDLORD',
    isAdmin: (state) => state.userInfo?.role === 'ADMIN',
    isTourist: (state) => state.userInfo?.role === 'TOURIST'
  },
  
  actions: {
    async login(loginData) {
      const { accessToken, refreshToken, user } = await authAPI.login(loginData)
      
      this.token = accessToken
      this.refreshToken = refreshToken
      this.userInfo = user
      
      localStorage.setItem('accessToken', accessToken)
      localStorage.setItem('refreshToken', refreshToken)
      localStorage.setItem('userInfo', JSON.stringify(user))
    },
    
    async register(registerData) {
      await authAPI.register(registerData)
    },
    
    async logout() {
      try {
        await authAPI.logout()
      } catch (error) {
        console.error('登出失败:', error)
      }
      
      this.token = ''
      this.refreshToken = ''
      this.userInfo = null
      
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('userInfo')
    },
    
    async refreshAccessToken() {
      const { accessToken } = await authAPI.refreshToken({
        refreshToken: this.refreshToken
      })
      
      this.token = accessToken
      localStorage.setItem('accessToken', accessToken)
    },
    
    async getUserInfo() {
      const userInfo = await userAPI.getUserInfo()
      this.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    
    async updateUserInfo(userInfo) {
      const updatedUser = await userAPI.updateUserInfo(userInfo)
      this.userInfo = updatedUser
      localStorage.setItem('userInfo', JSON.stringify(updatedUser))
    },
    
    async changePassword(oldPassword, newPassword) {
      await userAPI.changePassword({ oldPassword, newPassword })
    },
    
    async uploadAvatar(file) {
      const user = await userAPI.uploadAvatar(file)
      this.userInfo = user
      localStorage.setItem('userInfo', JSON.stringify(user))
    }
  }
})
