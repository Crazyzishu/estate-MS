<template>
  <div class="contract-form">
    <h1>{{ isEditMode ? '编辑合同' : '新增合同' }}</h1>

    <!-- 表单 -->
    <el-form :model="contract" label-width="120px" @submit.prevent="saveContract">
      <!-- 客户选择 -->
      <el-form-item label="客户姓名">
        <el-select v-model="contract.clientId" placeholder="请选择客户">
          <el-option
            v-for="client in clients"
            :key="client.clientId"
            :label="client.name"
            :value="client.clientId"
          />
        </el-select>
      </el-form-item>

      <!-- 房源选择 -->
      <el-form-item label="房源地址">
        <el-select v-model="contract.houseId" placeholder="请选择房源">
          <el-option
            v-for="house in houses"
            :key="house.houseId"
            :label="house.address"
            :value="house.houseId"
          />
        </el-select>
      </el-form-item>

      <!-- 销售员选择 -->
      <el-form-item label="负责销售">
        <el-select v-model="contract.managerId" placeholder="请选择销售员">
          <el-option
            v-for="manager in managers"
            :key="manager.managerId"
            :label="manager.realName"
            :value="manager.managerId"
          />
        </el-select>
      </el-form-item>

      <!-- 签约日期 -->
      <el-form-item label="签约日期">
        <el-date-picker v-model="contract.signTime" type="date" placeholder="选择日期" />
      </el-form-item>

      <!-- 合同金额 -->
      <el-form-item label="合同金额">
        <el-input v-model.number="contract.totalAmount" placeholder="请输入金额">
          <template #append>元</template>
        </el-input>
      </el-form-item>

      <!-- 付款计划 -->
      <el-form-item label="付款计划">
        <el-input v-model="contract.paymentPlan" placeholder="例如：首付30%+贷款70%" />
      </el-form-item>

      <!-- 合同状态 -->
      <el-form-item label="合同状态">
        <el-select v-model="contract.status" placeholder="请选择状态">
          <el-option label="待签约" value="待签约" />
          <el-option label="已签约" value="已签约" />
          <el-option label="已完成" value="已完成" />
          <el-option label="已取消" value="已取消" />
        </el-select>
      </el-form-item>

      <!-- 合同文件上传 -->
      <el-form-item label="合同文件">
        <el-upload
          v-model:file-list="contractFiles"
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

      <!-- 提交按钮 -->
      <el-form-item>
        <el-button type="primary" native-type="submit">{{ isEditMode ? '保存修改' : '提交' }}</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();

// 初始化数据
const contract = ref({
  clientId: '',
  houseId: '',
  managerId: '',
  signTime: null,
  totalAmount: 0,
  paymentPlan: '',
  status: '待签约',
});

const clients = ref([]);
const houses = ref([]);
const managers = ref([]);

const contractFiles = ref([]);
const dialogVisible = ref(false);
const dialogImageUrl = ref('');

const isEditMode = ref(false);

// 获取所有客户、房源、销售员数据
const fetchData = async () => {
  try {
    const [clientsRes, housesRes, managersRes] = await Promise.all([
      axios.get('/api/contracts/clients'),
      axios.get('/api/contracts/houses'),
      axios.get('/api/contracts/managers'),
    ]);

    clients.value = clientsRes.data;
    houses.value = housesRes.data;
    managers.value = managersRes.data;

    if (route.params.id) {
      isEditMode.value = true;
      const res = await axios.get(`/api/contracts/${route.params.id}`);
      contract.value = res.data;
    }
  } catch (error) {
    console.error('Failed to fetch data:', error);
  }
};

// 保存合同
const saveContract = async () => {
  try {
    if (isEditMode.value) {
      await axios.put(`/api/contracts/${contract.value.contractId}`, contract.value);
    } else {
      await axios.post('/api/contracts', contract.value);
    }

    router.push('/contracts');
  } catch (error) {
    console.error('Failed to save contract:', error);
  }
};

// 文件操作
const handleRemove = (file) => {
  console.log('Removed file:', file);
};

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

// 初始化数据
onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.contract-form {
  max-width: 800px;
  margin: 0 auto;
}
</style>
