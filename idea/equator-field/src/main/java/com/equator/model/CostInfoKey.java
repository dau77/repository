package com.equator.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.equator.model.BModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 成本版本表
 * </p>
 *
 * @author dau
 * @since 2018-08-28
 * @version 3.0-RC1_1.0_1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class CostInfoKey extends BModel {

    private static final long serialVersionUID = 1L;

    public CostInfoKey() {}

    public CostInfoKey(Long costInfoId, String source) {
        this.costInfoId = costInfoId;
        this.source = source;
    }



    /**
     * 成本版本ID
     */
    @TableId
    private Long costInfoId;

    /**
     * 计划源
     */
    private String source;



}
