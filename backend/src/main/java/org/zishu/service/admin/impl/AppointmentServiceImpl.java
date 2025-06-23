package org.zishu.service.admin.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.AppointmentMapper;
import org.zishu.pojo.Appointment;
import org.zishu.pojo.AppointmentQueryParam;
import org.zishu.service.admin.AppointmentService;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public List<Appointment> getPaginatedAppointments(AppointmentQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return appointmentMapper.selectByParam(param);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentMapper.selectAppointmentById(id);
    }

    @Override
    public boolean createAppointment(Appointment appointment) {
        return appointmentMapper.insertAppointment(appointment) > 0;
    }

    @Override
    public boolean updateAppointment(Long id, Appointment updated) {
        Appointment existing = appointmentMapper.selectAppointmentById(id);
        if (existing == null) {
            return false;
        }
        updated.setAppointmentId(id); // 设置主键
        return appointmentMapper.updateAppointment(updated) > 0;
    }

    @Override
    public boolean deleteAppointment(Long id) {
        return appointmentMapper.deleteAppointment(id) > 0;
    }
}
