package org.zishu.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.ComplaintMapper;
import org.zishu.pojo.*;
import org.zishu.service.admin.ComplaintService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    private ComplaintMapper complaintMapper;

    @Override
    public PageResult<Complaint> page(ComplaintQueryParam complaintQueryParam) {
        //1.设置分页参数
        PageHelper.startPage(complaintQueryParam.getPageNum(),complaintQueryParam.getPageSize());

        //2.执行查询
        List<Complaint> houseList = complaintMapper.list(complaintQueryParam);

        //3.解析想查询结果,并封装到PageResult
        Page<Complaint> p = (Page<Complaint>) houseList;
        return new PageResult<Complaint>(p.getTotal(), p.getResult());
    }

    @Override
    public Complaint getById(Long id) {
        return complaintMapper.getById(id);
    }

    @Override
    public void add(Complaint complaint) {
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdateAt(LocalDateTime.now());
        complaintMapper.insert(complaint);
    }

    @Override
    public void update(Complaint complaint) {
        complaintMapper.update(complaint);
    }

    @Override
    public void deleteById(Long id) {
        complaintMapper.deleteById(id);
    }
}
