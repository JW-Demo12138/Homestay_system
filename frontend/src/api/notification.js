import request from '@/utils/request'

export const notificationAPI = {
  // 获取通知列表
  getList() {
    return request({
      url: '/api/notification/list',
      method: 'get'
    })
  },
  
  // 标记通知为已读
  markAsRead(id) {
    return request({
      url: `/api/notification/read/${id}`,
      method: 'put'
    })
  },
  
  // 标记所有通知为已读
  markAllAsRead() {
    return request({
      url: '/api/notification/read/all',
      method: 'put',
      data: {}
    })
  },
  
  // 删除通知
  delete(id) {
    return request({
      url: `/api/notification/delete/${id}`,
      method: 'delete'
    })
  }
}