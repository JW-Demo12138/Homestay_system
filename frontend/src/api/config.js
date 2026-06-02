import request from '@/utils/request'

export const configAPI = {
  list() {
    return request({
      url: '/api/config/list',
      method: 'get'
    })
  },
  
  get(key) {
    return request({
      url: `/api/config/${key}`,
      method: 'get'
    })
  },
  
  update(key, value) {
    return request({
      url: `/api/config/${key}`,
      method: 'put',
      data: { value: value }
    })
  },
  
  batchUpdate(configMap) {
    return request({
      url: '/api/config/batch',
      method: 'put',
      data: configMap
    })
  }
}