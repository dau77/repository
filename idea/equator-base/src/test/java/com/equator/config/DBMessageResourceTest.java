package com.equator.config;

import com.equator.context.MessageManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DBMessageResourceTest {

    @Autowired(required = true)
    MessageSource messageSource;

    @Test
    public void msg() {



        System.out.println(MessageManager.getMsg("sb003", "a", "3", "4"));
//        System.out.println(MessageManager.getMsg("sb001", "a", "1", "2"));
        System.out.println(MessageManager.getMsg("page_field.selectByPageEnname.success"));
        System.out.println(MessageManager.getMsg("pkg-01-01"));
        System.out.println(messageSource.getMessage("sql-00-01-", null, Locale.SIMPLIFIED_CHINESE));


    }

}