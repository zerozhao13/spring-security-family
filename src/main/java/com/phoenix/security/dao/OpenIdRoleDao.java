package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@TableName("open_id_role")
public class OpenIdRoleDao extends BaseDao {
    String openId;
    Long rid;
}
