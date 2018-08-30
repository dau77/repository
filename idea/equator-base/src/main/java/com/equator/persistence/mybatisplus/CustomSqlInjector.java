package com.equator.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于替换部分SQL注入的方法处理
 */
public class CustomSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList() {
        return Stream.of(
                new Insert(),
                new Delete(),
                new DeleteByMap(),
                new CustomDeleteById(), //DeleteById
                new CustomDeleteBatchByIds(), //DeleteBatchByIds
                new Update(),
                new CustomUpdateById(), //UpdateById
                new CustomSelectById(), //SelectById(),
                new CustomSelectBatchByIds(), //SelectBatchByIds
                new SelectByMap(),
                new SelectOne(),
                new SelectCount(),
                new SelectMaps(),
                new SelectMapsPage(),
                new SelectObjs(),
                new SelectList(),
                new SelectPage()
        ).collect(Collectors.toList());
    }
}
