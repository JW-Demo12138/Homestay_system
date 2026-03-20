<template>
  <div class="landlord-orders">
    <!-- 顶部导航 -->
    <header class="navbar">
      <div class="navbar-container">
        <div class="logo" @click="goHome">
          <h1>民宿管理</h1>
        </div>
        <nav class="nav-links">
          <a href="#" @click="goHome">首页</a>
          <a href="#" @click="goToHomestays">民宿列表</a>
          <a href="#" @click="goToLandlord" class="active">房东中心</a>
          <a href="#" @click="goToMyOrders" class="active">我的订单</a>
        </nav>
        <div class="user-actions">
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
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>
    
    <main class="main-content">
      <div class="page-header">
        <h1>我的订单</h1>
        <p>管理您的民宿订单</p>
      </div>
      
      <!-- 筛选条件 -->
      <div class="filter-section">
        <el-card class="filter-card">
          <el-form :inline="true" :model="filterForm" class="filter-form">
            <el-form-item label="订单状态">
              <el-select v-model="filterForm.status" placeholder="全部状态" clearable>
                <el-option label="全部" value="" />
                <el-option label="待支付" value="PENDING" />
                <el-option label="已支付" value="PAID" />
                <el-option label="已完成" value="COMPLETED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
            <el-form-item label="民宿名称">
              <el-input v-model="filterForm.homestayName" placeholder="输入民宿名称" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadOrders">查询</el-button>
              <el-button @click="resetFilter">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
      
      <!-- 订单列表 -->
      <div class="orders-section">
        <el-card class="orders-card">
          <template #header>
            <div class="card-header">
              <h3>订单列表</h3>
              <span class="order-count">共 {{ orders.length }} 个订单</span>
            </div>
          </template>
          
          <el-table v-loading="loading" :data="orders" style="width: 100%">
            <el-table-column prop="id" label="订单编号" width="120" />
            <el-table-column label="民宿信息" min-width="300">
              <template #default="scope">
                <div class="homestay-info">
                  <img :src="getImageUrl(scope.row.homestayImage)" class="homestay-image" />
                  <div class="homestay-details">
                    <h4>{{ scope.row.homestayName }}</h4>
                    <p>{{ scope.row.homestayAddress }}</p>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="checkInDate" label="入住日期" width="120" />
            <el-table-column prop="checkOutDate" label="退房日期" width="120" />
            <el-table-column prop="price" label="价格" width="100">
              <template #default="scope">
                ¥{{ scope.row.price }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" width="180">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="viewOrder(scope.row)">查看</el-button>
                <el-button 
                  size="small" 
                  type="primary" 
                  @click="updateOrderStatus(scope.row, 'COMPLETED')"
                  v-if="scope.row.status === 'PAID'"
                >
                  完成
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <!-- 分页 -->
          <div class="pagination" v-if="total > 0">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
          
          <!-- 空状态 -->
          <div v-if="!loading && orders.length === 0" class="empty-state">
            <el-icon class="empty-icon"><Document /></el-icon>
            <p>暂无订单</p>
          </div>
        </el-card>
      </div>
    </main>
    
    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="orderDialogVisible"
      title="订单详情"
      width="600px"
      center
    >
      <div v-if="currentOrder" class="order-detail">
        <!-- 订单基本信息 -->
        <div class="detail-section">
          <h3>订单信息</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="订单编号">{{ currentOrder.id }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDate(currentOrder.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="订单状态">
              <el-tag :type="getStatusType(currentOrder.status)">
                {{ getStatusText(currentOrder.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="订单金额">¥{{ currentOrder.price }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <!-- 民宿信息 -->
        <div class="detail-section">
          <h3>民宿信息</h3>
          <div class="homestay-detail-info">
            <img :src="getImageUrl(currentOrder.homestayImage)" class="homestay-detail-image" />
            <div class="homestay-detail-details">
              <h4>{{ currentOrder.homestayName }}</h4>
              <p>{{ currentOrder.homestayAddress }}</p>
            </div>
          </div>
        </div>
        
        <!-- 预订信息 -->
        <div class="detail-section">
          <h3>预订信息</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="入住日期">{{ currentOrder.checkInDate }}</el-descriptions-item>
            <el-descriptions-item label="退房日期">{{ currentOrder.checkOutDate }}</el-descriptions-item>
            <el-descriptions-item label="预订人数">{{ currentOrder.guestCount || '未知' }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <!-- 顾客信息 -->
        <div class="detail-section">
          <h3>顾客信息</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="顾客姓名">{{ currentOrder.guestName || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentOrder.guestPhone || '未知' }}</el-descriptions-item>
            <el-descriptions-item label="顾客邮箱">{{ currentOrder.guestEmail || '未知' }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <!-- 特殊要求 -->
        <div class="detail-section" v-if="currentOrder.specialRequests">
          <h3>特殊要求</h3>
          <p class="special-requests">{{ currentOrder.specialRequests }}</p>
        </div>
      </div>
      <div v-else class="loading-state">
        <el-icon class="loading-icon"><Loading /></el-icon>
        <p>加载中...</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="orderDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { orderAPI } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown, Document, House, Money, Timer, Loading } from '@element-plus/icons-vue'
import { getImageUrl } from '@/utils'

const router = useRouter()
const userStore = useUserStore()

// 订单数据
const orders = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 订单详情对话框
const orderDialogVisible = ref(false)
const currentOrder = ref(null)

// 筛选条件
const filterForm = ref({
  status: '',
  homestayName: ''
})

// 加载订单数据
const loadOrders = async () => {
  loading.value = true
  try {
    const response = await orderAPI.getLandlordOrders({
      page: currentPage.value,
      status: filterForm.value.status,
      homestayName: filterForm.value.homestayName
    })
    
    // 处理响应数据
    if (response && response.orders) {
      orders.value = response.orders
      total.value = response.total || 0
    } else if (Array.isArray(response)) {
      orders.value = response
      total.value = response.length
    } else if (response.data) {
      orders.value = response.data.orders || response.data
      total.value = response.data.total || 0
    } else {
      orders.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('加载订单失败:', error)
    ElMessage.error('加载订单失败')
  } finally {
    loading.value = false
  }
}

// 重置筛选条件
const resetFilter = () => {
  filterForm.value = {
    status: '',
    homestayName: ''
  }
  loadOrders()
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadOrders()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadOrders()
}

// 查看订单详情
const viewOrder = async (order) => {
  try {
    // 调用订单详情接口获取完整信息
    const detailResponse = await orderAPI.getDetail(order.id)
    const orderDetail = detailResponse.data || detailResponse
    
    // 确保民宿信息存在，如果不存在则使用列表中的信息
    if (!orderDetail.homestayName || orderDetail.homestayName === '未知民宿') {
      orderDetail.homestayName = order.homestayName
      orderDetail.homestayAddress = order.homestayAddress
      orderDetail.homestayImage = order.homestayImage
    }
    
    currentOrder.value = orderDetail
    orderDialogVisible.value = true
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
  }
}

// 更新订单状态
const updateOrderStatus = async (order, status) => {
  try {
    await orderAPI.updateStatus(order.id, { status })
    ElMessage.success('订单状态更新成功')
    loadOrders()
  } catch (error) {
    console.error('更新订单状态失败:', error)
    ElMessage.error('更新订单状态失败')
  }
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    'PENDING': 'warning',
    'PAID': 'success',
    'COMPLETED': 'info',
    'CANCELLED': 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    'PENDING': '待支付',
    'PAID': '已支付',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return textMap[status] || status
}

// 导航方法
const goHome = () => {
  router.push('/')
}

const goToHomestays = () => {
  router.push('/homestays')
}

const goToLandlord = () => {
  router.push('/landlord')
}

const goToMyOrders = () => {
  router.push('/landlord/orders')
}

const goToUser = () => {
  router.push('/user')
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/')
}

// 格式化时间
const formatDate = (dateString) => {
  if (!dateString) return '未知时间'
  const date = new Date(dateString)
  if (isNaN(date.getTime())) return '未知时间'
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 组件挂载时加载数据
onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
/* 导航栏样式 */
.navbar {
  background: white;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 32px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #ff385c;
  cursor: pointer;
}

.nav-links {
  display: flex;
  gap: 32px;
}

.nav-links a {
  text-decoration: none;
  color: #333;
  font-weight: 500;
  transition: all 0.3s ease;
  padding: 8px 0;
  position: relative;
}

.nav-links a:hover,
.nav-links a.active {
  color: #ff385c;
}

.nav-links a.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: #ff385c;
  border-radius: 2px;
}

.user-actions {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.user-dropdown:hover {
  background-color: #fff1f4;
}

.username {
  font-size: 14px;
  font-weight: 500;
}

/* 主内容区 */
.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 32px;
}

.page-header {
  margin-bottom: 40px;
}

.page-header h1 {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.page-header p {
  font-size: 16px;
  color: #666;
  margin: 0;
}

/* 筛选区 */
.filter-section {
  margin-bottom: 30px;
}

.filter-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.filter-form {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 订单列表 */
.orders-section {
  margin-bottom: 30px;
}

.orders-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.order-count {
  font-size: 14px;
  color: #666;
}

/* 民宿信息 */
.homestay-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.homestay-image {
  width: 60px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
}

.homestay-details {
  flex: 1;
}

.homestay-details h4 {
  margin: 0 0 4px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.homestay-details p {
  margin: 0;
  font-size: 12px;
  color: #666;
  line-height: 1.4;
}

/* 分页 */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navbar-container {
    padding: 16px 24px;
  }
  
  .nav-links {
    display: none;
  }
  
  .main-content {
    padding: 32px 24px;
  }
  
  .filter-form {
    flex-direction: column;
    align-items: stretch;
  }
  
  .homestay-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .homestay-image {
    width: 100%;
    height: 80px;
  }
}

/* 订单详情样式 */
.order-detail {
  padding: 10px 0;
}

.detail-section {
  margin-bottom: 24px;
}

.detail-section h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.homestay-detail-info {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.homestay-detail-image {
  width: 120px;
  height: 80px;
  object-fit: cover;
  border-radius: 6px;
}

.homestay-detail-details {
  flex: 1;
}

.homestay-detail-details h4 {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.homestay-detail-details p {
  margin: 0;
  font-size: 14px;
  color: #666;
  line-height: 1.4;
}

.special-requests {
  padding: 12px;
  background-color: #f9f9f9;
  border-radius: 6px;
  font-size: 14px;
  line-height: 1.5;
  color: #333;
  margin: 0;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
  color: #999;
}

.loading-icon {
  font-size: 32px;
  margin-bottom: 16px;
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 响应式设计 - 订单详情 */
@media (max-width: 768px) {
  .homestay-detail-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .homestay-detail-image {
    width: 100%;
    height: 120px;
  }
}
</style>
