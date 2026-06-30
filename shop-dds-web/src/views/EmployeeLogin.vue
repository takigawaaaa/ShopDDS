<template>
  <div class="login-wrap">
    <div class="login-card">
      <h2>{{ roleCn }}登录</h2>
      <p class="login-sub">ShopDDS 决策支持系统</p>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent>
        <el-form-item label="员工编号" prop="username">
          <el-input v-model="form.username" placeholder="请输入员工编号" :prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" :prefix-icon="Lock" @keyup.enter="onLogin" />
        </el-form-item>
        <el-form-item label="验证码" prop="captcha">
          <div style="display:flex; gap:8px; width:100%;">
            <el-input v-model="form.captcha" placeholder="请输入验证码" :prefix-icon="Key" style="flex:1" />
            <img v-if="captchaImg" :src="captchaImg" alt="验证码" class="captcha-img" @click="refreshCaptcha" title="点击刷新" />
          </div>
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
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import { User, Lock, Key } from '@element-plus/icons-vue'
import { login, getCaptcha } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const roleCn = computed(() => (route.query.role as string) || '员工')
const roleCode = computed<'WAREHOUSE_ADMIN' | 'SUPERMARKET_ADMIN'>(() =>
  roleCn.value === '仓库管理员' ? 'WAREHOUSE_ADMIN' : 'SUPERMARKET_ADMIN'
)

const formRef = ref<FormInstance>()
const loading = ref(false)
const captchaImg = ref('')
const plainCaptcha = ref('')

const form = ref({ username: '', password: '', captcha: '' })
const rules = {
  username: [{ required: true, message: '请输入员工编号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

async function refreshCaptcha() {
  const data = await getCaptcha()
  captchaImg.value = data.image
  plainCaptcha.value = data.captcha
}
onMounted(refreshCaptcha)

async function onLogin() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    if (form.value.captcha.toUpperCase() !== plainCaptcha.value.toUpperCase()) {
      ElMessage.error('验证码错误')
      refreshCaptcha()
      return
    }
    loading.value = true
    try {
      const resp = await login({
        username: form.value.username,
        password: form.value.password,
        role: roleCode.value,
        captcha: form.value.captcha
      })
      auth.setLogin(resp.data)
      ElMessage.success('登录成功')
      router.push('/' + (roleCode.value === 'WAREHOUSE_ADMIN' ? 'warehouse' : 'supermarket'))
    } catch {
      refreshCaptcha()
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.captcha-img {
  height: 40px;
  border-radius: 4px;
  cursor: pointer;
  border: 1px solid #dcdfe6;
}
</style>