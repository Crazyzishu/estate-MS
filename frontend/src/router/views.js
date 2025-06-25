import Dashboard from '../views/Dashboard.vue'
import Houses from '../views/Houses.vue'
import Clients from '../views/Clients.vue'
import Contracts from '../views/Contracts.vue'
import Appointments from '../views/Appointments.vue'
// import EstateNews from '../views/EstateNews.vue'
import Complaint from '../views/Complaint.vue'
import UserManager from '../views/UserManager.vue'
import ContractForm from "../views/ContractForm.vue";
import LoginRegister from "../views/LoginRegister.vue";

export default [
    { path: '/', redirect: '/login' }, // 默认跳转到登录页
    { path: '/dashboard', component: Dashboard, meta: { title: '数据概览' } },
  { path: '/login', component: LoginRegister, meta: { title: '管理员登录' } },
  { path: '/houses', component: Houses, meta: { title: '房源管理' } },
  { path: '/clients', component: Clients, meta: { title: '客户管理' } },
  { path: '/contracts', component: Contracts, meta: { title: '合同管理' } },
  { path: '/appointments', component: Appointments, meta: { title: '预约看房' } },
  // { path: '/estateNews', component: EstateNews, meta: { titile: '楼盘动态' } },
  { path: '/complaint', component: Complaint, meta: { title: '投诉处理' } },
  { path: '/userManager', component: UserManager, meta: { title: '用户管理' } },
  { path: '/contracts/new', component: ContractForm, meta: { title: '新增合同' } },
]