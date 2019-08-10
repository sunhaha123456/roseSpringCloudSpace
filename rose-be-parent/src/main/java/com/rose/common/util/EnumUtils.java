package com.rose.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnumUtils {

    public static String returnValueByKey(Integer key, Class<? extends EnumBaseType> clazz) {
        for (Enum enumObj : (Enum[]) clazz.getEnumConstants()) {
            EnumBaseType e = clazz.cast(enumObj);
            if (key.equals(e.getKey())) {
                return e.getValue();
            }
        }
        log.info("未找到key对应的值！key：{}，class：{}", key, clazz);
        return null;
    }
}
