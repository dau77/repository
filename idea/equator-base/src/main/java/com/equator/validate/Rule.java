package com.equator.validate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rule {

    private String type = ValidateType.STRING.getValue();

    private Boolean required = false;

    private Integer len;

    private Integer max;

    private Integer min;

    private String message;

    private String pattern;

    private String validator;

    private String trigger;

}
