import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './router'
import App from './App.vue'

// 动态加载百度地图API
const loadBaiduMap = () => {
  return new Promise((resolve, reject) => {
    if (window.BMap) {
      console.log('百度地图API已经加载')
      resolve()
      return
    }
    
    console.log('开始加载百度地图API')
    console.log('API Key:', import.meta.env.VITE_BAIDU_MAP_AK)
    
    // 使用备用API密钥，确保地图能正常加载
    const apiKey = import.meta.env.VITE_BAIDU_MAP_AK || 'fFWcQQyzv5RrHOJ3BWNDVlJxkmSoEnMy'
    console.log('使用的API Key:', apiKey)
    
    const script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = `https://api.map.baidu.com/api?v=3.0&ak=${apiKey}&callback=initBaiduMap`
    
    // 添加全局回调函数
    window.initBaiduMap = () => {
      console.log('百度地图API加载成功')
      resolve()
    }
    
    script.onerror = (error) => {
      console.error('百度地图API加载失败:', error)
      // 即使加载失败，也继续执行，避免阻塞应用
      resolve()
    }
    
    document.head.appendChild(script)
    console.log('百度地图API脚本已添加到页面')
    
    // 添加超时处理
    setTimeout(() => {
      if (!window.BMap) {
        console.error('百度地图API加载超时')
        resolve()
      }
    }, 5000)
  })
}

// 加载百度地图API后再挂载应用
loadBaiduMap()
  .then(() => {
    const app = createApp(App)
    const pinia = createPinia()

    app.use(pinia)
    app.use(router)
    app.use(ElementPlus)

    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
      app.component(key, component)
    }

    app.mount('#app')
  })
  .catch(error => {
    console.error('百度地图API加载失败:', error)
    // 即使地图加载失败，也继续挂载应用
    const app = createApp(App)
    const pinia = createPinia()

    app.use(pinia)
    app.use(router)
    app.use(ElementPlus)

    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
      app.component(key, component)
    }

    app.mount('#app')
  })
