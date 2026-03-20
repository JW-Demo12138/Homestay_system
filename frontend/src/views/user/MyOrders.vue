<template>
  <div class="user-orders">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <h1>民宿预订系统</h1>
          </div>
          <div class="nav">
            <el-link @click="goHome">首页</el-link>
            <el-link @click="goToHomestays">民宿列表</el-link>
            <template v-if="userStore.isLandlord">
              <el-link @click="goToLandlord">房东中心</el-link>
            </template>
            <el-link @click="goToUser">个人中心</el-link>
            <el-link @click="handleLogout">退出</el-link>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <div class="page-header">
          <h2>我的订单</h2>
        </div>
        
        <el-tabs v-model="activeTab">
          <el-tab-pane label="全部订单" name="all">
            <div v-loading="loading">
              <el-empty v-if="allOrders.length === 0" description="暂无订单" />
              <el-table v-else :data="allOrders" style="width: 100%">
                <el-table-column prop="homestayName" label="民宿名称" />
                <el-table-column prop="homestayAddress" label="民宿地址" />
                <el-table-column prop="checkInDate" label="入住日期" width="120" />
                <el-table-column prop="checkOutDate" label="退房日期" width="120" />
                <el-table-column prop="price" label="价格" width="120">
                  <template #default="{ row }">
                    ¥{{ formatPrice(row.price) }}
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getStatusType(row.status)">
                      {{ ORDER_STATUS_MAP[row.status] }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="200">
                  <template #default="{ row }">
                    <el-button size="small" @click="handlePay(row)" v-if="row.status === 'PENDING'">
                      支付
                    </el-button>
                    <el-button size="small" type="danger" @click="handleCancel(row)" v-if="row.status === 'PENDING'">
                      取消
                    </el-button>
                    <el-button size="small" @click="handleComment(row)" v-if="row.status === 'COMPLETED'">
                      评价
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="待支付" name="pending">
            <div v-loading="loading">
              <el-empty v-if="pendingOrders.length === 0" description="暂无订单" />
              <el-table v-else :data="pendingOrders" style="width: 100%">
                <el-table-column prop="homestayName" label="民宿名称" />
                <el-table-column prop="homestayAddress" label="民宿地址" />
                <el-table-column prop="checkInDate" label="入住日期" width="120" />
                <el-table-column prop="checkOutDate" label="退房日期" width="120" />
                <el-table-column prop="price" label="价格" width="120">
                  <template #default="{ row }">
                    ¥{{ formatPrice(row.price) }}
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getStatusType(row.status)">
                      {{ ORDER_STATUS_MAP[row.status] }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="200">
                  <template #default="{ row }">
                    <el-button size="small" @click="handlePay(row)" v-if="row.status === 'PENDING'">
                      支付
                    </el-button>
                    <el-button size="small" type="danger" @click="handleCancel(row)" v-if="row.status === 'PENDING'">
                      取消
                    </el-button>
                    <el-button size="small" @click="handleComment(row)" v-if="row.status === 'COMPLETED'">
                      评价
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="已支付" name="paid">
            <div v-loading="loading">
              <el-empty v-if="paidOrders.length === 0" description="暂无订单" />
              <el-table v-else :data="paidOrders" style="width: 100%">
                <el-table-column prop="homestayName" label="民宿名称" />
                <el-table-column prop="homestayAddress" label="民宿地址" />
                <el-table-column prop="checkInDate" label="入住日期" width="120" />
                <el-table-column prop="checkOutDate" label="退房日期" width="120" />
                <el-table-column prop="price" label="价格" width="120">
                  <template #default="{ row }">
                    ¥{{ formatPrice(row.price) }}
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getStatusType(row.status)">
                      {{ ORDER_STATUS_MAP[row.status] }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="200">
                  <template #default="{ row }">
                    <el-button size="small" @click="handlePay(row)" v-if="row.status === 'PENDING'">
                      支付
                    </el-button>
                    <el-button size="small" type="danger" @click="handleCancel(row)" v-if="row.status === 'PENDING'">
                      取消
                    </el-button>
                    <el-button size="small" @click="handleComment(row)" v-if="row.status === 'COMPLETED'">
                      评价
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="已完成" name="completed">
            <div v-loading="loading">
              <el-empty v-if="completedOrders.length === 0" description="暂无订单" />
              <el-table v-else :data="completedOrders" style="width: 100%">
                <el-table-column prop="homestayName" label="民宿名称" />
                <el-table-column prop="homestayAddress" label="民宿地址" />
                <el-table-column prop="checkInDate" label="入住日期" width="120" />
                <el-table-column prop="checkOutDate" label="退房日期" width="120" />
                <el-table-column prop="price" label="价格" width="120">
                  <template #default="{ row }">
                    ¥{{ formatPrice(row.price) }}
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getStatusType(row.status)">
                      {{ ORDER_STATUS_MAP[row.status] }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="200">
                  <template #default="{ row }">
                    <el-button size="small" @click="handlePay(row)" v-if="row.status === 'PENDING'">
                      支付
                    </el-button>
                    <el-button size="small" type="danger" @click="handleCancel(row)" v-if="row.status === 'PENDING'">
                      取消
                    </el-button>
                    <el-button size="small" @click="handleComment(row)" v-if="row.status === 'COMPLETED'">
                      评价
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
    
    <!-- 评价对话框 -->
    <el-dialog
      v-model="commentDialogVisible"
      title="评价订单"
      width="500px"
      center
    >
      <el-form :model="commentForm" :rules="commentRules" ref="commentFormRef" label-width="80px">
        <el-form-item label="评分">
          <el-rate v-model="commentForm.rating" :max="5" show-score score-template="{{ value }}" />
        </el-form-item>
        <el-form-item label="评价内容" prop="content">
          <el-input
            v-model="commentForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入您的评价"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="commentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitComment" :loading="commentLoading">提交评价</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { orderAPI } from '@/api/order'
import { ratingAPI } from '@/api/rating'
import { homestayAPI } from '@/api/homestay'
import { formatPrice } from '@/utils'
import { ORDER_STATUS_MAP } from '@/config'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('all')
const orders = ref([])
const loading = ref(false)

// 评价相关
const commentDialogVisible = ref(false)
const commentFormRef = ref(null)
const commentLoading = ref(false)
const currentOrder = ref(null)
const commentForm = reactive({
  rating: 5,
  content: ''
})
const commentRules = {
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' }
  ]
}

const allOrders = computed(() => orders.value)
const pendingOrders = computed(() => orders.value.filter(o => o.status === 'PENDING'))
const paidOrders = computed(() => orders.value.filter(o => o.status === 'PAID'))
const completedOrders = computed(() => orders.value.filter(o => o.status === 'COMPLETED'))

const loadOrders = async () => {
  loading.value = true
  try {
    // 直接使用后端返回的订单数据
    const response = await orderAPI.getUserOrders()
    console.log('订单API响应:', response)
    
    // 检查响应结构
    const orderList = Array.isArray(response) ? response : response.data || []
    console.log('处理后的订单列表:', orderList)
    
    // 检查订单数据结构
    if (orderList && orderList.length > 0) {
      console.log('第一个订单详情:', orderList[0])
      // 检查订单是否有homestayId或homestay_id字段
      console.log('订单是否有homestayId:', 'homestayId' in orderList[0])
      console.log('订单是否有homestay_id:', 'homestay_id' in orderList[0])
      console.log('订单的homestayId:', orderList[0].homestayId)
      console.log('订单的homestay_id:', orderList[0].homestay_id)
    }
    
    orders.value = orderList
  } catch (error) {
    console.error('加载订单列表失败:', error)
  } finally {
    loading.value = false
  }
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

const getStatusType = (status) => {
  const map = {
    'PENDING': 'warning',
    'PAID': 'success',
    'CANCELLED': 'info',
    'COMPLETED': 'success'
  }
  return map[status] || 'info'
}

const handlePay = async (row) => {
  try {
    await orderAPI.pay(row.id, { payMethod: '微信支付' })
    ElMessage.success('支付成功')
    loadOrders()
  } catch (error) {
    console.error('支付失败:', error)
  }
}

const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm('确定要取消这个订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await orderAPI.cancel(row.id)
    ElMessage.success('取消成功')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败:', error)
    }
  }
}

const handleComment = (row) => {
  currentOrder.value = row
  commentForm.rating = 5
  commentForm.content = ''
  commentDialogVisible.value = true
}

const submitComment = async () => {
  const valid = await commentFormRef.value.validate()
  if (!valid) return
  
  commentLoading.value = true
  try {
    // 直接从订单对象中获取民宿ID
    const homestayId = currentOrder.value.homestayId
    
    if (!homestayId) {
      console.error('订单中缺少民宿ID信息:', currentOrder.value)
      ElMessage.error('订单中缺少民宿ID信息')
      return
    }
    
    await ratingAPI.create({
      homestayId: homestayId,
      userId: userStore.userId,
      rating: commentForm.rating,
      comment: commentForm.content
    })
    ElMessage.success('评价成功')
    commentDialogVisible.value = false
    loadOrders()
  } catch (error) {
    console.error('评价失败:', error)
    ElMessage.error('评价失败')
  } finally {
    commentLoading.value = false
  }
}

onMounted(() => {
  loadOrders()
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
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style>
