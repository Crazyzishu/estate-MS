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
      <el-button @click="exportData">
        <el-icon><Download /></el-icon> 导出数据
      </el-button>
      <el-button>
        <el-icon><Upload /></el-icon> 导入数据
      </el-button>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="filters" label-width="90px" label-position="left">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="地址关键词">
              <el-input v-model="filters.addressKeyword" placeholder="请输入地址关键词" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="价格区间">
              <el-input-number v-model="filters.minPrice" :controls="false" placeholder="最低价" />
              <span class="price-range-separator">-</span>
              <el-input-number v-model="filters.maxPrice" :controls="false" placeholder="最高价" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="房源状态">
              <el-select v-model="filters.status" placeholder="请选择">
                <el-option label="全部" value="" />
                <el-option label="待审核" value="待审核" />
                <el-option label="在售" value="在售" />
                <el-option label="已售" value="已售" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="filter-actions">
          <el-button @click="resetFilters">重置</el-button>
          <el-button type="primary" @click="searchHouses">搜索</el-button>
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
        
        <el-table-column prop="id" label="房源ID" width="150" />
        
        <el-table-column label="名称" width="200">
          <template #default="scope">
            <div class="house-info">
              <el-image :src="scope.row.image" class="house-image" />
              <span>{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="address" label="地址" />
        
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
        
        <el-table-column label="状态" width="100">
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
        
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button 
              size="small" 
              icon="View" 
              @click="openHouseDetail(scope.row.id)"
              plain
            />
            <el-button 
              size="small" 
              icon="Edit" 
              plain
            />
            <el-button 
              size="small" 
              icon="Delete" 
              plain
              @click="deleteHouse(scope.row.id)"
            />
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

    <!-- 房源录入模态框 -->
    <el-dialog
      v-model="isHouseModalOpen"
      title="房源录入"
      width="60%"
      :before-close="handleClose"
    >
      <el-form :model="currentHouse" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房源类型">
              <el-select v-model="currentHouse.type" placeholder="请选择">
                <el-option label="住宅" value="住宅" />
                <el-option label="商业" value="商业" />
                <el-option label="租赁" value="租赁" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房源地址">
              <el-input v-model="currentHouse.address" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格 (元)">
              <el-input-number v-model="currentHouse.price" :controls="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面积 (㎡)">
              <el-input-number v-model="currentHouse.area" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="户型">
              <el-input v-model="currentHouse.roomType" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责销售人员ID">
              <el-input-number v-model="currentHouse.salespersonId" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="房源描述">
          <el-input v-model="currentHouse.description" type="textarea" :rows="3" />
        </el-form-item>

        <el-form-item label="房源图片">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
          >
            <!-- <el-button size="small">
              <el-icon><Plus /></el-icon> 点击上传
            </el-button> -->
            <template #default>
              <i class="el-icon-plus"></i>
            </template>
          </el-upload>
          <p class="upload-hint">支持 JPG、PNG 格式，最大 5MB</p>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="isHouseModalOpen = false">取消</el-button>
          <el-button type="primary" @click="saveHouse">
            {{ currentHouse.id ? '保存修改' : '提交' }}
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
          <div class="detail-row">
            <span class="label">名称：</span>
            <span>{{ currentHouseDetails.name }}</span>
          </div>
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
          <div class="detail-row">
            <span class="label">朝向：</span>
            <span>{{ currentHouseDetails.orientation || '南北通透' }}</span>
          </div>
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
          <el-button type="primary" @click="editHouse(currentHouseDetails.id)">编辑房源</el-button>
          <el-button @click="deleteHouse(currentHouseDetails.id)">删除房源</el-button>
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

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';

// 过滤器
const filters = reactive({
  addressKeyword: '',
  minPrice: null,
  maxPrice: null,
  status: ''
});

// 分页
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(248);

// 房源列表
const houseList = ref([
  {
    id: '#20250615001',
    name: '朝阳区XX小区',
    address: '朝阳区XX路XX号',
    price: 12000000,
    area: 120,
    roomType: '3室2厅1卫',
    type: '住宅',
    status: '在售',
    image: 'https://picsum.photos/id/1040/100/100'
  },
  {
    id: '#20250615002',
    name: '海淀区XX花园',
    address: '海淀区XX路XX号',
    price: 15800000,
    area: 150,
    roomType: '4室2厅2卫',
    type: '住宅',
    status: '待审核',
    image: 'https://picsum.photos/id/1067/100/100'
  },
  {
    id: '#20250615003',
    name: '东城区XX大厦',
    address: '东城区XX路XX号',
    price: 22500000,
    area: 200,
    roomType: '整层',
    type: '商业',
    status: '已售',
    image: 'https://picsum.photos/id/164/100/100'
  },
  {
    id: '#20250615004',
    name: '西城区XX公寓',
    address: '西城区XX路XX号',
    price: 9600000,
    area: 90,
    roomType: '2室1厅1卫',
    type: '住宅',
    status: '在售',
    image: 'https://picsum.photos/id/1048/100/100'
  },
  {
    id: '#20250615005',
    name: '丰台区XX别墅',
    address: '丰台区XX路XX号',
    price: 35000000,
    area: 350,
    roomType: '5室3厅3卫',
    type: '住宅',
    status: '在售',
    image: 'https://picsum.photos/id/1076/100/100'
  }
]);

// 房源表单相关
const isHouseModalOpen = ref(false);
const currentHouse = ref({
  id: null,
  type: '住宅',
  address: '',
  price: null,
  area: null,
  roomType: '',
  salespersonId: null,
  description: '',
  images: []
});

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
const openHouseDetail = (houseId) => {
  const house = houseList.value.find(house => house.id === houseId);
  if (house) {
    currentHouseDetails.value = {...house};
    currentHouseId.value = houseId;
    isHouseDetailOpen.value = true;
  }
};

const editHouse = (houseId) => {
  const house = houseList.value.find(h => h.id === houseId);
  if (house) {
    currentHouse.value = {...house};
    isHouseModalOpen.value = true;
  }
};

const deleteHouse = (houseId) => {
  ElMessage.success('房源删除成功！'); // 实际使用时应替换为真实API调用
};

// 模态框操作
const toggleHouseModal = () => {
  isHouseModalOpen.value = !isHouseModalOpen.value;
  if (!isHouseModalOpen.value) {
    resetForm();
  }
};

const resetForm = () => {
  currentHouse.value = {
    id: null,
    type: '住宅',
    address: '',
    price: null,
    area: null,
    roomType: '',
    salespersonId: null,
    description: '',
    images: []
  };
};

const saveHouse = () => {
  ElMessage.success(currentHouse.value.id ? '房源信息更新成功！' : '房源信息提交成功！');
  toggleHouseModal();
  fetchHouses(); // 刷新房源列表
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
  fetchHouses();
};

const handleCurrentChange = (pageNum) => {
  currentPage.value = pageNum;
  fetchHouses();
};

// 数据加载
const fetchHouses = async () => {
  try {
    // 这里应该使用实际API获取数据
    // 示例：const response = await axios.get('/api/houses', { params: { ...filters, page: currentPage.value, limit: pageSize.value } });
    // houseList.value = response.data.items;
    // total.value = response.data.total;
    
    // 由于没有实际API，我们模拟分页
    // 在实际项目中应替换为真实的API调用
    ElMessage.info(`正在查询第${currentPage.value}页，每页${pageSize.value}条`);
  } catch (error) {
    ElMessage.error('房源数据加载失败');
    console.error(error);
  }
};

// 初始化加载数据
onMounted(() => {
  fetchHouses();
});
</script>

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