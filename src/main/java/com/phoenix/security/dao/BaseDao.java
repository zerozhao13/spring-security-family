package com.phoenix.security.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.phoenix.security.config.InstantConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.IGNORED;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseDao {
    @TableField(updateStrategy = IGNORED)
    @JsonSerialize(using = InstantConfig.InstantSerializer.class)
    @JsonDeserialize(using = InstantConfig.InstantDeserializer.class)
    Instant createTime = Instant.now();
    @JsonSerialize(using = InstantConfig.InstantSerializer.class)
    @JsonDeserialize(using = InstantConfig.InstantDeserializer.class)
    Instant updateTime = Instant.now();
    @TableField(update = "%s+1")
    Integer version;
}
