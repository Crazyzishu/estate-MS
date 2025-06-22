<template>
  <el-dialog v-model="visible" title="确认预约" width="30%">
    <div class="text-center">
      <div class="icon-container">
        <i class="el-icon-check text-success"></i>
      </div>
      <h4>确认该预约看房</h4>
      <p>确认后将通知客户预约已确认，并发送看房提醒</p>
      <el-form-item label="备注信息">
        <el-input v-model="remark" type="textarea" :rows="3" />
      </el-form-item>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="confirm">确认预约</el-button>
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

const emit = defineEmits(['update:modelValue', 'confirm'])

const visible = ref(props.modelValue)
const remark = ref('')

const confirm = () => {
  emit('confirm', remark.value)
  emit('update:modelValue', false)
}
</script>

<style scoped>
.icon-container {
  font-size: 48px;
  margin-bottom: 20px;
}

.text-success {
  color: #67C23A;
}
</style>
