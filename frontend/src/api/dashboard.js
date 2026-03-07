import request from '@/utils/request'

export const dashboardAPI = {
  getStats() {
    return request({
      url: '/api/dashboard/stats',
      method: 'get'
    })
  },
  
  getRecentOrders() {
    return request({
      url: '/api/dashboard/recent-orders',
      method: 'get'
    })
  },
  
  getRecentHomestays() {
    return request({
      url: '/api/dashboard/recent-homestays',
      method: 'get'
    })
  }
}
