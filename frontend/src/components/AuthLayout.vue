<!--登陆授权-->
<template>
  <div class="auth-container">
    <el-card class="auth-card">
      <div class="auth-header">
        <h2>{{ title }}</h2>
        <p class="text-sm text-gray-500">{{ subtitle }}</p>
      </div>
      <div class="auth-body">
        <slot />
      </div>
    </el-card>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  subtitle: String
})
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.auth-card {
  width: 400px;
  max-width: 90vw;
}

.auth-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #1f2d3d;
}
</style>
