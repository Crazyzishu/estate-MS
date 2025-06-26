<template>
    <div>
      <h1>数据概览</h1>
      <p>欢迎回来，经理！这是您的房地产管理系统概览</p>
      
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
              <!-- <p :class="card.trendClass">
                <i :class="card.trendIcon"></i> {{ card.trendText }}
              </p> -->
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
                <el-button
                    v-for="range in ['month', 'quarter', 'year']"
                    :key="range"
                    :type="activeTimeRange === range ? 'primary' : 'primary'"
                    :plain="activeTimeRange !== range"
                    @click="fetchSalesTrend(range)"
                >
                  {{ range === 'month' ? '本月' : range === 'quarter' ? '本季度' : '本年度' }}
                </el-button>
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
        <el-col :span="24">
          <el-card>
            <div slot="header" class="clearfix">
              <span>最近房源</span>
              <!-- 绑定点击事件 -->
              <el-button style="float: right; padding: 3px 0" type="text" @click="navigateToHouses">查看全部</el-button>
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
<!--              <el-table-column label="操作" width="150">-->
<!--                <template #default="scope">-->
<!--&lt;!&ndash;                  <el-button size="mini" icon="el-icon-view" @click="handleDetail(scope.$index, scope.row)">查看</el-button>&ndash;&gt;-->
<!--                  <el-button size="mini" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>-->
<!--                  <el-button size="mini" icon="el-icon-delete" @click="handleDelete(scope.$index, scope.row)">删除</el-button>-->
<!--                </template>-->
<!--              </el-table-column>-->
            </el-table>
          </el-card>
        </el-col>
<!--        <el-col :span="8">-->
<!--          <el-card>-->
<!--            <div slot="header" class="clearfix">-->
<!--              <span>近期预约</span>-->
<!--              <el-button style="float: right; padding: 3px 0" type="text">查看全部</el-button>-->
<!--            </div>-->
<!--            <div v-for="(appointment, index) in appointments" :key="index" style="margin-bottom: 20px">-->
<!--            <div style="display: flex; align-items: center; margin-bottom: 10px">-->
<!--              <div>-->
<!--                <p style="margin: 0">{{ appointment.name }}</p>-->
<!--                <p style="margin: 0; color: #999">{{ appointment.time }}</p>-->
<!--              </div>-->
<!--            </div>-->
<!--            <p style="margin: 5px 0">{{ appointment.property }}</p>-->
<!--            <el-button-group>-->
<!--              <el-button-->
<!--                size="mini"-->
<!--                :type="appointment.status === 0 ? 'primary' : ''"-->
<!--                @click="updateAppointmentStatus(appointment, 0)"-->
<!--              >-->
<!--                未完成-->
<!--              </el-button>-->
<!--              <el-button-->
<!--                size="mini"-->
<!--                :type="appointment.status === 1 ? 'primary' : ''"-->
<!--                @click="updateAppointmentStatus(appointment, 1)"-->
<!--              >-->
<!--                已完成-->
<!--              </el-button>-->
<!--              <el-button-->
<!--                size="mini"-->
<!--                :type="appointment.status === 2 ? 'primary' : ''"-->
<!--                @click="updateAppointmentStatus(appointment, 2)"-->
<!--              >-->
<!--                已取消-->
<!--              </el-button>-->
<!--            </el-button-group>-->
<!--          </div>-->
<!--          </el-card>-->
<!--        </el-col>-->
      </el-row>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import * as echarts from 'echarts'
  import axios from '../api/axios.js';

  //引入路由
  import { useRouter } from 'vue-router'

  const router = useRouter()

  //数据卡片
  const cards = ref([
    { title: '总房源数', value: '加载中...', trendClass: '', trendIcon: '', trendText: '' },
    { title: '本月销售额', value: '加载中...', trendClass: '', trendIcon: '', trendText: '' },
    { title: '客户总数', value: '加载中...', trendClass: '', trendIcon: '', trendText: '' },
    { title: '预约看房', value: '加载中...', trendClass: '', trendIcon: '', trendText: '' }
  ]);

  const houses = [
    { id: '#20250615001', name: '朝阳区香河小区', address: '朝阳区14路21号', price: '¥12,000,000', area: '120㎡', status: '在售', image: 'https://picsum.photos/id/1040/100/100' },
    { id: '#20250615002', name: '海淀区海滨花园', address: '海淀区54路26号', price: '¥15,800,000', area: '150㎡', status: '待审核', image: 'https://picsum.photos/id/1067/100/100' },
    { id: '#20250615003', name: '东城区携程大厦', address: '东城区崇阳路78号', price: '¥22,500,000', area: '200㎡', status: '已售', image: 'https://picsum.photos/id/164/100/100' },
    { id: '#20250615004', name: '西城区青年公寓', address: '西城区海兴路56号', price: '¥9,600,000', area: '90㎡', status: '在售', image: 'https://picsum.photos/id/1048/100/100' }
  ]


  // 跳转至房源管理页面
