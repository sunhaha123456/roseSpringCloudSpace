package com.rose.data.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDto implements Serializable{
    protected String token;
    protected String userId;
    protected String sessionId;

    public BaseDto() {
    }

    public BaseDto(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }
}