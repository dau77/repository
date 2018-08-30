package com.equator.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;

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
public class CostInfo extends CostInfoKey {

    private static final long serialVersionUID = 1L;

    public interface CostInfoAdd extends Default {}

    public interface CostInfoEdit extends Default {}


    /**
     * 成本版本号(暂时不用)
     */
    private String costInfoNo;


    /**
     * 计划表
     */
    private String planTable;


    /**
     * 计划ID
     */
    private Long planId;


    /**
     * 计划表
     */
    private String planNo;


    /**
     * 总应付金额折合人民币 暂时不用
     */
    private BigDecimal totalPay;


    /**
     * 总已付金额折合人民币 暂时不用
     */
    private BigDecimal totalPaid;


    /**
     * 订购人数 暂时不用
     */
    private String bookedNum;


    /**
     * 成本版本说明
     */
    private String versionRemark;


    /**
     * 成本版本名称，一般为成本预算和最终成本
     */
    private String versionName;


    /**
     * 添加人
     */
    private String whoAdd;


    /**
     * 添加时间
     */
    private Date addTime;


    /**
     * 修改人
     */
    private String editor;


    /**
     * 修改时间
     */
    private Date modifyTime;


    /**
     * 审核时间 (暂时不用)
     */
    private Date checkTime;


    /**
     * 是否最后一个版本  0 否   1 是
     */
    private Integer isLastVersion;


    /**
     * 成本版本状态   0 修改中   1 已提交  2 已过期
     */
    private Integer costInfoStatus;


    /**
     * 会计日期（凭证日期）
     */
    private Date accountingDate;


    /**
     * 计划名称 
     */
    private String planName;


    /**
     * 计划简称
     */
    private String planNameForShort;


    /**
     * 出团日期
     */
    private Date planTime_1;


    /**
     * 回团日期
     */
    private Date planTime_2;


    /**
     * 计划所属操作中心
     */
    private String opLoc;


    /**
     * 计划是否已经终结
     */
    private Integer finallyFlag;


    /**
     * 计调
     */
    private String op;


}
