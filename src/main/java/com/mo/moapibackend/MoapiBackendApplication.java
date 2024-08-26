package com.mo.moapibackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mo.moapibackend.mapper")
public class MoapiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoapiBackendApplication.class, args);
    }

}
