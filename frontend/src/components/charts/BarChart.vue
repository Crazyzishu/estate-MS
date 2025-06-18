<!-- BarChart条形图 -->
<template>
  <div ref="chart" style="width: 100%; height: 100%;"></div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  data: {
    type: Object,
    required: true
  }
})

const chart = ref(null)

let chartInstance = null

const initChart = () => {
  if (chartInstance) {
    chartInstance.dispose()
  }

  chartInstance = echarts.init(chart.value)
  chartInstance.setOption({
    tooltip: {},
    xAxis: {
      type: 'category',
      data: props.data.labels
    },
    yAxis: {},
    series: props.data.datasets.map(dataset => ({
      type: 'bar',
      data: dataset.data,
      itemStyle: { color: dataset.backgroundColor }
    }))
  })
}

onMounted(() => {
  initChart()
})
</script>