package org.zishu.pojo;

import java.util.List;

/**
 * @author Blank
 * @version 1.0
 * 分页结果封装类
 */
public class PageResult<T> {
    private Long total;//总记录数
    private List<T> rows;//当前页数据列表;

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
