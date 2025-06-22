<template>
    <div>
      <h1>数据概览</h1>
      <p>欢迎回来，张经理！这是您的房地产管理系统概览</p>
      
      <!-- 数据卡片 -->
      <el-row :gutter="20" style="margin: 20px 0">
        <el-col :span="6" v-for="(card, index) in cards" :key="index">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>{{ card.title }}</span>
              <el-button style="float: right; padding: 3px 0" type="text">{{ card.buttonText }}</el-button>
            </div>
            <div class="text item">
              <h3>{{ card.value }}</h3>
              <p :class="card.trendClass">
                <i :class="card.trendIcon"></i> {{ card.trendText }}
              </p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 图表 -->
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card>
            <div slot="header" class="clearfix">
              <span>销售趋势</span>
              <el-button-group style="float: right">
                <el-button type="primary" plain>本月</el-button>
                <el-button type="primary" plain>本季度</el-button>
                <el-button type="primary">本年度</el-button>
              </el-button-group>
            </div>
            <div ref="salesChart" style="height: 400px"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header" class="clearfix">
              <span>房源类型分布</span>
            </div>
            <div ref="houseTypeChart" style="height: 400px"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 最近房源 -->
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="16">
          <el-card>
            <div slot="header" class="clearfix">
              <span>最近房源</span>
              <el-button style="float: right; padding: 3px 0" type="text">查看全部</el-button>
            </div>
            <el-table :data="houses" border style="width: 100%">
              <el-table-column prop="id" label="房源ID" width="150"></el-table-column>
              <el-table-column label="名称" width="200">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <el-image :src="scope.row.image" style="width: 40px; height: 40px; margin-right: 10px"></el-image>
                    <span>{{ scope.row.name }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="address" label="地址"></el-table-column>
              <el-table-column prop="price" label="价格" width="120"></el-table-column>
              <el-table-column prop="area" label="面积" width="100"></el-table-column>
              <el-table-column label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '在售' ? 'success' : scope.row.status === '待审核' ? 'warning' : 'info'">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <el-button size="mini" icon="el-icon-view" @click="handleDetail(scope.$index, scope.row)"></el-button>
                  <el-button size="mini" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)"></el-button>
                  <el-button size="mini" icon="el-icon-delete" @click="handleDelete(scope.$index, scope.row)"></el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header" class="clearfix">
              <span>近期预约</span>
              <el-button style="float: right; padding: 3px 0" type="text">查看全部</el-button>
            </div>
            <div v-for="(appointment, index) in appointments" :key="index" style="margin-bottom: 20px">
              <div style="display: flex; align-items: center; margin-bottom: 10px">
                <el-avatar :src="appointment.avatar" style="margin-right: 10px"></el-avatar>
                <div>
                  <p style="margin: 0">{{ appointment.name }}</p>
                  <p style="margin: 0; color: #999">{{ appointment.time }}</p>
                </div>
              </div>
              <p style="margin: 5px 0">{{ appointment.property }}</p>
              <el-button-group>
                <el-button size="mini">确认</el-button>
                <el-button size="mini">推迟</el-button>
                <el-button size="mini">取消</el-button>
              </el-button-group>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import * as echarts from 'echarts'
  
  const cards = [
    { title: '总房源数', value: '248', trendClass: 'text-success', trendIcon: 'el-icon-arrow-up', trendText: '12% 较上月', buttonText: '' },
    { title: '本月销售额', value: '¥56,248,000', trendClass: 'text-success', trendIcon: 'el-icon-arrow-up', trendText: '8% 较上月', buttonText: '' },
    { title: '客户总数', value: '1,254', trendClass: 'text-success', trendIcon: 'el-icon-arrow-up', trendText: '15% 较上月', buttonText: '' },
    { title: '预约看房', value: '42', trendClass: 'text-danger', trendIcon: 'el-icon-arrow-down', trendText: '3% 较上月', buttonText: '' }
  ]
  
  const houses = [
    { id: '#20250615001', name: '朝阳区XX小区', address: '朝阳区XX路XX号', price: '¥12,000,000', area: '120㎡', status: '在售', image: 'https://picsum.photos/id/1040/100/100' },
    { id: '#20250615002', name: '海淀区XX花园', address: '海淀区XX路XX号', price: '¥15,800,000', area: '150㎡', status: '待审核', image: 'https://picsum.photos/id/1067/100/100' },
    { id: '#20250615003', name: '东城区XX大厦', address: '东城区XX路XX号', price: '¥22,500,000', area: '200㎡', status: '已售', image: 'https://picsum.photos/id/164/100/100' },
    { id: '#20250615004', name: '西城区XX公寓', address: '西城区XX路XX号', price: '¥9,600,000', area: '90㎡', status: '在售', image: 'https://picsum.photos/id/1048/100/100' }
  ]
  
  const appointments = [
    { name: '李先生', time: '今天 14:00', property: '朝阳区XX小区', avatar: 'https://picsum.photos/id/1012/100/100' },
    { name: '王女士', time: '今天 16:30', property: '海淀区XX花园', avatar: 'https://picsum.photos/id/1027/100/100' },
    { name: '张先生', time: '明天 10:00', property: '西城区XX公寓', avatar: 'https://picsum.photos/id/1025/100/100' },
    { name: '赵女士', time: '明天 15:00', property: '东城区XX大厦', avatar: 'https://picsum.photos/id/1074/100/100' }
  ]
  
  const salesChart = ref(null)
  const houseTypeChart = ref(null)
  
  onMounted(() => {
    initSalesChart()
    initHouseTypeChart()
  })
  
  const initSalesChart = () => {
    const chart = echarts.init(salesChart.value)
    chart.setOption({
      tooltip: {
        trigger: 'axis'
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        name: '销售额 (万元)',
        type: 'line',
        stack: '总量',
        data: [4200, 3800, 5600, 4800, 6200, 5800, 6500, 7200, 6800, 7500, 8200, 9100],
        smooth: true,
        lineStyle: {
          color: '#165DFF'
        },
        areaStyle: {
          color: 'rgba(22, 93, 255, 0.1)'
        }
      }]
    })
  }
  
  const initHouseTypeChart = () => {
  const chart = echarts.init(houseTypeChart.value)
  chart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [{
      name: '房源类型',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold'
        }
      },
      labelLine: { // ✅ 放到 series 顶层
        show: false
      },
      data: [ // ✅ 放到 series 顶层
        { value: 335, name: 'A区房源', itemStyle: { color: '#5470C6' } },
        { value: 310, name: 'B区房源', itemStyle: { color: '#91CC75' } },
        { value: 274, name: 'C区房源', itemStyle: { color: '#FAC858' } },
        { value: 235, name: 'D区房源', itemStyle: { color: '#EE6666' } },
        { value: 400, name: 'E区房源', itemStyle: { color: '#73C0DE' } },
        { value: 10, name: '租赁', itemStyle: { color: '#FAAD14' } }
      ]
    }]
  })
}
  
  const handleDetail = (index, row) => {
    console.log(index, row)
  }
  const handleEdit = (index, row) => {
    console.log(index, row)
  }
  const handleDelete = (index, row) => {
    console.log(index, row)
  }
  </script>