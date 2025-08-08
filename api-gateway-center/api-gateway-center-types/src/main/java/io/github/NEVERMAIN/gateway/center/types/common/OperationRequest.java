package io.github.NEVERMAIN.gateway.center.types.common;

import org.apache.commons.lang3.StringUtils;

public class OperationRequest<T> {

    /**
     * 分页开始索引
     */
    private int pageStart = 0;
    /**
     * 分页结束索引
     */
    private int pageEnd = 0;
    /**
     * 当前页码
     */
    private int pageIndex;
    /**
     * 每页显示条数
     */
    private int pageSize;
    /**
     * 查询参数
     */
    private T data;

    public OperationRequest() {
    }

    public OperationRequest(String page, String rows){
        this.pageIndex = StringUtils.isEmpty(page) ? 1 : Integer.parseInt(page);
        this.pageSize = StringUtils.isEmpty(rows) ? 10 : Integer.parseInt(rows);
        if ( 0 == this.pageIndex){
            this.pageIndex = 1;
        }
        this.pageStart = (this.pageIndex - 1) * this.pageSize;
        this.pageEnd = this.pageStart + this.pageSize;
    }

    public OperationRequest(int page, int rows) {
        this.pageIndex = 0 == page ? 1 : page;
        this.pageSize = 0 == rows ? 10 : rows;
        this.pageStart = (this.pageIndex - 1) * this.pageSize;
        this.pageEnd = this.pageSize;
    }

    public void setPage(String page, String rows) {
        this.pageIndex = StringUtils.isEmpty(page) ? 1 : Integer.parseInt(page);
        this.pageSize = StringUtils.isEmpty(page) ? 10 : Integer.parseInt(rows);
        if (0 == this.pageIndex) {
            this.pageIndex = 1;
        }
        this.pageStart = (this.pageIndex - 1) * this.pageSize;
        this.pageEnd = this.pageSize;
    }


    public int getPageStart() {
        return pageStart;
    }

    public int getPageEnd() {
        return pageEnd;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        if (data instanceof String) {
            String str = (String) data;
            if (StringUtils.isEmpty(str)){
                data = null;
            }
        }
        this.data = data;
    }
}
