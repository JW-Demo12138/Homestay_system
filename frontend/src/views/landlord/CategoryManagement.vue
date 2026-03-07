<template>
  <div class="category-management">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <h1>民宿预订系统</h1>
          </div>
          <div class="nav">
            <el-link @click="goHome">首页</el-link>
            <el-link @click="goToHomestays">民宿列表</el-link>
            <template v-if="userStore.isLoggedIn">
              <el-link v-if="userStore.isLandlord" @click="goToLandlord">房东中心</el-link>
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
                    <el-dropdown-item
                      v-if="userStore.isLoggedIn"
                      @click="goToOrders"
                    >
                      我的订单
                    </el-dropdown-item>
                    <el-dropdown-item
                      v-if="userStore.isLandlord"
                      @click="goToLandlord"
                    >
                      房东中心
                    </el-dropdown-item>
                    <el-dropdown-item divided @click="handleLogout">
                      退出登录
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-link @click="goToLogin">登录</el-link>
              <el-link @click="goToRegister">注册</el-link>
            </template>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item @click="goHome">首页</el-breadcrumb-item>
          <el-breadcrumb-item @click="goToLandlord">房东中心</el-breadcrumb-item>
          <el-breadcrumb-item>分类管理</el-breadcrumb-item>
        </el-breadcrumb>
        
        <div class="management-content">
          <h2 class="page-title">分类管理</h2>
          
          <!-- 分类列表 -->
          <div class="category-list">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>分类列表</span>
                  <el-button type="primary" @click="showAddDialog">
                    <el-icon><Plus /></el-icon>
                    添加分类
                  </el-button>
                </div>
              </template>
              
              <el-table :data="categories" style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="name" label="分类名称" />
                <el-table-column prop="description" label="描述" />
                <el-table-column label="操作" width="200" fixed="right">
                  <template #default="scope">
                    <el-button type="primary" size="small" @click="showEditDialog(scope.row)">
                      编辑
                    </el-button>
                    <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </div>
      </el-main>
    </el-container>
    
    <!-- 添加分类弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="添加分类"
      width="500px"
    >
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="分类名称">
          <el-input v-model="addForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类描述">
          <el-input
            v-model="addForm.description"
            type="textarea"
            placeholder="请输入分类描述"
            rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAdd">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 编辑分类弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑分类"
      width="500px"
    >
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="分类名称">
          <el-input v-model="editForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类描述">
          <el-input
            v-model="editForm.description"
            type="textarea"
            placeholder="请输入分类描述"
            rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEdit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getImageUrl } from '@/utils'
import { ElMessage } from 'element-plus'
import { ArrowDown, Plus } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 分类列表
const categories = ref([
  { id: 1, name: '经济型', description: '价格实惠，适合预算有限的用户' },
  { id: 2, name: '舒适型', description: '设施齐全，舒适整洁' },
  { id: 3, name: '豪华型', description: '高端设施，豪华体验' },
  { id: 4, name: '特色型', description: '具有独特风格和特色的民宿' }
])

// 添加分类弹窗
const addDialogVisible = ref(false)
const addForm = reactive({
  name: '',
  description: ''
})

// 编辑分类弹窗
const editDialogVisible = ref(false)
const editForm = reactive({
  id: '',
  name: '',
  description: ''
})

// 显示添加分类弹窗
const showAddDialog = () => {
  // 重置表单
  addForm.name = ''
  addForm.description = ''
  addDialogVisible.value = true
}

// 显示编辑分类弹窗
const showEditDialog = (category) => {
  // 填充表单
  editForm.id = category.id
  editForm.name = category.name
  editForm.description = category.description
  editDialogVisible.value = true
}

// 处理添加分类
const handleAdd = () => {
  // 验证表单
  if (!addForm.name) {
    ElMessage.error('请输入分类名称')
    return
  }
  
  // 模拟添加分类
  const newCategory = {
    id: categories.value.length + 1,
    name: addForm.name,
    description: addForm.description
  }
  
  categories.value.push(newCategory)
  addDialogVisible.value = false
  ElMessage.success('分类添加成功')
}

// 处理编辑分类
const handleEdit = () => {
  // 验证表单
  if (!editForm.name) {
    ElMessage.error('请输入分类名称')
    return
  }
  
  // 模拟编辑分类
  const index = categories.value.findIndex(item => item.id === editForm.id)
  if (index !== -1) {
    categories.value[index] = {
      ...editForm
    }
    editDialogVisible.value = false
    ElMessage.success('分类编辑成功')
  }
}

// 处理删除分类
const handleDelete = (id) => {
  // 模拟删除分类
  const index = categories.value.findIndex(item => item.id === id)
  if (index !== -1) {
    categories.value.splice(index, 1)
    ElMessage.success('分类删除成功')
  }
}

// 导航方法
const goHome = () => {
  router.push('/')
}

const goToHomestays = () => {
  router.push('/homestays')
}

const goToLogin = () => {
  router.push('/login')
}

const goToRegister = () => {
  router.push('/register')
}

const goToUser = () => {
  router.push('/user')
}

const goToLandlord = () => {
  router.push('/landlord')
}

const goToOrders = () => {
  router.push('/user/orders')
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}

onMounted(() => {
  // 页面加载时的初始化操作
  console.log('分类管理页面加载')
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

.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
}

.management-content {
  padding: 0 0 40px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
}

.category-list {
  margin-bottom: 32px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  font-weight: 500;
}

.user-dropdown .el-avatar {
  margin-right: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    padding: 0 12px;
  }
  
  .logo h1 {
    font-size: 20px;
  }
  
  .nav {
    gap: 12px;
  }
  
  .nav .el-link {
    font-size: 14px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .card-header .el-button {
    align-self: flex-end;
  }
}
</style>