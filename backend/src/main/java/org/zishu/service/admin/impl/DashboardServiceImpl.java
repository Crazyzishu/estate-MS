package org.zishu.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.DashboardMapper;
import org.zishu.service.admin.DashboardService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    // 工具方法：安全转换为 Double
    private Double toDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public Map<String, Object> getDashboardCards() {
        Map<String, Object> data = dashboardMapper.getDashboardCards();

        // 确保趋势字段是 Double 类型
        if (data != null) {
            data.put("houseGrowthRate", toDouble(data.get("houseGrowthRate")));
            data.put("clientGrowthRate", toDouble(data.get("clientGrowthRate")));
            data.put("appointmentGrowthRate", toDouble(data.get("appointmentGrowthRate")));
        }

        return data;
    }

    @Override
    public List<Map<String, Object>> getSalesTrend(String timeRange) {
        List<Map<String, Object>> rawResult = dashboardMapper.getSalesTrend(timeRange);

        return rawResult.stream().map(map -> {
            Map<String, Object> newMap = new HashMap<>(map);
            Object amountObj = map.get("salesAmount");

            // 强制转换 BigDecimal → Double
            if (amountObj instanceof BigDecimal) {
                newMap.put("salesAmount", ((BigDecimal) amountObj).doubleValue());
            } else if (amountObj instanceof Number) {
                newMap.put("salesAmount", ((Number) amountObj).doubleValue());
            } else {
                newMap.put("salesAmount", 0.0); // 默认值防止 NaN
            }

            return newMap;
        }).collect(Collectors.toList());
    }


    @Override
    public List<Map<String, Object>> getHouseRoomTypeDistribution() {
        return dashboardMapper.getHouseRoomTypeDistribution();
    }


    @Override
    public List<Map<String, Object>> getRecentHouses() {
        return dashboardMapper.getRecentHouses();
    }

    @Override
    public List<Map<String, Object>> getRecentAppointments() {
        return dashboardMapper.getRecentAppointments();
    }
}
