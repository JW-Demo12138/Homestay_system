import request from '@/utils/request'

export const userAPI = {
  getUserInfo() {
    return request({
      url: '/api/user/info',
      method: 'get'
    })
  },
  
  updateUserInfo(data) {
    return request({
      url: '/api/user/update',
      method: 'put',
      data
    })
  },
  
  changePassword(data) {
    return request({
      url: '/api/user/change-password',
      method: 'post',
      data
    })
  },
  
  uploadAvatar(file) {
    const formData = new FormData()
    formData.append('avatar', file)
    
    return request({
      url: '/api/user/upload-avatar',
      method: 'post',
      data: formData
    })
  },
  
  getUserList(params) {
    return request({
      url: '/api/user/admin/list',
      method: 'get',
      params
    })
  },
  
  updateUserStatus(id, data) {
    return request({
      url: `/api/user/admin/status/${id}`,
      method: 'put',
      data
    })
  }
}
