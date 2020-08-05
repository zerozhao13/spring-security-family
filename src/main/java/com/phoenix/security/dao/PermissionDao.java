package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PermissionDao extends BaseDao {
    @TableId(type = IdType.ASSIGN_ID)
    private Long pid;
    private String permission;
    private String resource;
    private String des;
}
