package org.zishu.service.admin;

import org.zishu.pojo.ComplaintQueryParam;
import org.zishu.pojo.PageResult;
import org.zishu.pojo.Complaint;
import org.zishu.pojo.Result;

import java.util.List;

public interface ComplaintService {
    PageResult<Complaint> page(ComplaintQueryParam complaintQueryParam);
    Complaint getById(Long id);
    void add(Complaint complaint);
    void update(Complaint complaint);
    void deleteById(Long id);
}
