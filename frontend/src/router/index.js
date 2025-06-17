import { createRouter, createWebHistory } from 'vue-router'
import IndexView from '../views/index/index.vue'

const routes = [
  {
    path: '/',
    name: 'index',
    component: IndexView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router