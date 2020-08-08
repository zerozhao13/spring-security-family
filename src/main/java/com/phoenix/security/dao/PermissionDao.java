package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("permission")
public class PermissionDao extends BaseDao {
    @TableId(type = IdType.ASSIGN_ID)
    private Long pid;
    private String permission;
    private String resource;
    private String des;
}
