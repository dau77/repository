package com.equator.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

public class SqlInjectorUtils {

    /**
     * 判断表中是否存在source字段
     * @param tableInfo
     * @return
     */
    public static boolean isExistedSourceField(TableInfo tableInfo) {
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        for(TableFieldInfo fieldInfo : fieldList) {
            if("source".equals(fieldInfo.getColumn())) {
                return true;
            }
        }
        return false;
    }
}
