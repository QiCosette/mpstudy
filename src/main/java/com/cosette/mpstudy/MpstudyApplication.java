package com.cosette.mpstudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cosette.mpstudy.dao")
public class MpstudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpstudyApplication.class, args);
    }

}
