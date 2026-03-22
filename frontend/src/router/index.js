import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { requiresAuth: false, title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { requiresAuth: false, title: '注册' }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/Home.vue'),
    meta: { requiresAuth: false, title: '首页' }
  },
  {
    path: '/homestays',
    name: 'HomestayList',
    component: () => import('@/views/homestay/List.vue'),
    meta: { requiresAuth: false, title: '民宿列表' }
  },
  {
    path: '/homestays/:id',
    name: 'HomestayDetail',
    component: () => import('@/views/homestay/Detail.vue'),
    meta: { requiresAuth: false, title: '民宿详情' }
  },
  {
    path: '/landlord',
    name: 'LandlordDashboard',
    component: () => import('@/views/landlord/Dashboard.vue'),
    meta: { requiresAuth: true, roles: ['LANDLORD'], title: '房东中心' }
  },
  {
    path: '/landlord/homestays',
    name: 'LandlordHomestays',
    component: () => import('@/views/landlord/MyHomestays.vue'),
    meta: { requiresAuth: true, roles: ['LANDLORD'], title: '我的民宿' }
  },
  {
    path: '/landlord/homestays/create',
    name: 'CreateHomestay',
    component: () => import('@/views/landlord/CreateHomestay.vue'),
    meta: { requiresAuth: true, roles: ['LANDLORD'], title: '发布民宿' }
  },
  {
    path: '/landlord/homestays/:id/edit',
    name: 'EditHomestay',
    component: () => import('@/views/landlord/EditHomestay.vue'),
    meta: { requiresAuth: true, roles: ['LANDLORD'], title: '编辑民宿' }
  },
  {
    path: '/landlord/orders',
    name: 'LandlordOrders',
    component: () => import('@/views/landlord/MyOrders.vue'),
    meta: { requiresAuth: true, roles: ['LANDLORD'], title: '房东订单' }
  },
  {
    path: '/landlord/experiences',
    name: 'LandlordExperiences',
    component: () => import('@/views/landlord/MyExperiences.vue'),
    meta: { requiresAuth: true, roles: ['LANDLORD'], title: '我的体验项目' }
  },
  {
    path: '/landlord/experiences/create',
    name: 'CreateExperience',
    component: () => import('@/views/landlord/CreateExperience.vue'),
    meta: { requiresAuth: true, roles: ['LANDLORD'], title: '创建体验项目' }
  },
  {
    path: '/landlord/experiences/:id/edit',
    name: 'EditExperience',
    component: () => import('@/views/landlord/EditExperience.vue'),
    meta: { requiresAuth: true, roles: ['LANDLORD'], title: '编辑体验项目' }
  },
  {
    path: '/user',
    name: 'UserCenter',
    component: () => import('@/views/user/Profile.vue'),
    meta: { requiresAuth: true, title: '个人中心' }
  },
  {
    path: '/user/orders',
    name: 'UserOrders',
    component: () => import('@/views/user/MyOrders.vue'),
    meta: { requiresAuth: true, title: '我的订单' }
  },
  { path: '/feature/topic', name: 'FeatureTopic', component: () => import('@/views/feature/FeatureTopic.vue'), meta: { title: '特色专题' } },
  { path: '/feature/experience/:id', name: 'FeatureExperienceDetail', component: () => import('@/views/feature/ExperienceDetail.vue'), meta: { title: '体验项目详情' } },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('@/views/admin/Dashboard.vue'),
    meta: { requiresAuth: true, roles: ['ADMIN'], title: '管理员中心' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.meta.roles && !to.meta.roles.includes(userStore.role)) {
    next('/')
  } else if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
    next('/')
  } else {
    next()
  }
})

export default router
