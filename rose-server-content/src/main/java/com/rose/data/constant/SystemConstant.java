package com.rose.data.constant;

public interface SystemConstant {

    // redis 中用户信息key前缀
    String LOGIN_USER_INFO_PREFIX = "rose2_admin_user_id_";

    // redis 中用户信息保存时间
    long LOGIN_USER_INFO__SAVE_TIME = 8 * 60;

    // redis中用户登录验证码前缀
    String LOGIN_CODE_PREFIX = "rose2_admin_login_code_";

    // redis中用户登录验证码保存时间 5分钟
    long LOGIN_CODE_SAVE_TIME = 5;

    // 系统前后端交互中 token、userId key名
    String SYSTEM_TOKEN_NAME = "token";
    String SYSTEM_USER_ID = "userId";
}