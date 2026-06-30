<template>
  <el-container class="layout">
    <el-aside :width="collapsed ? '64px' : '220px'" class="aside">
      <div class="logo">
        <el-icon><ShoppingCart /></el-icon>
        <span v-show="!collapsed">ShopDDS 超市</span>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        :collapse="collapsed"
        background-color="#3a2d1a"
        text-color="#d8c9a8"
        active-text-color="#fff"
      >
        <el-menu-item index="/supermarket"><el-icon><DataBoard /></el-icon><span>工作台</span></el-menu-item>
        <el-menu-item index="/supermarket/commodities"><el-icon><Goods /></el-icon><span>商品查询</span></el-menu-item>
        <el-menu-item index="/supermarket/application"><el-icon><EditPen /></el-icon><span>提交申请</span></el-menu-item>
        <el-menu-item index="/supermarket/assigns"><el-icon><List /></el-icon><span>我的分配</span></el-menu-item>
        <el-menu-item index="/supermarket/receive"><el-icon><Van /></el-icon><span>收物表</span></el-menu-item>
        <el-menu-item index="/supermarket/members"><el-icon><Avatar /></el-icon><span>会员积分</span></el-menu-item>
        <el-menu-item index="/supermarket/password"><el-icon><Lock /></el-icon><span>修改密码</span></el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <el-icon class="collapse-btn" @click="collapsed = !collapsed"><Fold v-if="!collapsed" /><Expand v-else /></el-icon>
        <div class="header-right">
          <span>欢迎，{{ auth.user?.name }}（{{ auth.user?.facilityName }}）</span>
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
.aside { background:#3a2d1a; transition: width 0.2s; overflow-x: hidden; }
.logo { height:60px; color:#fff; font-size:18px; font-weight:600; display:flex; align-items:center; gap:8px; padding-left:20px; white-space:nowrap; }
.header { background:#fff; display:flex; align-items:center; justify-content:space-between; box-shadow:0 1px 4px rgba(0,0,0,0.06); }
.collapse-btn { font-size:22px; cursor:pointer; }
.header-right { display:flex; align-items:center; gap:12px; }
.main { background:#f0f2f5; padding:0; overflow-y:auto; }
</style>