<template>
    <div class="login-register-container">
      <div class="background"></div>
      <div class="content">
        <el-card class="auth-card">
          <div class="auth-header">
            <h2>{{ isLogin ? '登录' : '注册' }}</h2>
          </div>
          <el-form @submit.prevent="handleSubmit" class="auth-form">
            <el-form-item>
              <el-input
                v-model="username"
                placeholder="用户名"
                prefix-icon="el-icon-user"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="password"
                type="password"
                show-password
                placeholder="密码"
                prefix-icon="el-icon-lock"
              ></el-input>
            </el-form-item>
            <el-form-item v-if="!isLogin">
              <el-input
                v-model="realName"
                placeholder="真实姓名"
                prefix-icon="el-icon-user-solid"
              ></el-input>
            </el-form-item>
            <el-form-item v-if="!isLogin">
              <el-input
                v-model="email"
                placeholder="邮箱"
                prefix-icon="el-icon-message"
              ></el-input>
            </el-form-item>
            <el-form-item v-if="!isLogin">
              <el-input
                v-model="phone"
                placeholder="手机号"
                prefix-icon="el-icon-phone"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                native-type="submit"
                class="auth-button"
              >
                {{ isLogin ? '登录' : '注册' }}
              </el-button>
            </el-form-item>
          </el-form>
          <div class="switch-auth">
            <el-button text @click="toggleForm">
              {{ isLogin ? '去注册' : '去登录' }}
            </el-button>
          </div>
          <div class="message" v-if="message">{{ message }}</div>
        </el-card>
      </div>
    </div>
  </template>
  
  <script>
  import axios from '../api/axios.js';
  import { useRouter } from 'vue-router';
  
  export default {
    setup() {
      const router = useRouter();
  
      return {
        router
      };
    },
    data() {
      return {
        isLogin: true,
        username: '',
        password: '',
        realName: '',
        email: '',
        phone: '',
        message: ''
      };
    },
    methods: {
      toggleForm() {
        this.isLogin = !this.isLogin;
        if (this.isLogin) {
          this.username = '';
          this.password = '';
        }
        this.realName = '';
        this.email = '';
        this.phone = '';
        this.message = '';
      },
      async handleSubmit() {
        try {
          if (this.isLogin) {
            const response = await axios.post('/api/login', { username: this.username, password: this.password });
            if (response.data.code === 1) {
              this.router.push('/dashboard');
            } else {
              this.message = response.data.msg;
            }
          } else {
            const response = await axios.post('/api/register', {
              username: this.username,
              password: this.password,
              realName: this.realName,
              email: this.email,
              phone: this.phone
            });
            if (response.data.code === 1) {
              this.toggleForm();
              this.message = ''; // 清空消息
            } else {
              this.message = response.data.msg;
            }
          }
        } catch (error) {
          this.message = error.response?.data?.msg || '操作失败';
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .login-register-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    position: relative;
    overflow: hidden;
  }
  
  .background {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #4e54c8, #8f94fb);
    z-index: -1;
  }
  
  .content {
    z-index: 1;
    width: 100%;
    max-width: 400px;
    padding: 20px;
  }
  
  .auth-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    padding: 30px;
  }
  
  .auth-header {
    text-align: center;
    margin-bottom: 30px;
  }
  
  .auth-header h2 {
    color: #303133;
    font-size: 28px;
    font-weight: 600;
  }
  
  .auth-form {
    margin-bottom: 20px;
  }
  
  .auth-form .el-form-item {
    margin-bottom: 20px;
  }
  
  .auth-button {
    width: 100%;
    height: 48px;
    font-size: 16px;
    border-radius: 8px;
  }
  
  .switch-auth {
    text-align: center;
  }
  
  .message {
    text-align: center;
    color: #f56c6c;
    margin-top: 15px;
    font-size: 14px;
  }
  </style>