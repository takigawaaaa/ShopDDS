<template>
  <div class="login-wrap">
    <div class="login-card">
      <h2>总部管理员登录</h2>
      <p class="login-sub">ShopDDS 决策支持系统</p>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent>
        <el-form-item label="管理员编号" prop="username">
          <el-input v-model="form.username" placeholder="请输入管理员编号" :prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" :prefix-icon="Lock" @keyup.enter="onLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width:100%" size="large" @click="onLogin">登 录</el-button>
        </el-form-item>
      </el-form>

      <p class="login-sub" style="text-align:center;">
        <el-link type="primary" :underline="false" @click="$router.push('/login')">返回身份选择</el-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = ref({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入管理员编号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function onLogin() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const resp = await login({
        username: form.value.username,
        password: form.value.password,
        role: 'HEAD_ADMIN'
      })
      auth.setLogin(resp.data)
      ElMessage.success('登录成功')
      router.push('/headadmin')
    } finally {
      loading.value = false
    }
  })
}
</script>