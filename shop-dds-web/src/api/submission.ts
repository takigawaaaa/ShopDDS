import request from '@/utils/request'

// 仓库管理员提交库存
export const submitInventory = (commodityId: string, amount: number) =>
  request.post('/warehouse/inventories', { commodityId, amount })

// 超市管理员提交申请
export const submitApplication = (commodityId: string, amount: number) =>
  request.post('/supermarket/applications', { commodityId, amount })