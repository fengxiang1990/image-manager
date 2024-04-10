package com.fxa.image.common;

public class RedisConst {
    public static final String KEY_USER_PWD_MAP = "key_user_pwd_map";
    public static final String KEY_USER_LOGIN_MAP = "key_user_login_map";

    /**
     * 内容自增长ID
     */

    public static final String KEY_USER_CONTENT_COUNTER= "content_id_gen:uid:";
    /**
     * 用户内容ID列表
     */
    public static final String KEY_USER_CONTENT_LIST= "user_content:list:";
    /**
     * 内容列表 热度排序
     */
    public static final String KEY_CONTENT_Z_PREFIX_SCORE = "content:hotness:";

    /**
     * 内容列表 时间排序
     */
    public static final String KEY_CONTENT_Z_PREFIX_CREATE_TIME = "content:crttime:";

    /**
     * 内容列表 审核状态排序
     */
    public static final String KEY_CONTENT_Z_PREFIX_STATUS = "content:status:";


    /**
     * 内容列表
     */
    public static final String KEY_CONTENT_HASH = "content:hash:";

}
