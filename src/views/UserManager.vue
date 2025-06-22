<template>
    <div class="users-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="title">用户管理</h1>
        <p class="description">管理和维护所有用户信息</p>
      </div>
  
      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="toggleUserModal">
          <el-icon><Plus /></el-icon> 新增用户
        </el-button>
        <el-button @click="exportData">
          <el-icon><Download /></el-icon> 导出数据
        </el-button>
        <el-button>
          <el-icon><Upload /></el-icon> 导入数据
        </el-button>
      </div>
  
      <!-- 筛选条件 -->
      <el-card class="filter-card">
        <el-form :model="filters" label-width="90px" label-position="left">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="用户姓名">
                <el-input v-model="filters.name" placeholder="请输入用户姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="用户角色">
                <el-select v-model="filters.role" placeholder="请选择">
                  <el-option label="全部" value="" />
                  <el-option label="管理员" value="管理员" />
                  <el-option label="普通用户" value="普通用户" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="用户状态">
                <el-select v-model="filters.status" placeholder="请选择">
                  <el-option label="全部" value="" />
                  <el-option label="启用" value="启用" />
                  <el-option label="禁用" value="禁用" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <div class="filter-actions">
            <el-button @click="resetFilters">重置</el-button>
            <el-button type="primary" @click="searchUsers">搜索</el-button>
          </div>
        </el-form>
      </el-card>
  
      <!-- 用户列表 -->
      <el-card>
        <div class="list-header">
          <h3 class="list-title">用户列表</h3>
          <span class="total-count">共 <span class="primary-text">{{ total }}</span> 条数据</span>
        </div>
  
        <el-table :data="userList" border style="width: 100%" :row-key="getRowKey">
          <el-table-column type="selection" width="40" />
  
          <el-table-column prop="id" label="用户ID" width="150" />
  
          <el-table-column label="姓名" width="200">
            <template #default="scope">
              <div class="user-info">
                <el-image :src="scope.row.avatar" class="user-avatar" />
                <span>{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
  
          <el-table-column prop="email" label="邮箱" width="200" />
  
          <el-table-column label="角色" width="100">
            <template #default="scope">
              {{ scope.row.role }}
            </template>
          </el-table-column>
  
          <el-table-column label="状态" width="120">
            <template #default="scope">
              <el-tag 
                :type="scope.row.status === '启用' ? 'success' : 'danger'"
                size="small"
              >
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
  
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button 
                size="small" 
                icon="View" 
                plain
                @click="openUserDetail(scope.row.id)"
              />
              <el-button 
                size="small" 
                icon="Edit" 
                plain
              />
              <el-button 
                size="small" 
                icon="Delete" 
                plain
                @click="deleteUser(scope.row.id)"
              />
            </template>
          </el-table-column>
        </el-table>
  
        <!-- 分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 20]"
            layout="sizes, prev, pager, next"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
  
      <!-- 用户录入模态框 -->
      <el-dialog
        v-model="isUserModalOpen"
        title="用户录入"
        width="60%"
        :before-close="handleClose"
      >
        <el-form :model="currentUser" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户角色">
                <el-select v-model="currentUser.role" placeholder="请选择">
                  <el-option label="管理员" value="管理员" />
                  <el-option label="普通用户" value="普通用户" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户姓名">
                <el-input v-model="currentUser.name" />
              </el-form-item>
            </el-col>
          </el-row>
  
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="联系邮箱">
                <el-input v-model="currentUser.email" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户状态">
                <el-select v-model="currentUser.status" placeholder="请选择">
                  <el-option label="启用" value="启用" />
                  <el-option label="禁用" value="禁用" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
  
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="isUserModalOpen = false">取消</el-button>
            <el-button type="primary" @click="saveUser">
              {{ currentUser.id ? '保存修改' : '提交' }}
            </el-button>
          </div>
        </template>
      </el-dialog>
  
      <!-- 用户详情抽屉 -->
      <el-drawer
        v-model="isUserDetailOpen"
        :title="'用户详情 - #' + currentUserId"
        direction="rtl"
        size="60%"
      >
        <div class="user-detail">
          <div class="detail-section">
            <h4 class="section-title">基本信息</h4>
            <div class="detail-row">
              <span class="label">用户姓名：</span>
              <span>{{ currentUserDetails.name }}</span>
            </div>
            <div class="detail-row">
              <span class="label">用户角色：</span>
              <span>{{ currentUserDetails.role }}</span>
            </div>
            <div class="detail-row">
              <span class="label">联系邮箱：</span>
              <span>{{ currentUserDetails.email }}</span>
            </div>
            <div class="detail-row">
              <span class="label">用户状态：</span>
              <span>{{ currentUserDetails.status }}</span>
            </div>
          </div>
  
          <div class="detail-actions">
            <el-button type="primary" @click="editUser(currentUserDetails.id)">编辑用户</el-button>
            <el-button @click="deleteUser(currentUserDetails.id)">删除用户</el-button>
          </div>
        </div>
      </el-drawer>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, reactive } from 'vue';
  import { ElMessage } from 'element-plus';
  import axios from 'axios';
  
  // 过滤器
  const filters = reactive({
    name: '',
    role: '',
    status: ''
  });
  
  // 分页
  const currentPage = ref(1);
  const pageSize = ref(5);
  const total = ref(156);
  
  // 用户列表
  const userList = ref([
    {
      id: '#U20250615001',
      name: '张三',
      email: 'zhangsan@example.com',
      role: '普通用户',
      status: '启用',
      avatar: 'https://picsum.photos/id/1005/100/100'
    },
    {
      id: '#U20250615002',
      name: '李四',
      email: 'lisi@example.com',
      role: '管理员',
      status: '禁用',
      avatar: 'https://picsum.photos/id/1006/100/100'
    },
    {
      id: '#U20250615003',
      name: '王五',
      email: 'wangwu@example.com',
      role: '普通用户',
      status: '启用',
      avatar: 'https://picsum.photos/id/1011/100/100'
    }
  ]);
  
  // 用户表单相关
  const isUserModalOpen = ref(false);
  const currentUser = ref({
    id: null,
    role: '普通用户',
    name: '',
    email: '',
    status: '启用'
  });
  
  // 用户详情相关
  const isUserDetailOpen = ref(false);
  const currentUserId = ref('');
  const currentUserDetails = ref({});
  
  // 获取行的唯一标识
  const getRowKey = (row) => row.id;
  
  // 表格操作
  const openUserDetail = (userId) => {
    const user = userList.value.find(user => user.id === userId);
    if (user) {
      currentUserDetails.value = {...user};
      currentUserId.value = userId;
      isUserDetailOpen.value = true;
    }
  };
  
  const editUser = (userId) => {
    const user = userList.value.find(u => u.id === userId);
    if (user) {
      currentUser.value = {...user};
      isUserModalOpen.value = true;
    }
  };
  
  const deleteUser = (userId) => {
    ElMessage.success('用户删除成功！'); // 实际使用时应替换为真实API调用
  };
  
  // 模态框操作
  const toggleUserModal = () => {
    isUserModalOpen.value = !isUserModalOpen.value;
    if (!isUserModalOpen.value) {
      resetForm();
    }
  };
  
  const resetForm = () => {
    currentUser.value = {
      id: null,
      role: '普通用户',
      name: '',
      email: '',
      status: '启用'
    };
  };
  
  const saveUser = () => {
    ElMessage.success(currentUser.value.id ? '用户信息更新成功！' : '用户信息提交成功！');
    toggleUserModal();
    fetchUsers(); // 刷新用户列表
  };
  
  // 分页操作
  const handleSizeChange = (newSize) => {
    pageSize.value = newSize;
    fetchUsers();
  };
  
  const handleCurrentChange = (pageNum) => {
    currentPage.value = pageNum;
    fetchUsers();
  };
  
  // 数据加载
  const fetchUsers = async () => {
    try {
      // 这里应该使用实际API获取数据
      // 示例：const response = await axios.get('/api/users', { params: { ...filters, page: currentPage.value, limit: pageSize.value } });
      // userList.value = response.data.items;
      // total.value = response.data.total;
      
      // 由于没有实际API，我们模拟分页
      // 在实际项目中应替换为真实的API调用
      ElMessage.info(`正在查询第${currentPage.value}页，每页${pageSize.value}条`);
    } catch (error) {
      ElMessage.error('用户数据加载失败');
      console.error(error);
    }
  };
  
  // 初始化加载数据
  onMounted(() => {
    fetchUsers();
  });
  </script>
  
  <style scoped>
  .users-container {
    padding: 20px;
  }
  
  .page-header {
    margin-bottom: 20px;
  }
  
  .title {
    font-size: 1.5rem;
    font-weight: bold;
    color: #1D2129;
  }
  
  .description {
    color: #86909C;
    margin-top: 4px;
  }
  
  .action-buttons {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
  }
  
  .filter-card {
    margin-bottom: 20px;
  }
  
  .detail-row {
    display: flex;
    margin-bottom: 10px;
  }
  
  .label {
    font-weight: bold;
    width: 90px;
    flex-shrink: 0;
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
  
  .user-info {
    display: flex;
    align-items: center;
  }
  
  .user-avatar {
    width: 40px;
    height: 40px;
    margin-right: 10px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .section-title {
    font-weight: bold;
    margin-bottom: 10px;
    padding-bottom: 5px;
    border-bottom: 1px solid #eee;
    margin-bottom: 10px;
  }
  
  .detail-actions {
    margin-top: 20px;
  }
  </style>