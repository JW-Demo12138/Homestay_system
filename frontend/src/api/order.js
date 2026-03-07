import request from '@/utils/request'

export const orderAPI = {
  create(data) {
    return request({
      url: '/api/order/create',
      method: 'post',
      data
    })
  },
  
  getUserOrders() {
    return request({
      url: '/api/order/list',
      method: 'get'
    })
  },
  
  getLandlordOrders(params) {
    return request({
      url: '/api/order/landlord/orders',
      method: 'get',
      params
    })
  },
  
  getDetail(id) {
    return request({
      url: `/api/order/detail/${id}`,
      method: 'get'
    })
  },
  
  updateStatus(id, data) {
    return request({
      url: `/api/order/update/${id}`,
      method: 'put',
      data
    })
  },
  
  pay(id, data) {
    return request({
      url: `/api/order/pay/${id}`,
      method: 'post',
      data
    })
  },
  
  cancel(id) {
    return request({
      url: `/api/order/cancel/${id}`,
      method: 'post'
    })
  },
  
  comment(id, data) {
    return request({
      url: `/api/order/comment/${id}`,
      method: 'post',
      data
    })
  }
}
