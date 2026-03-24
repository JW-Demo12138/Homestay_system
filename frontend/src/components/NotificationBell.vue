<template>
  <div class="notification-bell">
    <el-dropdown trigger="click" @command="handleCommand">
      <el-button class="bell-button" :class="{ 'has-notification': unreadCount > 0 }">
        <el-icon><BellFilled /></el-icon>
        <span v-if="unreadCount > 0" class="notification-badge">{{ unreadCount }}</span>
      </el-button>
      <template #dropdown>
        <el-dropdown-menu>
          <div class="notification-header">
            <span>通知</span>
          </div>
          <el-dropdown-item v-if="notifications.length === 0" disabled>
            暂无通知
          </el-dropdown-item>
          <div v-else class="notification-list">
            <div 
              v-for="notification in notifications" 
              :key="notification.id"
              class="notification-item" 
              :class="{ 'unread': !notification.read }"
              @click="handleNotificationClick(notification)"
            >
              <div class="notification-content">
                <div class="notification-title">{{ notification.title }}</div>
                <div class="notification-message">{{ notification.message }}</div>
                <div class="notification-time">{{ formatTime(notification.createdAt) }}</div>
              </div>
            </div>
          </div>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { BellFilled } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { notificationAPI } from '@/api/notification'
import { ElMessage } from 'element-plus'

const router = useRouter()
const notifications = ref([])

// 计算未读通知数量
const unreadCount = computed(() => {
  return notifications.value.filter(n => !n.read).length
})

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取通知列表
const getNotifications = async () => {
  try {
    const result = await notificationAPI.getList()
    if (Array.isArray(result)) {
      notifications.value = result
    }
  } catch (error) {
    console.error('获取通知列表失败:', error)
  }
}



// 处理通知点击
const handleNotificationClick = async (notification) => {
  // 标记为已读
  if (!notification.read) {
    try {
      await notificationAPI.markAsRead(notification.id)
      notification.read = true
    } catch (error) {
      console.error('标记通知为已读失败:', error)
    }
  }
  
  // 根据通知类型跳转到相应页面
  if (notification.type === 'experience_review') {
    router.push(`/landlord/experiences/${notification.referenceId}/edit`)
  } else if (notification.type === 'homestay_review') {
    router.push(`/landlord/homestays/${notification.referenceId}/edit`)
  }
}

// 处理下拉菜单命令
const handleCommand = (command) => {
  // 可以添加其他命令处理逻辑
}

// 组件挂载时获取通知列表
onMounted(() => {
  getNotifications()
})
</script>

<style scoped>
.notification-bell {
  position: relative;
}

.bell-button {
  position: relative;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.bell-button:hover {
  background-color: #f5f7fa;
}

.bell-button.has-notification {
  color: #409eff;
}

.notification-badge {
  position: absolute;
  top: 0;
  right: 0;
  min-width: 18px;
  height: 18px;
  padding: 0 6px;
  border-radius: 9px;
  background-color: #f56c6c;
  color: white;
  font-size: 12px;
  line-height: 18px;
  text-align: center;
  transform: translate(50%, -50%);
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #ebeef5;
}

.notification-header span {
  font-weight: bold;
  font-size: 14px;
  color: #333;
}

.notification-list {
  max-height: 400px;
  overflow-y: auto;
  min-width: 300px;
}

.notification-item {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.notification-item:hover {
  background-color: #f5f7fa;
}

.notification-item.unread {
  background-color: #ecf5ff;
}

.notification-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.notification-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.notification-message {
  font-size: 13px;
  color: #666;
  line-height: 1.4;
}

.notification-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}
</style>