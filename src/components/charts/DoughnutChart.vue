<!-- Doughnut chart甜甜圈图 -->
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
    tooltip: { trigger: 'item' },
    legend: { show: false },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: { show: false },
        data: props.data.labels.map((label, index) => ({
          name: label,
          value: props.data.datasets[0].data[index]
        })),
        itemStyle: {
          borderRadius: 5,
          borderColor: '#fff',
          borderWidth: 2
        }
      }
    ]
  })
}

onMounted(() => {
  initChart()
})
</script>