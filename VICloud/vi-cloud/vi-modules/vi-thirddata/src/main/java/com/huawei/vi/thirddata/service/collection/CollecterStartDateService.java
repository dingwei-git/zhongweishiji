/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.collection;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * 采集开始时间处理
 *
 * @since 2019-08-06
 */
public interface CollecterStartDateService {
    /**
     * yyyy-MM-dd HH:mm:00 获取分钟
     */
    String REGEX_DATE = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:(\\d{2}):\\d{2}$";

    /**
     * formate 整分
     */
    String DATE_FORMAT = "yyyy-MM-dd HH:mm:00";

    /**
     * 避免频繁创建pattern对象
     */
    Pattern PATTERN = Pattern.compile(REGEX_DATE);

    /**
     * 周期10min 00/10/20/30/40/50.00
     */
    int TEN = 10;

    /**
     * 周期20min 00/20/40.00
     */
    int TWENTY = 20;

    /**
     * 周期30min 00.00 30.00
     */
    int THIRTH = 30;

    /**
     * 周期60min 00.00
     */
    int SIXTH = 60;

    /**
     * 获取采集开始日期
     *
     * @param date 日期
     * @param modelNum 采集周期
     * @return date
     */
    Date getCollecterStartDate(Date date, int modelNum);
}
