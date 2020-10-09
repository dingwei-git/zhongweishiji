/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.vi.entity.vo;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * 字典bean
 *
 * @since 2020-04-15
 */
@XmlType(name = "dictionary")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Dictionary implements Serializable {
    /**
     * 常用字段pid集合pidList
     */
    public static final String PID_LIST = "pidList";

    /**
     * 常用字段name集合nameList
     */
    public static final String NAME_LIST = "nameList";

    /**
     * 常用字段is_show集合isShowList
     */
    public static final String SHOW_LIST = "showList";

    /**
     * 字典属性新增时，不能超过10条，需要进行判断
     */
    public static final int NUM_10 = 10;

    /**
     * 单个层级的id长度,即字典类别id长度
     */
    public static final int ONE_LEVEL_LENGTH = 3;

    /**
     * 摄像机类别id,默认类别id
     */
    public static final long DEFAULT_CAMERA_ID = 100L;

    /**
     * Pattern对象:/（）中英文、.java,用于校验字典属性、字典类型、字典项名称
     */
    public static final Pattern PATTERN_NUMBER_OR_LETTER_OR_CHINESE_OR_BRACKETS =
        Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9()/]+$");

    /**
     * Pattern对象:/ 英文、.java,用于校验字典属性、字典类型、字典项编码
     */
    public static final Pattern PATTERN_NUMBER_OR_LETTER_OR_SLASH = Pattern.compile("^[a-zA-Z0-9/]+$");

    private static final long serialVersionUID = -1665699640880139954L;

    @XmlAttribute(name = "id")
    private long id;

    @XmlAttribute(name = "pid")
    private long pid;

    /**
     * 字典表中对应的编码
     */
    @XmlAttribute(name = "code")
    private String code;

    @XmlAttribute(name = "name")
    private String name;

    /**
     * 是否选用
     */
    @XmlAttribute(name = "isShow")
    private boolean isShow;

    /**
     * 是否为默认字典属性
     */
    @XmlAttribute(name = "isDefault")
    private boolean isDefault;

    /**
     * pidname 主要用于字典属性编辑，所以也可以定义为字典类别名称
     */
    private String pidName;

    /**
     * 标记一机一档业务使用字段
     */
    @XmlAttribute(name = "tagged")
    private String tagged;
}
