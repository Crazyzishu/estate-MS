import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as echarts from 'echarts'
import axios from 'axios'

import LoginRegister from "./views/LoginRegister.vue";

const app = createApp(App)

// 检查是否有全局路由守卫
router.beforeEach((to, from, next) => {
    // 打印日志辅助调试
    console.log('即将从', from.path, '跳转到', to.path)
    // 确保这里的逻辑不会阻止正常跳转
    next()
  })

app.use(router)
app.use(ElementPlus)
app.provide('echarts', echarts)
app.provide('axios', axios)
//app.config.globalProperties.$echarts = echarts
app.mount('#app')

