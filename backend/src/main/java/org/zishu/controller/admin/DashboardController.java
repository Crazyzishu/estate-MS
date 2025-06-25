package org.zishu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.Result;
import org.zishu.service.admin.DashboardService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/cards")
    public Result getCards() {
        return Result.success(dashboardService.getDashboardCards());
    }

    @GetMapping("/sales-trend")
    public Result getSalesTrend(@RequestParam String timeRange) {
        return Result.success(dashboardService.getSalesTrend(timeRange));
    }

    @GetMapping("/house-types")
    public Result getHouseTypes() {
        return Result.success(dashboardService.getHouseRoomTypeDistribution());
    }

    @GetMapping("/recent-houses")
    public Result getRecentHouses() {
        return Result.success(dashboardService.getRecentHouses());
    }

    @GetMapping("/house-room-types")
    public Result getHouseRoomTypes() {
        return Result.success(dashboardService.getHouseRoomTypeDistribution());
    }

    @GetMapping("/recent-appointments")
    public Result getRecentAppointments() {
        return Result.success(dashboardService.getRecentAppointments());
    }
}
