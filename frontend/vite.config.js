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
      '/dashboard': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true,
        rewrite: path => path.replace(/^\/dashboard/, '/dashboard') // 保持路径一致
      }
    }
  }
})