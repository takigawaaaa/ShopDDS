<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">员工管理</span>
          <el-button type="primary" :icon="Plus" @click="openAdd">新增</el-button>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="employeeId" label="编号" width="140" />
        <el-table-column prop="employeeName" label="姓名" width="140" />
        <el-table-column prop="employeeIde" label="身份" width="140">
          <template #default="{ row }">{{ roleText(row.employeeIde) }}</template>
        </el-table-column>
        <el-table-column prop="employeeTel" label="电话" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-popconfirm title="确定删除该员工?" @confirm="onDelete(row.employeeId)">
              <template #reference><el-button size="small" type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增员工" width="460px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编号" prop="employeeId">
          <el-input v-model="form.employeeId" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="姓名" prop="employeeName">
          <el-input v-model="form.employeeName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="密码" prop="employeePwd">
          <el-input v-model="form.employeePwd" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="身份" prop="employeeIde">
          <el-select v-model="form.employeeIde" placeholder="请选择身份" style="width:100%">
            <el-option label="仓库管理员" value="WAREHOUSE_ADMIN" />
            <el-option label="超市管理员" value="SUPERMARKET_ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="employeeTel">
          <el-input v-model="form.employeeTel" placeholder="请输入电话" />
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
import { listEmployees, addEmployee, deleteEmployee } from '@/api/employee'
import type { Employee } from '@/types'

const list = ref<Employee[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const formRef = ref<FormInstance>()

const form = reactive<Employee>({
  employeeId: '',
  employeeName: '',
  employeePwd: '',
  employeeIde: '',
  employeeTel: ''
})

const rules: FormRules = {
  employeeId: [{ required: true, message: '请输入编号', trigger: 'blur' }],
  employeeName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  employeePwd: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少 6 位', trigger: 'blur' }],
  employeeIde: [{ required: true, message: '请选择身份', trigger: 'change' }],
  employeeTel: [{ required: true, message: '请输入电话', trigger: 'blur' }]
}

function roleText(ide: string) {
  if (ide === 'WAREHOUSE_ADMIN') return '仓库管理员'
  if (ide === 'SUPERMARKET_ADMIN') return '超市管理员'
  if (ide === 'HEAD_ADMIN') return '总部管理员'
  return ide
}

async function load() {
  loading.value = true
  try {
    const res = await listEmployees()
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}

function openAdd() {
  Object.assign(form, { employeeId: '', employeeName: '', employeePwd: '', employeeIde: '', employeeTel: '' })
  dialogVisible.value = true
}

async function onSave() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      await addEmployee({ ...form })
      ElMessage.success('新增成功')
      dialogVisible.value = false
      await load()
    } finally {
      saving.value = false
    }
  })
}

async function onDelete(id: string) {
  await deleteEmployee(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.page-title{ font-size:16px; font-weight:600; }
</style>