package com.kanade.backend.common;

public interface Constant {
    String USER_LOGIN_STATE = "user_login";
    String EMAIL_VERIFY_CODE = "email:verify";

    // 签到记录
    String USER_SIGN_IN_REDIS_KEY_PREFIX = "user:signins";

    // 获取用户签到记录的 Redis Key
    static String getUserSignInRedisKey(int year, long userId) {
        return String.format("%s:%s:%s", USER_SIGN_IN_REDIS_KEY_PREFIX, year, userId);
    }
}
