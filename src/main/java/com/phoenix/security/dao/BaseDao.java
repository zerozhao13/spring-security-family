package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.IGNORED;

@Getter
@Setter
public class BaseDao {
    @TableField(updateStrategy = IGNORED)
    Instant createTime;
    Instant updateTime = Instant.now();
    @TableField(update = "%s+1")
    Integer version;
}
