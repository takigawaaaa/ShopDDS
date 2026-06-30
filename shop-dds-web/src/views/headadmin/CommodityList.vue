<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">商品管理</span>
          <el-button type="primary" :icon="Plus" @click="openAdd">新增</el-button>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="commodityId" label="编号" width="120" />
        <el-table-column prop="commodityBarcode" label="条形码" width="160" />
        <el-table-column prop="commodityName" label="名称" minWidth="140" />
        <el-table-column prop="commodityClass" label="类别" width="120" />
        <el-table-column prop="commodityUnit" label="单位" width="100" />
        <el-table-column prop="commodityPrice" label="单价" width="120" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-popconfirm title="确定删除该商品?" @confirm="onDelete(row.commodityId)">
              <template #reference><el-button size="small" type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="title" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="编号" prop="commodityId">
          <el-input v-model="form.commodityId" :disabled="isEdit" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="条形码" prop="commodityBarcode">
          <el-input v-model="form.commodityBarcode" placeholder="请输入条形码" />
        </el-form-item>
        <el-form-item label="名称" prop="commodityName">
          <el-input v-model="form.commodityName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="类别" prop="commodityClass">
          <el-input v-model="form.commodityClass" placeholder="请输入类别" />
        </el-form-item>
        <el-form-item label="单位" prop="commodityUnit">
          <el-input v-model="form.commodityUnit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="单价" prop="commodityPrice">
          <el-input-number v-model="form.commodityPrice" :min="0" :precision="2" :step="0.1" controls-position="right" style="width:100%" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { listCommodities, addCommodity, updateCommodity, deleteCommodity } from '@/api/commodity'
import type { Commodity } from '@/types'

const list = ref<Commodity[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()

const form = reactive<Commodity>({
  commodityId: '', commodityBarcode: '', commodityName: '', commodityClass: '', commodityUnit: '', commodityPrice: 0
})

const title = computed(() => isEdit.value ? '编辑商品' : '新增商品')

const rules: FormRules = {
  commodityId: [{ required: true, message: '请输入编号', trigger: 'blur' }],
  commodityBarcode: [{ required: true, message: '请输入条形码', trigger: 'blur' }],
  commodityName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  commodityClass: [{ required: true, message: '请输入类别', trigger: 'blur' }],
  commodityUnit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
  commodityPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }]
}

async function load() {
  loading.value = true
  try {
    const res = await listCommodities()
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}

function openAdd() {
  isEdit.value = false
  Object.assign(form, { commodityId: '', commodityBarcode: '', commodityName: '', commodityClass: '', commodityUnit: '', commodityPrice: 0 })
  dialogVisible.value = true
}

function openEdit(row: Commodity) {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

async function onSave() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      if (isEdit.value) {
        await updateCommodity(form.commodityId, { ...form })
        ElMessage.success('修改成功')
      } else {
        await addCommodity({ ...form })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      await load()
    } finally {
      saving.value = false
    }
  })
}

async function onDelete(id: string) {
  await deleteCommodity(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.page-title{ font-size:16px; font-weight:600; }
</style>