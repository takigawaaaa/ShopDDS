<template>
  <el-container class="layout">
    <el-aside :width="collapsed ? '64px' : '220px'" class="aside">
      <div class="logo">
        <el-icon><Box /></el-icon>
        <span v-show="!collapsed">ShopDDS 总部</span>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        :collapse="collapsed"
        background-color="#001529"
        text-color="#b7bdc6"
        active-text-color="#fff"
      >
        <el-menu-item index="/headadmin"><el-icon><DataBoard /></el-icon><span>工作台</span></el-menu-item>
        <el-sub-menu index="org">
          <template #title><el-icon><User /></el-icon><span>组织管理</span></template>
          <el-menu-item index="/headadmin/headadmins">总部管理员</el-menu-item>
          <el-menu-item index="/headadmin/employees">员工管理</el-menu-item>
          <el-menu-item index="/headadmin/warehouses">仓库管理</el-menu-item>
          <el-menu-item index="/headadmin/supermarkets">超市管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/headadmin/commodities"><el-icon><Goods /></el-icon><span>商品管理</span></el-menu-item>
        <el-menu-item index="/headadmin/distances"><el-icon><Place /></el-icon><span>距离管理</span></el-menu-item>
        <el-sub-menu index="decision">
          <template #title><el-icon><Histogram /></el-icon><span>决策中心</span></template>
          <el-menu-item index="/headadmin/summary">数据汇总</el-menu-item>
          <el-menu-item index="/headadmin/assigns">物资分配</el-menu-item>
          <el-menu-item index="/headadmin/allocations">物资调拨</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/headadmin/members"><el-icon><Avatar /></el-icon><span>会员管理</span></el-menu-item>
        <el-menu-item index="/headadmin/sales-records"><el-icon><Tickets /></el-icon><span>销售记录</span></el-menu-item>
        <el-menu-item index="/headadmin/password"><el-icon><Lock /></el-icon><span>修改密码</span></el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <el-icon class="collapse-btn" @click="collapsed = !collapsed"><Fold v-if="!collapsed" /><Expand v-else /></el-icon>
        <div class="header-right">
          <span>欢迎，{{ auth.user?.name }}（总部管理员）</span>
          <el-button text type="danger" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()
const collapsed = ref(false)

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100vh; }
.aside { background: #001529; transition: width 0.2s; overflow-x: hidden; }
.logo {
  height: 60px; color: #fff; font-size: 18px; font-weight: 600;
  display: flex; align-items: center; gap: 8px; padding-left: 20px;
  white-space: nowrap;
}
.header {
  background: #fff; display: flex; align-items: center; justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}
.collapse-btn { font-size: 22px; cursor: pointer; }
.header-right { display: flex; align-items: center; gap: 12px; }
.main { background: #f0f2f5; padding: 0; overflow-y: auto; }
</style>