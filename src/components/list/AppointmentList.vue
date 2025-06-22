<template>
  <el-card class="appointment-list-card">
    <div class="list-header">
      <h3>预约看房列表</h3>
      <div>共 <span class="font-medium text-primary">156</span> 条数据</div>
    </div>
    <el-table :data="appointments" style="width: 100%">
      <el-table-column prop="id" label="预约编号" width="120" />
      <el-table-column label="客户信息">
        <template #default="scope">
          <div class="customer-info">
            <el-avatar :src="scope.row.avatar" size="small" />
            <div>
              <p>{{ scope.row.customerName }}</p>
              <p class="text-sm text-gray-500">{{ scope.row.phone }}</p>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="property" label="预约楼盘" width="120" />
      <el-table-column label="预约时间">
        <template #default="scope">
          <p>{{ scope.row.date }}</p>
          <p class="text-sm text-gray-500">{{ scope.row.timeRange }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="consultant" label="预约顾问" width="120" />
      <el-table-column label="状态">
        <template #default="scope">
          <el-tag :type="getTagType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" type="text" @click="$emit('view-detail', scope.row.id)">查看详情</el-button>
          <el-button v-if="scope.row.status === '待确认'" size="small" type="text" @click="$emit('confirm', scope.row.id)">确认预约</el-button>
          <el-button size="small" type="text" @click="$emit('cancel', scope.row.id)">取消预约</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
    />
  </el-card>
</template>

<script setup>
import { ref } from 'vue'

const appointments = ref([
  // 预约数据
  {
    id: '#AP20250618001',
    customerName: '李先生',
    phone: '138****6789',
    avatar: 'https://picsum.photos/id/1001/100/100',
    property: '朝阳一号',
    date: '2025-06-20',
    timeRange: '10:00-11:30',
    consultant: '张经理',
    status: '待确认'
  }
  // 其他预约数据
])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(156)

const getTagType = (status) => {
  switch (status) {
    case '待确认':
      return 'warning'
    case '已确认':
      return 'success'
    case '已完成':
      return 'primary'
    case '已取消':
      return 'info'
    default:
      return 'info'
  }
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  // 处理分页逻辑
}
</script>

<style scoped>
.appointment-list-card {
  margin-bottom: 20px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.customer-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
