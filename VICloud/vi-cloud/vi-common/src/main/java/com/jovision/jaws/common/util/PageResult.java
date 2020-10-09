package com.jovision.jaws.common.util;

import lombok.Data;


@Data
public class PageResult {

    private int total;
    private int currPage;
    private int pageSize;
    private Object data = null;
    public static final String TOTAL="total";
    public static final String CURRPAGE="currPage";
    public static final String PAGESIZE="pageSize";


    private PageResult() {
    }

    public static PageResult success(int total,int currPage,int pageSize,Object data) {
        PageResult result = new PageResult();
        result.setTotal(total);
        result.setCurrPage(currPage);
        result.setPageSize(pageSize);
        result.setData(data);
        return result;
    }

}
