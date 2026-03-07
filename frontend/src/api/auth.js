import request from '@/utils/request'

export const authAPI = {
  login(data) {
    return request({
      url: '/api/auth/login',
      method: 'post',
      data
    })
  },
  
  register(data) {
    return request({
      url: '/api/auth/register',
      method: 'post',
      data
    })
  },
  
  refreshToken(data) {
    return request({
      url: '/api/auth/refresh',
      method: 'post',
      data
    })
  },
  
  logout() {
    return request({
      url: '/api/auth/logout',
      method: 'post'
    })
  }
}
