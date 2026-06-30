<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listMembers, addPoints } from '@/api/member'
import type { Member } from '@/types'

const loading = ref(false)
const submitting = ref(false)
const members = ref<Member[]>([])
const keyword = ref('')

const form = ref({
  cardnum: '',
  amount: 0
})
const RATE = 1 // 1元 = 1积分
const points = computed(() => Math.floor(form.value.amount * RATE))

const filteredMembers = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return members.value
  return members.value.filter(
    (m) =>
      m.memberCardnum.toLowerCase().includes(kw) ||
      m.memberName.toLowerCase().includes(kw)
  )
})

const loadMembers = async () => {
  loading.value = true
  try {
    const res = await listMembers()
    members.value = res.data || []
  } finally {
    loading.value = false
  }
}

const onSettle = async () => {
  if (!form.value.cardnum) {
    ElMessage.warning('请输入会员卡号')
    return
  }
  if (!form.value.amount || form.value.amount <= 0) {
    ElMessage.warning('请输入有效的消费金额')
    return
  }
  submitting.value = true
  try {
    await addPoints(form.value.cardnum, points.value)
    ElMessage.success(`积分结算成功，本次增加 ${points.value} 积分`)
    form.value.cardnum = ''
    form.value.amount = 0
    await loadMembers()
  } catch (e) {
    ElMessage.error('结算失败，请核对会员卡号')
  } finally {
    submitting.value = false
  }
}

onMounted(loadMembers)
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>会员积分结算</template>

      <el-form
        :model="form"
        label-width="110px"
        style="max-width: 520px"
        inline-message
      >
        <el-form-item label="会员卡号">
          <el-input
            v-model="form.cardnum"
            placeholder="请输入会员卡号"
            clearable
          />
        </el-form-item>
        <el-form-item label="消费金额(元)">
          <el-input-number
            v-model="form.amount"
            :min="0"
            :precision="2"
            :step="1"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="换算积分">
          <el-tag type="success" size="large">{{ points }} 积分</el-tag>
          <span class="rate-tip">换算比例：1元 = {{ RATE }}积分</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="onSettle">
            积分结算
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>会员列表（用于核对卡号）</span>
          <el-input
            v-model="keyword"
            placeholder="按卡号或姓名搜索"
            clearable
            style="width: 240px"
            :prefix-icon="'Search'"
          />
        </div>
      </template>
      <el-table
        v-loading="loading"
        :data="filteredMembers"
        stripe
        border
        style="width: 100%"
        max-height="420"
      >
        <el-table-column prop="memberCardnum" label="会员卡号" min-width="140" />
        <el-table-column prop="memberName" label="会员姓名" min-width="120" />
        <el-table-column prop="memberTel" label="联系电话" min-width="140" />
        <el-table-column prop="memberPoints" label="当前积分" min-width="120" align="right" />
        <el-empty v-if="!filteredMembers.length && !loading" slot="empty" description="暂无会员数据" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.rate-tip {
  margin-left: 12px;
  color: #909399;
  font-size: 13px;
}
</style>