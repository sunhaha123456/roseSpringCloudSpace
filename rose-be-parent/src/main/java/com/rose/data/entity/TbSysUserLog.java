package com.rose.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "tb_sys_user_log")
public class TbSysUserLog {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_modified")
    private Date lastModified;

    /**
     * ip地址
     */
    private String ip;

    /**
     * url地址
     */
    private String url;

    @Column(name = "user_id")
    private Long userId;

    /**
     * 参数
     */
    private String args;

    /**
     * 返回
     */
    private String ret;
}