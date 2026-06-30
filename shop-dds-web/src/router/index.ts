import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import type { Role } from '@/types'

const routes: RouteRecordRaw[] = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: () => import('@/views/LoginSelection.vue') },
  { path: '/login/headadmin', name: 'HeadAdminLogin', component: () => import('@/views/HeadAdminLogin.vue') },
  { path: '/login/employee', name: 'EmployeeLogin', component: () => import('@/views/EmployeeLogin.vue') },

  // 总部管理员
  {
    path: '/headadmin',
    component: () => import('@/layouts/HeadAdminLayout.vue'),
    meta: { role: 'HEAD_ADMIN' as Role },
    children: [
      { path: '', name: 'HeadDashboard', component: () => import('@/views/headadmin/Dashboard.vue') },
      { path: 'headadmins', component: () => import('@/views/headadmin/HeadAdminList.vue') },
      { path: 'employees', component: () => import('@/views/headadmin/EmployeeList.vue') },
      { path: 'warehouses', component: () => import('@/views/headadmin/WarehouseList.vue') },
      { path: 'supermarkets', component: () => import('@/views/headadmin/SupermarketList.vue') },
      { path: 'commodities', component: () => import('@/views/headadmin/CommodityList.vue') },
      { path: 'distances', component: () => import('@/views/headadmin/DistanceManage.vue') },
      { path: 'summary', component: () => import('@/views/headadmin/SummaryView.vue') },
      { path: 'assigns', component: () => import('@/views/headadmin/AssignView.vue') },
      { path: 'allocations', component: () => import('@/views/headadmin/AllocateView.vue') },
      { path: 'members', component: () => import('@/views/headadmin/MemberList.vue') },
      { path: 'sales-records', component: () => import('@/views/headadmin/SalesRecordList.vue') },
      { path: 'password', component: () => import('@/views/ChangePassword.vue') }
    ]
  },

  // 仓库管理员
  {
    path: '/warehouse',
    component: () => import('@/layouts/WarehouseLayout.vue'),
    meta: { role: 'WAREHOUSE_ADMIN' as Role },
    children: [
      { path: '', name: 'WarehouseDashboard', component: () => import('@/views/warehouse/Dashboard.vue') },
      { path: 'commodities', component: () => import('@/views/warehouse/CommodityView.vue') },
      { path: 'inventory', component: () => import('@/views/warehouse/InventorySubmit.vue') },
      { path: 'dispatch', component: () => import('@/views/warehouse/DispatchView.vue') },
      { path: 'password', component: () => import('@/views/ChangePassword.vue') }
    ]
  },

  // 超市管理员
  {
    path: '/supermarket',
    component: () => import('@/layouts/SupermarketLayout.vue'),
    meta: { role: 'SUPERMARKET_ADMIN' as Role },
    children: [
      { path: '', name: 'SupermarketDashboard', component: () => import('@/views/supermarket/Dashboard.vue') },
      { path: 'commodities', component: () => import('@/views/supermarket/CommodityView.vue') },
      { path: 'application', component: () => import('@/views/supermarket/ApplicationSubmit.vue') },
      { path: 'receive', component: () => import('@/views/supermarket/ReceiveView.vue') },
      { path: 'members', component: () => import('@/views/supermarket/MemberPoints.vue') },
      { path: 'assigns', component: () => import('@/views/supermarket/MyAssignView.vue') },
      { path: 'password', component: () => import('@/views/ChangePassword.vue') }
    ]
  },

  { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const auth = useAuthStore()
  const needRole = to.meta.role as Role | undefined

  // 角色首页快捷
  const homeOf = (r: Role) =>
    r === 'HEAD_ADMIN' ? '/headadmin'
    : r === 'WAREHOUSE_ADMIN' ? '/warehouse'
    : '/supermarket'

  if (!needRole) {
    if (auth.isLoggedIn && to.path.startsWith('/login')) {
      return next(homeOf(auth.role!))
    }
    return next()
  }

  // 受保护页面
  if (!auth.isLoggedIn) return next('/login')
  if (auth.role !== needRole) {
    return next(homeOf(auth.role!))
  }
  next()
})

export default router