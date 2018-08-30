package com.equator;

import com.equator.generator.FrontEndMPGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

public class test {

    @Test
    public void testFrontEndMPGenerator(){

        SpringApplication.run(EquatorFieldApplication.class);

        new FrontEndMPGenerator("fieldAdd")
                .setAuthor("lwd")
                //.setOutputDir("G:\\vue_project\\tmpsrc")
                .setOutputDir("C:\\Users\\lenovo\\Desktop\\Vue\\template")
                .execute();
        //System.out.println("测试");
    }
}
