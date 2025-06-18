<template>
  <el-card class="trend-chart-card">
    <div class="card-header">
      <h3>预约趋势</h3>
      <div class="chart-controls">
        <el-button size="small" @click="changeTimeRange('week')">本周</el-button>
        <el-button size="small" type="primary" @click="changeTimeRange('month')">本月</el-button>
      </div>
    </div>
    <div ref="chartContainer" style="width: 100%; height: 400px;"></div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'

const chartContainer = ref(null)
const timeRange = ref('month')
let chartInstance = null

const initChart = () => {
  if (chartInstance) {
    chartInstance.dispose()
  }
  if (chartContainer.value) {
    chartInstance = echarts.init(chartContainer.value)
    const option = {
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [65, 78, 90, 105, 115, 128],
        type: 'line',
        smooth: true,
        itemStyle: {
          color: '#165DFF'
        },
        areaStyle: {
          color: 'rgba(22, 93, 255, 0.1)'
        }
      }]
    }
    if (option && typeof option === 'object') {
      chartInstance.setOption(option)
    } else {
      console.error('ECharts 配置项不是有效的对象', option)
    }
  }
}

const changeTimeRange = (range) => {
  timeRange.value = range
  initChart()
}

onMounted(() => {
  initChart()
})

watch(timeRange, initChart)
</script>

<style scoped>
.trend-chart-card {
  /* 样式 */
}
</style>
