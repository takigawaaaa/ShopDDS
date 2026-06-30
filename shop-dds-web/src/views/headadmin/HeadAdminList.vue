<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">总部管理员管理</span>
          <el-button type="primary" :icon="Plus" @click="openAdd">新增</el-button>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="headadminId" label="编号" width="160" />
        <el-table-column prop="headadminName" label="姓名" width="160" />
        <el-table-column prop="headadminTel" label="电话" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-popconfirm title="确定删除该管理员?" @confirm="onDelete(row.headadminId)">
              <template #reference><el-button size="small" type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增总部管理员" width="460px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编号" prop="headadminId">
          <el-input v-model="form.headadminId" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="姓名" prop="headadminName">
          <el-input v-model="form.headadminName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="密码" prop="headadminPwd">
          <el-input v-model="form.headadminPwd" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="电话" prop="headadminTel">
          <el-input v-model="form.headadminTel" placeholder="请输入电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { listHeadAdmins, addHeadAdmin, deleteHeadAdmin } from '@/api/employee'
import type { HeadAdmin } from '@/types'

const list = ref<HeadAdmin[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const formRef = ref<FormInstance>()

const form = reactive<HeadAdmin>({
  headadminId: '',
  headadminName: '',
  headadminPwd: '',
  headadminTel: ''
})

const rules: FormRules = {
  headadminId: [{ required: true, message: '请输入编号', trigger: 'blur' }],
  headadminName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  headadminPwd: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少 6 位', trigger: 'blur' }],
  headadminTel: [{ required: true, message: '请输入电话', trigger: 'blur' }]
}

async function load() {
  loading.value = true
  try {
    const res = await listHeadAdmins()
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}

function openAdd() {
  Object.assign(form, { headadminId: '', headadminName: '', headadminPwd: '', headadminTel: '' })
  dialogVisible.value = true
}

async function onSave() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      await addHeadAdmin({ ...form })
      ElMessage.success('新增成功')
      dialogVisible.value = false
      await load()
    } finally {
      saving.value = false
    }
  })
}

async function onDelete(id: string) {
  await deleteHeadAdmin(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.page-title{ font-size:16px; font-weight:600; }
</style>