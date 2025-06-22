package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardMapper {

    // 获取首页卡片数据（总房源数、本月销售额、客户总数、预约看房数）
    Map<String, Object> getDashboardCards();

    // 获取销售趋势数据（按月分组）
    List<Map<String, Object>> getSalesTrend(String timeRange);

    // 获取房源类型分布数据（户型统计）
    List<Map<String, Object>> getHouseRoomTypeDistribution();

    // 获取最近房源列表
    List<Map<String, Object>> getRecentHouses();

    // 获取近期预约信息（关联客户表）
    List<Map<String, Object>> getRecentAppointments();
}