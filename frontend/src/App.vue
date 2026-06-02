<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

onMounted(async () => {
  // 如果用户已登录，自动刷新用户信息，确保角色等数据正确
  if (userStore.isLoggedIn && userStore.token) {
    try {
      await userStore.getUserInfo()
    } catch (error) {
      console.error('刷新用户信息失败:', error)
    }
  }
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #f7f7f7;
}

#app {
  min-height: 100vh;
}
</style>
