package com.huawei.vi.entity.po;

import lombok.Data;

@Data
public class DossierStatistics {

    /*
    * 提交数
    * */
    private String subNUmber;

    /*
    * 审核数
    * */
    private String auditsNumber;

    /*
    * 设备总数
    * */
    private String totalNumber;

    /*
    * 建档率
    * */
    private String dossierPercent;

    /*
    * 审核通过率
    * */
    private String passPercent;

    /*
    * 层级
    * */
    private String organ;

    /*
    * 层级1
    * */
    private String organization1;

    /*
     * 层级2
     * */
    private String organization2;

    /*
     * 层级3
     * */
    private String organization3;

    /*
     * 层级4
     * */
    private String organization4;

    /*
    * 建档数
    * */
    private String dossier;

}
