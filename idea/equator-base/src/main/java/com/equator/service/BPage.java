package com.equator.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页基类
 */
@Getter
@Setter
@ToString
public class BPage {
    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;
    /**
     * 当前页
     */
    private long current = 1;
}
