
package com.jovision.jaws.common.util;


import com.huawei.utils.CommonUtil;

/**
 * ParamToUrl
 *
 * @version 1.0, 2018年6月25日
 * @since 2018-06-25
 */
public final class ParamToUrl {
    private static final String HTTPS = "https://";

    private static final String LOGIN = "login";

    private static final String LOGOUT = "logout";

    /**
     * 构造函数
     */
    private ParamToUrl() {
    }

    /**
     * getUrl
     *
     * @param api api
     * @param ipAddr ipAddr
     * @return String [返回类型说明]
     */
    public static String getUrl(String api, String ipAddr) {
        if (CommonUtil.isExistsNull(api, ipAddr)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        switch (api) {
            case LOGIN:
            case LOGOUT:
                stringBuilder.append(HTTPS)
                    .append(ipAddr)
                    .append(ProtocolConfig.getParameterConfig().getSomeUrl())
                    .append(api);
                break;
            default:
                break;
        }
        return stringBuilder.toString();
    }
}
