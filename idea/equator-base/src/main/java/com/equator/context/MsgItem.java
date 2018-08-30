package com.equator.context;

import com.equator.context.MessageManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 用于获取并保存消息的code, 参数, 消息内容
 */
@Getter
@ToString
@EqualsAndHashCode
public class MsgItem {

    private String field;

    private String code;

    private String msg;

    public MsgItem(String code, String... arg) {
        this.field = "";
        this.code = code;
        this.msg = MessageManager.getMsg(code, arg);
    }

    public MsgItem(String field, String code, String message) {
        this.field = field;
        this.code = code;
        this.msg = message;
    }
}
