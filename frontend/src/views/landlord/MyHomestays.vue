<template>
  <div class="my-homestays">
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
          <h2>我的民宿</h2>
          <el-button type="primary" @click="goToCreate">
            <el-icon><Plus /></el-icon>
            发布民宿
          </el-button>
        </div>
        
        <el-table :data="homestays" v-loading="loading" style="width: 100%">
          <el-table-column prop="name" label="民宿名称" />
          <el-table-column prop="address" label="地址" />
          <el-table-column prop="roomType" label="房型" width="120" />
          <el-table-column prop="price" label="价格" width="120">
            <template #default="{ row }">
              ¥{{ formatPrice(row.price) }}/晚
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { homestayAPI } from '@/api/homestay'
import { formatPrice } from '@/utils'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const homestays = ref([])

const loadHomestays = async () => {
  loading.value = true
  try {
    homestays.value = await homestayAPI.getLandlordList()
  } catch (error) {
    console.error('加载民宿列表失败:', error)
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const map = {
    0: 'info',
    1: 'success',
    2: 'warning'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    0: '下架',
    1: '上架',
    2: '待审核'
  }
  return map[status] || '未知'
}

const handleEdit = (row) => {
  router.push(`/landlord/homestays/${row.id}/edit`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这个民宿吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await homestayAPI.delete(row.id)
    ElMessage.success('删除成功')
    loadHomestays()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const goToCreate = () => {
  router.push('/landlord/homestays/create')
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

onMounted(() => {
  loadHomestays()
})
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
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style>
