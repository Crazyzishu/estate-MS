import request from "../utils/request";
import { descriptionItemProps, imageEmits } from "element-plus";

//查询房源信息
export const queryPageApi=
(address,minPrice,maxPrice,status,pageNum,pageSize)=> 
    request.get(`/houses/query?address=${address}&minPrice=${minPrice}&maxPrice=${maxPrice}&status=${status}&pageNum=${pageNum}&pageSize=${pageSize}`);

//新增房源
export const addHouseApi=(house)=>
    request.post('/houses',house);

//根据id查询房源信息
export const queryInfoApi=(houseId)=>
    request.get(`/houses/${houseId}`);

//修改房源信息
export const updateHouseApi=(house)=>
    request.put('/houses',house);

//删除房源信息
export const deleteHouseApi=(houseId)=>
    request.delete(`/houses/${houseId}`);

