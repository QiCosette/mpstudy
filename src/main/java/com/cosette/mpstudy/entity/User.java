package com.cosette.mpstudy.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by cosette.qi on 2019/5/29
 */

@Data
public class User {

    //主键
    private Long id;

    private String name;

    private Integer age;

    private String email;

    //直属上级
    private Long managerId;

    //创建时间
    private LocalDateTime createTime;
}
