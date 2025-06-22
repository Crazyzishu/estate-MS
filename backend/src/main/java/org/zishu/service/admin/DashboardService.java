package org.zishu.service.admin;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardCards();
    List<Map<String, Object>> getSalesTrend(String timeRange); // 支持参数传入时间范围
//    List<Map<String, Object>> getHouseTypeDistribution();
    List<Map<String, Object>> getHouseRoomTypeDistribution(); // 新增方法
    List<Map<String, Object>> getRecentHouses();
    List<Map<String, Object>> getRecentAppointments();
}

