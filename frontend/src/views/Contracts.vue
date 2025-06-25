<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  queryPageApi,
  getClientsApi,
  getHousesApi,
  getManagersApi,
  addContractApi,
  queryInfoApi,
  updateContractApi,
  deleteContractApi
} from '../api/contract'
import BarChart from '../components/charts/BarChart.vue'
import LineChart from '../components/charts/LineChart.vue'
import PieChart from '../components/charts/PieChart.vue'
import DoughnutChart from '../components/charts/DoughnutChart.vue'
// 引入需要的图标组件
import { Plus, View, Edit, Delete } from '@element-plus/icons-vue'

// 统计图表数据
// const monthContractChartData = {
//   labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
//   datasets: [{
//     label: '合同数量',
//     data: [12, 15, 10, 18, 20, 28],
//     backgroundColor: '#165DFF'
//   }]
// }

// const monthAmountChartData = {
//   labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
//   datasets: [
//     {
//       label: '合同金额',
//       data: [1500, 1800, 1300, 2100, 2300, 4250],
//       borderColor: '#0FC6C2',
//       fill: true,
//       backgroundColor: 'rgba(15, 198, 194, 0.1)'
//     }
//   ]
// }

// const pendingContractsChartData = {
//   labels: ['待签约', '已签约'],
//   datasets: [{
//     data: [12, 88],
//     backgroundColor: ['#FAAD14', '#165DFF']
//   }]
// }

// const completionRateChartData = {
//   labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
//   datasets: [
//     {
//       label: '完成率',
//       data: [75, 78, 80, 82, 84, 86],
//       borderColor: '#7362BC',
//       fill: true,
//       backgroundColor: 'rgba(115, 98, 188, 0.1)'
//     }
//   ]
// }

// const contractTypeChartData = {
//   labels: ['购房合同', '租房合同', '定金合同', '委托合同'],
//   datasets: [{
//     data: [65, 20, 10, 5],
//     backgroundColor: ['#165DFF', '#0FC6C2', '#F77234', '#7362BC']
//   }]
// }

// 过滤器
const searchContract = ref({
  contractId: '',
  clientName: '',
  contractType: '',
  status: ''
})

// 钩子函数初始化加载数据
onMounted(() => {
  search()
  getAllClients()
  getAllHouses()
  getAllManagers()
})

const resetForm = () => {
  currentContract.value = {
    contractId: null,
    clientId: '',
    houseId: '',
    managerId: '1001',
    totalAmount: null,
    paymentPlan: '一次性付款',
    signTime: new Date(),
    contractFile: ''
  }
}

//重置筛选条件
const clear = () => {
  searchContract.value = {
    contractId: '',
    clientName: '',
    contractType: '',
    status: ''
  }
  search()
}

// 数据加载
const search = async () => {
  const result = await queryPageApi(
    searchContract.value.contractId,
    searchContract.value.clientName,
    searchContract.value.contractType,
    searchContract.value.status,
    currentPage.value,
    pageSize.value
  )
  if (result.code) {
    total.value = result.data.total
    contractList.value = result.data.rows
  }
}

// 分页
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 客户和房源数据
const clients = ref([])
const getAllClients = async () => {
  const result = await getClientsApi()
  if (result.code) {
    clients.value = result.data
  }
}
const houses = ref([])
const getAllHouses = async () => {
  const result = await getHousesApi()
  if (result.code) {
    houses.value = result.data
  }
}
const managers = ref([])
const getAllManagers = async () => {
  const result = await getManagersApi()
  if (result.code) {
    managers.value = result.data
  }
}

// 合同列表
const contractList = ref([])

// 合同表单相关
const isContractAddModalOpen = ref(false)
const isContractUpModalOpen = ref(false)
const dialogVisible = ref(false)
const dialogImageUrl = ref('')
const dialogTitle = ref('')

const currentContract = ref({
  contractId: null,
  clientId: '',
  houseId: '',
  managerId: '1001',
  totalAmount: null,
  paymentPlan: '一次性付款',
  signTime: new Date(),
  status: '待签约',
  contractFile: ''
})

