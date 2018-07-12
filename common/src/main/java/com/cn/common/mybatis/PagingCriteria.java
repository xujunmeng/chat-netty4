package com.cn.common.mybatis;

import java.io.Serializable;

/**
 * @author james
 * @date 2018/7/12
 */
public class PagingCriteria implements Serializable {
    protected static final String SqlOrderASC = "ASC";
    protected static final String SqlOrderDESC = "DESC";
    private Integer pageIndex = Integer.valueOf(0);
    private Integer pageSize = Integer.valueOf(20);
    private long totalCount;
    private String orderBy;
    private String sortBy;
    private boolean paging = true;

    public PagingCriteria() {
    }

    public int getPageIndex() {
        return this.pageIndex.intValue();
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = Integer.valueOf(pageIndex);
    }

    public int getPageSize() {
        return this.pageSize.intValue();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = Integer.valueOf(pageSize);
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortBy() {
        return this.sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isPaging() {
        return this.paging;
    }

    public void paging(boolean paging) {
        this.paging = paging;
    }

}