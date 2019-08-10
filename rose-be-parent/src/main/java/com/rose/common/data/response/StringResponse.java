package com.rose.common.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 功能：当返回String 时，需用此类统一封装
 * @author sunpeng
 * @date 2018
 */
@Data
@AllArgsConstructor
@JsonSerialize
public class StringResponse implements Serializable {

    // 响应数据
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String response;
}