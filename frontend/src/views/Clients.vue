<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { queryPageApi, addClientApi, updateClientApi, queryInfoApi, deleteClientApi } from '../api/client';
// 引入需要用到的图标组件
import { Plus, View, Edit, Delete } from '@element-plus/icons-vue';

// 钩子函数,初始化加载数据
onMounted(() => {
  search();
});

// 过滤器
const searchClient = ref({
  name: '',
  type: '',
  intent: ''
});

//重置筛选条件
const clear = () => {
  searchClient.value = {
    name: '',
    type: '',
    intent: ''
  };
  search();
};

// 分页
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);

// 客户列表
const clientList = ref([]);

// 客户表单相关
const isClientModalOpen = ref(false);
const dialogTitle = ref();

const client = ref({
  clientId: null,
  name: '',
  phone: '',
  type: '个人',
  intent: '',
  email: '',
  budget: '',
  roomRequirement: '',
  area: '',
  preferredArea: '',
  description: '',
  additionalRequirements: ''
});

// 客户详情相关
const isClientDetailOpen = ref(false);
const currentClientId = ref('');
const currentClientDetails = ref({});

// 获取行的唯一标识
const getRowKey = (row) => row.id;

// 表格操作
const openClientDetail = async (clientId) => {
  const result = await queryInfoApi(clientId);
  if (result.code) {
    currentClientDetails.value = result.data;
    currentClientId.value = clientId;
    isClientDetailOpen.value = true;
  } else {
    ElMessage.error('获取客户信息失败！');
  }
};

const editClient = async (clientId) => {
  const result = await queryInfoApi(clientId);
  if (result.code) {
    isClientModalOpen.value = true;
    dialogTitle.value = '修改客户信息';
    client.value = result.data;
  } else {
    ElMessage.error('查询客户信息失败！');
  }
};

const deleteClient = (clientId) => {
  ElMessageBox.confirm('确定要删除该客户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 用户点击确定，调用删除接口
    const result = await deleteClientApi(clientId);
    if (result.code) {
      ElMessage.success('删除成功');
      search(); // 刷新列表
    } else {
      ElMessage.error('删除失败');
    }
  }).catch(() => {
    // 用户点击取消，不做任何操作
    ElMessage.info('已取消删除');
  });
};

// 模态框操作
const toggleClientModal = () => {
  dialogTitle.value = '新增客户';
  isClientModalOpen.value = !isClientModalOpen.value;
  if (!isClientModalOpen.value) {
    resetForm();
  }
};

const resetForm = () => {
  client.value = {
    clientId: null,
    name: '',
    phone: '',
    type: '个人',
    intent: '',
    email: '',
    budget: '',
    roomRequirement: '',
    area: '',
    preferredArea: '',
    description: '',
    additionalRequirements: ''
  };
  isClientModalOpen.value = !isClientModalOpen.value;
};

const save = async () => {
  const typeMap = {
    '个人': 1,
    '企业': 2
  };
  //将文本值转换为数字
  client.value.type = typeMap[client.value.type];

  if (client.value.clientId) {
    const result = await updateClientApi(client.value);
    if (result.code) { //成功
      ElMessage.success('保存成功');
      isClientModalOpen.value = false;
      search();
    } else { //失败
      ElMessage.error('保存失败');
    }
  } else {
    const result = await addClientApi(client.value);
    if (result.code) { //成功
      ElMessage.success('保存成功');
      isClientModalOpen.value = false;
      search();
    } else { //失败
      ElMessage.error('保存失败');
    }
  }
};

// 分页操作
const handleSizeChange = (newSize) => {
  pageSize.value = newSize;
  search();
};

const handleCurrentChange = (pageNum) => {
  currentPage.value = pageNum;
  search();
};

