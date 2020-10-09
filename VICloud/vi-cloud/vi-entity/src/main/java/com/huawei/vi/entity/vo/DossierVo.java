package com.huawei.vi.entity.vo;

import lombok.Data;

@Data
public class DossierVo {

    /*
    * id
    * */
    private String id;

    /*
    * name
    * */
    private String name;

    /*
    * 层级
    * */
    private String leves;

    /*
    * 层级id
    * */
    private String[]organization;

    /*
    * 当前页
    * */
    private Integer currentPage;

    /*
    * 每页条数
    * */
    private Integer pageSize;





}
