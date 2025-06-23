package org.zishu.service.admin;

import org.zishu.pojo.Appointment;
import org.zishu.pojo.AppointmentQueryParam;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getPaginatedAppointments(AppointmentQueryParam param);
    Appointment getAppointmentById(Long id);
    boolean createAppointment(Appointment appointment);
    boolean updateAppointment(Long id, Appointment updated);
    boolean deleteAppointment(Long id);
}