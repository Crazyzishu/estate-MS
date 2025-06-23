<template>
    <div class="header">
      <el-header style="background-color: #fff; padding: 0 20px">
        <div style="display: flex; justify-content: space-between; align-items: center">
          <div style="display: flex; align-items: center">
            <i class="el-icon-menu" style="margin-right: 10px"></i>
            <h1 style="margin: 0; font-size: 1.5rem">房产管理系统</h1>
          </div>
          <div style="display: flex; align-items: center">
            <!-- <el-input v-model="searchQuery" placeholder="全局搜索..." style="width: 200px; margin-right: 10px" /> -->
            <el-badge :value="3" class="item" style="margin-right: 20px">
              <el-button icon="el-icon-bell" circle />
            </el-badge>
            <el-dropdown>
              <span class="el-dropdown-link" style="cursor: pointer">
                张经理<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  
  const searchQuery = ref('')
  </script>