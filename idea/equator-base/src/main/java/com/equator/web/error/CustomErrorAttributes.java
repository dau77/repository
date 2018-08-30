package com.equator.web.error;

import com.equator.config.AppServerProperties;
import com.equator.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义错误属性
 */
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Autowired
    private AppServerProperties appServerProperties;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes =  new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        addStatus(errorAttributes, webRequest);
        errorAttributes.put(AppServerProperties.NAME_ID, appServerProperties.getId());
        addResult(errorAttributes, webRequest);
        return errorAttributes;
    }

    private void addStatus(Map<String, Object> errorAttributes,
                           RequestAttributes requestAttributes) {
        Integer status = getAttribute(requestAttributes,
                "javax.servlet.error.status_code");
        if (status == null) {
            errorAttributes.put("status", 999);
            errorAttributes.put("error", "None");
            return;
        }
        errorAttributes.put("status", status);
        try {
            errorAttributes.put("error", HttpStatus.valueOf(status).getReasonPhrase());
        }
        catch (Exception ex) {
            // Unable to obtain a reason
            errorAttributes.put("error", "Http Status " + status);
        }
    }

    private void addResult(Map<String,Object> errorAttributes, WebRequest webRequest) {
        Result result = (Result) webRequest.getAttribute("resultModel", RequestAttributes.SCOPE_REQUEST);
        if(result != null) {
            for(Map.Entry<String, Object> entry : result.entrySet()) {
                errorAttributes.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }
}
