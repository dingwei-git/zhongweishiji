/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import com.huawei.FortifyUtiltools.FortifyUtils;
import com.huawei.utils.CheckUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * 工具类
 *
 * @version 1.0, 2018年7月30日
 * @since 2019-09-18
 */
public final class PermissionUtil {
    /**
     * 公共日志类
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionUtil.class);

    /**
     * 构造函数
     */
    private PermissionUtil() {
    }

    /**
     * 对Linux系统系下的文件路径赋值权限
     *
     * @param path 路径
     */
    public static void permission4Linux(String path) {
        if (CheckUtils.isNullOrBlank(path)) {
            LOGGER.error("path is null or blank");
        }
        File pathFile = FileUtils.getFile(path.trim());
        if (pathFile.exists()) {
            String systemName = FortifyUtils.getSysProperty("os.name").toLowerCase(Locale.ENGLISH);

            // 对Linux系统上传的文件 赋予权限
            if (systemName.startsWith("linux")) {
                String command = "chmod 640 -R " + path;
                try {
                    FortifyUtils.runtimeExec(command);
                } catch (IOException e) {
                    LOGGER.error("Changed name to fail up {}", e.getMessage());
                }
            }
        } else {
            LOGGER.error("path is not exists");
        }
    }
}
