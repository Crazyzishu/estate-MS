package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zishu.pojo.Complaint;
import org.zishu.pojo.ComplaintQueryParam;

import java.util.List;

@Mapper
public interface ComplaintMapper {


    List<Complaint> list(ComplaintQueryParam complaintQueryParam);

    Complaint getById(Long id);

    void insert(Complaint complaint);

    void update(Complaint complaint);

    void deleteById(Long id);
}