// 合同详情相关
const isContractDetailOpen = ref(false)
const currentContractId = ref('')
const currentContractDetails = ref({})

// 获取行的唯一标识
const getRowKey = (row) => row.id

// 格式化货币
const formatCurrency = (amount) => {
  if (!amount) return '¥0'
  return `¥${amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')}`
}

// 获取合同状态标签类型
const getContractStatusTagType = (status) => {
  switch (status) {
    case '已签约':
      return 'success'
    case '待签约':
      return 'warning'
    case '已完成':
      return 'info'
    // case '已终止':
    //   return 'danger'
    // case '已过期':
    //   return 'info'
    default:
      return ''
  }
}

// 表格操作
const openContractDetail = async (contractId) => {
  const result = await queryInfoApi(contractId)
  if (result.code) {
    currentContractDetails.value = result.data
    currentContractId.value = contractId
    isContractDetailOpen.value = true
  } else {
    ElMessage.error('获取合同信息失败！')
  }
}

const editContract = async (contractId) => {
  const result = await queryInfoApi(contractId)
  if (result.code) {
    isContractUpModalOpen.value = true
    dialogTitle.value = '修改合同信息'
    currentContract.value = result.data
  } else {
    ElMessage.error('查询合同信息失败！')
  }
}

const deleteContract = (contractId) => {
  ElMessageBox.confirm('确定要删除该客户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 用户点击确定，调用删除接口
    const result = await deleteContractApi(contractId)
    if (result.code) {
      ElMessage.success('删除成功')
      search() // 刷新列表
    } else {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 用户点击取消，不做任何操作
    ElMessage.info('已取消删除')
  }) // 实际使用时应替换为真实API调用
}

// 模态框操作
const toggleContractAddModal = () => {
  isContractAddModalOpen.value = !isContractAddModalOpen.value
  if (!isContractAddModalOpen.value) {
    resetForm()
  }
}
const toggleContractUpModal = () => {
  isContractUpModalOpen.value = !isContractUpModalOpen.value
  if (!isContractUpModalOpen.value) {
    resetForm()
  }
}

const save = async () => {
  if (currentContract.value.contractId) {
    const result = await updateContractApi(currentContract.value)
    if (result.code) { //成功
      ElMessage.success('保存成功')
      isContractUpModalOpen.value = false
      search()
    } else { //失败
      ElMessage.error('保存失败')
    }
  } else {
    const result = await addContractApi(currentContract.value)
    if (result.code) { //成功
      ElMessage.success('保存成功')
      isContractAddModalOpen.value = false
      search()
    } else { //失败
      ElMessage.error('保存失败')
    }
  }
}

// 文件上传
const handleRemove = (file, fileList) => {
  console.log('文件移除', file, fileList)
}

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

// 分页操作
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  search()
}

const handleCurrentChange = (pageNum) => {
  currentPage.value = pageNum
  search()
}

// 下载合同
// const downloadContract = (contractId) => {
//   ElMessage.success('合同下载成功！')
// }
</script>

