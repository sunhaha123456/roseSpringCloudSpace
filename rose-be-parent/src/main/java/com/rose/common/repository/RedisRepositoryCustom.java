package com.rose.common.repository;

/**
 * 功能：redis interface
 * @author sunpeng
 * @date 2018
 */
public interface RedisRepositoryCustom {

    // 保存，永久
    void save(String key, String value);

    // 保存，单位分钟
    void saveMinutes(String key, String value, long time);

    // 保存，单位天数
    void saveDays(String key, String value, long time);

    // 设置超时时间，单位分钟
    void expireMinutes(String key, long time);

    // 设置超时时间，单位天数
    void expireDays(String key, long time);

    /**
     * 功能：获取对象
     * 备注：要获取的对象，是非list、String对象，以json形式存储到redis中的
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getClassObj(String key, Class<T> clazz);

    String getString(String key);

    void delete(String key);
}