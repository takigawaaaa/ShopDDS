<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">距离管理</span>
          <div>
            <el-button type="primary" :icon="Plus" @click="openAdd">新增</el-button>
            <el-popconfirm title="确定清空全部距离记录?此操作不可恢复" @confirm="onClearAll">
              <template #reference><el-button type="danger" :icon="Delete">清空全部</el-button></template>
            </el-popconfirm>
          </div>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="warehouseId" label="仓库编号" width="180" />
        <el-table-column prop="supermarketId" label="超市编号" width="180" />
        <el-table-column prop="distance" label="距离" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-popconfirm title="确定删除该距离记录?" @confirm="onDelete(row.warehouseId, row.supermarketId)">
              <template #reference><el-button size="small" type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增距离记录" width="460px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="仓库" prop="warehouseId">
          <el-select v-model="form.warehouseId" placeholder="请选择仓库" filterable style="width:100%" :loading="whLoading">
            <el-option v-for="w in warehouses" :key="w.id" :label="w.name" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="超市" prop="supermarketId">
          <el-select v-model="form.supermarketId" placeholder="请选择超市" filterable style="width:100%" :loading="smLoading">
            <el-option v-for="s in supermarkets" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="距离" prop="distance">
          <el-input-number v-model="form.distance" :min="0" :precision="2" :step="1" controls-position="right" style="width:100%" />
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
import { Plus, Delete } from '@element-plus/icons-vue'
import { listDistances, addDistance, deleteDistance, deleteAllDistances } from '@/api/distance'
import { getWarehouses, getSupermarkets } from '@/api/common'
import type { Distance, DropdownItem } from '@/types'

const list = ref<Distance[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const formRef = ref<FormInstance>()

const warehouses = ref<DropdownItem[]>([])
const supermarkets = ref<DropdownItem[]>([])
const whLoading = ref(false)
const smLoading = ref(false)

const form = reactive<Distance>({ warehouseId: '', supermarketId: '', distance: 0 })

const rules: FormRules = {
  warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
  supermarketId: [{ required: true, message: '请选择超市', trigger: 'change' }],
  distance: [{ required: true, message: '请输入距离', trigger: 'blur' }]
}

async function load() {
  loading.value = true
  try {
    const res = await listDistances()
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}

async function loadDropdowns() {
  whLoading.value = true; smLoading.value = true
  try {
    const [w, s] = await Promise.all([getWarehouses(), getSupermarkets()])
    warehouses.value = w.data ?? []
    supermarkets.value = s.data ?? []
  } finally {
    whLoading.value = false; smLoading.value = false
  }
}

function openAdd() {
  Object.assign(form, { warehouseId: '', supermarketId: '', distance: 0 })
  if (warehouses.value.length === 0 || supermarkets.value.length === 0) loadDropdowns()
  dialogVisible.value = true
}

async function onSave() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      await addDistance({ ...form })
      ElMessage.success('新增成功')
      dialogVisible.value = false
      await load()
    } finally {
      saving.value = false
    }
  })
}

async function onDelete(warehouseId: string, supermarketId: string) {
  await deleteDistance(warehouseId, supermarketId)
  ElMessage.success('删除成功')
  await load()
}

async function onClearAll() {
  await deleteAllDistances()
  ElMessage.success('已清空全部距离记录')
  await load()
}

onMounted(load)
</script>

<style scoped>
.page-title{ font-size:16px; font-weight:600; }
</style>