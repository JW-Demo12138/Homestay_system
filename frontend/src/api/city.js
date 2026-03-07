import request from '@/utils/request'

export const cityAPI = {
  getHotCities() {
    return request({
      url: '/api/city/hot',
      method: 'get'
    })
  },
  
  getAllCities() {
    return request({
      url: '/api/city/all',
      method: 'get'
    })
  },
  
  search(name) {
    return request({
      url: '/api/city/search',
      method: 'get',
      params: { name }
    })
  }
}
