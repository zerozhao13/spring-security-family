package com.phoenix.security.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenIdRoleDao extends BaseDao {
    Integer openId;
    Integer rid;
}
