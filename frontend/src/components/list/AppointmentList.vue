<template>
  <div class="appointment-list">
    <!-- 表格 -->
    <el-table :data="paginatedAppointments" border style="width: 100%">
      <el-table-column prop="appointmentId" label="预约ID"></el-table-column>
      <el-table-column prop="clientId" label="客户ID"></el-table-column>
      <el-table-column prop="houseId" label="房源ID"></el-table-column>
      <el-table-column prop="visitTime" label="预约时间"></el-table-column>
      <el-table-column prop="managerId" label="销售ID"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-tag :type="getTagType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
    </el-table>

    <!-- 分页器 -->
    <el-pagination
      layout="prev, pager, next"
      :total="appointments.length"
      :page-size="pageSize"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; text-align: center"
    />
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AppointmentList',
  data() {
    return {
      appointments: [], // 所有预约记录
      paginatedAppointments: [], // 当前页数据
      pageSize: 10,
      currentPage: 1,
      loading: true,
    };
  },
  mounted() {
    console.log('组件已挂载，准备加载预约列表');
    this.fetchAppointments();
  },
  methods: {
    /**
     * 获取预约列表数据
     */
    async fetchAppointments() {
      try {
        console.log('开始请求 /api/appointments 接口');
        const response = await axios.get('/api/appointments', {
          params: {
            pageNum: this.currentPage,
            pageSize: this.pageSize
          }
        });

        console.log('请求成功:', response.data);
        if (response.data.code === 1) {
          this.appointments = response.data.data || [];
          this.paginateData(this.currentPage);
        } else {
          console.error('获取数据失败:', response.data.msg);
        }
      } catch (error) {
        console.error('请求失败:', error.response?.data || error.message);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 分页处理函数
     */
    paginateData(page) {
      const start = (page - 1) * this.pageSize;
      const end = start + this.pageSize;
      this.paginatedAppointments = this.appointments.slice(start, end);
    },

    /**
     * 切换页码时触发
     */
    handleCurrentChange(page) {
      this.currentPage = page;
      this.paginateData(page);
    },

    /**
     * 根据状态映射 Tag 类型
     */
    getTagType(status) {
      switch (status) {
        case '已确认':
          return 'success';
        case '已完成':
          return 'info';
        case '待确认':
          return 'warning';
        default:
          return 'danger';
      }
    }
  }
};
</script>


<style scoped>
.appointment-list {
  padding: 10px;
}
</style>
