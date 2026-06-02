<template>
  <div class="order-management">
    <div class="page-header">
      <h2>订单管理</h2>
    </div>
    
    <el-card class="filter-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="订单类型">
          <el-select v-model="searchForm.orderType" placeholder="请选择类型">
            <el-option label="全部" value="" />
            <el-option label="民宿订单" value="HOMESTAY" />
            <el-option label="体验订单" value="EXPERIENCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="待支付" value="PENDING" />
            <el-option label="已支付" value="PAID" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchOrders">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <el-table :data="orders" style="width: 100%">
        <el-table-column prop="id" label="订单ID" width="100"></el-table-column>
        <el-table-column label="订单类型" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.homestayId ? 'success' : 'warning'">
              {{ scope.row.homestayId ? '民宿订单' : '体验订单' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="商品信息" width="200">
          <template #default="scope">
            <div v-if="scope.row.homestayName">
              {{ scope.row.homestayName }}
            </div>
            <div v-else>
              体验项目
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="订单金额" width="120">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="guestCount" label="人数" width="100"></el-table-column>
        <el-table-column prop="guestName" label="用户名" width="120"></el-table-column>
        <el-table-column prop="guestPhone" label="联系方式" width="130"></el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <div class="action-buttons">
              <el-button size="small" @click="viewOrderDetail(scope.row)">详情</el-button>
              <el-button 
                v-if="scope.row.status === 'PENDING'" 
                size="small" 
                type="danger" 
                @click="cancelOrder(scope.row)"
              >
                取消订单
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        :current-page="pagination.current"
        :page-size="pagination.size"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, prev, pager, next, jumper"
      />
    </el-card>
    
    <el-dialog
      v-model="detailDialogVisible"
      title="订单详情"
      width="600px"
    >
      <el-form :model="currentOrder" label-width="120px">
        <el-form-item label="订单ID">
          {{ currentOrder.id }}
        </el-form-item>
        <el-form-item label="订单类型">
          {{ currentOrder.homestayId ? '民宿订单' : '体验订单' }}
        </el-form-item>
        <el-form-item label="民宿名称">
          {{ currentOrder.homestayName || '-' }}
        </el-form-item>
        <el-form-item label="订单金额">
          ¥{{ currentOrder.price }}
        </el-form-item>
        <el-form-item label="入住人数">
          {{ currentOrder.guestCount || 1 }}
        </el-form-item>
        <el-form-item label="入住日期">
          {{ currentOrder.checkInDate || '-' }}
        </el-form-item>
        <el-form-item label="离店日期">
          {{ currentOrder.checkOutDate || '-' }}
        </el-form-item>
        <el-form-item label="用户名">
          {{ currentOrder.guestName }}
        </el-form-item>
        <el-form-item label="联系方式">
          {{ currentOrder.guestPhone }}
        </el-form-item>
        <el-form-item label="客人邮箱">
          {{ currentOrder.guestEmail || '-' }}
        </el-form-item>
        <el-form-item label="订单状态">
          {{ getStatusText(currentOrder.status) }}
        </el-form-item>
        <el-form-item label="创建时间">
          {{ formatTime(currentOrder.createTime) }}
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { orderAPI } from '@/api/order';
import { ElMessage, ElMessageBox } from 'element-plus';

const orders = ref([]);
const searchForm = reactive({
  orderType: '',
  status: ''
});
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
});
const detailDialogVisible = ref(false);
const currentOrder = ref({});

const loadOrders = async () => {
  try {
    const params = {
      page: pagination.value.current,
      size: pagination.value.size
    };
    if (searchForm.orderType && searchForm.orderType !== '') {
      params.orderType = searchForm.orderType;
    }
    if (searchForm.status && searchForm.status !== '') {
      params.status = searchForm.status;
    }
    
    const result = await orderAPI.adminList(params);
    if (result && result.orders) {
      orders.value = result.orders;
      pagination.value.total = result.total || 0;
    } else {
      orders.value = [];
      pagination.value.total = 0;
    }
  } catch (error) {
    console.error('加载订单列表失败:', error);
    ElMessage.error('加载订单列表失败');
    orders.value = [];
    pagination.value.total = 0;
  }
};

const searchOrders = () => {
  pagination.value.current = 1;
  loadOrders();
};

const resetSearch = () => {
  searchForm.orderType = '';
  searchForm.status = '';
  pagination.value.current = 1;
  loadOrders();
};

const handleSizeChange = (size) => {
  pagination.value.size = size;
  pagination.value.current = 1;
  loadOrders();
};

const handleCurrentChange = (current) => {
  pagination.value.current = current;
  loadOrders();
};

const getStatusType = (status) => {
  switch (status) {
    case 'PENDING':
      return 'warning';
    case 'PAID':
      return 'primary';
    case 'COMPLETED':
      return 'success';
    case 'CANCELLED':
      return 'danger';
    default:
      return 'info';
  }
};

const getStatusText = (status) => {
  switch (status) {
    case 'PENDING':
      return '待支付';
    case 'PAID':
      return '已支付';
    case 'COMPLETED':
      return '已完成';
    case 'CANCELLED':
      return '已取消';
    default:
      return '未知';
  }
};

const formatTime = (time) => {
  if (!time)
    return '-';
  return new Date(time).toLocaleString('zh-CN');
};

const viewOrderDetail = async (order) => {
  try {
    const result = await orderAPI.adminDetail(order.id);
    if (result) {
      currentOrder.value = result;
      detailDialogVisible.value = true;
    }
  } catch (error) {
    console.error('获取订单详情失败:', error);
    ElMessage.error('获取订单详情失败');
  }
};

const cancelOrder = async (order) => {
  try {
    await ElMessageBox.confirm(
      '确定要取消该订单吗？',
      '确认',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    );
    await orderAPI.cancel(order.id);
    ElMessage.success('订单已取消');
    loadOrders();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消订单失败:', error);
      ElMessage.error('取消订单失败');
    }
  }
};

onMounted(() => {
  loadOrders();
});
</script>

<style scoped>
.order-management {
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

.filter-card {
  margin-bottom: 20px;
}

.table-card {
  border-radius: 8px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
  align-items: center;
}

.action-buttons .el-button {
  flex-shrink: 0;
}
</style>