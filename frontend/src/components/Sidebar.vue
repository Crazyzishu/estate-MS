<!-- 侧边栏 -->
<template>
    <el-aside style="background-color: #fff; border-right: 1px solid #e4e7ed">
      <el-menu
        default-active="1"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-sub-menu index="1">
          <template #title>
            <i class="el-icon-location"></i>
            <span>主要功能</span>
          </template>
          <el-menu-item index="1-1" @click="navigateTo('/dashboard')">数据概览</el-menu-item>
          <el-menu-item index="1-2" @click="navigateTo('/houses')">房源管理</el-menu-item>
          <el-menu-item index="1-3" @click="navigateTo('/clients')">客户管理</el-menu-item>
          <el-menu-item index="1-4" @click="navigateTo('/contracts')">合同管理</el-menu-item>
          <!-- <el-menu-item index="1-5" @click="navigateTo('/estatenews')">楼盘动态</el-menu-item> -->
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <i class="el-icon-document"></i>
            <span>客户服务</span>
          </template>
<!--          <el-menu-item index="2-1" @click="navigateTo('/appointments')">预约看房</el-menu-item>-->
          <el-menu-item index="2-2" @click="navigateTo('/complaint')">投诉处理</el-menu-item>
        </el-sub-menu>
        <!-- <el-sub-menu index="3">
          <template #title>
            <i class="el-icon-setting"></i>
            <span>系统管理</span>
          </template>
          <el-menu-item index="3-1" @click="navigateTo('/usermanager')">用户管理</el-menu-item>
        </el-sub-menu> -->
      </el-menu>
    </el-aside>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  const navigateTo = (path) => {
    router.push(path)
  }
  
  const handleOpen = (key, keyPath) => {
    console.log(key, keyPath)
  }
  const handleClose = (key, keyPath) => {
    console.log(key, keyPath)
  }
  </script>
