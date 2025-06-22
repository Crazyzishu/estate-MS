<template>
  <div class="appointments-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>预约看房管理</h1>
      <p>查看和管理所有预约看房请求</p>
    </div>
    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="openAddAppointmentModal">
        <el-icon><Plus /></el-icon> 新增预约
      </el-button>
      <el-button>
        <el-icon><Download /></el-icon> 导出数据
      </el-button>
    </div>
    <!-- 筛选条件 -->
    <AppointmentFilter @search="handleSearch" @reset="handleReset" />
    <!-- 预约统计卡片 -->
    <AppointmentStats />
    <!-- 预约趋势图 -->
    <AppointmentTrendChart />
    <!-- 预约列表 -->
    <AppointmentList @view-detail="openAppointmentDetail" @confirm="confirmAppointment" @cancel="cancelAppointment" />
    <!-- 新增预约模态框 -->
    <AddAppointmentModal v-model:visible="addAppointmentVisible" @submit="handleAddAppointment" />
    <!-- 确认预约模态框 -->
    <ConfirmAppointmentModal v-model:visible="confirmAppointmentVisible" @confirm="handleConfirmAppointment" />
    <!-- 取消预约模态框 -->
    <CancelAppointmentModal v-model:visible="cancelAppointmentVisible" @cancel="handleCancelAppointment" />
    <!-- 预约详情模态框 -->
    <AppointmentDetailModal v-model:visible="appointmentDetailVisible" :appointmentId="selectedAppointmentId" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus, Download } from '@element-plus/icons-vue'
import AppointmentFilter from '../components/filter/AppointmentFilter.vue'
import AppointmentStats from '../components/status/AppointmentStats.vue'
import AppointmentTrendChart from '../components/charts/AppointmentTrendChart.vue'
import AppointmentList from '../components/list/AppointmentList.vue'
import AddAppointmentModal from '../components/modal/AddAppointmentModal.vue'
import ConfirmAppointmentModal from '../components/modal/ConfirmAppointmentModal.vue'
import CancelAppointmentModal from '../components/modal/CancelAppointmentModal.vue'
import AppointmentDetailModal from '../components/modal/AppointmentDetailModal.vue'

const addAppointmentVisible = ref(false)
const confirmAppointmentVisible = ref(false)
const cancelAppointmentVisible = ref(false)
const appointmentDetailVisible = ref(false)
const selectedAppointmentId = ref('')

const openAddAppointmentModal = () => {
  addAppointmentVisible.value = true
}

const openAppointmentDetail = (id) => {
  selectedAppointmentId.value = id
  appointmentDetailVisible.value = true
}

const confirmAppointment = (id) => {
  selectedAppointmentId.value = id
  confirmAppointmentVisible.value = true
}

const cancelAppointment = (id) => {
  selectedAppointmentId.value = id
  cancelAppointmentVisible.value = true
}

const handleSearch = (filters) => {
  // 处理搜索逻辑
  console.log('搜索条件:', filters)
}

const handleReset = () => {
  // 处理重置逻辑
  console.log('重置筛选条件')
}

const handleAddAppointment = (appointment) => {
  // 处理新增预约逻辑
  console.log('新增预约:', appointment)
  addAppointmentVisible.value = false
}

const handleConfirmAppointment = () => {
  // 处理确认预约逻辑
  console.log('确认预约')
  confirmAppointmentVisible.value = false
}

const handleCancelAppointment = () => {
  // 处理取消预约逻辑
  console.log('取消预约')
  cancelAppointmentVisible.value = false
}
</script>

<style scoped>
.appointments-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.action-buttons {
  margin-bottom: 20px;
}
</style>
