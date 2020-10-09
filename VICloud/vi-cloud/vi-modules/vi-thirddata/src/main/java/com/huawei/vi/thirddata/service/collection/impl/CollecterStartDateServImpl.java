/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.collection.impl;

import com.huawei.vi.thirddata.service.collection.CollecterStartDateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * 正常情况下的启动时间：随机时间
 *
 * @since 2019-08-06
 */
@Service("collecterStartDateServ")
public class CollecterStartDateServImpl implements CollecterStartDateService {
    private static final Logger LOG = LoggerFactory.getLogger(CollecterStartDateServImpl.class);

    @Override
    public Date getCollecterStartDate(Date date, int modelNum) {
        if (modelNum == 0) {
            return date;
        }
        if (!judgeModelNum(modelNum)) {
            LOG.error("Illegal parameter modelNum {}", modelNum);
            return date;
        }
        String formatDate = new SimpleDateFormat(DATE_FORMAT).format(date);
        Matcher matcher = PATTERN.matcher(formatDate);
        if (matcher.find()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.SECOND, 00);
            int minute = Integer.parseInt(matcher.group(1));
            if (minute % modelNum != 0) {
                calendar.add(Calendar.MINUTE, -(minute % modelNum));
            }
            return calendar.getTime();
        }
        LOG.error("date formate not end with {}.00 . date: {}", modelNum, date);
        return date;
    }

    /**
     * 对求模的约束 只能支持 10/20/30/60
     *
     * @param modelNum 模数
     * @return true/false 判断结果
     */
    private boolean judgeModelNum(int modelNum) {
        return modelNum == TEN || modelNum == TWENTY || modelNum == THIRTH || modelNum == SIXTH;
    }
}
