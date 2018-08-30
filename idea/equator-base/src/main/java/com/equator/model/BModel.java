package com.equator.model;

import com.equator.json.JSONFilter;
import com.equator.validate.MModelRule;

import java.io.Serializable;

/**
 * 实体基类
 */
@MModelRule
@JSONFilter
public abstract class BModel implements Serializable {
}
