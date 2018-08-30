package com.equator.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.equator.model.BModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;

/**
 * <p>
 * 机票基础票价表
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
public class AirticketFareBasis extends BModel {

    private static final long serialVersionUID = 1L;

    public interface AirticketFareBasisAdd extends Default {}

    public interface AirticketFareBasisEdit extends Default {}


    /**
     * 机票基础票价ID
     */
    @TableId
    private Integer airticketFareBasisId;


    /**
     * 起飞机场三字码
     */
    private String fromAirport;


    /**
     * 目的机场三字码
     */
    private String destAirport;


    /**
     * 里程
     */
    private Integer mileage;


    /**
     * Y舱票价
     */
    private Integer fareY;


    /**
     * 修改时间
     */
    private Date modifiedTime;


}
