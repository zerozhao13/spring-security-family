package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("user_open_id")
public class UserOpenIdDao extends BaseDao {
    @TableId(type = IdType.ASSIGN_UUID)
    String openId;
    Long uid;
    Long appId;
}
