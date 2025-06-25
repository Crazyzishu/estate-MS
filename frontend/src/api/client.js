import request from "../utils/request";

//查询客户信息
export const queryPageApi=
(name,type,intent,pageNum,pageSize)=> 
    request.get(`/clients/query?name=${name}&type=${type}&intent=${intent}&pageNum=${pageNum}&pageSize=${pageSize}`);

//新增客户
export const addClientApi=(client)=>
    request.post('/clients',client);

//修改客户信息
export const updateClientApi=(client)=>
    request.put('/clients',client);

//根据id查询客户信息
export const queryInfoApi=(clientId)=>
    request.get(`/clients/${clientId}`);

//根据id删除客户信息
export const deleteClientApi=(clientId)=>
    request.delete(`/clients/${clientId}`);
