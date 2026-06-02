<template>
  <div class="admin-dashboard">
    <div class="page-header">
      <h2>数据概览</h2>
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
          <h3>{{ pendingHomestaysCount }}</h3>
          <p>待审核民宿</p>
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
    
    <!-- 待审核民宿 -->
    <el-card class="section-card">
      <template #header>
        <div class="card-header">
          <h3>待审核民宿</h3>
          <span class="card-tip">审核民宿信息，确保内容符合平台规范</span>
        </div>
      </template>
      
      <div v-if="pendingHomestays.length > 0" class="homestays-list">
        <el-table :data="pendingHomestays" style="width: 100%">
          <el-table-column prop="name" label="民宿名称" width="200"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
          <el-table-column prop="roomType" label="房型" width="120"></el-table-column>
          <el-table-column prop="price" label="价格" width="100">
            <template #default="scope">
              ¥{{ scope.row.price }}/晚
            </template>
          </el-table-column>
          <el-table-column prop="ownerId" label="房东ID" width="100"></el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" type="primary" @click="approveHomestay(scope.row)">通过</el-button>
              <el-button size="small" type="danger" @click="rejectHomestay(scope.row)">驳回</el-button>
              <el-button size="small" @click="viewHomestayDetail(scope.row.id)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-else class="empty-state">
        <el-empty description="暂无待审核民宿" />
      </div>
    </el-card>
    
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
import { homestayAPI } from '@/api/homestay'
import { dashboardAPI } from '@/api/dashboard'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const pendingExperiences = ref([])
const pendingExperiencesCount = ref(0)
const pendingHomestays = ref([])
const pendingHomestaysCount = ref(0)
const totalExperiencesCount = ref(0)
const totalHomestaysCount = ref(0)
const totalUsersCount = ref(0)

const rejectDialogVisible = ref(false)
const rejectForm = ref({
  reason: ''
})
const currentItem = ref(null)
const currentType = ref('experience')

const loadData = async () => {
  try {
    const pendingExperienceResult = await experienceAPI.getPending()
    if (Array.isArray(pendingExperienceResult)) {
      pendingExperiences.value = pendingExperienceResult
      pendingExperiencesCount.value = pendingExperienceResult.length
    }
    
    const pendingHomestayResult = await homestayAPI.getPending()
    if (Array.isArray(pendingHomestayResult)) {
      pendingHomestays.value = pendingHomestayResult
      pendingHomestaysCount.value = pendingHomestayResult.length
    }
    
    const stats = await dashboardAPI.getStats()
    if (stats) {
      totalExperiencesCount.value = stats.totalExperiences || 0
      totalHomestaysCount.value = stats.totalHomestays || 0
      totalUsersCount.value = stats.totalUsers || 0
    }
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

const refreshData = () => {
  loadData()
}

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

const approveHomestay = async (homestay) => {
  try {
    await homestayAPI.review(homestay.id, 1, '')
    ElMessage.success('审核通过')
    loadData()
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败')
  }
}

const rejectExperience = (experience) => {
  currentItem.value = experience
  currentType.value = 'experience'
  rejectForm.value.reason = ''
  rejectDialogVisible.value = true
}

const rejectHomestay = (homestay) => {
  currentItem.value = homestay
  currentType.value = 'homestay'
  rejectForm.value.reason = ''
  rejectDialogVisible.value = true
}

const confirmReject = async () => {
  if (!rejectForm.value.reason) {
    ElMessage.warning('请输入驳回原因')
    return
  }
  
  try {
    if (currentType.value === 'experience') {
      await experienceAPI.review(currentItem.value.id, 3, rejectForm.value.reason)
    } else if (currentType.value === 'homestay') {
      await homestayAPI.review(currentItem.value.id, 3, rejectForm.value.reason)
    }
    ElMessage.success('已驳回')
    rejectDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('驳回失败:', error)
    ElMessage.error('驳回失败')
  }
}

const viewExperienceDetail = (id) => {
  router.push(`/feature/experience/${id}`)
}

const viewHomestayDetail = (id) => {
  router.push(`/homestays/${id}`)
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stats-section {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.stat-card {
  flex: 1;
  min-width: 150px;
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
</style>