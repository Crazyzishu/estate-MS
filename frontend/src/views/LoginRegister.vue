<template>
    <div class="auth-container">
      <h2>{{ isRegistering ? 'Register' : 'Login' }}</h2>
      <form @submit.prevent="handleSubmit" class="auth-form">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" id="username" v-model="formData.username" required />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" id="password" v-model="formData.password" required />
        </div>
        <div class="form-group" v-if="isRegistering">
          <label for="realName">Real Name:</label>
          <input type="text" id="realName" v-model="formData.realName" required />
        </div>
        <div class="form-group" v-if="isRegistering">
          <label for="email">Email:</label>
          <input type="email" id="email" v-model="formData.email" required />
        </div>
        <div class="form-group" v-if="isRegistering">
          <label for="phone">Phone:</label>
          <input type="tel" id="phone" v-model="formData.phone" required />
        </div>
        <button type="submit" class="btn">{{ isRegistering ? 'Register' : 'Login' }}</button>
      </form>
      <p v-if="error" class="error-message">{{ error }}</p>
      <p v-if="message" class="success-message">{{ message }}</p>
      <button @click="toggleForm" class="toggle-btn">
        {{ isRegistering ? 'Already have an account? Login' : 'Need an account? Register' }}
      </button>
    </div>
  </template>
  
  <script>
  import axios from '../api/axios.js';
  
  export default {
    data() {
      return {
        formData: {
          username: '',
          password: '',
          realName: '',
          email: '',
          phone: ''
        },
        isRegistering: false,
        error: '',
        message: ''
      };
    },
    methods: {
      toggleForm() {
        this.isRegistering = !this.isRegistering;
        this.error = '';
        this.message = '';
      },
      async handleSubmit() {
        try {
          if (this.isRegistering) {
            await axios.post('/register', this.formData);
            this.message = 'Registration successful!';
            this.toggleForm();
          } else {
            const response = await axios.post('/login', this.formData);
            console.log(response.data);
            this.message = 'Login successful!';
          }
        } catch (err) {
          this.error = err.response?.data || 'An error occurred';
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .auth-container {
    max-width: 400px;
    margin: 50px auto;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    text-align: center;
    color: #333;
  }
  
  .auth-form {
    display: flex;
    flex-direction: column;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  input[type="text"],
  input[type="password"],
  input[type="email"],
  input[type="tel"] {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  .btn {
    background-color: #007bff;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
  }
  
  .btn:hover {
    background-color: #0056b3;
  }
  
  .toggle-btn {
    margin-top: 15px;
    background-color: transparent;
    color: #007bff;
    border: none;
    cursor: pointer;
    font-size: 14px;
  }
  
  .toggle-btn:hover {
    text-decoration: underline;
  }
  
  .error-message {
    color: red;
    text-align: center;
  }
  
  .success-message {
    color: green;
    text-align: center;
  }
  </style>