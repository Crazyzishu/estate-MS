<template>
  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
    <el-card class="stat-card">
      <div class="flex items-center justify-between mb-4">
        <h3>本月预约总数</h3>
        <div class="bg-primary/10 p-2 rounded-lg">
          <i class="fa fa-calendar-check-o text-primary"></i>
        </div>
      </div>
      <div class="flex items-end justify-between">
        <div>
          <p class="text-3xl font-bold">128</p>
          <p class="text-success text-sm flex items-center">
            <i class="fa fa-arrow-up mr-1"></i> 18.5%
            <span class="text-gray-500 ml-1">较上月</span>
          </p>
        </div>
        <div class="w-20 h-20">
          <canvas ref="chart1"></canvas>
        </div>
      </div>
    </el-card>
    <!-- 其他统计卡片 -->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const chart1 = ref(null)

onMounted(() => {
  if (chart1.value) {
    const myChart = echarts.init(chart1.value)
    // 修正配置项结构，移除多余的 options 字段
    const option = {
      series: [{
        type: 'pie',
        data: [
          { value: 75, name: '部分1' }, 
          { value: 25, name: '部分2' }
        ],
        backgroundColor: ['#165DFF', 'rgba(0, 0, 0, 0.05)'],
        borderWidth: 0,
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
        }
      }],
      // 移除错误的 options 字段
      // options: {
      //   responsive: true,
      //   maintainAspectRatio: false,
      //   cutout: '80%',
      //   plugins: {
      //     legend: {
      //       display: false
      //     },
      //     tooltip: {
      //       enabled: false
      //     }
      //   }
      // }
    }
    if (option && typeof option === 'object') {
      myChart.setOption(option)
    } else {
      console.error('ECharts 配置项不是有效的对象', option)
    }
  }
})
</script>

<style scoped>
.stat-card {
  /* 统计卡片样式 */
}
</style>
