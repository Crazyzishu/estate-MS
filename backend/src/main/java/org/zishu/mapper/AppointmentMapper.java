package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zishu.pojo.Appointment;
import org.zishu.pojo.AppointmentQueryParam;

import java.util.List;

@Mapper
public interface AppointmentMapper {

    List<Appointment> selectByParam(AppointmentQueryParam param);

    Appointment selectAppointmentById(Long id);

    int insertAppointment(Appointment appointment);

    int updateAppointment(Appointment appointment);

    int deleteAppointment(Long id);
}