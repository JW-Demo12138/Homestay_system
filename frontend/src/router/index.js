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
    name: 'Admin',
    component: () => import('@/views/admin/Layout.vue'),
    meta: { requiresAuth: true, roles: ['ADMIN'] },
    children: [
      {
        path: '',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/UserManagement.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'orders',
        name: 'AdminOrders',
        component: () => import('@/views/admin/OrderManagement.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'reviews',
        name: 'AdminReviews',
        component: () => import('@/views/admin/ReviewManagement.vue'),
        meta: { title: '评价管理' }
      },
      {
        path: 'announcement',
        name: 'AdminAnnouncement',
        component: () => import('@/views/admin/AnnouncementManagement.vue'),
        meta: { title: '公告管理' }
      },
      {
        path: 'config',
        name: 'AdminConfig',
        component: () => import('@/views/admin/ConfigManagement.vue'),
        meta: { title: '系统配置' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  
  // 如果用户已登录但没有完整的用户信息，尝试刷新
  if (userStore.isLoggedIn && !userStore.userInfo) {
    try {
      await userStore.getUserInfo()
    } catch (error) {
      console.error('路由守卫中刷新用户信息失败:', error)
    }
  }
  
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
