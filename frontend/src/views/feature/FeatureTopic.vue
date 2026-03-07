<template>
  <div class="topic-page">

    <!-- 1️⃣ Banner -->
    <div 
      class="banner" 
      :style="{ backgroundImage: 'url(' + (topic.bannerImage || defaultBanner) + ')' }"
    >
      <div class="banner-overlay">
        <h1>{{ topic.title }}</h1>
        <p>{{ topic.description }}</p>
      </div>
    </div>

    <!-- 2️⃣ 民宿列表 -->
    <div class="content">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="topic.homestays.length === 0" class="empty">
        <h2>暂无「{{ tag }}」民宿</h2>
        <p>可以去首页看看其他精选内容</p>
        <button @click="goHome">去首页</button>
      </div>
      <div v-else class="grid">
        <div
          class="card"
          v-for="item in topic.homestays"
          :key="item.id"
          @click="goDetail(item.id)"
        >
          <img 
            :src="((item.imageUrl || '').split(',')[0] || defaultImg)" 
            @error="handleImgError"
          />
          <div class="info">
            <h3>{{ item.name }}</h3>
            <p class="address">{{ item.address }}</p>
            <p class="price">￥{{ item.price }} / 晚</p>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from "vue"
import { useRoute, useRouter } from "vue-router"
import axios from "axios"

const route = useRoute()
const router = useRouter()

const loading = ref(true)

const topic = ref({
  title: "",
  description: "",
  bannerImage: "",
  homestays: []
})

const tag = computed(() => route.query.tag || "精选专题")

const defaultBanner = 'https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80'
const defaultImg = 'https://images.unsplash.com/photo-1590490360268-837e83a78017?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=800&q=80'

const loadData = (tagValue) => {
  loading.value = true

  axios.get("/api/feature/topic", {
    params: { tag: tagValue }
  }).then(res => {

    const data = res.data.data

    // 后端没数据
    if (!data || data.length === 0) {
      topic.value = {
        title: tagValue,
        description: "探索更多优质民宿，发现属于你的旅行灵感",
        bannerImage: defaultBanner,
        homestays: []
      }
      return
    }

    topic.value = {
      title: tagValue,
      description: "为你精选优质民宿推荐",
      bannerImage: defaultBanner,
      homestays: data
    }

  }).catch(() => {
    topic.value = {
      title: tagValue,
      description: "专题加载失败",
      bannerImage: defaultBanner,
      homestays: []
    }
  }).finally(() => {
    loading.value = false
  })
}

const handleImgError = (e) => {
  e.target.src = defaultImg
}

const goDetail = (id) => {
  router.push(`/homestays/${id}`)
}

const goHome = () => {
  router.push("/")
}

onMounted(() => {
  loadData(tag.value)
})

watch(tag, (newTag) => {
  loadData(newTag)
})
</script>

<style scoped>
.topic-page {
  font-family: -apple-system, BlinkMacSystemFont;
}

.banner {
  height: 420px;
  background-size: cover;
  background-position: center;
  position: relative;
  overflow: hidden;
}

.banner::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    to bottom,
    rgba(0,0,0,0.2),
    rgba(0,0,0,0.6)
  );
  z-index: 1;
}

.banner-overlay {
  position: absolute;
  bottom: 60px;
  left: 80px;
  color: white;
  z-index: 2;
}

.banner-overlay h1 {
  font-size: 42px;
  font-weight: 600;
}

.banner-overlay p {
  font-size: 18px;
  opacity: 0.9;
}

.content {
  padding: 60px 100px;
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.loading {
  font-size: 18px;
  color: #666;
}

.empty {
  text-align: center;
  padding: 100px 0;
}

.empty h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 12px;
}

.empty p {
  font-size: 16px;
  color: #666;
  margin-bottom: 24px;
}

.empty button {
  margin-top: 20px;
  padding: 10px 24px;
  border-radius: 24px;
  border: none;
  background: #ff385c;
  color: white;
  cursor: pointer;
  transition: 0.3s;
  font-size: 14px;
  font-weight: 500;
}

.empty button:hover {
  opacity: 0.85;
  transform: translateY(-2px);
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 28px;
  width: 100%;
}

.card {
  border-radius: 18px;
  overflow: hidden;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 18px rgba(0,0,0,0.08);
}

.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}

.card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.info {
  padding: 16px;
}

.info h3 {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
}

.info .address {
  margin: 0 0 12px;
  font-size: 14px;
  color: #666;
}

.price {
  font-weight: 600;
  font-size: 18px;
  color: #ff385c;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner {
    height: 300px;
  }
  
  .banner-overlay {
    bottom: 40px;
    left: 40px;
  }
  
  .banner-overlay h1 {
    font-size: 32px;
  }
  
  .content {
    padding: 40px 20px;
  }
  
  .grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;
  }
}
</style>