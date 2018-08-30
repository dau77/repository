package com.equator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Locale;

/**
* Created by Mybatis Generator (LombokPlugin) 2018-07-13 16:52:27
*/
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message extends BModel {

    /**
    * 系统消息ID
    */
    @TableId(type = IdType.INPUT)
    private String msgId;

    /**
    * 系统消息内容
    */
    private String message;

    /**
     * 正则表达式
     */
    private String regexp;

    /**
    * 语言
    */
    @TableId(type = IdType.INPUT)
    private String locale = Locale.CHINA.toString();
}