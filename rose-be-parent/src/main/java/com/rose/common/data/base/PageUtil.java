package com.rose.common.data.base;

@lombok.Data
public class PageUtil {
    private Integer pageNo;
    private Integer pageSize;

    public PageUtil() {
    }

    public PageUtil(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public long getStart() {
        return (this.getPageNo() - 1) * this.getPageSize();
    }
}