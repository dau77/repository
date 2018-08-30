package com.equator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//如果*Mapper.xml与Mapper接口不在同一个包下，则需要的主配置文件中配置mybatis.mapper-locations
@MapperScan("com.equator.dao")
//
@SpringBootApplication
public class EquatorFieldApplication
        //extends SpringBootServletInitializer
{

    public static void main(String[] args) {
        SpringApplication.run(EquatorFieldApplication.class, args);
    }

    /*把项目打包成war包步骤
    0、需要将访问路径配置成使用项目名称访问 application.properties server.servlet.context-path=/equator-field
    1、在pom配置文件中修改  <packaging>jar</packaging>    -> <packaging>war</packaging>
    2、在pom配置文件加上 servlet依赖，和打包时忽略内嵌tomcat，
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        &lt;!&ndash;忽略内嵌tomcat，打包部署到tomcat。注*本地运行的时候要把这一段忽略引入个注释掉，要不然项目启动不了&ndash;&gt;
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>
    3、 修改启动程序入口。打开下面configure方法的注释*/

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(EquatorFieldApplication.class);
    }*/
}
