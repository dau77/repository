package com.equator.web;

import com.equator.config.AppServerProperties;
import com.equator.context.MsgItem;
import com.equator.context.SpringContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用于保存controller的返回结果
 */
public class Result extends LinkedHashMap<String, Object> {

    public static final String NAME_RESULT_CODE = "rcode"; //放另一个类？Constant ?考虑一下是否需要，直接用消息code感觉是否太复杂太细？

    public static final String NAME_MESSAGES = "messages";

    public Result(int resultCode) {
        AppServerProperties properties = (AppServerProperties) SpringContext.getBean("appServerProperties");

        //默认。服务器ID
        this.put(AppServerProperties.NAME_ID, properties.getId());
        //
        this.put(NAME_RESULT_CODE, resultCode);
        this.put(NAME_MESSAGES, new ArrayList<MsgItem>());
    }

    public Result(int resultCode, String messageCode, String... messageArgs) {
        this(resultCode);
        //
        ArrayList<MsgItem> msgItems = this.getMsgItemList();
        msgItems.add(new MsgItem(messageCode, messageArgs));
    }

    public Result(String messageCode, String... messageArgs) {
        this(0, messageCode, messageArgs);
    }

//    /**
//     * 成功默认
//     * @param messageCode
//     */
//    public Result(String messageCode) {
//        this(0, messageCode);
//    }

    public void addMsg(MsgItem item) {
        getMsgItemList().add(item);
    }

    public void addMsgs(List<MsgItem> items) {
        getMsgItemList().addAll(items);
    }

    public ArrayList<MsgItem> getMsgItemList() {
        return (ArrayList<MsgItem>) this.get(NAME_MESSAGES);
    }




    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }


    public static Result success(String messageCode, String... messageArgs) {
        return new Result(messageCode, messageArgs);
    }

    public static Result error(String messageCode, String... messageArgs) {
        return new Result(100, messageCode, messageArgs);
    }

    public static Result error(int code, String messageCode, String... messageArgs) {
        return new Result(code, messageCode, messageArgs);
    }
}
