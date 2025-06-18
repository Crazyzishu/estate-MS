<template>
  <el-card class="filter-card">
    <el-form :model="filters" label-width="90px" label-position="left">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="预约编号">
            <el-input v-model="filters.id" placeholder="请输入预约编号" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="客户姓名">
            <el-input v-model="filters.customerName" placeholder="请输入客户姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态">
            <el-select v-model="filters.status" placeholder="请选择">
              <el-option label="全部" value="" />
              <el-option label="待确认" value="待确认" />
              <el-option label="已确认" value="已确认" />
              <el-option label="已完成" value="已完成" />
              <el-option label="已取消" value="已取消" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="filter-actions">
        <el-button @click="$emit('reset')">重置</el-button>
        <el-button type="primary" @click="$emit('search', filters)">搜索</el-button>
      </div>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'

const filters = ref({
  id: '',
  customerName: '',
  status: ''
})
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}

.filter-actions {
  margin-top: 20px;
}
</style>