const navigateToHouses = () => {
    router.push('/houses')
}

  //数据暂存
  //, avatar: 'https://picsum.photos/id/1012/100/100'
  //, avatar: 'https://picsum.photos/id/1027/100/100'
  //, avatar: 'https://picsum.photos/id/1025/100/100'
  //, avatar: 'https://picsum.photos/id/1074/100/100'
  const appointments = [
    { name: '李先生', time: '今天 14:00', property: '朝阳区XX小区' },
    { name: '王女士', time: '今天 16:30', property: '海淀区XX花园' },
    { name: '张先生', time: '明天 10:00', property: '西城区XX公寓' },
    { name: '赵女士', time: '明天 15:00', property: '东城区XX大厦' }
  ]
  //销售趋势
  const salesChart = ref(null)
  //房源类型
  const houseTypeChart = ref(null)
  //记录当前选中的时间范围
  const activeTimeRange = ref('month')

  // 销售趋势数据
  const salesData = ref([]);
  // 房源类型数据
  const houseRoomTypeData = ref([]);
  
  onMounted(() => {
    fetchDashboardCards(); // 获取数据卡片
    fetchSalesTrend('month'); // 默认加载本月数据
    fetchHouseRoomTypes();
  })
//根据状态值返回对应颜色类型
  const getStatusTagType = (status) => {
    switch (status) {
      case '待确认':
        return 'warning';
      case '已确认':
        return 'success';
      case '已完成':
        return 'info';
      default:
        return 'default';
    }
  };




  const fetchDashboardCards = async () => {
    try {
      const response = await axios.get('/api/dashboard/cards');
      if (response.data.code === 1 && response.data.data) {
        const data = response.data.data;

        // 工具函数：安全获取数值
        const safeNumber = (value, defaultValue = 0) => {
          const num = parseFloat(value);
          return isNaN(num) ? defaultValue : num;
        };

        cards.value = [
          {
            title: '总房源数',
            value: data.totalHouses || '0',
            trendClass: safeNumber(data.houseGrowthRate) >= 0 ? 'text-success' : 'text-danger',
            trendIcon: safeNumber(data.houseGrowthRate) >= 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down',
            trendText: `${Math.abs(safeNumber(data.houseGrowthRate)).toFixed(2)}% 较上月`
          },
          {
            title: '本月销售额',
            value: data.monthlySales || '0',
            trendClass: safeNumber(data.clientGrowthRate) >= 0 ? 'text-success' : 'text-danger',
            trendIcon: safeNumber(data.clientGrowthRate) >= 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down',
            trendText: `${Math.abs(safeNumber(data.clientGrowthRate)).toFixed(2)}% 较上月`
          },
          {
            title: '客户总数',
            value: data.totalClients || '0',
            trendClass: safeNumber(data.clientGrowthRate) >= 0 ? 'text-success' : 'text-danger',
            trendIcon: safeNumber(data.clientGrowthRate) >= 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down',
            trendText: `${Math.abs(safeNumber(data.clientGrowthRate)).toFixed(2)}% 较上月`
          },
          {
            title: '预约看房',
            value: data.pendingAppointments || '0',
            trendClass: safeNumber(data.appointmentGrowthRate) >= 0 ? 'text-success' : 'text-danger',
            trendIcon: safeNumber(data.appointmentGrowthRate) >= 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down',
            trendText: `${Math.abs(safeNumber(data.appointmentGrowthRate)).toFixed(2)}% 较上月`
          }
        ];
      }
    } catch (error) {
      console.error('获取数据卡片失败:', error);
      cards.value = cards.value.map(card => ({
        ...card,
        trendText: '加载失败'
      }));
    }
  };


  const fetchSalesTrend = async (timeRange = 'month') => {
    try {
      const res = await axios.get(`/api/dashboard/sales-trend?timeRange=${timeRange}`);
      if (res.data.code === 1) {
        console.log('【DEBUG】原始数据:', res.data.data); // 查看原始数据类型
        let rawData = res.data.data || [];

        // 确保 salesAmount 是数值类型
        let safeData = rawData.map(item => ({
          date: item.date,
          salesAmount: isNaN(parseFloat(item.salesAmount)) ? 0 : parseFloat(item.salesAmount)
        }));

        // 补全空缺数据
        let filledData = fillMissingDates(safeData, timeRange);
        salesData.value = filledData;

        activeTimeRange.value = timeRange;
        updateSalesChart(timeRange);
      }
    } catch (error) {
      console.error('获取销售趋势失败:', error);
    }
  };


  // 获取房源户型分布
  const fetchHouseRoomTypes = async () => {
    try {
      const res = await axios.get('/api/dashboard/house-room-types');
      if (res.data.code === 1) {
        houseRoomTypeData.value = res.data.data;
        initHouseTypeChart();
      }
    } catch (error) {
      console.error('获取房源类型失败:', error);
    }
  };

  //初始化销售趋势图表逻辑
  const updateSalesChart = (timeRange) => {
    const chart = echarts.init(salesChart.value);

    // 提取原始 date 字段和销售金额字段
    const rawDates = salesData.value.map(item => item.date); // 【关键】保留原始 "2025-06"
    const amounts = salesData.value.map(item => parseFloat(item.salesAmount || 0));

    // 构造 X 轴标签
    const xAxisLabels = rawDates.map(dateStr => {
      const d = new Date(dateStr);
      if (timeRange === 'month') {
        return `${d.getDate()}日`;
      } else {
        return `${d.getMonth() + 1}月`;
      }
    });

    // tooltip 格式化函数
    const tooltipFormatter = (params) => {
      const index = params[0].dataIndex;
      const dateStr = rawDates[index];
      const d = new Date(dateStr);

      let label;
      if (timeRange === 'month') {
        label = `${d.getFullYear()}年 ${d.getMonth()+1}月 ${d.getDate()}日`;
      } else {
        label = `${d.getFullYear()}年 ${d.getMonth()+1}月`;
      }

      return `${label}<br/>销售额: ${params[0].value} 万元`;
    };

    const option = {
      tooltip: {
        trigger: 'axis',
        formatter: tooltipFormatter
      },
      xAxis: {
        type: 'category',
        data: xAxisLabels, // 使用构造好的标签，而不是原始 date
        boundaryGap: false,
        axisLabel: {
          rotate: 45,
          // 使用原始 date 字符串构造 Date 对象
          formatter: function(value, index) {
            const dateStr = rawDates[index]; // 使用原始 date 字符串
            const d = new Date(dateStr);     // 避免使用格式化后的字符串
            if (timeRange === 'month') {
              return `${d.getDate()}日`;
            } else {
              return `${d.getMonth() + 1}月`;
            }
          }
        }
      },
      yAxis: {
        type: 'value',
        name: '销售额 (万元)'
      },
      series: [{
        name: '销售额 (万元)',
        type: 'line',
        stack: '总量',
        smooth: true,
        lineStyle: { color: '#165DFF' },
        areaStyle: { color: 'rgba(22, 93, 255, 0.1)' },
        data: amounts
      }]
    };

    chart.setOption(option, true);
  };



  //初始化房源户型分布饼图
  const initHouseTypeChart = () => {
    const chart = echarts.init(houseTypeChart.value);

    const data = houseRoomTypeData.value.map(item => ({
      value: item.count,
      name: item.typeName,
      itemStyle: {
        color: '#' + Math.floor(Math.random()*16777215).toString(16)
      }
    }));

    const option = {
      tooltip: { trigger: 'item' },
      legend: { top: '5%', left: 'center' },
      series: [{
        name: '房源户型',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: { show: false, position: 'center' },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: { show: false },
        data: data
      }]
    };

    chart.setOption(option, true);
  };

  //动态填充缺失月份
  const fillMissingDates = (data, timeRange) => {
    const filledData = [];

    if (timeRange === 'month') {
      // 补全本月每天的数据（假设最大天数为 31）
      const year = new Date().getFullYear();
      const month = new Date().getMonth() + 1;
      const daysInMonth = new Date(year, month, 0).getDate();

      for (let day = 1; day <= daysInMonth; day++) {
        const dateStr = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        const found = data.find(d => d.date === dateStr);
        filledData.push({
          date: dateStr,
          salesAmount: found ? found.salesAmount : 0
        });
      }

    } else if (timeRange === 'quarter') {
      // 补全当前季度的三个月
      const now = new Date();
      const quarterStartMonths = {
        1: 1, 2: 4, 3: 7, 4: 10
      };
      const currentQuarter = Math.floor((now.getMonth() + 3) / 3); // 获取当前季度
      const startMonth = quarterStartMonths[currentQuarter];

      for (let i = 0; i < 3; i++) {
        const year = now.getFullYear();
        const month = startMonth + i;
        const dateStr = `${year}-${String(month).padStart(2, '0')}`;

        const found = data.find(d => d.date === dateStr);
        filledData.push({
          date: dateStr,
          salesAmount: found ? found.salesAmount : 0
        });
      }

    } else if (timeRange === 'year') {
      // 补全全年 1~12 月
      const year = new Date().getFullYear();

      for (let month = 1; month <= 12; month++) {
        const dateStr = `${year}-${String(month).padStart(2, '0')}`;
        const found = data.find(d => d.date === dateStr);
        filledData.push({
          date: dateStr,
          salesAmount: found ? found.salesAmount : 0
        });
      }
    }

    return filledData;
  };

  </script>