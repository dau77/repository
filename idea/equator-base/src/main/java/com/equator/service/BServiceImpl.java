package com.equator.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.dao.BMapper;

/**
 * 服务实现基类
 * @param <M>
 * @param <T>
 */
public abstract class BServiceImpl<M extends BMapper<T>, T> extends ServiceImpl<M, T> implements BService<T> {
}
