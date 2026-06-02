<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="220px" class="aside">
        <div class="logo-section">
          <div class="logo">🏠</div>
          <h1>民宿管理系统</h1>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="side-menu"
          mode="vertical"
          @select="handleMenuSelect"
        >
          <el-menu-item index="dashboard">
            <el-icon><Document /></el-icon>
            <span>数据概览</span>
          </el-menu-item>
          <el-menu-item index="users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="orders">
            <el-icon><Picture /></el-icon>
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="reviews">
            <el-icon><BellFilled /></el-icon>
            <span>评价管理</span>
          </el-menu-item>
          <el-menu-item index="announcement">
            <el-icon><ChatDotRound /></el-icon>
            <span>公告管理</span>
          </el-menu-item>
          <el-menu-item index="config">
            <el-icon><Clock /></el-icon>
            <span>系统配置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-content">
            <div class="header-title">{{ pageTitle }}</div>
            <div class="header-right">
              <el-dropdown>
                <div class="user-dropdown">
                  <el-avatar :size="32" :src="getImageUrl(userStore.userInfo?.avatar)">
                    {{ userStore.username?.charAt(0).toUpperCase() }}
                  </el-avatar>
                  <span class="username">{{ userStore.username }}</span>
                  <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </div>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="goToUser">个人中心</el-dropdown-item>
                    <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </el-header>
        <el-main class="main">
          <router-view @update:title="updateTitle" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getImageUrl } from '@/utils'
import { Document, User, Picture, BellFilled, ChatDotRound, Clock, ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const pageTitle = ref('数据概览')

const activeMenu = computed(() => {
  const path = route.path
  if (path.startsWith('/admin/users')) return 'users'
  if (path.startsWith('/admin/orders')) return 'orders'
  if (path.startsWith('/admin/reviews')) return 'reviews'
  if (path.startsWith('/admin/announcement')) return 'announcement'
  if (path.startsWith('/admin/config')) return 'config'
  return 'dashboard'
})

const updateTitle = (title) => {
  pageTitle.value = title
}

const handleMenuSelect = (index) => {
  switch (index) {
    case 'dashboard':
      router.push('/admin')
      break
    case 'users':
      router.push('/admin/users')
      break
    case 'orders':
      router.push('/admin/orders')
      break
    case 'reviews':
      router.push('/admin/reviews')
      break
    case 'announcement':
      router.push('/admin/announcement')
      break
    case 'config':
      router.push('/admin/config')
      break
  }
}

const goToUser = () => {
  router.push('/user')
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  width: 100%;
}

.admin-layout :deep(.el-container) {
  height: 100vh;
  display: flex;
  flex-direction: row;
}

.aside {
  background: linear-gradient(180deg, #5e72e4 0%, #3f51b5 100%);
  color: white;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.logo-section {
  padding: 30px 20px;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  flex-shrink: 0;
}

.logo {
  font-size: 48px;
  margin-bottom: 12px;
}

.logo-section h1 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: white;
}

.side-menu {
  border-right: none;
  padding-top: 20px;
  flex: 1;
  overflow-y: auto;
  background: transparent;
}

.side-menu :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.9);
  height: 50px;
  line-height: 50px;
  margin: 0 10px;
  border-radius: 8px;
  margin-bottom: 4px;
  background: transparent;
}

.side-menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.15) !important;
}

.side-menu :deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.25) !important;
  color: white !important;
}

.side-menu :deep(.el-icon) {
  color: rgba(255, 255, 255, 0.8);
  width: 24px;
  height: 24px;
}

.admin-layout :deep(.el-container) .el-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 0;
  flex-shrink: 0;
  height: 60px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 30px;
}

.header-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f0f4f7;
}

.username {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.main {
  padding: 20px 30px;
  background: #f5f7fa;
  flex: 1;
  overflow-y: auto;
}
</style>