<template>
  <div class="contracts-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="title">合同管理</h1>
      <p class="description">管理和维护所有房产合同信息</p>
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="toggleContractAddModal">
        <el-icon><Plus /></el-icon> 新增合同
      </el-button>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="searchContract" label-width="90px" label-position="left">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="合同编号">
              <el-input v-model="searchContract.contractId" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户姓名">
              <el-input v-model="searchContract.clientName" placeholder="请输入客户姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同类型">
              <el-select v-model="searchContract.contractType" placeholder="请选择">
                <el-option label="全部" value="" />
                <el-option label="购房" value="1" />
                <el-option label="租房" value="2" />
                <el-option label="投资" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="合同状态">
              <el-select v-model="searchContract.status" placeholder="请选择">
                <el-option label="全部" value="" />
                <el-option label="已签约" value="已签约" />
                <el-option label="待签约" value="待签约" />
                <el-option label="已完成" value="已完成" />
                <!-- <el-option label="已终止" value="已终止" />
                <el-option label="已过期" value="已过期" /> -->
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

    <!-- 合同统计卡片 -->
    <!-- <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
      <el-card class="stat-card">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-gray-500 font-medium">本月签约合同</h3>
          <div class="bg-primary/10 p-2 rounded-lg">
            <i class="fa fa-file-text-o text-primary"></i>
          </div>
        </div>
        <div class="flex items-end justify-between">
          <div>
            <p class="text-3xl font-bold">28</p>
            <p class="text-success text-sm flex items-center mt-1">
              <i class="fa fa-arrow-up mr-1"></i> 12.5%
              <span class="text-gray-500 ml-1">较上月</span>
            </p>
          </div>
          <div class="w-20 h-20">
            <BarChart :data="monthContractChartData" />
          </div>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-gray-500 font-medium">本月合同金额</h3>
          <div class="bg-success/10 p-2 rounded-lg">
            <i class="fa fa-money text-success"></i>
          </div>
        </div>
        <div class="flex items-end justify-between">
          <div>
            <p class="text-3xl font-bold">¥42.5M</p>
            <p class="text-success text-sm flex items-center mt-1">
              <i class="fa fa-arrow-up mr-1"></i> 8.3%
              <span class="text-gray-500 ml-1">较上月</span>
            </p>
          </div>
          <div class="w-20 h-20">
            <LineChart :data="monthAmountChartData" />
          </div>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-gray-500 font-medium">待签约合同</h3>
          <div class="bg-warning/10 p-2 rounded-lg">
            <i class="fa fa-clock-o text-warning"></i>
          </div>
        </div>
        <div class="flex items-end justify-between">
          <div>
            <p class="text-3xl font-bold">12</p>
            <p class="text-danger text-sm flex items-center mt-1">
              <i class="fa fa-arrow-up mr-1"></i> 20%
              <span class="text-gray-500 ml-1">较上月</span>
            </p>
          </div>
          <div class="w-20 h-20">
            <DoughnutChart :data="pendingContractsChartData" />
          </div>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-gray-500 font-medium">合同完成率</h3>
          <div class="bg-secondary/10 p-2 rounded-lg">
            <i class="fa fa-line-chart text-secondary"></i>
          </div>
        </div>
        <div class="flex items-end justify-between">
          <div>
            <p class="text-3xl font-bold">86%</p>
            <p class="text-success text-sm flex items-center mt-1">
              <i class="fa fa-arrow-up mr-1"></i> 3.2%
              <span class="text-gray-500 ml-1">较上月</span>
            </p>
          </div>
          <div class="w-20 h-20">
            <LineChart :data="completionRateChartData" />
          </div>
        </div>
      </el-card>
    </div>

    <!-- 合同类型分布 -->
    <!-- <el-card class="mb-6">
      <div class="flex items-center justify-between mb-6">
        <h3 class="font-bold text-gray-800">合同类型分布</h3>
        <div class="text-sm text-gray-500">近6个月</div>
      </div>

      <div class="h-64">
        <PieChart :data="contractTypeChartData" />
      </div>
    </el-card> -->

    <!-- 合同列表 -->
    <el-card>
      <div class="list-header">
        <h3 class="font-bold text-gray-800">合同列表</h3>
        <span class="total-count">共 <span class="font-medium text-primary">{{ total }}</span> 条数据</span>
      </div>

      <el-table :data="contractList" border style="width: 100%" :row-key="getRowKey">
        <el-table-column type="selection" width="40" />

        <el-table-column prop="contractId" label="合同编号" width="180" />

        <el-table-column label="客户姓名" width="150">
          <template #default="scope">
            {{ scope.row.clientName }}
          </template>
        </el-table-column>

        <el-table-column prop="address" label="房源地址" min-width="200" />

        <el-table-column prop="contractType" label="合同类型" width="120">
          <template #default="scope">
            {{ scope.row.contractType }}
          </template>
        </el-table-column>

        <el-table-column label="签约日期" width="120">
          <template #default="scope">
            {{ scope.row.signTime }}
          </template>
        </el-table-column>

        <el-table-column label="金额" width="150">
          <template #default="scope">
            <span class="font-medium text-primary">{{ formatCurrency(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="120">
          <template #default="scope">
            <el-tag
              :type="getContractStatusTagType(scope.row.status)"
              size="small"
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button
              size="small"
              :icon="View"
              plain
              @click="openContractDetail(scope.row.contractId)"
            >查看详情</el-button>
            <el-button
              size="small"
              :icon="Edit"
              type="primary"
              @click="editContract(scope.row.contractId)"
              plain
            >编辑</el-button>
            <el-button
              size="small"
              :icon="Delete"
              type="danger"
              plain
              @click="deleteContract(scope.row.contractId)"
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

    <!-- 合同录入模态框 -->
    <el-dialog
      v-model="isContractAddModalOpen"
      :title="新增合同"
      width="60%"
      :before-close="handleClose"
    >
      <el-form :model="currentContract" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户姓名">
              <el-select v-model="currentContract.clientId" placeholder="请选择客户" style="width: 100%">
                <el-option
                  v-for="client in clients"
                  :key="client.clientId"
                  :label="client.clientName"
                  :value="client.clientId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房源地址">
              <el-select v-model="currentContract.houseId" placeholder="请选择房源" style="width: 100%">
                <el-option
                  v-for="house in houses"
                  :key="house.houseId"
                  :label="house.address"
                  :value="house.houseId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签约日期">
              <el-date-picker
                v-model="currentContract.signTime"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同金额">
              <el-input v-model.number="currentContract.totalAmount" placeholder="请输入金额">
                <template #append>元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人">
              <el-select v-model="currentContract.managerId" placeholder="请选择">
                <el-option
                  v-for="manager in managers"
                  :key="manager.managerId"
                  :label="manager.managerName"
                  :value="manager.managerId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付方式">
              <el-select v-model="currentContract.paymentPlan" placeholder="请选择">
                <el-option label="一次性付款" value="一次性付款" />
                <el-option label="按揭贷款" value="按揭贷款" />
                <el-option label="分期付款" value="分期付款" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-form-item label="合同条款">
          <el-input v-model="currentContract.terms" type="textarea" :rows="3" />
        </el-form-item> -->

        <el-form-item label="附件上传">
          <el-upload
            v-model="currentContract.contractFile"
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
          >
            <template #default>
              <el-button size="small">
                <el-icon><Plus /></el-icon> 点击上传
              </el-button>
              <i class="el-icon-plus"></i>
            </template>
          </el-upload>
          <el-dialog v-model="dialogVisible">
            <img w-full :src="dialogImageUrl" alt="预览图" />
          </el-dialog>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="isContractAddModalOpen = false">取消</el-button>
          <el-button type="primary" @click="save">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 合同编辑对话框 -->
    <el-dialog
      v-model="isContractUpModalOpen"
      :title="修改合同信息"
      width="60%"
      :before-close="handleClose"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="签约日期">
            <el-date-picker
              v-model="currentContract.signTime"
              type="date"
              placeholder="选择日期"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人">
            <el-select v-model="currentContract.managerId" placeholder="请选择">
              <el-option
                v-for="manager in managers"
                :key="manager.managerId"
                :label="manager.managerName"
                :value="manager.managerId"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="客户意向">
            <el-select v-model="currentContract.status" placeholder="请选择">
              <el-option label="待签约" value="待签约" />
              <el-option label="已签约" value="已签约" />
              <el-option label="已完成" value="已完成" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="isContractUpModalOpen = false">取消</el-button>
          <el-button type="primary" @click="save">
            保存修改
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 合同详情抽屉 -->
    <el-drawer
      v-model="isContractDetailOpen"
      :title="'合同详情 - #' + currentContractId"
      direction="rtl"
      size="60%"
    >
      <div class="contract-detail">
        <div class="detail-section">
          <h4 class="section-title">基本信息</h4>
          <div class="detail-row">
            <span class="label">合同编号：</span>
            <span>{{ currentContractDetails.contractId }}</span>
          </div>
          <div class="detail-row">
            <span class="label">客户姓名：</span>
            <span>{{ currentContractDetails.clientName }}</span>
          </div>
          <div class="detail-row">
            <span class="label">房源地址：</span>
            <span>{{ currentContractDetails.address }}</span>
          </div>
          <div class="detail-row">
            <span class="label">合同类型：</span>
            <span>{{ currentContractDetails.contractType }}</span>
          </div>
          <div class="detail-row">
            <span class="label">签约日期：</span>
            <span>{{ currentContractDetails.signTime }}</span>
          </div>
          <div class="detail-row">
            <span class="label">合同金额：</span>
            <span class="amount">{{ formatCurrency(currentContractDetails.totalAmount) }}</span>
          </div>
          <div class="detail-row">
            <span class="label">状态：</span>
            <el-tag
              :type="getContractStatusTagType(currentContractDetails.status)"
              size="small"
            >
              {{ currentContractDetails.status }}
            </el-tag>
          </div>
          <div class="detail-row">
            <span class="label">负责人：</span>
            <span>{{ currentContractDetails.managerName }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4 class="section-title">详细条款</h4>
          <p class="description-text">{{ '未提供详细条款' }}</p>
        </div>

        <div class="detail-section">
          <h4 class="section-title">合同附件</h4>
          <div v-if="currentContractDetails.attachments && currentContractDetails.attachments.length > 0" class="attachments">
            <div v-for="(attachment, index) in currentContractDetails.attachments" :key="index" class="attachment-item">
              <i class="fa fa-file-pdf-o text-red-500"></i>
              <span>{{ attachment.name }}</span>
              <a :href="attachment.url" target="_blank" class="download-link">
                <el-button type="text">下载</el-button>
              </a>
            </div>
          </div>
          <p v-else class="no-attachments">暂无附件</p>
        </div>

        <!-- <div class="detail-section">
          <h4 class="section-title">跟进记录</h4>
          <div class="follow-up-record">
            <div class="record-item">
              <div class="record-header">
                <span class="record-title">合同起草</span>
                <span class="record-time">2025-06-15 10:30</span>
              </div>
              <p class="record-content">合同初稿已准备完成，等待客户确认。</p>
              <div class="record-author">跟进人：{{ currentContractDetails.salesperson }}</div>
            </div>

            <div class="record-item">
              <div class="record-header">
                <span class="record-title">客户确认</span>
                <span class="record-time">2025-06-17 15:00</span>
              </div>
              <p class="record-content">客户对合同条款表示满意，同意签署。</p>
              <div class="record-author">跟进人：{{ currentContractDetails.salesperson }}</div>
            </div>

            <div class="record-item">
              <div class="record-header">
                <span class="record-title">正式签约</span>
                <span class="record-time">2025-06-18 09:30</span>
              </div>
              <p class="record-content">合同正式签署，款项支付流程启动。</p>
              <div class="record-author">跟进人：{{ currentContractDetails.salesperson }}</div>
            </div>
          </div>
        </div> -->

        <div class="detail-actions">
          <el-button type="primary" @click="editContract(currentContractDetails.contractId)">编辑合同</el-button>
          <el-button @click="deleteContract(currentContractDetails.contractId)">删除合同</el-button>
          <!-- <el-button @click="downloadContract(currentContractDetails.contractId)">下载合同</el-button> -->
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
.contracts-container {
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

.amount {
  font-weight: bold;
  color: #165DFF;
}

.attachments {
  margin-top: 10px;
}

.attachment-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  padding: 8px;
  border-radius: 4px;
  background-color: #f2f3f5;
}

.attachment-item i {
  margin-right: 8px;
  margin-left: 4px;
}

.download-link {
  margin-left: auto;
  color: #165DFF;
}

.no-attachments {
  color: #999;
  font-style: italic;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-row {
  display: flex;
  margin-bottom: 10px;
}

.detail-row .label {
  width: 90px;
  flex-shrink: 0;
}

.detail-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

/* 可以添加更多样式... */
</style>