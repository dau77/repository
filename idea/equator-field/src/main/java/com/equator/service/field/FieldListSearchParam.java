package com.equator.service.field;

import com.equator.service.BPCPage;
import lombok.Data;

@Data
public class FieldListSearchParam extends BPCPage {
    /**
     * 页面英文名
     */
    private String pageEnname;

    /**
     * 语言
     */
    private String locale;
}
