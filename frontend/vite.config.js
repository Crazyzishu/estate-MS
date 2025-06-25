import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import ElementPlus from 'unplugin-element-plus/vite'

export default defineConfig({
  plugins: [
    vue(),
    ElementPlus(),
  ],
  server: {
    port: 5175,
      proxy: {
        // 将以 /api 开头的请求代理到后端服务器
      '/api': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '') // 保持路径一致
      }
    }
  }
})