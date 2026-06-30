import request from '@/utils/request'
import type { Member, SalesRecord, Result } from '@/types'

export const listMembers = () => request.get<Result<Member[]>, Result<Member[]>>('/members')
export const addPoints = (memberCardnum: string, points: number) => request.patch('/members/points', { memberCardnum, points })

export const listSalesRecords = () => request.get<Result<SalesRecord[]>, Result<SalesRecord[]>>('/sales-records')