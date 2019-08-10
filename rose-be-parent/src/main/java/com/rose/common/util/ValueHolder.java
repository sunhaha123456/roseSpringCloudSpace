package com.rose.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

@Slf4j
@Singleton
@Component
public class ValueHolder {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public void setUserIdHolder(Long userId) {
        this.userIdHolder.set(userId);
    }

    public Long getUserIdHolder() {
        return this.userIdHolder.get();
    }

    public void setTokenHolder(String token) {
        if (StringUtil.isEmpty(token)) {
            this.tokenHolder.set(null);
        } else {
            this.tokenHolder.set(token);
        }
    }

    public String getTokenHolder() {
        return tokenHolder.get();
    }

    public void removeAll() {
        this.userIdHolder.remove();
        this.tokenHolder.remove();
    }
}