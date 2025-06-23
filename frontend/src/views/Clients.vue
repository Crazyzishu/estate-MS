<template>
  <div class="clients-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="title">客户管理</h1>
      <p class="description">管理和维护所有客户信息</p>
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="toggleClientModal">
        <el-icon><Plus /></el-icon> 新增客户
      </el-button>
      <!-- <el-button @click="exportData">
        <el-icon><Download /></el-icon> 导出数据
      </el-button>
      <el-button>
        <el-icon><Upload /></el-icon> 导入数据
      </el-button> -->
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="filters" label-width="90px" label-position="left">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="客户姓名">
              <el-input v-model="filters.name" placeholder="请输入客户姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户类型">
              <el-select v-model="filters.type" placeholder="请选择">
                <el-option label="全部" value="" />
                <el-option label="个人" value="个人" />
                <el-option label="企业" value="企业" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户意向">
              <el-select v-model="filters.intention" placeholder="请选择">
                <el-option label="全部" value="" />
                <el-option label="购房" value="购房" />
                <el-option label="租房" value="租房" />
                <el-option label="投资" value="投资" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="filter-actions">
          <el-button @click="resetFilters">重置</el-button>
          <el-button type="primary" @click="searchClients">搜索</el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 客户列表 -->
    <el-card>
      <div class="list-header">
        <h3 class="list-title">客户列表</h3>
        <span class="total-count">共 <span class="primary-text">{{ total }}</span> 条数据</span>
      </div>

      <el-table :data="clientList" border style="width: 100%" :row-key="getRowKey">
        <el-table-column type="selection" width="40" />

        <el-table-column prop="id" label="客户ID" width="150" />

        <el-table-column label="姓名" width="200">
          <template #default="scope">
            <div class="client-info">
              <el-image :src="scope.row.avatar" class="client-avatar" />
              <span>{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="phone" label="联系电话" width="150" />

        <el-table-column label="类型" width="100">
          <template #default="scope">
            {{ scope.row.type }}
          </template>
        </el-table-column>

        <el-table-column label="意向" width="120">
          <template #default="scope">
            {{ scope.row.intention }}
          </template>
        </el-table-column>

        <el-table-column label="状态" width="120">
          <template #default="scope">
            <el-tag 
              :type="scope.row.status === '潜在客户' ? 'info' : 
                     scope.row.status === '跟进中' ? 'warning' : 
                     scope.row.status === '已成交' ? 'success' : 'danger'"
              size="small"
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="跟进人" width="120">
          <template #default="scope">
            {{ scope.row.assignee }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button 
              size="small" 
              icon="View" 
              plain
              @click="openClientDetail(scope.row.id)"
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
              @click="deleteClient(scope.row.id)"
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

    <!-- 客户录入模态框 -->
    <el-dialog
      v-model="isClientModalOpen"
      title="客户录入"
      width="60%"
      :before-close="handleClose"
    >
      <el-form :model="currentClient" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户类型">
              <el-select v-model="currentClient.type" placeholder="请选择">
                <el-option label="个人" value="个人" />
                <el-option label="企业" value="企业" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户姓名">
              <el-input v-model="currentClient.name" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="currentClient.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电子邮箱">
              <el-input v-model="currentClient.email" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户意向">
              <el-select v-model="currentClient.intention" placeholder="请选择">
                <el-option label="购房" value="购房" />
                <el-option label="租房" value="租房" />
                <el-option label="投资" value="投资" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责销售">
              <el-select v-model="currentClient.assigneeId" placeholder="请选择">
                <el-option label="王经理 (ID: 1001)" value="1001" />
                <el-option label="张经理 (ID: 1002)" value="1002" />
                <el-option label="李经理 (ID: 1003)" value="1003" />
                <el-option label="赵经理 (ID: 1004)" value="1004" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="客户需求">
          <el-input v-model="currentClient.requirements" type="textarea" :rows="3" />
        </el-form-item>

        <el-form-item label="客户来源">
          <el-select v-model="currentClient.source" placeholder="请选择" style="width: 100%">
            <el-option label="网络广告" value="网络广告" />
            <el-option label="朋友介绍" value="朋友介绍" />
            <el-option label="电话营销" value="电话营销" />
            <el-option label="社交媒体" value="社交媒体" />
            <el-option label="门店咨询" value="门店咨询" />
            <el-option label="活动现场" value="活动现场" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="isClientModalOpen = false">取消</el-button>
          <el-button type="primary" @click="saveClient">
            {{ currentClient.id ? '保存修改' : '提交' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 客户详情抽屉 -->
    <el-drawer
      v-model="isClientDetailOpen"
      :title="'客户详情 - #' + currentClientId"
      direction="rtl"
      size="60%"
    >
      <div class="client-detail">
        <div class="detail-section">
          <h4 class="section-title">基本信息</h4>
          <div class="detail-row">
            <span class="label">客户姓名：</span>
            <span>{{ currentClientDetails.name }}</span>
          </div>
          <div class="detail-row">
            <span class="label">客户类型：</span>
            <span>{{ currentClientDetails.type }}</span>
          </div>
          <div class="detail-row">
            <span class="label">联系电话：</span>
            <span>{{ currentClientDetails.phone }}</span>
          </div>
          <div class="detail-row">
            <span class="label">电子邮箱：</span>
            <span>{{ currentClientDetails.email }}</span>
          </div>
          <div class="detail-row">
            <span class="label">客户来源：</span>
            <span>{{ currentClientDetails.source }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4 class="section-title">购房意向</h4>
          <div class="detail-row">
            <span class="label">客户意向：</span>
            <span>{{ currentClientDetails.intention }}</span>
          </div>
          <div class="detail-row">
            <span class="label">预算范围：</span>
            <span>{{ currentClientDetails.budget || '1000-1500万' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">户型要求：</span>
            <span>{{ currentClientDetails.roomType || '3室2厅' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">面积要求：</span>
            <span>{{ currentClientDetails.area || '120-150㎡' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">区域偏好：</span>
            <span>{{ currentClientDetails.location || '朝阳区' }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4 class="section-title">客户需求</h4>
          <p class="description-text">{{ currentClientDetails.demand || '希望购买一套位于朝阳区的三居室，预算在1000-1500万之间，最好是南北通透，采光好，周边配套设施齐全，有学校和商场。' }}</p>
        </div>

        <div class="detail-section">
          <h4 class="section-title">跟进记录</h4>
          <div class="follow-up-record">
            <div class="record-item">
              <div class="record-header">
                <span class="record-title">首次电话沟通</span>
                <span class="record-time">2025-06-10 10:30</span>
              </div>
              <p class="record-content">客户对朝阳区XX小区比较感兴趣，询问了价格和户型信息，已发送相关资料。</p>
              <div class="record-author">跟进人：{{ currentClientDetails.assignee || '王经理' }}</div>
            </div>
            
            <div class="record-item">
              <div class="record-header">
                <span class="record-title">安排看房</span>
                <span class="record-time">2025-06-12 14:00</span>
              </div>
              <p class="record-content">已安排客户于2025-06-15上午10点看房，推荐了3套符合需求的房源。</p>
              <div class="record-author">跟进人：{{ currentClientDetails.assignee || '王经理' }}</div>
            </div>
            
            <div class="record-item">
              <div class="record-header">
                <span class="record-title">看房反馈</span>
                <span class="record-time">2025-06-16 09:15</span>
              </div>
              <p class="record-content">客户对XX小区2号楼1502室比较满意，觉得户型和采光都很好，正在考虑中，预计本周内给出答复。</p>
              <div class="record-author">跟进人：{{ currentClientDetails.assignee || '王经理' }}</div>
            </div>
          </div>
        </div>

        <div class="detail-section">
          <h4 class="section-title">关注房源</h4>
          <div class="property-gallery">
            <div class="property-item" v-for="(property, index) in currentClientDetails.properties" :key="index">
              <el-image :src="property.image" class="property-image" />
              <div class="property-name">{{ property.name }}</div>
              <div class="property-details">
                <div class="property-room">{{ property.room }}</div>
                <div class="property-price">¥{{ formatNumber(property.price) }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="detail-actions">
          <el-button type="primary" @click="editClient(currentClientDetails.id)">编辑客户</el-button>
          <el-button @click="deleteClient(currentClientDetails.id)">删除客户</el-button>
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
  type: '',
  intention: ''
});

// 分页
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(156);

// 客户列表
const clientList = ref([
  {
    id: '#C20250615001',
    name: '李明',
    phone: '138****1234',
    email: 'liming@example.com',
    type: '个人',
    intention: '购房',
    status: '潜在客户',
    assignee: '王经理',
    avatar: 'https://picsum.photos/id/1005/100/100'
  },
  {
    id: '#C20250615002',
    name: '张华',
    phone: '139****5678',
    email: 'zhanghua@example.com',
    type: '个人',
    intention: '投资',
    status: '已成交',
    assignee: '张经理',
    avatar: 'https://picsum.photos/id/1006/100/100'
  },
  {
    id: '#C20250615003',
    name: '万达集团',
    phone: '010-12345678',
    email: 'wanke@wanke.com',
    type: '企业',
    intention: '购房',
    status: '跟进中',
    assignee: '李经理',
    avatar: 'https://picsum.photos/id/1011/100/100'
  },
  {
    id: '#C20250615004',
    name: '王丽',
    phone: '137****8765',
    email: 'wangli@example.com',
    type: '个人',
    intention: '租房',
    status: '已流失',
    assignee: '赵经理',
    avatar: 'https://picsum.photos/id/1027/100/100'
  },
  {
    id: '#C20250615005',
    name: '华为科技',
    phone: '010-87654321',
    email: 'huawei@example.com',
    type: '企业',
    intention: '投资',
    status: '潜在客户',
    assignee: '王经理',
    avatar: 'https://picsum.photos/id/1025/100/100'
  }
]);

// 客户表单相关
const isClientModalOpen = ref(false);
const currentClient = ref({
  id: null,
  type: '个人',
  name: '',
  phone: '',
  email: '',
  intention: '',
  source: '',
  requirements: '',
  assigneeId: '1001'
});

// 客户详情相关
const isClientDetailOpen = ref(false);
const currentClientId = ref('');
const currentClientDetails = ref({});

// 获取行的唯一标识
const getRowKey = (row) => row.id;

// 格式化数字
const formatNumber = (number) => {
  if (!number) return '0';
  return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

// 表格操作
const openClientDetail = (clientId) => {
  const client = clientList.value.find(client => client.id === clientId);
  if (client) {
    currentClientDetails.value = {...client};
    currentClientId.value = clientId;
    isClientDetailOpen.value = true;
  }
};

const editClient = (clientId) => {
  const client = clientList.value.find(h => h.id === clientId);
  if (client) {
    currentClient.value = {...client};
    isClientModalOpen.value = true;
  }
};

const deleteClient = (clientId) => {
  ElMessage.success('客户删除成功！'); // 实际使用时应替换为真实API调用
};

// 模态框操作
const toggleClientModal = () => {
  isClientModalOpen.value = !isClientModalOpen.value;
  if (!isClientModalOpen.value) {
    resetForm();
  }
};

const resetForm = () => {
  currentClient.value = {
    id: null,
    type: '个人',
    name: '',
    phone: '',
    email: '',
    intention: '',
    source: '',
    requirements: '',
    assigneeId: '1001'
  };
};

const saveClient = () => {
  ElMessage.success(currentClient.value.id ? '客户信息更新成功！' : '客户信息提交成功！');
  toggleClientModal();
  fetchClients(); // 刷新客户列表
};

// 文件上传
const handleRemove = (file, fileList) => {
  console.log('文件移除', file, fileList);
};

const handlePictureCardPreview = (file) => {
  currentImageIndex.value = currentClientDetails.value.images.indexOf(file.url);
  showImageViewer.value = true;
};

// 分页操作
const handleSizeChange = (newSize) => {
  pageSize.value = newSize;
  fetchClients();
};

const handleCurrentChange = (pageNum) => {
  currentPage.value = pageNum;
  fetchClients();
};

// 数据加载
const fetchClients = async () => {
  try {
    // 这里应该使用实际API获取数据
    // 示例：const response = await axios.get('/api/clients', { params: { ...filters, page: currentPage.value, limit: pageSize.value } });
    // clientList.value = response.data.items;
    // total.value = response.data.total;
    
    // 由于没有实际API，我们模拟分页
    // 在实际项目中应替换为真实的API调用
    ElMessage.info(`正在查询第${currentPage.value}页，每页${pageSize.value}条`);
  } catch (error) {
    ElMessage.error('客户数据加载失败');
    console.error(error);
  }
};

// 初始化加载数据
onMounted(() => {
  fetchClients();
});
</script>

<style scoped>
.clients-container {
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

.client-info {
  display: flex;
  align-items: center;
}

.client-avatar {
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

.record-item {
  border-left: 2px solid #165DFF;
  padding-left: 10px;
  margin-bottom: 15px;
}

.record-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.record-title {
  font-weight: bold;
}

.record-time {
  color: #999;
}

.record-content {
  margin: 5px 0;
  color: #5E6670;
}

.record-author {
  color: #86909C;
  font-size: 12px;
}

.property-gallery {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 10px;
}

.property-item {
  background-color: #F2F3F5;
  border-radius: 4px;
  padding: 10px;
}

.property-image {
  width: 100%;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.property-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.property-details {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #86909C;
}

.property-room {
  flex: 1;
}

.property-price {
  flex: 1;
  color: #165DFF;
}
</style>