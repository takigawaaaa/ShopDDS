import request from '@/utils/request'
import type { Commodity, Result } from '@/types'

export const listCommodities = () => request.get<Result<Commodity[]>, Result<Commodity[]>>('/commodities')
export const addCommodity = (data: Commodity) => request.post('/commodities', data)
export const updateCommodity = (id: string, data: Commodity) => request.put(`/commodities/${id}`, data)
export const deleteCommodity = (id: string) => request.delete(`/commodities/${id}`)