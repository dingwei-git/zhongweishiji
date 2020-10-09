package com.huawei.vi.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ImageCountVO implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /*
    * 时间
    * */
    private String time;

    /*
    * 开始时间
    * */
    private String startTime;

    /*
    * 结束时间
    * */
    private String endTime;

    /*
    * 时间维度
    * */
    private String timeType;

    /*
    * 查询维度
    * */
    private String selectType;

    /*
    * 网元维度
    * */
    private String[]netWorkList;

    /*
    * 指标场景
    * */
    private String[]sceneVoList;

    /*
    * 组织设备
    * */
    private String[] organization;

    /*
    * 图片数量
    * */
    private Integer pictureCount;

    /*
    * 层级
    * */
    private List<String> organ;

    /*
    * 来源
    * */
    private String source;

    /*
     * 每页条数
     * */
    private Integer currentPage;

    /*
     * 当前页
     * */
    private Integer pageSize;


}