// 数据加载
const search = async () => {
  const result = await queryPageApi(
    searchClient.value.name,
    searchClient.value.type,
    searchClient.value.intent,
    currentPage.value,
    pageSize.value
  );
  if (result.code) {
    total.value = result.data.total;
    clientList.value = result.data.rows;
  }
};
</script>

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
        <!-- 使用引入的图标组件 -->
        <el-icon><Plus /></el-icon> 新增客户
      </el-button>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="searchClient" label-width="90px" label-position="left">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="客户姓名">
              <el-input v-model="searchClient.name" placeholder="请输入客户姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户类型">
              <el-select v-model="searchClient.type" placeholder="请选择">
                <el-option label="全部" value="" />
                <el-option label="个人" value="1" />
                <el-option label="企业" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户意向">
              <el-select v-model="searchClient.intent" placeholder="请选择">
                <el-option label="全部" value="" />
                <el-option label="购房" value="1" />
                <el-option label="租房" value="2" />
                <el-option label="投资" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="filter-actions">
          <el-button @click="clear">重置</el-button>
          <el-button type="primary" @click="search">搜索</el-button>
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

        <el-table-column prop="clientId" label="客户ID" width="150" />

        <el-table-column label="姓名" width="150">
          <template #default="scope">
            <div class="client-info">
              <span>{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="phone" label="联系电话" width="150" />

        <el-table-column label="类型" width="80">
          <template #default="scope">
            {{ scope.row.type }}
          </template>
        </el-table-column>

        <el-table-column label="意向" width="80">
          <template #default="scope">
            {{ scope.row.intent }}
          </template>
        </el-table-column>

        <el-table-column label="电子邮箱" width="200">
          <template #default="scope">
            <el-tag>
              {{ scope.row.email }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" >
          <template #default="scope">
            <el-button 
              size="small" 
              :icon="View" 
              plain
              @click="openClientDetail(scope.row.clientId)"
            >详情</el-button>
            <el-button 
              size="small" 
              :icon="Edit"
              type="primary" 
              plain
              @click="editClient(scope.row.clientId)"
            >编辑</el-button>
            <el-button 
              size="small" 
              :icon="Delete"
              type="danger" 
              plain
              @click="deleteClient(scope.row.clientId)"
            >删除</el-button>
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

    <!-- 客户 录入/修改 对话框 -->
    <el-dialog
      v-model="isClientModalOpen"
      :title="dialogTitle"
      width="60%"
      :before-close="resetForm"
    >
      <el-form :model="client" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户类型">
              <el-select v-model="client.type" placeholder="请选择">
                <el-option label="个人" value="1" />
                <el-option label="企业" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户姓名">
              <el-input v-model="client.name" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="client.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电子邮箱">
              <el-input v-model="client.email" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户意向">
              <el-select v-model="client.intent" placeholder="请选择">
                <el-option label="购房" value="1" />
                <el-option label="租房" value="2" />
                <el-option label="投资" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="户型要求">
              <el-input v-model="client.roomRequirement" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="首选区域">
              <el-input v-model="client.preferredArea" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算">
              <el-input v-model="client.budget"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="需求面积">
              <el-input v-model="client.area"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="客户需求">
          <el-input v-model="client.additionalRequirements" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="resetForm">取消</el-button>
          <el-button type="primary" @click="save">
            {{ client.clientId ? '保存' : '提交' }}
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
        </div>

        <div class="detail-section">
          <h4 class="section-title">购房意向</h4>
          <div class="detail-row">
            <span class="label">客户意向：</span>
            <span>{{ currentClientDetails.intent }}</span>
          </div>
          <div class="detail-row">
            <span class="label">预算范围：</span>
            <span>{{ currentClientDetails.budget}}元</span>
          </div>
          <div class="detail-row">
            <span class="label">户型要求：</span>
            <span>{{ currentClientDetails.roomRequirement}}</span>
          </div>
          <div class="detail-row">
            <span class="label">面积要求：</span>
            <span>{{ currentClientDetails.area}}㎡</span>
          </div>
          <div class="detail-row">
            <span class="label">区域偏好：</span>
            <span>{{ currentClientDetails.preferredArea}}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4 class="section-title">客户需求</h4>
          <p class="description-text">{{ currentClientDetails.demand}}</p>
        </div>

        <div class="detail-actions">
          <!-- <el-button type="primary" @click="editClient(currentClientDetails.id)">编辑客户</el-button> -->
          <!-- <el-button @click="deleteClient(currentClientDetails.id)">删除客户</el-button> -->
        </div>
      </div>
    </el-drawer>
  </div>
</template>

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