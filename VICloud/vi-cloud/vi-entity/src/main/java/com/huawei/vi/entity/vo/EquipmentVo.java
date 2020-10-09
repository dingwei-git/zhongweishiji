package com.huawei.vi.entity.vo;

import lombok.Data;

@Data
public class EquipmentVo {

    private String filed0;// 层级一name
    private String filed1;// 层级二name
    private String filed2;// 层级三name
    private String id0;// 层级一id
    private String id1;// 层级二id
    private String id2;// 层级三id
    private String sheLveName;// 厂家

    private String ipType;// 类型
    private String model;// 型号
    private String zeroToThree;// 0到3年数量
    private String threeToFive;// 3到5年数量
    private String fiveToTen;// 5年以上数量
    private String unknown;// 未绑定档案数量

}
