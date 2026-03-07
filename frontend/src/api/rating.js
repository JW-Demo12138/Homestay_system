import request from '@/utils/request'

export const ratingAPI = {
  create(data) {
    return request({
      url: '/api/rating/create',
      method: 'post',
      data
    })
  },
  
  getHomestayRatings(id) {
    return request({
      url: `/api/rating/homestay/${id}`,
      method: 'get'
    })
  },
  
  getAverageRating(id) {
    return request({
      url: `/api/rating/average/${id}`,
      method: 'get'
    })
  }
}
