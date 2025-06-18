<template>
  <el-dialog v-model="visible" title="新增预约看房" width="50%">
    <el-form :model="appointment" label-width="120px">
      <el-form-item label="客户姓名">
        <el-input v-model="appointment.customerName" />
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="appointment.phone" />
      </el-form-item>
      <el-form-item label="预约楼盘">
        <el-select v-model="appointment.property">
          <el-option label="朝阳一号" value="朝阳一号" />
          <el-option label="海淀华府" value="海淀华府" />
          <el-option label="东城国际" value="东城国际" />
          <el-option label="西城时代" value="西城时代" />
          <el-option label="南城花园" value="南城花园" />
        </el-select>
      </el-form-item>
      <el-form-item label="预约顾问">
        <el-select v-model="appointment.consultant">
          <el-option label="张经理 (ID: 1001)" value="1001" />
          <el-option label="李经理 (ID: 1002)" value="1002" />
          <el-option label="王经理 (ID: 1003)" value="1003" />
          <el-option label="赵经理 (ID: 1004)" value="1004" />
          <el-option label="陈经理 (ID: 1005)" value="1005" />
        </el-select>
      </el-form-item>
      <el-form-item label="预约日期">
        <el-date-picker v-model="appointment.date" type="date" placeholder="请选择日期" />
      </el-form-item>
      <el-form-item label="预约时间段">
        <el-select v-model="appointment.timeRange">
          <el-option label="09:00-10:30" value="09:00-10:30" />
          <el-option label="10:00-11:30" value="10:00-11:30" />
          <el-option label="14:00-15:30" value="14:00-15:30" />
          <el-option label="15:00-16:30" value="15:00-16:30" />
          <el-option label="16:00-17:30" value="16:00-17:30" />
        </el-select>
      </el-form-item>
      <el-form-item label="客户来源">
        <el-select v-model="appointment.source">
          <el-option label="线上广告" value="线上广告" />
          <el-option label="社交媒体" value="社交媒体" />
          <el-option label="朋友推荐" value="朋友推荐" />
          <el-option label="电话咨询" value="电话咨询" />
          <el-option label="现场咨询" value="现场咨询" />
          <el-option label="其他渠道" value="其他渠道" />
        </el-select>
      </el-form-item>
      <el-form-item label="客户需求">
        <el-select v-model="appointment.need">
          <el-option label="刚需购房" value="刚需购房" />
          <el-option label="改善型住房" value="改善型住房" />
          <el-option label="投资购房" value="投资购房" />
          <el-option label="养老住房" value="养老住房" />
          <el-option label="学区房" value="学区房" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注信息">
        <el-input v-model="appointment.remark" type="textarea" :rows="3" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submitAppointment">提交预约</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'submit'])

const visible = ref(props.modelValue)
const appointment = ref({
  customerName: '',
  phone: '',
  property: '',
  consultant: '',
  date: '',
  timeRange: '',
  source: '',
  need: '',
  remark: ''
})

const submitAppointment = () => {
  emit('submit', appointment.value)
  emit('update:modelValue', false)
}
</script>
