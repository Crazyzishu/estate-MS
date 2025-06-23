<template>
  <auth-layout :title="isLogin ? '管理员登录' : '注册新账号'" subtitle="请输入以下信息完成操作">
    <el-form ref="form" :model="formData" :rules="rules" label-position="top">
      <div v-if="!isLogin">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" placeholder="请输入真实姓名" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱地址" />
        </el-form-item>
      </div>

      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" placeholder="请输入用户名" />
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input v-model="formData.password" type="password" show-password placeholder="请输入密码" />
      </el-form-item>

      <div v-if="!isLogin">
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="formData.confirmPassword" type="password" show-password placeholder="请再次输入密码" />
        </el-form-item>
      </div>

      <el-button type="primary" @click="submit" style="width: 100%; margin-top: 10px;">
        {{ isLogin ? '登录' : '注册' }}
      </el-button>

      <div class="toggle-mode mt-4 text-center">
        <span @click="toggleMode" class="text-blue-600 cursor-pointer">
          {{ isLogin ? '没有账号？立即注册' : '已有账号？立即登录' }}
        </span>
      </div>
    </el-form>
  </auth-layout>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login, register } from '../api/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const isLogin = ref(true)

const formData = ref({
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
  email: ''
})

const validatePassword = (rule, value, callback) => {
  if (value !== formData.value.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = ref({
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3~20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 6, message: '密码至少为6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
  ]
})

const toggleMode = () => {
  isLogin.value = !isLogin.value
  // 清空表单
  formData.value = {
    username: '',
    password: '',
    confirmPassword: '',
    realName: '',
    phone: '',
    email: ''
  }
}

const submit = async () => {
  try {
    if (!isLogin.value) {
      if (formData.value.password !== formData.value.confirmPassword) {
        ElMessage.error('两次输入的密码不一致')
        return
      }

      await register(formData.value)
      ElMessage.success('注册成功，请登录')
      toggleMode()
    } else {
      const res = await login({
        username: formData.value.username,
        password: formData.value.password
      })

      if (res.data.code === 200) {
        localStorage.setItem('token', res.data.data.token)
        ElMessage.success('登录成功')
        router.push('/')
      } else {
        ElMessage.error(res.data.message || '登录失败')
      }
    }
  } catch (error) {
    console.error(error)
    ElMessage.error(isLogin.value ? '登录失败，请检查用户名或密码' : '注册失败，请检查输入内容')
  }
}
</script>

<style scoped>
.auth-body .el-form {
  width: 100%;
}

.toggle-mode:hover {
  color: #409EFF;
}
</style>
