<template>
  <div id="app" :class="{ 'auth-layout': isAuthPage }">
    <!-- 如果不是登录页，则展示默认布局 -->
    <el-container v-if="!isAuthPage" style="height: 100vh">
      <el-aside width="200px">
        <Sidebar />
      </el-aside>
      <el-container>
        <el-header>
          <Header />
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>

    <!-- 如果是登录页，则直接展示 router-view 内容 -->
    <router-view v-else />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from './components/Sidebar.vue'
import Header from './components/Header.vue'

const route = useRoute()

// 判断当前是否为登录页（可扩展 meta.layout）
const isAuthPage = computed(() => {
  return route.path === '/login'
})
</script>

<style scoped>
.auth-layout {
  background-color: #f5f7fa;
}
</style>
