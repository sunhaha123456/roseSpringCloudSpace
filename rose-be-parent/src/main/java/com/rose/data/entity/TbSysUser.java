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
@Table(name = "tb_sys_user")
public class TbSysUser {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_modified")
    private Date lastModified;

    /**
     * 角色组id
     */
    @Column(name = "role_group_id")
    private Integer roleGroupId;

    /**
     * 用户名
     */
    private String uname;

    /**
     * 密码
     */
    private String upwd;

    /**
     * 用户状态
     */
    @Column(name = "user_state")
    private Integer userState;
}