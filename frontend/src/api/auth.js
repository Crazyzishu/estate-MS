import apiClient from './axios';

export default {
  login(credentials) {
    return apiClient.post('/api/login', credentials); // 这里的路径必须与后端 Controller 上的 @RequestMapping 匹配
  },
  register(userData) {
    return apiClient.post('/api/register', userData);
  }
};