<template>
  <div class="announcement-management">
    <div class="page-header">
      <h2>公告管理</h2>
    </div>

    <el-card class="send-card">
      <el-form :model="form" label-width="120px">
        <el-form-item label="公告标题">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="公告内容">
          <el-input
            v-model="form.message"
            type="textarea"
            :rows="4"
            placeholder="请输入公告内容"
          />
        </el-form-item>
        <el-form-item label="发送对象">
          <el-select v-model="form.announcementType">
            <el-option label="全体用户" value="ALL" />
            <el-option label="仅房东" value="LANDLORD" />
            <el-option label="仅游客" value="TOURIST" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="sendAnnouncement" :loading="sendLoading">
            发送公告
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="list-card">
      <el-table :data="announcements" style="width: 100%">
        <el-table-column prop="title" label="公告标题" width="200" />
        <el-table-column prop="message" label="公告内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="发送时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="small" type="danger" @click="deleteAnnouncement(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { notificationAPI } from '@/api/notification'
import { ElMessage, ElMessageBox } from 'element-plus'

const sendLoading = ref(false)
const announcements = ref([])

const form = reactive({
  title: '',
  message: '',
  announcementType: 'ALL'
})

const loadAnnouncements = async () => {
  try {
    const res = await notificationAPI.getAnnouncementList()
    if (res && Array.isArray(res)) {
      // 去重，只显示每条公告一次（去掉重复的userId）
      const uniqueMap = new Map()
      res.forEach(item => {
        const key = `${item.title}-${item.createdAt}`
        if (!uniqueMap.has(key)) {
          uniqueMap.set(key, item)
        }
      })
      announcements.value = Array.from(uniqueMap.values())
    } else if (res) {
      announcements.value = res
    }
  } catch (error) {
    console.error('加载公告列表失败:', error)
  }
}

const sendAnnouncement = async () => {
  if (!form.title || !form.message) {
    ElMessage.warning('请填写完整信息')
    return
  }

  try {
    sendLoading.value = true
    await notificationAPI.sendAnnouncement({
      title: form.title,
      message: form.message,
      announcementType: form.announcementType
    })
    ElMessage.success('公告发送成功')
    form.title = ''
    form.message = ''
    loadAnnouncements()
  } catch (error) {
    console.error('发送公告失败:', error)
    ElMessage.error('发送公告失败')
  } finally {
    sendLoading.value = false
  }
}

const deleteAnnouncement = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条公告吗？', '确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await notificationAPI.deleteAnnouncement(id)
    ElMessage.success('删除成功')
    loadAnnouncements()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除公告失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  loadAnnouncements()
})
</script>

<style scoped>
.announcement-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.send-card {
  margin-bottom: 20px;
}

.list-card {
  border-radius: 8px;
}
</style>
