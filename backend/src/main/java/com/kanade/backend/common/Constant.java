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
    // 连续签到天数 KEY
     static String getUserContinuousSignKey(long userId) {
        return String.format("sign:continuous:%d", userId);
    }
    // 最后签到日期 KEY
     static String getUserLastSignDateKey(long userId) {
        return String.format("sign:lastdate:%d", userId);
    }
     static String getUserQuestionHashKey(long userId) {
        return String.format("question:count:user:%d", userId);
    }
}
