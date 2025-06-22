<template>
  <el-dialog v-model="visible" title="取消预约" width="30%">
    <div class="text-center">
      <div class="icon-container">
        <i class="el-icon-close text-danger"></i>
      </div>
      <h4>取消该预约看房</h4>
      <p>取消后将通知客户预约已取消，请选择取消原因</p>
      <el-form-item label="取消原因">
        <el-select v-model="reason" placeholder="请选择原因">
          <el-option label="客户主动取消" value="客户主动取消" />
          <el-option label="时间冲突" value="时间冲突" />
          <el-option label="房源问题" value="房源问题" />
          <el-option label="价格不满意" value="价格不满意" />
          <el-option label="其他原因" value="其他原因" />
        </el-select>
      </el-form-item>
      <el-form-item label="详细说明">
        <el-input v-model="remark" type="textarea" :rows="3" placeholder="请输入详细说明（选填）" />
      </el-form-item>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="danger" @click="cancel">确认取消</el-button>
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

const emit = defineEmits(['update:modelValue', 'cancel'])

const visible = ref(props.modelValue)
const reason = ref('')
const remark = ref('')

const cancel = () => {
  emit('cancel', { reason, remark })
  emit('update:modelValue', false)
}
</script>

<style scoped>
.icon-container {
  font-size: 48px;
  margin-bottom: 20px;
}

.text-danger {
  color: #F56C6C;
}
</style>
