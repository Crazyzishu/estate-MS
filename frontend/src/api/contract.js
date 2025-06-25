import request from '../utils/request';

//查询合同信息
export const queryPageApi=
(contractId,clientName,contractType,status,pageNum,pageSize)=> 
    request.get(`/contracts/query?contractId=${contractId}&clientName=${clientName}&contractType=${contractType}&status=${status}&pageNum=${pageNum}&pageSize=${pageSize}`)

//查询全部客户
export const getClientsApi=()=> request.get('/contracts/getClients')

//查询全部房源
export const getHousesApi=()=> request.get('/contracts/getHouses')

//查询全部经理
export const getManagersApi=()=> request.get('/contracts/getManagers')

//新增合同
export const addContractApi=(contract)=>
    request.post(`/contracts`,contract)

//根据id查询合同信息
export const queryInfoApi=(contractId)=>
    request.get(`/contracts/${contractId}`)

//修改合同信息
export const updateContractApi=(contract)=>
    request.put(`/contracts`,contract)

//删除合同信息
export const deleteContractApi=(contractId)=>
    request.delete(`/contracts/${contractId}`)