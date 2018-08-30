package com.equator.exception;

import com.equator.context.MsgItem;
import com.equator.web.Result;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class BindResultException extends RuntimeException {

    private Result result;

    private final BindingResult bindingResult;

    public BindResultException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
        //
        this.result = new Result(1002); //????????
        List<ObjectError> objectErrorList = bindingResult.getAllErrors();
        for(ObjectError objectError : objectErrorList) {
            String field = objectError.getObjectName();
            String code = objectError.getCode();
            String message = objectError.getDefaultMessage();
            MsgItem msgItem = new MsgItem(field, code, message);
            this.result.addMsg(msgItem);
        }

    }
}