package com.equator.validate;

public class MRangeValidator implements Validator<Integer> {
    private Integer min;

    private Integer max;

    public MRangeValidator(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validate(Integer value) {
        // 最小值
        if (min != null && value < min) {
            return false;
        }
        // 最大值
        if (max != null && value > max) {
            return false;
        }
        return true;
    }

}
