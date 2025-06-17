import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 导入 ElementPlus 及其样式
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

//导入icon
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 创建 Vue 应用并挂载路由和 ElementPlus
const app = createApp(App)
app.use(router)
app.use(ElementPlus)

app.mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}


