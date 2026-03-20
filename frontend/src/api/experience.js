import request from '@/utils/request'

export const experienceAPI = {
  getList(params) {
    return request({
      url: '/api/experience/list',
      method: 'get',
      params
    })
  },
  
  getDetail(id) {
    return request({
      url: `/api/experience/detail/${id}`,
      method: 'get'
    })
  },
  
  create(data) {
    return request({
      url: '/api/experience/create',
      method: 'post',
      data
    })
  },
  
  update(id, data) {
    return request({
      url: `/api/experience/update/${id}`,
      method: 'put',
      data
    })
  },
  
  delete(id) {
    return request({
      url: `/api/experience/delete/${id}`,
      method: 'delete'
    })
  },
  
  updateStatus(id, status) {
    return request({
      url: `/api/experience/status/${id}`,
      method: 'put',
      data: { status }
    })
  },
  
  getLandlordList() {
    return request({
      url: '/api/experience/landlord/list',
      method: 'get'
    })
  },
  
  getPending() {
    return request({
      url: '/api/experience/pending',
      method: 'get'
    })
  },
  
  review(id, status, remark) {
    return request({
      url: '/api/experience/review',
      method: 'post',
      data: { id, status, remark }
    })
  }
}
