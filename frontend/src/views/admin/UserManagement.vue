<template>
  <div class="user-management">
    <div class="page-header">
      <h2>用户管理</h2>
    </div>
    
    <el-card class="filter-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="请选择角色">
            <el-option label="全部" value="" />
            <el-option label="游客" value="TOURIST" />
            <el-option label="房东" value="LANDLORD" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="正常" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchUsers">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <el-table :data="users" style="width: 100%">
        <el-table-column prop="id" label="用户ID" width="100"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="150"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
        <el-table-column prop="role" label="角色" width="120">
          <template #default="scope">
            <el-tag :type="getRoleType(scope.row.role)">{{ getRoleText(scope.row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <div class="action-buttons">
              <el-button size="small" @click="viewUserDetail(scope.row)">详情</el-button>
              <el-button 
                size="small" 
                :type="scope.row.status === 1 ? 'danger' : 'success'"
                @click="toggleUserStatus(scope.row)"
              >
                {{ scope.row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button size="small" type="warning" @click="resetPassword(scope.row)">重置密码</el-button>
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
      title="用户详情"
      width="500px"
    >
      <el-form :model="currentUser" label-width="100px">
        <el-form-item label="用户ID">
          {{ currentUser.id }}
        </el-form-item>
        <el-form-item label="用户名">
          {{ currentUser.username }}
        </el-form-item>
        <el-form-item label="手机号">
          {{ currentUser.phone }}
        </el-form-item>
        <el-form-item label="邮箱">
          {{ currentUser.email }}
        </el-form-item>
        <el-form-item label="角色">
          {{ getRoleText(currentUser.role) }}
        </el-form-item>
        <el-form-item label="状态">
          {{ currentUser.status === 1 ? '正常' : '禁用' }}
        </el-form-item>
        <el-form-item label="注册时间">
          {{ formatTime(currentUser.createTime) }}
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
    
    <el-dialog
      v-model="resetDialogVisible"
      title="重置密码"
      width="400px"
    >
      <el-form :model="resetForm">
        <el-form-item label="新密码">
          <el-input
            v-model="resetForm.password"
            type="password"
            placeholder="请输入新密码"
          />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input
            v-model="resetForm.confirmPassword"
            type="password"
            placeholder="请确认新密码"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmResetPassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { userAPI } from '@/api/user';
import { ElMessage } from 'element-plus';
const router = useRouter();
const users = ref([]);
const searchForm = reactive({
 username: '',
 role: '',
 status: ''
});
const pagination = ref({
 current: 1,
 size: 10,
 total: 0
});
const detailDialogVisible = ref(false);
const resetDialogVisible = ref(false);
const currentUser = ref({});
const resetForm = ref({
 password: '',
 confirmPassword: ''
});
const loadUsers = async () => {
 try {
 const params = {
 page: pagination.value.current,
 size: pagination.value.size
 };
 if (searchForm.username) {
 params.username = searchForm.username;
 }
 if (searchForm.role) {
 params.role = searchForm.role;
 }
 if (searchForm.status !== '' && searchForm.status !== null && searchForm.status !== undefined) {
 params.status = searchForm.status;
 }
 const result = await userAPI.adminList(params);
 if (result) {
 users.value = result.records || [];
 pagination.value.total = result.total || 0;
 }
 }
 catch (error) {
 console.error('加载用户列表失败:', error);
 ElMessage.error('加载用户列表失败');
 }
};
const searchUsers = () => {
 pagination.value.current = 1;
 loadUsers();
};
const resetSearch = () => {
 searchForm.username = '';
 searchForm.role = '';
 searchForm.status = '';
 pagination.value.current = 1;
 loadUsers();
};
const handleSizeChange = (size) => {
 pagination.value.size = size;
 pagination.value.current = 1;
 loadUsers();
};
const handleCurrentChange = (current) => {
 pagination.value.current = current;
 loadUsers();
};
const getRoleType = (role) => {
 switch (role) {
 case 'ADMIN':
 return 'danger';
 case 'LANDLORD':
 return 'warning';
 default:
 return 'info';
 }
};
const getRoleText = (role) => {
 switch (role) {
 case 'ADMIN':
 return '管理员';
 case 'LANDLORD':
 return '房东';
 default:
 return '游客';
 }
};
const formatTime = (time) => {
 if (!time)
 return '-';
 return new Date(time).toLocaleString('zh-CN');
};
const viewUserDetail = (user) => {
 currentUser.value = user;
 detailDialogVisible.value = true;
};
const toggleUserStatus = async (user) => {
 try {
 await userAPI.updateUserStatus(user.id, { status: user.status === 1 ? 0 : 1 });
 ElMessage.success(user.status === 1 ? '已禁用用户' : '已启用用户');
 loadUsers();
 }
 catch (error) {
 console.error('修改用户状态失败:', error);
 ElMessage.error('修改用户状态失败');
 }
};
const resetPassword = (user) => {
 currentUser.value = user;
 resetForm.value = {
 password: '',
 confirmPassword: ''
 };
 resetDialogVisible.value = true;
};
const confirmResetPassword = async () => {
 if (!resetForm.value.password) {
 ElMessage.warning('请输入新密码');
 return;
 }
 if (resetForm.value.password !== resetForm.value.confirmPassword) {
 ElMessage.warning('两次输入的密码不一致');
 return;
 }
 try {
 await userAPI.resetPassword(currentUser.value.id, resetForm.value.password);
 ElMessage.success('密码重置成功');
 resetDialogVisible.value = false;
 }
 catch (error) {
 console.error('重置密码失败:', error);
 ElMessage.error('重置密码失败');
 }
};
onMounted(() => {
 loadUsers();
});
</script>

<style scoped>
.user-management {
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