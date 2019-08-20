package com.rose.common.util;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {

    public static String getValueByHeaderOrParam(HttpServletRequest request, String paramKey) {
        if (StringUtil.isEmpty(paramKey)) {
            return "";
        }
        String value = request.getHeader(paramKey);
        if (StringUtil.isEmpty(value)) {
            value = request.getParameter(paramKey);
        }
        return value;
    }
}