import request from '@/utils/request'

export const geocodingAPI = {
  addressToCoords(address, city) {
    return request({
      url: '/api/geocoding/address-to-coords',
      method: 'get',
      params: { address, city }
    })
  },
  
  // 添加防抖 + 强制刷新参数
  searchRoute(origin, dest, mode = 'driving', force = false) {
    console.log('searchRoute 参数:', { origin, dest, mode, force });
    // 检查参数格式
    if (!origin || !dest) {
      return Promise.reject(new Error('缺少必要的参数'));
    }
    // 检查坐标格式
    const originParts = origin.split(',');
    const destParts = dest.split(',');
    if (originParts.length !== 2 || destParts.length !== 2) {
      return Promise.reject(new Error('坐标格式错误'));
    }
    return request({
      url: '/api/map/route',
      method: 'get',
      params: { origin, destination: dest, mode, refresh: force },
      timeout: 15000  // 比后端长一点
    }).catch(error => {
      console.error('路线规划错误:', error);
      console.error('错误响应:', error.response);
      console.error('错误响应数据:', error.response?.data);
      throw error;
    })
  }
}
