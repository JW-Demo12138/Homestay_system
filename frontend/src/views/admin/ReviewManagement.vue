<template>
  <div class="review-management">
    <div class="page-header">
      <h2>评价管理</h2>
    </div>
    
    <el-card class="filter-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="评价内容">
          <el-input v-model="searchForm.keyword" placeholder="请输入评价内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchReviews">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <el-table :data="reviews" style="width: 100%">
        <el-table-column prop="ratingId" label="评价ID" width="100"></el-table-column>
        <el-table-column prop="homestayId" label="民宿ID" width="100"></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
        <el-table-column prop="rating" label="评分" width="100">
          <template #default="scope">
            <div class="rating-stars">
              <el-icon v-for="i in 5" :key="i" class="star" :class="{ active: i <= scope.row.rating }">
                <Star />
              </el-icon>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="comment" label="评价内容" width="350">
          <template #default="scope">
            <span :title="scope.row.comment">{{ scope.row.comment && scope.row.comment.slice(0, 50) }}{{ scope.row.comment && scope.row.comment.length > 50 ? '...' : '' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评价时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <div class="action-buttons">
              <el-button size="small" @click="viewReviewDetail(scope.row)">详情</el-button>
              <el-button size="small" type="danger" @click="deleteReview(scope.row)">删除</el-button>
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
      title="评价详情"
      width="500px"
    >
      <el-form :model="currentReview" label-width="100px">
        <el-form-item label="评价ID">
          {{ currentReview.ratingId }}
        </el-form-item>
        <el-form-item label="民宿ID">
          {{ currentReview.homestayId }}
        </el-form-item>
        <el-form-item label="用户ID">
          {{ currentReview.userId }}
        </el-form-item>
        <el-form-item label="评分">
          <div class="rating-stars">
            <el-icon v-for="i in 5" :key="i" class="star" :class="{ active: i <= currentReview.rating }">
              <Star />
            </el-icon>
          </div>
        </el-form-item>
        <el-form-item label="评价内容">
          {{ currentReview.comment }}
        </el-form-item>
        <el-form-item label="评价时间">
          {{ formatTime(currentReview.createTime) }}
        </el-form-item>
        <el-form-item label="更新时间">
          {{ formatTime(currentReview.updateTime) }}
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
import { ratingAPI } from '@/api/rating';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Star } from '@element-plus/icons-vue';

const reviews = ref([]);
const searchForm = reactive({
  keyword: ''
});
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
});
const detailDialogVisible = ref(false);
const currentReview = ref({});

const loadReviews = async () => {
  try {
    const params = {
      page: pagination.value.current,
      size: pagination.value.size
    };
    if (searchForm.keyword && searchForm.keyword.trim() !== '') {
      params.keyword = searchForm.keyword.trim();
    }
    
    const result = await ratingAPI.adminList(params);
    if (result && result.records) {
      reviews.value = result.records;
      pagination.value.total = result.total || 0;
    } else {
      reviews.value = [];
      pagination.value.total = 0;
    }
  } catch (error) {
    console.error('加载评价列表失败:', error);
    ElMessage.error('加载评价列表失败');
    reviews.value = [];
    pagination.value.total = 0;
  }
};

const searchReviews = () => {
  pagination.value.current = 1;
  loadReviews();
};

const resetSearch = () => {
  searchForm.keyword = '';
  pagination.value.current = 1;
  loadReviews();
};

const handleSizeChange = (size) => {
  pagination.value.size = size;
  pagination.value.current = 1;
  loadReviews();
};

const handleCurrentChange = (current) => {
  pagination.value.current = current;
  loadReviews();
};

const formatTime = (time) => {
  if (!time)
    return '-';
  return new Date(time).toLocaleString('zh-CN');
};

const viewReviewDetail = (review) => {
  currentReview.value = review;
  detailDialogVisible.value = true;
};

const deleteReview = async (review) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评价吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });
    await ratingAPI.delete(review.ratingId);
    ElMessage.success('删除成功');
    loadReviews();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评价失败:', error);
      ElMessage.error('删除评价失败');
    }
  }
};

onMounted(() => {
  loadReviews();
});
</script>

<style scoped>
.review-management {
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

.rating-stars {
  display: flex;
  gap: 4px;
}

.rating-stars .star {
  color: #ddd;
}

.rating-stars .star.active {
  color: #f5a623;
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