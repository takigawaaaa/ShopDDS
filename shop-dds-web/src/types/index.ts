// 统一后端响应结构
export interface Result<T = any> {
  code: number
  msg: string
  data: T
}

// 角色
export type Role = 'HEAD_ADMIN' | 'WAREHOUSE_ADMIN' | 'SUPERMARKET_ADMIN'

// 登录请求/响应
export interface LoginRequest {
  username: string
  password: string
  role: Role
}
export interface LoginResponse {
  token: string
  userId: string
  name: string
  role: Role
  facilityId: string | null
  facilityName: string | null
}

// 下拉框项
export interface DropdownItem {
  id: string
  name: string
}

// 实体类型
export interface Commodity {
  commodityId: string
  commodityBarcode: string
  commodityName: string
  commodityClass: string
  commodityUnit: string
  commodityPrice: number | string
}
export interface HeadAdmin {
  headadminId: string
  headadminName: string
  headadminPwd?: string
  headadminTel: string
}
export interface Employee {
  employeeId: string
  employeeName: string
  employeePwd?: string
  employeeIde: string
  employeeTel: string
}
export interface Supermarket {
  supermarketId: string
  supermarketName: string
  supermarketAdr: string
  supermarketTel: string
  employeeId: string
}
export interface Warehouse {
  warehouseId: string
  warehouseName: string
  warehouseAdr: string
  warehouseTel: string
  employeeId: string
}
export interface Member {
  memberId: string
  memberName: string
  memberCardnum: string
  memberTel: string
  memberPoints: number
}
export interface SalesRecord {
  transid: string
  supermarketId: string
  commodityId: string
  quantity: number
  totalPrice: number
  memberId: string | null
  saleTime: string
}
export interface Inventory {
  commodityId: string
  warehouseId: string
  invAmount: number
  invTime?: string
}
export interface Application {
  supermarketId: string
  commodityId: string
  aplAmount: number
  aplTime?: string
}
export interface Distance {
  warehouseId: string
  supermarketId: string
  distance: number
}
export interface Assign {
  supermarketId: string
  commodityId: string
  assignAmount: number
}
export interface Allocate {
  supermarketId: string
  warehouseId: string
  commodityId: string
  allocateAmount: number
}
export interface AllInventory {
  commodityId: string
  totalInventory: number
}
export interface AllApplication {
  commodityId: string
  totalApplication: number
}
export interface SummaryResult {
  inventorySummary: AllInventory[]
  applicationSummary: AllApplication[]
}
export interface AllocationResult {
  processedCount: number
  message: string
  details: string[]
}