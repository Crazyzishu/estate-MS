<template>
  <el-dialog v-model="visible" :title="`预约详情 ${appointmentId}`" width="70%">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <h4>预约基本信息</h4>
        <div class="detail-item">
          <span class="label">预约编号：</span>
          <span>{{ appointmentId }}</span>
        </div>
        <div class="detail-item">
          <span class="label">客户姓名：</span>
          <span>{{ appointment.customerName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">手机号码：</span>
          <span>{{ appointment.phone }}</span>
        </div>
        <div class="detail-item">
          <span class="label">预约楼盘：</span>
          <span>{{ appointment.property }}</span>
        </div>
        <div class="detail-item">
          <span class="label">预约日期：</span>
          <span>{{ appointment.date }}</span>
        </div>
        <div class="detail-item">
          <span class="label">预约时间段：</span>
          <span>{{ appointment.timeRange }}</span>
        </div>
        <div class="detail-item">
          <span class="label">预约顾问：</span>
          <span>{{ appointment.consultant }}</span>
        </div>
        <div class="detail-item">
          <span class="label">状态：</span>
          <el-tag :type="getTagType(appointment.status)">{{ appointment.status }}</el-tag>
        </div>
      </div>
      <div>
        <h4>客户信息</h4>
        <div class="detail-item">
          <span class="label">客户来源：</span>
          <span>{{ appointment.source }}</span>
        </div>
        <div class="detail-item">
          <span class="label">客户需求：</span>
          <span>{{ appointment.need }}</span>
        </div>
        <div class="detail-item">
          <span class="label">备注信息：</span>
          <span>{{ appointment.remark }}</span>
        </div>
      </div>
    </div>
    <div class="mt-6">
      <h4>预约跟进记录</h4>
      <el-timeline>
        <el-timeline-item
          v-for="record in appointment.records"
          :key="record.time"
          :timestamp="record.time"
        >
          {{ record.content }}
        </el-timeline-item>
      </el-timeline>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  appointmentId: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(props.modelValue)

const appointment = computed(() => {
  // 根据 appointmentId 获取预约详情数据
  return {
    customerName: '李先生',
    phone: '138****6789',
    property: '朝阳一号',
    date: '2025-06-20',
    timeRange: '10:00-11:30',
    consultant: '张经理',
    status: '待确认',
    source: '线上广告',
    need: '刚需购房',
    remark: '李先生是第一次购房，对朝阳一号的小户型比较感兴趣，希望了解更多信息。',
    records: [
      {
        time: '2025-06-18 09:30:25',
        content: '客户李先生通过线上广告预约看房，对朝阳一号的小户型比较感兴趣。'
      }
    ]
  }
})

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
</script>

<style scoped>
.detail-item {
  display: flex;
  margin-bottom: 10px;
}

.label {
  width: 120px;
  font-weight: bold;
}
</style>
