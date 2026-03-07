import { ref } from 'vue'

// 假设我们已经在index.html中加载了高德地图API
export function useAMap() {
  const map = ref(null)
  const driving = ref(null)
  const transit = ref(null)
  const walking = ref(null)
  const geocoder = ref(null)
  
  // 同步设置安全密钥
  if (typeof window !== 'undefined' && !window._AMapSecurityConfig) {
    window._AMapSecurityConfig = {
      securityJsCode: '644b9e75d2bb248b8f2770407b3b94ef'
    }
  }
  
  const initMap = async (containerId, options = {}) => {
    if (!window.AMap) {
      throw new Error('高德地图API未加载')
    }
    
    // 初始化地图
    map.value = new window.AMap.Map(containerId, {
      zoom: options.zoom || 15,
      center: options.center || [116.397428, 39.90923]
    })
    
    // 延迟初始化服务，确保地图完全加载
    await new Promise(resolve => setTimeout(resolve, 100))
    
    // 初始化驾驶规划服务
    await new Promise(resolve => {
      window.AMap.plugin('AMap.Driving', function() {
        driving.value = new window.AMap.Driving({
          map: map.value,
          panel: 'panel',
          policy: window.AMap.DrivingPolicy.LEAST_TIME
        })
        resolve()
      })
    })
    
    // 初始化公交规划服务
    await new Promise(resolve => {
      window.AMap.plugin('AMap.Transfer', function() {
        transit.value = new window.AMap.Transfer({
          map: map.value,
          city: '北京',
          panel: 'panel'
        })
        resolve()
      })
    })
    
    // 初始化步行规划服务
    await new Promise(resolve => {
      window.AMap.plugin('AMap.Walking', function() {
        walking.value = new window.AMap.Walking({
          map: map.value,
          panel: 'panel'
        })
        resolve()
      })
    })
    
    // 初始化地理编码服务
    await new Promise(resolve => {
      window.AMap.plugin('AMap.Geocoder', function() {
        geocoder.value = new window.AMap.Geocoder({
          city: "全国",
          radius: 1000,
          extensions: "all"
        })
        resolve()
      })
    })
    
    return { 
      map: map.value, 
      driving: driving.value,
      transit: transit.value,
      walking: walking.value,
      geocoder: geocoder.value
    }
  }
  
  return { 
    map, 
    driving, 
    transit, 
    walking, 
    geocoder, 
    initMap 
  }
}