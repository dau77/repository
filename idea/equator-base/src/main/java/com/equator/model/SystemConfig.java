package com.equator.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* Created by Mybatis Generator (LombokPlugin) 2018-07-13 16:52:27
*/
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemConfig extends BModel {
    /**
    * This method returns the value of the database column system_config.system_config_id
    */
    @TableId
    private Integer systemConfigId;

    /**
    * This method returns the value of the database column system_config.company_loc
    */
    private String companyLoc;

    /**
    * This method returns the value of the database column system_config.category
    */
    private String category;

    /**
    * This method returns the value of the database column system_config.key_name
    */
    private String keyName;

    /**
    * This method returns the value of the database column system_config.key_value
    */
    private String keyValue;

    /**
    * This method returns the value of the database column system_config.remark
    */
    private String remark;

    /**
    * This method returns the value of the database column system_config.order_by
    */
    private Short orderBy;

    /**
    * This method returns the value of the database column system_config.key_value2
    */
    private String keyValue2;

    /**
    * This method returns the value of the database column system_config.param_type
    */
    private String paramType;

    /**
    * This method returns the value of the database column system_config.config_type
    */
    private Short configType;

    /**
    * This method returns the value of the database column system_config.start_time
    */
    private Date startTime;

    /**
    * This method returns the value of the database column system_config.end_time
    */
    private Date endTime;
}