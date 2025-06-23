package org.zishu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.Appointment;
import org.zishu.pojo.AppointmentQueryParam;
import org.zishu.pojo.Result;
import org.zishu.service.admin.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 分页查询预约记录
     */
    @GetMapping
    public Result getPaginatedAppointments(AppointmentQueryParam param) {
        List<Appointment> appointments = appointmentService.getPaginatedAppointments(param);
        return Result.success(appointments);
    }

    /**
     * 根据 ID 获取单个预约
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment == null) {
            return Result.error("未找到对应的预约记录");
        }
        return Result.success(appointment);
    }

    /**
     * 新增预约记录
     */
    @PostMapping
    public Result add(@RequestBody Appointment appointment) {
        boolean success = appointmentService.createAppointment(appointment);
        return success ? Result.success(appointment) : Result.error("新增预约失败");
    }

    /**
     * 更新预约记录
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Appointment appointmentDetails) {
        boolean success = appointmentService.updateAppointment(id, appointmentDetails);
        return success ? Result.success(appointmentDetails) : Result.error("更新失败：预约记录不存在");
    }

    /**
     * 删除预约记录
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        boolean success = appointmentService.deleteAppointment(id);
        return success ? Result.success(true) : Result.error("删除失败：预约记录不存在");
    }
}