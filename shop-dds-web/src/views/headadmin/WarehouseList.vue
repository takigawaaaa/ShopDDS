<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">仓库管理</span>
          <el-button type="primary" :icon="Plus" @click="openAdd">新增</el-button>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="warehouseId" label="编号" width="120" />
        <el-table-column prop="warehouseName" label="名称" minWidth="140" />
        <el-table-column prop="warehouseAdr" label="地址" minWidth="180" />
        <el-table-column prop="warehouseTel" label="电话" width="140" />
        <el-table-column prop="employeeId" label="管理员编号" width="120" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row, 'name')">改名</el-button>
            <el-button size="small" @click="openEdit(row, 'address')">改地址</el-button>
            <el-button size="small" @click="openEdit(row, 'manager')">改管理员</el-button>
            <el-popconfirm title="确定删除该仓库?" @confirm="onDelete(row.warehouseId)">
              <template #reference><el-button size="small" type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增弹窗 -->
    <el-dialog v-model="addVisible" title="新增仓库" width="480px">
      <el-form ref="addFormRef" :model="addForm" :rules="addRules" label-width="90px">
        <el-form-item label="编号" prop="warehouseId">
          <el-input v-model="addForm.warehouseId" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="名称" prop="warehouseName">
          <el-input v-model="addForm.warehouseName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="地址" prop="warehouseAdr">
          <el-input v-model="addForm.warehouseAdr" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="电话" prop="warehouseTel">
          <el-input v-model="addForm.warehouseTel" placeholder="请输入电话" />
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
        <el-form-item v-if="editType==='name'" label="名称" prop="warehouseName">
          <el-input v-model="editForm.warehouseName" />
        </el-form-item>
        <el-form-item v-else-if="editType==='address'" label="地址" prop="warehouseAdr">
          <el-input v-model="editForm.warehouseAdr" type="textarea" />
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
import { listWarehouses, addWarehouse, deleteWarehouse, updateWareName, updateWareAddress, updateWareManager } from '@/api/organization'
import { getWarehouseManagers } from '@/api/common'
import type { Warehouse, DropdownItem } from '@/types'

const list = ref<Warehouse[]>([])
const loading = ref(false)
const managers = ref<DropdownItem[]>([])
const managerLoading = ref(false)

const addVisible = ref(false)
const editVisible = ref(false)
const saving = ref(false)
const editType = ref<'name' | 'address' | 'manager'>('name')
const addFormRef = ref<FormInstance>()
const editFormRef = ref<FormInstance>()

const addForm = reactive<Warehouse>({
  warehouseId: '', warehouseName: '', warehouseAdr: '', warehouseTel: '', employeeId: ''
})
const editForm = reactive<{ id: string; warehouseName: string; warehouseAdr: string; employeeId: string }>({
  id: '', warehouseName: '', warehouseAdr: '', employeeId: ''
})

const addRules: FormRules = {
  warehouseId: [{ required: true, message: '请输入编号', trigger: 'blur' }],
  warehouseName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  warehouseAdr: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  warehouseTel: [{ required: true, message: '请输入电话', trigger: 'blur' }],
  employeeId: [{ required: true, message: '请选择管理员', trigger: 'change' }]
}
const editRules = computed<FormRules>(() => {
  if (editType.value === 'name') return { warehouseName: [{ required: true, message: '请输入名称', trigger: 'blur' }] }
  if (editType.value === 'address') return { warehouseAdr: [{ required: true, message: '请输入地址', trigger: 'blur' }] }
  return { employeeId: [{ required: true, message: '请选择管理员', trigger: 'change' }] }
})
const editTitle = computed(() => editType.value === 'name' ? '修改名称' : editType.value === 'address' ? '修改地址' : '修改管理员')

async function load() {
  loading.value = true
  try {
    const res = await listWarehouses()
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}

async function loadManagers() {
  managerLoading.value = true
  try {
    const res = await getWarehouseManagers()
    managers.value = res.data ?? []
  } finally {
    managerLoading.value = false
  }
}

function openAdd() {
  Object.assign(addForm, { warehouseId: '', warehouseName: '', warehouseAdr: '', warehouseTel: '', employeeId: '' })
  if (managers.value.length === 0) loadManagers()
  addVisible.value = true
}

function openEdit(row: Warehouse, type: 'name' | 'address' | 'manager') {
  editType.value = type
  editForm.id = row.warehouseId
  editForm.warehouseName = row.warehouseName
  editForm.warehouseAdr = row.warehouseAdr
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
      await addWarehouse({ ...addForm })
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
      if (editType.value === 'name') await updateWareName(editForm.id, editForm.warehouseName)
      else if (editType.value === 'address') await updateWareAddress(editForm.id, editForm.warehouseAdr)
      else await updateWareManager(editForm.id, editForm.employeeId)
      ElMessage.success('修改成功')
      editVisible.value = false
      await load()
    } finally {
      saving.value = false
    }
  })
}

async function onDelete(id: string) {
  await deleteWarehouse(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.page-title{ font-size:16px; font-weight:600; }
</style>