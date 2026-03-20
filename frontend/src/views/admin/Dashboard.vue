<template>
  <div class="admin-dashboard">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <h1>民宿预订系统</h1>
          </div>
          <div class="nav">
            <el-link @click="goHome">首页</el-link>
            <el-link @click="goToHomestays">民宿列表</el-link>
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
                  <el-dropdown-item @click="goToUser">
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <div class="page-header">
          <h2>管理员中心</h2>
          <el-button type="primary" @click="refreshData">刷新数据</el-button>
        </div>
        
        <!-- 统计卡片 -->
        <div class="stats-section">
          <el-card class="stat-card">
            <div class="stat-item">
              <h3>{{ pendingExperiencesCount }}</h3>
              <p>待审核体验项目</p>
            </div>
          </el-card>
          <el-card class="stat-card">
            <div class="stat-item">
              <h3>{{ totalExperiencesCount }}</h3>
              <p>总体验项目</p>
            </div>
          </el-card>
          <el-card class="stat-card">
            <div class="stat-item">
              <h3>{{ totalHomestaysCount }}</h3>
              <p>总民宿数量</p>
            </div>
          </el-card>
          <el-card class="stat-card">
            <div class="stat-item">
              <h3>{{ totalUsersCount }}</h3>
              <p>总用户数量</p>
            </div>
          </el-card>
        </div>
        
        <!-- 待审核体验项目 -->
        <el-card class="section-card">
          <template #header>
            <div class="card-header">
              <h3>待审核体验项目</h3>
              <span class="card-tip">审核体验项目，确保内容符合平台规范</span>
            </div>
          </template>
          
          <div v-if="pendingExperiences.length > 0" class="experiences-list">
            <el-table :data="pendingExperiences" style="width: 100%">
              <el-table-column prop="name" label="体验名称" width="200"></el-table-column>
              <el-table-column prop="type" label="体验类型" width="120"></el-table-column>
              <el-table-column prop="price" label="价格" width="100">
                <template #default="scope">
                  ¥{{ scope.row.price }}
                </template>
              </el-table-column>
              <el-table-column prop="duration" label="时长(分钟)" width="100"></el-table-column>
              <el-table-column prop="location" label="体验地点"></el-table-column>
              <el-table-column prop="ownerId" label="房东ID" width="100"></el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="approveExperience(scope.row)">通过</el-button>
                  <el-button size="small" type="danger" @click="rejectExperience(scope.row)">驳回</el-button>
                  <el-button size="small" @click="viewExperienceDetail(scope.row.id)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else class="empty-state">
            <el-empty description="暂无待审核体验项目" />
          </div>
        </el-card>
      </el-main>
    </el-container>
    
    <!-- 驳回原因对话框 -->
    <el-dialog
      v-model="rejectDialogVisible"
      title="驳回原因"
      width="400px"
    >
      <el-form :model="rejectForm">
        <el-form-item label="驳回原因">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入驳回原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmReject">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { experienceAPI } from '@/api/experience'
import { getImageUrl } from '@/utils'
import { ElMessage } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 数据
const pendingExperiences = ref([])
const pendingExperiencesCount = ref(0)
const totalExperiencesCount = ref(0)
const totalHomestaysCount = ref(0)
const totalUsersCount = ref(0)

// 驳回对话框
const rejectDialogVisible = ref(false)
const rejectForm = ref({
  reason: ''
})
const currentExperience = ref(null)

// 加载数据
const loadData = async () => {
  try {
    // 加载待审核体验项目
    const pendingResult = await experienceAPI.getPending()
    if (Array.isArray(pendingResult)) {
      pendingExperiences.value = pendingResult
      pendingExperiencesCount.value = pendingResult.length
    }
    
    // 这里可以添加其他统计数据的加载
    // 例如：总体验项目数、总民宿数、总用户数等
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

// 刷新数据
const refreshData = () => {
  loadData()
}

// 批准体验项目
const approveExperience = async (experience) => {
  try {
    await experienceAPI.review(experience.id, 1, '')
    ElMessage.success('审核通过')
    loadData()
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败')
  }
}

// 驳回体验项目
const rejectExperience = (experience) => {
  currentExperience.value = experience
  rejectForm.value.reason = ''
  rejectDialogVisible.value = true
}

// 确认驳回
const confirmReject = async () => {
  if (!rejectForm.value.reason) {
    ElMessage.warning('请输入驳回原因')
    return
  }
  
  try {
    await experienceAPI.review(currentExperience.value.id, 3, rejectForm.value.reason)
    ElMessage.success('已驳回')
    rejectDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('驳回失败:', error)
    ElMessage.error('驳回失败')
  }
}

// 查看体验项目详情
const viewExperienceDetail = (id) => {
  // 这里可以跳转到体验项目详情页面
  console.log('查看体验项目详情:', id)
}

// 导航方法
const goHome = () => {
  router.push('/')
}

const goToHomestays = () => {
  router.push('/homestays')
}

const goToUser = () => {
  router.push('/user')
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}

// 组件挂载时加载数据
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 80px;
  padding: 0 40px;
}

.logo {
  cursor: pointer;
  font-size: 18px;
  font-weight: bold;
  color: #5e72e4;
  margin-bottom: 10px;
}

.nav {
  display: flex;
  gap: 30px;
}

.nav .el-link {
  font-size: 18px;
  color: #5e72e4;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav .el-link:hover {
  color: #3f51b5;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h2 {
  margin: 0;
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stats-section {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  flex: 1;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stat-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.stat-item {
  text-align: center;
  padding: 20px;
}

.stat-item h3 {
  font-size: 32px;
  font-weight: bold;
  color: #5e72e4;
  margin: 0 0 8px 0;
}

.stat-item p {
  font-size: 14px;
  color: #888;
  margin: 0;
}

.section-card {
  margin-bottom: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.card-tip {
  font-size: 14px;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
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
  font-size: 16px;
  font-weight: 600;
}
</style>