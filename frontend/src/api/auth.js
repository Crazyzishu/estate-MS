import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // 确保这个地址正确指向你的后端服务
});

export default {
  login(credentials) {
    return apiClient.post('/api/login', credentials); // 这里的路径必须与后端 Controller 上的 @RequestMapping 匹配
  },
  register(userData) {
    return apiClient.post('/api/register', userData);
  }
};
