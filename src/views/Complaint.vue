<template>
  <div class="complaint-container">
    <!-- 页面标题 -->
    <div class="mb-8">
      <h1 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold text-gray-800">投诉处理管理</h1>
      <p class="text-gray-500 mt-1">查看和处理所有客户投诉</p>
    </div>

    <!-- 操作按钮 -->
    <div class="flex flex-col sm:flex-row gap-4 mb-6">
      <el-button type="primary" @click="openAddComplaintModal">
        <i class="fa fa-plus"></i> 新增投诉
      </el-button>
      <el-button @click="exportData">
        <i class="fa fa-download"></i> 导出数据
      </el-button>
    </div>

    <!-- 筛选条件 -->
    <el-form :model="filters" label-width="90px" inline class="mb-4">
      <el-form-item label="投诉编号">
        <el-input v-model="filters.number" placeholder="请输入编号" />
      </el-form-item>
      <el-form-item label="客户姓名">
        <el-input v-model="filters.clientName" placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="楼盘名称">
        <el-select v-model="filters.property" placeholder="请选择楼盘">
          <el-option label="朝阳一号" value="朝阳一号" />
          <el-option label="海淀华府" value="海淀华府" />
          <el-option label="东城国际" value="东城国际" />
          <el-option label="西城时代" value="西城时代" />
          <el-option label="南城花园" value="南城花园" />
        </el-select>
      </el-form-item>
      <el-form-item label="投诉类型">
        <el-select v-model="filters.type" placeholder="请选择类型">
          <el-option label="服务态度" value="服务态度" />
          <el-option label="房源信息" value="房源信息" />
          <el-option label="合同条款" value="合同条款" />
          <el-option label="交付问题" value="交付问题" />
          <el-option label="质量问题" value="质量问题" />
          <el-option label="其他问题" value="其他问题" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="filters.status" placeholder="请选择状态">
          <el-option label="待分配" value="待分配" />
          <el-option label="处理中" value="处理中" />
          <el-option label="已解决" value="已解决" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchComplaints">查询</el-button>
        <el-button @click="resetFilters">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 投诉统计 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
      <el-card>
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-gray-500 font-medium">本月投诉总数</h3>
          <div class="bg-danger/10 p-2 rounded-lg">
            <i class="fa fa-exclamation-circle text-danger"></i>
          </div>
        </div>
        <div class="flex items-end justify-between">
          <div>
            <p class="text-3xl font-bold">{{ monthlyComplaintCount }}</p>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 投诉趋势图 -->
    <el-card class="mb-6">
      <div class="flex items-center justify-between mb-6">
        <h3 class="font-bold text-gray-800">投诉趋势</h3>
        <div class="flex gap-2">
          <el-button size="small" @click="changeComplaintTrendRange('week')">本周</el-button>
          <el-button size="small" type="primary" @click="changeComplaintTrendRange('month')">本月</el-button>
          <el-button size="small" @click="changeComplaintTrendRange('quarter')">本季度</el-button>
          <el-button size="small" @click="changeComplaintTrendRange('year')">本年度</el-button>
        </div>
      </div>
      <div class="h-64">
        <canvas ref="complaintTrendChart"></canvas>
      </div>
    </el-card>

    <!-- 投诉类型分布 -->
    <el-card class="mb-6">
      <div class="flex items-center justify-between mb-6">
        <h3 class="font-bold text-gray-800">投诉类型分布</h3>
      </div>
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div class="h-64">
          <canvas ref="complaintTypeChart"></canvas>
        </div>
        <div class="space-y-4">
          <div v-for="(type, index) in complaintTypeDistribution" :key="index">
            <div class="flex items-center justify-between mb-1">
              <span class="text-sm font-medium">{{ type.label }}</span>
              <span class="text-sm font-medium">{{ type.percentage }}%</span>
            </div>
            <div class="w-full bg-gray-100 rounded-full h-2">
              <div :class="`bg-${type.color} h-2 rounded-full`" :style="{ width: `${type.percentage}%` }"></div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 投诉列表 -->
    <el-card>
      <!-- 投诉列表 -->
      <div class="overflow-x-auto">
        <el-table :data="complaintList" border style="width: 100%">
          <el-table-column prop="id" label="投诉编号" width="150" />
          <el-table-column label="客户信息" width="180">
            <template #default="scope">
              <div class="flex items-center gap-3">
                <img :src="scope.row.avatar" alt="客户头像" class="w-8 h-8 rounded-full object-cover" />
                <div>
                  <p class="font-medium">{{ scope.row.clientName }}</p>
                  <p class="text-sm text-gray-500">{{ scope.row.phone }}</p>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="property" label="投诉楼盘" width="120" />
          <el-table-column prop="type" label="投诉类型" width="120" />
          <el-table-column label="提交时间" width="150">
            <template #default="scope">
              <p>{{ scope.row.date }}</p>
              <p class="text-sm text-gray-500">{{ scope.row.time }}</p>
            </template>
          </el-table-column>
          <el-table-column label="处理状态" width="120">
            <template #default="scope">
              <el-tag :type="getComplaintStatusTagType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" type="primary" @click="openComplaintDetail(scope.row.id)">
                <i class="fa fa-eye"></i> 查看详情
              </el-button>
              <el-button size="small" :disabled="scope.row.status !== '待分配'" @click="assignComplaint(scope.row.id)">
                <i class="fa fa-user-plus"></i> 分配处理人
              </el-button>
              <el-button size="small" :disabled="scope.row.status !== '处理中'" @click="markAsResolved(scope.row.id)">
                <i class="fa fa-check-circle"></i> 标记为已解决
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="flex flex-col sm:flex-row items-center justify-between mt-6 gap-4">
        <div class="text-sm text-gray-500">
          显示 <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span> 到
          <span class="font-medium">{{ Math.min(currentPage * pageSize, total) }}</span> 条，共
          <span class="font-medium">{{ total }}</span> 条
        </div>
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

    <!-- 新增投诉模态框 -->
    <el-dialog v-model="isAddComplaintModalOpen" title="新增投诉" width="60%">
      <el-form :model="newComplaint" :rules="complaintRules" ref="addComplaintForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户姓名" prop="clientName">
              <el-input v-model="newComplaint.clientName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="newComplaint.phone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="投诉楼盘" prop="property">
              <el-select v-model="newComplaint.property" placeholder="请选择楼盘">
                <el-option label="朝阳一号" value="朝阳一号" />
                <el-option label="海淀华府" value="海淀华府" />
                <el-option label="东城国际" value="东城国际" />
                <el-option label="西城时代" value="西城时代" />
                <el-option label="南城花园" value="南城花园" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投诉类型" prop="type">
              <el-select v-model="newComplaint.type" placeholder="请选择类型">
                <el-option label="服务态度" value="服务态度" />
                <el-option label="房源信息" value="房源信息" />
                <el-option label="合同条款" value="合同条款" />
                <el-option label="交付问题" value="交付问题" />
                <el-option label="质量问题" value="质量问题" />
                <el-option label="其他问题" value="其他问题" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="投诉日期" prop="date">
              <el-date-picker v-model="newComplaint.date" type="date" placeholder="选择日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投诉来源" prop="source">
              <el-select v-model="newComplaint.source" placeholder="请选择来源">
                <el-option label="电话投诉" value="电话投诉" />
                <el-option label="在线投诉" value="在线投诉" />
                <el-option label="现场投诉" value="现场投诉" />
                <el-option label="邮件投诉" value="邮件投诉" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="投诉内容" prop="content">
          <el-input v-model="newComplaint.content" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="附件上传">
          <el-upload
            v-model:file-list="newComplaint.attachments"
            action="#"
            multiple
            :auto-upload="false"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <el-dialog v-model="dialogVisible">
            <img w-full :src="dialogImageUrl" alt="预览图" />
          </el-dialog>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="isAddComplaintModalOpen = false">取消</el-button>
          <el-button type="primary" @click="submitComplaint">提交投诉</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 投诉详情模态框 -->
    <el-dialog v-model="isComplaintDetailModalOpen" title="投诉详情" width="60%">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <div>
          <h4 class="font-medium text-gray-800 mb-4">投诉基本信息</h4>
          <div class="space-y-3">
            <div class="flex">
              <span class="text-gray-500 w-24">投诉编号：</span>
              <span class="font-medium">{{ currentComplaintDetail.id }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">投诉时间：</span>
              <span>{{ currentComplaintDetail.date }} {{ currentComplaintDetail.time }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">投诉楼盘：</span>
              <span>{{ currentComplaintDetail.property }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">投诉类型：</span>
              <span>{{ currentComplaintDetail.type }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">投诉来源：</span>
              <span>{{ currentComplaintDetail.source }}</span>
            </div>
          </div>
        </div>
        <div>
          <h4 class="font-medium text-gray-800 mb-4">客户信息</h4>
          <div class="space-y-3">
            <div class="flex">
              <span class="text-gray-500 w-24">客户姓名：</span>
              <span class="font-medium">{{ currentComplaintDetail.clientName }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">手机号码：</span>
              <span>{{ currentComplaintDetail.phone }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">购房状态：</span>
              <span>{{ currentComplaintDetail.purchaseStatus }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">房号：</span>
              <span>{{ currentComplaintDetail.roomNumber }}</span>
            </div>
            <div class="flex">
              <span class="text-gray-500 w-24">签约日期：</span>
              <span>{{ currentComplaintDetail.signDate }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="mb-6">
        <h4 class="font-medium text-gray-800 mb-4">投诉内容</h4>
        <div class="bg-gray-50 rounded-lg p-4">
          <p class="text-gray-700">{{ currentComplaintDetail.content }}</p>
        </div>
      </div>
      <div class="mb-6">
        <h4 class="font-medium text-gray-800 mb-4">投诉附件</h4>
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          <div v-for="(attachment, index) in currentComplaintDetail.attachments" :key="index" class="bg-gray-50 rounded-lg p-3 flex items-center gap-3">
            <div class="bg-primary/10 w-10 h-10 rounded-lg flex items-center justify-center">
              <i class="fa fa-file-image-o text-primary"></i>
            </div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium truncate">{{ attachment.name }}</p>
              <p class="text-xs text-gray-500">{{ attachment.size }}</p>
            </div>
            <el-button type="text" @click="downloadAttachment(attachment.url)">
              <i class="fa fa-download"></i>
            </el-button>
          </div>
        </div>
      </div>
      <div class="mb-6">
        <h4 class="font-medium text-gray-800 mb-4">处理操作</h4>
        <div class="flex gap-3">
          <el-button type="success" v-if="currentComplaintDetail.status === '待分配'" @click="assignComplaint(currentComplaintDetail.id)">
            <i class="fa fa-user-plus"></i> 分配处理人
          </el-button>
          <el-button @click="contactClient(currentComplaintDetail.phone)">
            <i class="fa fa-phone"></i> 联系客户
          </el-button>
          <el-button @click="sendEmail(currentComplaintDetail.email)">
            <i class="fa fa-envelope"></i> 发送邮件
          </el-button>
        </div>
      </div>
      <div>
        <h4 class="font-medium text-gray-800 mb-4">处理记录</h4>
        <div class="space-y-4">
          <div v-for="(record, index) in currentComplaintDetail.processRecords" :key="index" class="bg-gray-50 rounded-lg p-4">
            <div class="flex items-center justify-between mb-2">
              <h5 class="font-medium">{{ record.action }}</h5>
              <span class="text-xs text-gray-500">{{ record.time }}</span>
            </div>
            <p class="text-sm text-gray-600">{{ record.description }}</p>
            <div class="flex items-center justify-end mt-2">
              <span class="text-xs text-gray-500">操作人：{{ record.operator }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 分配处理人模态框 -->
    <el-dialog v-model="isAssignComplaintModalOpen" title="分配处理人" width="40%">
      <el-form :model="assignForm" :rules="assignRules" ref="assignFormRef" label-width="120px">
        <el-form-item label="处理人" prop="handler">
          <el-select v-model="assignForm.handler" placeholder="请选择处理人">
            <el-option label="张经理 (ID: 1001)" value="1001" />
            <el-option label="李经理 (ID: 1002)" value="1002" />
            <el-option label="王经理 (ID: 1003)" value="1003" />
            <el-option label="赵经理 (ID: 1004)" value="1004" />
            <el-option label="陈经理 (ID: 1005)" value="1005" />
          </el-select>
        </el-form-item>
        <el-form-item label="预计处理时间" prop="estimatedTime">
          <el-date-picker v-model="assignForm.estimatedTime" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="assignForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="isAssignComplaintModalOpen = false">取消</el-button>
          <el-button type="primary" @click="submitAssign">确认分配</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import axios from "axios";


// 筛选条件
const filters = reactive({
  number: '',
  clientName: '',
  property: '',
  type: '',
  status: ''
})

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 投诉列表
const complaintList = ref([])

// API 基础路径
const API_BASE_URL = '/api/complaints'

// 分页事件
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  fetchComplaints()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  fetchComplaints()
}

// 获取状态标签类型
const getComplaintStatusTagType = (status) => {
  switch (status) {
    case '待分配':
      return 'info'
    case '处理中':
      return 'warning'
    case '已解决':
      return 'success'
    default:
      return ''
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchComplaints()
})

// 搜索投诉
const searchComplaints = () => {
  currentPage.value = 1
  fetchComplaints()
}

// 重置筛选条件
const resetFilters = () => {
  Object.keys(filters).forEach(key => {
    filters[key] = ''
  })
  currentPage.value = 1
  fetchComplaints()
}

// 本月投诉总数
const monthlyComplaintCount = ref(0)

// 投诉趋势图
const complaintTrendChart = ref(null)
let trendChartInstance = null

// 投诉类型分布
const complaintTypeDistribution = ref([
  { label: '服务态度', percentage: 28, color: 'danger' },
  { label: '房源信息', percentage: 24, color: 'warning' },
  { label: '合同条款', percentage: 18, color: 'primary' },
  { label: '交付问题', percentage: 16, color: 'success' },
  { label: '质量问题', percentage: 10, color: 'info' },
  { label: '其他问题', percentage: 4, color: 'dark' }
])

// 投诉类型图表
const complaintTypeChart = ref(null)
let typeChartInstance = null

// 新增投诉模态框
const isAddComplaintModalOpen = ref(false)
const newComplaint = reactive({
  clientName: '',
  phone: '',
  property: '',
  type: '',
  date: null,
  source: '',
  content: '',
  attachments: []
})

const complaintRules = {
  clientName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
  property: [{ required: true, message: '请选择投诉楼盘', trigger: 'change' }],
  type: [{ required: true, message: '请选择投诉类型', trigger: 'change' }],
  date: [{ required: true, message: '请选择投诉日期', trigger: 'change' }],
  source: [{ required: true, message: '请选择投诉来源', trigger: 'change' }],
  content: [{ required: true, message: '请输入投诉内容', trigger: 'blur' }]
}

const addComplaintForm = ref(null)

// 投诉详情模态框
const isComplaintDetailModalOpen = ref(false)
const currentComplaintDetail = ref({})

// 分配处理人模态框
const isAssignComplaintModalOpen = ref(false)
const assignForm = reactive({
  handler: '',
  estimatedTime: null,
  remark: ''
})

const assignRules = {
  handler: [{ required: true, message: '请选择处理人', trigger: 'change' }],
  estimatedTime: [{ required: true, message: '请选择预计处理时间', trigger: 'change' }]
}

const assignFormRef = ref(null)

// 图片预览
const dialogVisible = ref(false)
const dialogImageUrl = ref('')

// 打开新增投诉模态框
const openAddComplaintModal = () => {
  isAddComplaintModalOpen.value = true
  Object.keys(newComplaint).forEach(key => {
    if (Array.isArray(newComplaint[key])) {
      newComplaint[key] = []
    } else {
      newComplaint[key] = ''
    }
  })
  newComplaint.date = null
}

// 提交投诉
const submitComplaint = async () => {
  if (!addComplaintForm.value) return
  await addComplaintForm.value.validate(async valid => {
    if (valid) {
      ElMessage.success('投诉提交成功')
      isAddComplaintModalOpen.value = false
      fetchComplaints()
    }
  })
}


// 获取投诉列表
const fetchComplaints = async () => {
  try {
    const response = await axios.get(API_BASE_URL, {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        number: filters.number,
        clientName: filters.clientName,
        property: filters.property,
        type: filters.type,
        status: filters.status
      }
    })

    if (response.data.code === 200) {
      complaintList.value = response.data.data.list || []
      total.value = response.data.data.total || 0
    } else {
      ElMessage.error('获取投诉列表失败')
    }
  } catch (error) {
    console.error('Error fetching complaints:', error)
    ElMessage.error('网络请求出错，请检查后端服务')
  }
}


// 打开投诉详情模态框
const openComplaintDetail = (id) => {
  currentComplaintDetail.value = complaintList.value.find(item => item.id === id) || {}
  isComplaintDetailModalOpen.value = true
}

// 分配处理人
const assignComplaint = (id) => {
  assignForm.handler = ''
  assignForm.estimatedTime = null
  assignForm.remark = ''
  isAssignComplaintModalOpen.value = true
}

// 提交分配
const submitAssign = async () => {
  if (!assignFormRef.value) return
  await assignFormRef.value.validate(valid => {
    if (valid) {
      ElMessage.success('分配处理人成功')
      isAssignComplaintModalOpen.value = false
      fetchComplaints()
    }
  })
}

// 标记为已解决
const markAsResolved = (id) => {
  ElMessage.success('标记为已解决成功')
  fetchComplaints()
}

// 联系客户
const contactClient = (phone) => {
  ElMessage.info(`联系客户：${phone}`)
}

// 发送邮件
const sendEmail = (email) => {
  ElMessage.info(`发送邮件给：${email}`)
}

// 下载附件
const downloadAttachment = (url) => {
  ElMessage.info(`下载附件：${url}`)
}

// 切换投诉趋势图范围
const changeComplaintTrendRange = range => {
  ElMessage.info(`切换到 ${range} 投诉趋势`)
  updateComplaintTrendChart()
}

// 更新投诉趋势图
const updateComplaintTrendChart = async () => {
  if (trendChartInstance) {
    trendChartInstance.destroy()
  }
  await nextTick()
  if (complaintTrendChart.value) {
    const ctx = complaintTrendChart.value.getContext('2d')
    trendChartInstance = new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['1月', '2月', '3月', '4月', '5月', '6月'],
        datasets: [{
          label: '投诉数量',
          data: [35, 32, 28, 30, 32, 28],
          borderColor: '#165DFF',
          fill: true,
          backgroundColor: 'rgba(22, 93, 255, 0.1)'
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top'
          }
        }
      }
    })
  }
}

// 更新投诉类型图表
const updateComplaintTypeChart = async () => {
  if (typeChartInstance) {
    typeChartInstance.destroy()
  }
  await nextTick()
  if (complaintTypeChart.value) {
    const ctx = complaintTypeChart.value.getContext('2d')
    typeChartInstance = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: complaintTypeDistribution.value.map(item => item.label),
        datasets: [{
          data: complaintTypeDistribution.value.map(item => item.percentage),
          backgroundColor: ['#FF4D4F', '#FAAD14', '#165DFF', '#52C41A', '#86909C', '#1D2129']
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'right'
          }
        }
      }
    })
  }
}

// 处理图片预览
const handlePictureCardPreview = file => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

// 处理文件移除
const handleRemove = (file, fileList) => {
  console.log(file, fileList)
}

// 导出数据
const exportData = () => {
  ElMessage.success('数据导出成功')
}

// 初始化加载数据
onMounted(() => {
  fetchComplaints()
  updateComplaintTrendChart()
  updateComplaintTypeChart()
})

// 组件卸载时销毁图表实例
onUnmounted(() => {
  if (trendChartInstance) trendChartInstance.destroy()
  if (typeChartInstance) typeChartInstance.destroy()
})
</script>

<style scoped>
.complaint-container {
  padding: 20px;
}
</style>