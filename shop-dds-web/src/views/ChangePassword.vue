<template>
  <div class="page-container">
    <el-card class="box-card" style="max-width:480px; margin:0 auto;">
      <template #header><span class="page-title">修改密码</span></template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="form.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirm">
          <el-input v-model="form.confirm" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="onSubmit">提交修改</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import { changeEmployeePassword, changeHeadAdminPassword } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const formRef = ref<FormInstance>()
const loading = ref(false)
const form = ref({ oldPassword: '', newPassword: '', confirm: '' })

const rules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, message: '密码至少 6 位', trigger: 'blur' }],
  confirm: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: (_r: any, v: string, cb: any) => v === form.value.newPassword ? cb() : cb(new Error('两次密码不一致')), trigger: 'blur' }
  ]
}

function reset() { form.value = { oldPassword: '', newPassword: '', confirm: '' } }

async function onSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      if (auth.role === 'HEAD_ADMIN') {
        await changeHeadAdminPassword(form.value.oldPassword, form.value.newPassword)
      } else {
        await changeEmployeePassword(form.value.oldPassword, form.value.newPassword)
      }
      ElMessage.success('密码修改成功，请重新登录')
      auth.logout()
      window.location.href = '/login'
    } finally {
      loading.value = false
    }
  })
}
</script>