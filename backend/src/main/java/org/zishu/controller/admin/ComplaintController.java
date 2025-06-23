package org.zishu.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.Complaint;
import org.zishu.pojo.ComplaintQueryParam;
import org.zishu.pojo.PageResult;
import org.zishu.service.admin.ComplaintService;
import org.zishu.pojo.Result;

@Slf4j
@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    /**
     * 分页查询投诉列表
     */
    @GetMapping("/query")
    public Result page(ComplaintQueryParam complaintQueryParam){
        log.info("分页查询: {}", complaintQueryParam);
        PageResult<Complaint> pageResult = complaintService.page(complaintQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 根据ID获取投诉详情
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        log.info("根据ID获取投诉信息: {}", id);
        Complaint complaint = complaintService.getById(id);
        return Result.success(complaint);
    }

    /**
     * 新增投诉
     */
    @PostMapping
    public Result add(@RequestBody Complaint complaint) {
        log.info("新增投诉: {}", complaint);
        complaintService.add(complaint);
        return Result.success();
    }

    /**
     * 更新投诉
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Complaint complaint) {
        log.info("更新投诉信息: {}", complaint);
        complaint.setComplaintId(id);
        complaintService.update(complaint);
        return Result.success();
    }

    /**
     * 删除投诉
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        log.info("删除投诉: {}", id);
        complaintService.deleteById(id);
        return Result.success();
    }
}
