<script setup>
import { ref, onMounted, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { queryPageApi, addHouseApi, queryInfoApi, updateHouseApi, deleteHouseApi } from '../api/house';
// 引入需要的图标
import { View, Edit, Delete, Plus } from '@element-plus/icons-vue';

// 钩子函数,初始化加载数据
onMounted(() => {
  search();
});

// 搜索表单对象
const searchHouse = ref({
  addressKeyword: '',
  minPrice: '0',
  maxPrice: '50000',
  status: ''
});

// 房源列表
const houseList = ref([]);

// 分页
const currentPage = ref(1);//页码
const pageSize = ref(5);//每页展示记录数
const total = ref(0);//总记录数

//查询房源信息
const search = async () => { 
  const result = await queryPageApi(
    searchHouse.value.addressKeyword,
    searchHouse.value.minPrice,
    searchHouse.value.maxPrice,
    searchHouse.value.status,
    currentPage.value,
    pageSize.value
  );
  if(result.code){
    total.value = result.data.total;
    houseList.value = result.data.rows;
  }
};

//清空
const clear = () => { 
  searchHouse.value = {
    addressKeyword: '',
    minPrice: '0',
    maxPrice: '50000',
    status: ''
  };
  search();
};

// 房源表单相关
const dialogVisible = ref(false);
const dialogTitle = ref('房源录入');

// 房源详情相关
const isHouseDetailOpen = ref(false);
const currentHouseId = ref('');
const currentHouseDetails = ref({});
const showImageViewer = ref(false);
const currentImageIndex = ref(0);

// 获取行的唯一标识
const getRowKey = (row) => row.id;

// 格式化数字
const formatNumber = (number) => {
  if (!number) return '0';
  return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

// 表格操作
const openHouseDetail = async (houseId) => {
  const result = await queryInfoApi(houseId);
  if (result.code) {
    currentHouseDetails.value = result.data;
    currentHouseId.value = houseId;
    isHouseDetailOpen.value = true;
  } else {
    ElMessage.error('获取房源信息失败！');
  }
};

const editHouse = async (houseId) => {
  const result = await queryInfoApi(houseId);
  console.log('API 返回结果:', result); // 添加调试日志
  if (result.code) {
    dialogVisible.value = true;
    dialogTitle.value = '修改房源信息';
    house.value = result.data;
  } else {
    ElMessage.error('查询客户信息失败！');
  }
};

const deleteHouse = (houseId) => {
  ElMessageBox.confirm('确定要删除该房源吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 用户点击确定，调用删除接口
    const result = await deleteHouseApi(houseId);
    if (result.code) {
      ElMessage.success('删除成功');
      search(); // 刷新列表
    } else {
      ElMessage.error('删除失败');
    }
  }).catch(() => {
    // 用户点击取消，不做任何操作
    ElMessage.info('已取消删除');
  });
};

// 模态框操作
const toggleHouseModal = () => {
  dialogTitle.value = '房源录入';
  dialogVisible.value = !dialogVisible.value;
  if (!dialogVisible.value) {
    resetForm();
  }
};

const resetForm = () => {
  house.value = {
    type: '住宅',
    address: '',
    price: null,
    area: null,
    roomType: '',
    managerId: null,
    description: '',
    images: ''
  };
  dialogVisible.value = !dialogVisible.value;
};

const house = ref({
  houseId: null,
  type: '住宅',
  address: '',
  price: null,
  area: null,
  roomType: '',
  managerId: null,
  description: '',
  images: ''
});

const save = async () => {
  if(house.value.houseId){
    const result = await updateHouseApi(house.value);
    if (result.code) { // 成功
      ElMessage.success('保存成功');
      dialogVisible.value = false;
      search();
    } else { // 失败
      ElMessage.error('保存失败');
    }
  } else { 
    const result = await addHouseApi(house.value);
    if (result.code) { // 成功
      ElMessage.success('保存成功');
      dialogVisible.value = false;
      search();
    } else { // 失败
      ElMessage.error('保存失败');
    }
  }
};

// 文件上传
const handleRemove = (file, fileList) => {
  console.log('文件移除', file, fileList);
};

const handlePictureCardPreview = (file) => {
  currentImageIndex.value = currentHouseDetails.value.images.indexOf(file.url);
  showImageViewer.value = true;
};

// 分页操作
const handleSizeChange = (newSize) => {
  pageSize.value = newSize;
  search();
};

const handleCurrentChange = (pageNum) => {
  currentPage.value = pageNum;
  search();
};
</script>
<template>
  <div class="houses-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="title">房源管理</h1>
      <p class="description">管理和维护所有房源信息</p>
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="toggleHouseModal">
        <el-icon><Plus /></el-icon> 新增房源
      </el-button>
      <!-- <el-button @click="exportData">
        <el-icon><Download /></el-icon> 导出数据
      </el-button>
      <el-button>
        <el-icon><Upload /></el-icon> 导入数据
      </el-button> -->
    </div>

    <!-- 搜索栏 -->
    <el-card class="filter-card">
      <el-form :model="searchHouse" label-width="90px" label-position="left">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="地址关键词">
              <el-input v-model="searchHouse.addressKeyword" placeholder="请输入地址关键词" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="价格区间">
              <el-input-number v-model="searchHouse.minPrice" :controls="false" placeholder="最低价" />
              <span class="price-range-separator">-</span>
              <el-input-number v-model="searchHouse.maxPrice" :controls="false" placeholder="最高价" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="房源状态">
              <el-select v-model="searchHouse.status" placeholder="请选择">
                <el-option label="全部" value="" />
                <el-option label="待审核" value="待审核" />
                <el-option label="在售" value="在售" />
                <el-option label="已售" value="已售" />
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

    <!-- 房源列表 -->
    <el-card>
      <div class="list-header">
        <h3 class="list-title">房源列表</h3>
        <span class="total-count">共 <span class="primary-text">{{ total }}</span> 条数据</span>
      </div>

      <el-table :data="houseList" border style="width: 100%" :row-key="getRowKey">
        <el-table-column type="selection" width="40" />
        
        <el-table-column prop="houseId" label="房源ID" width="50" />
        
        <el-table-column prop="address" label="地址" width="250" />
        
        <el-table-column prop="price" label="价格" width="120">
          <template #default="scope">
            ¥{{ formatNumber(scope.row.price) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="area" label="面积" width="100">
          <template #default="scope">
            {{ scope.row.area }}㎡
          </template>
        </el-table-column>
        
        <el-table-column prop="roomType" label="户型" width="120" />

        <el-table-column prop="description" label="描述" width="200"></el-table-column>
        
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag 
              :type="scope.row.status === '在售' ? 'success' : 
                     scope.row.status === '待审核' ? 'warning' : 'info'"
              size="small"
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作">
          <template #default="scope">
            <el-button 
              size="small" 
              :icon="View" 
              @click="openHouseDetail(scope.row.houseId)"
              plain
            >详情</el-button>
            <el-button 
              size="small" 
              :icon="Edit" 
              type="primary"
              @click="editHouse(scope.row.houseId)"
              plain
            >编辑</el-button>
            <el-button 
              size="small" 
              :icon="Delete" 
              type="danger"
              plain
              @click="deleteHouse(scope.row.houseId)"
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

    <!-- 房源录入/房源修改的对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="60%"
      :before-close="resetForm"
    >
      <el-form :model="house" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房源类型">
              <el-select v-model="house.type" placeholder="请选择">
                <el-option label="住宅" value="住宅" />
                <el-option label="商业" value="商业" />
                <el-option label="租赁" value="租赁" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房源地址">
              <el-input v-model="house.address" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格 (元)">
              <el-input-number v-model="house.price" :controls="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面积 (㎡)">
              <el-input-number v-model="house.area" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="户型">
              <el-input v-model="house.roomType" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责销售人员ID">
              <el-input-number v-model="house.managerId" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="房源描述">
          <el-input v-model="house.description" type="textarea" :rows="3" />
        </el-form-item>

        <el-form-item label="房源图片">
          <el-upload
            v-model="house.images"
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
          <p class="upload-hint">支持 JPG、PNG 格式，最大 5MB</p>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="resetForm">取消</el-button>
          <el-button type="primary" @click="save">
            {{ house.houseId ? '保存' : '提交' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 房源详情抽屉 -->
    <el-drawer
      v-model="isHouseDetailOpen"
      :title="'房源详情 - #' + currentHouseId"
      direction="rtl"
      size="60%"
    >
      <div class="house-detail">
        <div class="detail-section">
          <h4 class="section-title">基本信息</h4>
          <!-- <div class="detail-row">
            <span class="label">名称：</span>
            <span>{{ currentHouseDetails.name }}</span>
          </div> -->
          <div class="detail-row">
            <span class="label">地址：</span>
            <span>{{ currentHouseDetails.address }}</span>
          </div>
          <div class="detail-row">
            <span class="label">价格：</span>
            <span class="price">¥{{ formatNumber(currentHouseDetails.price) }}</span>
          </div>
          <div class="detail-row">
            <span class="label">面积：</span>
            <span>{{ currentHouseDetails.area }}㎡</span>
          </div>
          <div class="detail-row">
            <span class="label">户型：</span>
            <span>{{ currentHouseDetails.roomType }}</span>
          </div>
          <!-- <div class="detail-row">
            <span class="label">朝向：</span>
            <span>{{ currentHouseDetails.orientation || '南北通透' }}</span>
          </div> -->
        </div>

        <div class="detail-section">
          <h4 class="section-title">详细信息</h4>
          <div class="detail-row">
            <span class="label">房源类型：</span>
            <span>{{ currentHouseDetails.type }}</span>
          </div>
          <div class="detail-row">
            <span class="label">装修情况：</span>
            <span>{{ currentHouseDetails.decoration || '精装修' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">楼层：</span>
            <span>{{ currentHouseDetails.floor || '中层/共18层' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">电梯：</span>
            <span>{{ currentHouseDetails.elevator || '3部' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">产权：</span>
            <span>{{ currentHouseDetails.titleDeed || '商品房' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">建成年代：</span>
            <span>{{ currentHouseDetails.buildYear || '2018年' }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4 class="section-title">销售信息</h4>
          <div class="detail-row">
            <span class="label">上架时间：</span>
            <span>{{ currentHouseDetails.listingDate || '2025-06-15' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">状态：</span>
            <span :class="{
              'text-success': currentHouseDetails.status === '在售',
              'text-warning': currentHouseDetails.status === '待审核'
            }">
              {{ currentHouseDetails.status }}
            </span>
          </div>
          <div class="detail-row">
            <span class="label">负责销售：</span>
            <span>{{ currentHouseDetails.salesperson || '李经理 (ID: 1001)' }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4 class="section-title">房源描述</h4>
          <p class="description-text">{{ currentHouseDetails.description }}</p>
        </div>

        <div class="detail-section">
          <h4 class="section-title">房源图片</h4>
          <div class="image-gallery">
            <el-image 
              v-for="(image, index) in currentHouseDetails.images" 
              :key="index" 
              :src="image" 
              class="gallery-image"
              @click="previewImage(image)"
            />
          </div>
        </div>

        <div class="detail-actions">
          <!-- <el-button type="primary" @click="editHouse(currentHouseDetails.id)">编辑房源</el-button> -->
          <!-- <el-button @click="deleteHouse(currentHouseDetails.id)">删除房源</el-button> -->
        </div>
      </div>
    </el-drawer>

    <!-- 图片预览 -->
    <el-image-viewer
      v-if="showImageViewer"
      :url-list="currentHouseDetails.images"
      :initial-index="currentImageIndex"
      @close="showImageViewer = false"
    />
  </div>
</template>
<style scoped>
.houses-container {
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

.price-range-separator {
  margin: 0 10px;
  color: #999;
}

.filter-actions {
  text-align: right;
  margin-top: 20px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.list-title {
  font-weight: bold;
  color: #1D2129;
}

.total-count {
  font-size: 14px;
  color: #86909C;
}

.primary-text {
  color: #165DFF;
}

.house-info {
  display: flex;
  align-items: center;
}

.house-image {
  width: 40px;
  height: 40px;
  margin-right: 10px;
  border-radius: 4px;
  object-fit: cover;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.house-detail {
  padding: 20px;
}

.detail-section {
  margin-bottom: 20px;
}

.section-title {
  font-weight: bold;
  margin-bottom: 10px;
  padding-bottom: 5px;
  border-bottom: 1px solid #eee;
  margin-bottom: 10px;
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

.price {
  font-weight: bold;
  color: #165DFF;
}

.description-text {
  line-height: 1.6;
  color: #5E6670;
}

.image-gallery {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 10px;
}

.gallery-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
  transition: transform 0.2s;
}

.gallery-image:hover {
  transform: scale(1.05);
}

.detail-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}
</style>