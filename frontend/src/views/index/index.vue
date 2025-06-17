<script setup>

</script>

<template>
    <div class="home">
      <el-header style="height: auto; padding: 0;">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1"><el-icon><HomeFilled /></el-icon>房源管理</el-menu-item>
          <el-menu-item index="2"><el-icon><UserFilled /></el-icon>客户管理</el-menu-item>
          <el-menu-item index="3"><el-icon><List /></el-icon>合同管理</el-menu-item>
          <el-menu-item index="4"><el-icon><TrendCharts /></el-icon>楼盘动态</el-menu-item>
          <el-menu-item index="5"><el-icon><PhoneFilled /></el-icon>投诉处理</el-menu-item>
          <el-menu-item index="6">用户权限管理</el-menu-item>
          <el-menu-item index="7">数据统计</el-menu-item>
        </el-menu>
      </el-header>
  
      <el-main>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="always">
              <h3>房源数量分布</h3>
              <div ref="pieChart" style="width: 100%; height: 400px;"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="always">
              <h3>销售额趋势</h3>
              <div ref="lineChart" style="width: 100%; height: 400px;"></div>
            </el-card>
          </el-col>
        </el-row>
  
        <el-card shadow="always" style="margin-top: 20px;">
          <h3>最近消息通知</h3>
          <el-timeline>
            <el-timeline-item timestamp="2025/06/15" placement="top">
              <el-card>
                <p>新房源上线：朝阳区XX小区</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item timestamp="2025/06/14" placement="top">
              <el-card>
                <p>客户预约看房：张三预约查看朝阳区XX小区</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item timestamp="2025/06/13" placement="top">
              <el-card>
                <p>投诉提交：李四投诉房源描述与实际不符</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-main>
    </div>
  </template>
  
  <script>
  import * as echarts from 'echarts'
  import axios from 'axios'
  
  export default {
    data() {
      return {
        activeIndex: '1',
        pieChart: null,
        lineChart: null
      }
    },
    mounted() {
      this.initPieChart()
      this.initLineChart()
    },
    methods: {
      handleSelect(key) {
        console.log(key)
      },
      async initPieChart() {
        const response = await axios.get('/api/statistics', { params: { type: '房源数量分布' } })
        const data = response.data.data.list.map(item => ({ value: item.count, name: item.type }))
        this.pieChart = echarts.init(this.$refs.pieChart)
        this.pieChart.setOption({
          series: [
            {
              name: '房源数量分布',
              type: 'pie',
              radius: '50%',
              data: data
            }
          ]
        })
      },
      async initLineChart() {
        const response = await axios.get('/api/statistics', { params: { type: '销售额趋势' } })
        const dates = response.data.data.dates
        const sales = response.data.data.sales
        this.lineChart = echarts.init(this.$refs.lineChart)
        this.lineChart.setOption({
          xAxis: {
            type: 'category',
            data: dates
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            data: sales,
            type: 'line'
          }]
        })
      }
    }
  }
  </script>
  
  <style scoped>
  .el-header {
    background-color: #B3C0D1;
    color: var(--el-text-color-primary);
    text-align: center;
    line-height: 60px;
  }
  
  .el-main {
    background-color: #E9EEF3;
    color: var(--el-text-color-primary);
    text-align: center;
    line-height: 160px;
  }
  
  body > .el-container {
    margin-bottom: 40px;
  }
  
  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }
  
  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
  </style>