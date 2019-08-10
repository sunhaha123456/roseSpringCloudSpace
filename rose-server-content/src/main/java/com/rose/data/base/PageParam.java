package com.rose.data.base;

import lombok.Data;

@Data
public class PageParam {
    private Integer page; //第几页
    private Integer rows; //每页多少条记录
}