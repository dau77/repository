package com.equator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//如果*Mapper.xml与Mapper接口不在同一个包下，则需要的主配置文件中配置mybatis.mapper-locations
@MapperScan("com.equator.dao")
//
@SpringBootApplication
public class EquatorBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquatorBaseApplication.class, args);
    }
}
