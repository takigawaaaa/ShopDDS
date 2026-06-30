<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">超市管理</span>
          <el-button type="primary" :icon="Plus" @click="openAdd">新增</el-button>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="supermarketId" label="编号" width="120" />
        <el-table-column prop="supermarketName" label="名称" minWidth="140" />
        <el-table-column prop="supermarketAdr" label="地址" minWidth="180" />
        <el-table-column prop="supermarketTel" label="电话" width="140" />
        <el-table-column prop="employeeId" label="管理员编号" width="120" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row, 'name')">改名</el-button>
            <el-button size="small" @click="openEdit(row, 'address')">改地址</el-button>
            <el-button size="small" @click="openEdit(row, 'manager')">改管理员</el-button>
            <el-popconfirm title="确定删除该超市?" @confirm="onDelete(row.supermarketId)">
              <template #reference><el-button size="small" type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增弹窗 -->
    <el-dialog v-model="addVisible" title="新增超市" width="480px">
      <el-form ref="addFormRef" :model="addForm" :rules="addRules" label-width="90px">
        <el-form-item label="编号" prop="supermarketId">
          <el-input v-model="addForm.supermarketId" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="名称" prop="supermarketName">
          <el-input v-model="addForm.supermarketName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="地址" prop="supermarketAdr">
          <el-input v-model="addForm.supermarketAdr" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="电话" prop="supermarketTel">
          <el-input v-model="addForm.supermarketTel" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="管理员" prop="employeeId">
          <el-select v-model="addForm.employeeId" placeholder="请选择管理员" filterable style="width:100%" :loading="managerLoading">
            <el-option v-for="m in managers" :key="m.id" :label="m.name" :value="m.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addVisible=false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSaveAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑弹窗 -->
    <el-dialog v-model="editVisible" :title="editTitle" width="460px">
      <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
        <el-form-item v-if="editType==='name'" label="名称" prop="supermarketName">
          <el-input v-model="editForm.supermarketName" />
        </el-form-item>
        <el-form-item v-else-if="editType==='address'" label="地址" prop="supermarketAdr">
          <el-input v-model="editForm.supermarketAdr" type="textarea" />
        </el-form-item>
        <el-form-item v-else label="管理员" prop="employeeId">
          <el-select v-model="editForm.employeeId" placeholder="请选择管理员" filterable style="width:100%" :loading="managerLoading">
            <el-option v-for="m in managers" :key="m.id" :label="m.name" :value="m.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible=false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSaveEdit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { listSupermarkets, addSupermarket, deleteSupermarket, updateSuperName, updateSuperAddress, updateSuperManager } from '@/api/organization'
import { getSupermarketManagers } from '@/api/common'
import type { Supermarket, DropdownItem } from '@/types'

const list = ref<Supermarket[]>([])
const loading = ref(false)
const managers = ref<DropdownItem[]>([])
const managerLoading = ref(false)

const addVisible = ref(false)
const editVisible = ref(false)
const saving = ref(false)
const editType = ref<'name' | 'address' | 'manager'>('name')
const addFormRef = ref<FormInstance>()
const editFormRef = ref<FormInstance>()

const addForm = reactive<Supermarket>({
  supermarketId: '', supermarketName: '', supermarketAdr: '', supermarketTel: '', employeeId: ''
})
const editForm = reactive<{ id: string; supermarketName: string; supermarketAdr: string; employeeId: string }>({
  id: '', supermarketName: '', supermarketAdr: '', employeeId: ''
})

const addRules: FormRules = {
  supermarketId: [{ required: true, message: '请输入编号', trigger: 'blur' }],
  supermarketName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  supermarketAdr: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  supermarketTel: [{ required: true, message: '请输入电话', trigger: 'blur' }],
  employeeId: [{ required: true, message: '请选择管理员', trigger: 'change' }]
}
const editRules = computed<FormRules>(() => {
  if (editType.value === 'name') return { supermarketName: [{ required: true, message: '请输入名称', trigger: 'blur' }] }
  if (editType.value === 'address') return { supermarketAdr: [{ required: true, message: '请输入地址', trigger: 'blur' }] }
  return { employeeId: [{ required: true, message: '请选择管理员', trigger: 'change' }] }
})
const editTitle = computed(() => editType.value === 'name' ? '修改名称' : editType.value === 'address' ? '修改地址' : '修改管理员')

async function load() {
  loading.value = true
  try {
    const res = await listSupermarkets()
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}

async function loadManagers() {
  managerLoading.value = true
  try {
    const res = await getSupermarketManagers()
    managers.value = res.data ?? []
  } finally {
    managerLoading.value = false
  }
}

function openAdd() {
  Object.assign(addForm, { supermarketId: '', supermarketName: '', supermarketAdr: '', supermarketTel: '', employeeId: '' })
  if (managers.value.length === 0) loadManagers()
  addVisible.value = true
}

function openEdit(row: Supermarket, type: 'name' | 'address' | 'manager') {
  editType.value = type
  editForm.id = row.supermarketId
  editForm.supermarketName = row.supermarketName
  editForm.supermarketAdr = row.supermarketAdr
  editForm.employeeId = row.employeeId
  if (type === 'manager' && managers.value.length === 0) loadManagers()
  editVisible.value = true
}

async function onSaveAdd() {
  if (!addFormRef.value) return
  await addFormRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      await addSupermarket({ ...addForm })
      ElMessage.success('新增成功')
      addVisible.value = false
      await load()
    } finally {
      saving.value = false
    }
  })
}

async function onSaveEdit() {
  if (!editFormRef.value) return
  await editFormRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      if (editType.value === 'name') await updateSuperName(editForm.id, editForm.supermarketName)
      else if (editType.value === 'address') await updateSuperAddress(editForm.id, editForm.supermarketAdr)
      else await updateSuperManager(editForm.id, editForm.employeeId)
      ElMessage.success('修改成功')
      editVisible.value = false
      await load()
    } finally {
      saving.value = false
    }
  })
}

async function onDelete(id: string) {
  await deleteSupermarket(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.page-title{ font-size:16px; font-weight:600; }
</style>