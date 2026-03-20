<template>
  <div class="my-experiences">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <h1>民宿预订系统</h1>
          </div>
          <div class="nav">
            <el-link @click="goHome">首页</el-link>
            <el-link @click="goToHomestays">民宿列表</el-link>
            <el-link @click="goToLandlord">房东中心</el-link>
            <el-link @click="goToUser">个人中心</el-link>
            <el-link @click="handleLogout">退出</el-link>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <div class="page-header">
          <h2>我的体验项目</h2>
          <el-button type="primary" @click="goToCreateExperience">创建体验项目</el-button>
        </div>
        
        <el-card class="experience-list-card">
          <template #header>
            <div class="card-header">
              <h3>体验项目列表</h3>
              <span class="card-tip">管理您发布的乡村特色体验项目</span>
            </div>
          </template>
          
          <el-table :data="experiences" style="width: 100%">
            <el-table-column prop="name" label="体验名称" width="200">
              <template #default="scope">
                <div class="experience-name">{{ scope.row.name }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="体验类型" width="120"></el-table-column>
            <el-table-column prop="price" label="价格" width="100">
              <template #default="scope">
                ¥{{ scope.row.price }}
              </template>
            </el-table-column>
            <el-table-column prop="duration" label="时长(分钟)" width="100"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="goToEditExperience(scope.row.id)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteExperience(scope.row.id)">删除</el-button>
                <el-button size="small" @click="toggleStatus(scope.row)">
                  {{ scope.row.status === 1 ? '下架' : '上架' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div v-if="experiences.length === 0" class="empty-state">
            <el-empty description="暂无体验项目" />
            <el-button type="primary" @click="goToCreateExperience" style="margin-top: 20px">
              创建体验项目
            </el-button>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { experienceAPI } from '@/api/experience'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const experiences = ref([])

// 组件挂载时获取体验项目列表
onMounted(() => {
  getExperienceList()
})

// 获取体验项目列表
const getExperienceList = async () => {
  try {
    const result = await experienceAPI.getLandlordList()
    // 响应拦截器直接返回了data，所以result就是data
    if (Array.isArray(result)) {
      experiences.value = result
    }
  } catch (error) {
    console.error('获取体验项目列表失败:', error)
    ElMessage.error('获取体验项目列表失败')
  }
}

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case 1:
      return 'success'
    case 0:
      return 'warning'
    case 2:
      return 'info'
    default:
      return ''
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 1:
      return '上架'
    case 0:
      return '下架'
    case 2:
      return '待审核'
    default:
      return '未知'
  }
}

// 跳转到创建体验项目页面
const goToCreateExperience = () => {
  router.push('/landlord/experiences/create')
}

// 跳转到编辑体验项目页面
const goToEditExperience = (id) => {
  router.push(`/landlord/experiences/${id}/edit`)
}

// 删除体验项目
const deleteExperience = (id) => {
  ElMessageBox.confirm('确定要删除这个体验项目吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await experienceAPI.delete(id)
      // 响应拦截器会在成功时直接返回data，失败时抛出错误
      ElMessage.success('删除成功')
      getExperienceList()
    } catch (error) {
      console.error('删除体验项目失败:', error)
      ElMessage.error('删除体验项目失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 切换体验项目状态
const toggleStatus = async (experience) => {
  try {
    const newStatus = experience.status === 1 ? 0 : 1
    await experienceAPI.updateStatus(experience.id, newStatus)
    // 响应拦截器会在成功时直接返回data，失败时抛出错误
    ElMessage.success(newStatus === 1 ? '上架成功' : '下架成功')
    getExperienceList()
  } catch (error) {
    console.error('更新状态失败:', error)
    ElMessage.error('更新状态失败')
  }
}

// 导航方法
const goBack = () => {
  router.back()
}

const goHome = () => {
  router.push('/')
}

const goToHomestays = () => {
  router.push('/homestays')
}

const goToLandlord = () => {
  router.push('/landlord')
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
.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
}

.logo {
  cursor: pointer;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  color: #667eea;
}

.nav {
  display: flex;
  gap: 20px;
}

.nav .el-link {
  font-size: 16px;
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

.experience-list-card {
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

.experience-name {
  font-weight: 500;
  color: #333;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
}

/* 优化整体UI */
.experience-list-card {
  margin-bottom: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.experience-list-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

/* 优化按钮样式 */
.el-button {
  transition: all 0.3s ease;
}

.el-button:hover {
  transform: translateY(-1px);
}
</style>
