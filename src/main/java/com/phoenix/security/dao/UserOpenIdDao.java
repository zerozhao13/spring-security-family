package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOpenIdDao {
    @TableId(type = IdType.ASSIGN_UUID)
    String openId;
    Long uid;
    Long cid;
}
