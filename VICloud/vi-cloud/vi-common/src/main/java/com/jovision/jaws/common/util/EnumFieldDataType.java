/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

/**
 * EnumFieldDataType
 *
 * @since 2018-07-02
 */
public enum EnumFieldDataType {
    /**
     * 1字节的无符号整数
     */
    CSRAS_FT_UINT8((byte) 1),
    /**
     * 2字节的无符号整数
     */
    CSRAS_FT_UINT16((byte) 2),
    /**
     * 4字节的无符号整数
     */
    CSRAS_FT_UINT32((byte) 3),
    /**
     * 8字节的无符号整数
     */
    CSRAS_FT_UINT64((byte) 4),
    /**
     * 8字节的浮点数
     */
    CSRAS_FT_DOUBLE((byte) 5),
    /**
     * 日期和时间类型，显示为：2000-01-01 20:00:00
     */
    CSRAS_FT_DATETIME((byte) 6),
    /**
     * 文本，UTF8字符串，值表示为：长度（2B）+值（长度） 长度包括’\0’结识符
     */
    CSRAS_FT_TEXT((byte) 7),
    /**
     * 百分值，长度1字节，无小数，值范围为：0 - 100
     */
    CSRAS_FT_PERCENT((byte) 8),
    /**
     * MAC地址，6个字节，以网络字节序方式提供 字符串格式：00:00:00:00:00:00
     */
    CSRAS_FT_MAC((byte) 9),
    /**
     * 由1字节的IP版本和4字节的IPV4地址或者16字节的IPV6地址组成，即IPV4: 4 + value[4]，IPV6: 6 + value[16]
     */
    CSRAS_FT_IPADDR((byte) 10),
    /**
     * 交易内容， 值表示为：长度（4B）+值（长度）
     */
    CSRAS_FT_TRANSCONTENT((byte) 11);
    /**
     * 比特值
     */
    private byte value;

    /**
     * EnumFieldDataType
     *
     * @param value value
     */
    EnumFieldDataType(byte value) {
        this.value = value;
    }

    /**
     * get枚举类型
     *
     * @param value value
     * @return EnumFieldDataType
     */
    public static EnumFieldDataType get(byte value) {
        for (EnumFieldDataType enumFieldType : values()) {
            if (enumFieldType.value == value) {
                return enumFieldType;
            }
        }
        return null;
    }

    /**
     * toString
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        sb.append(super.toString()).append("(").append(value).append(")");
        return sb.toString();
    }
}
