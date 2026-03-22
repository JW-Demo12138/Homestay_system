import request from '@/utils/request'

export const homestayAPI = {
  getList(params) {
    return request({
      url: '/api/homestay/list',
      method: 'get',
      params
    })
  },
  
  getDetail(id) {
    return request({
      url: `/api/homestay/detail/${id}`,
      method: 'get'
    })
  },
  
  create(data) {
    return request({
      url: '/api/homestay/create',
      method: 'post',
      data
    })
  },
  
  update(id, data) {
    return request({
      url: `/api/homestay/update/${id}`,
      method: 'put',
      data
    })
  },
  
  delete(id) {
    return request({
      url: `/api/homestay/delete/${id}`,
      method: 'delete'
    })
  },
  
  search(params) {
    return request({
      url: '/api/homestay/search',
      method: 'get',
      params
    })
  },
  
  getRecommend() {
    return request({
      url: '/api/homestay/recommend',
      method: 'get'
    })
  },
  
  getLandlordList() {
    return request({
      url: '/api/homestay/landlord/list',
      method: 'get'
    })
  },
  
  import(data) {
    return request({
      url: '/api/homestay/import',
      method: 'post',
      data
    })
  },
  
  getPending() {
    return request({
      url: '/api/homestay/pending',
      method: 'get'
    })
  },
  
  review(id, status, remark) {
    return request({
      url: '/api/homestay/review',
      method: 'post',
      data: { id, status, remark }
    })
  }
}
