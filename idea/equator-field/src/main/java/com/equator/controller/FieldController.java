package com.equator.controller;

import com.equator.model.Field;
import com.equator.service.field.FieldListSearchParam;
import com.equator.service.field.FieldService;
import com.equator.util.AssertUtils;
import com.equator.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/field")
public class FieldController extends BController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FieldService fieldService;

    @GetMapping
    public Result selectList(FieldListSearchParam param) {
        Result model = new Result("field.selectList.success");
        model.put("field_list", fieldService.page(param));
        return model;
    }

    @PostMapping
    //@Validated({Field.FieldAdd.class})
    public Result save(@Validated({Field.FieldAdd.class}) @RequestBody Field field) {
        boolean rv = fieldService.save(field);
        AssertUtils.isTrue(rv, "field.save.failure");
        Result model = new Result("field.save.success");
        model.put("field", field);
        return model;
    }

    @PutMapping
    public Result updateById(@Validated({Field.FieldAdd.class}) @RequestBody Field field) {
        boolean rv = fieldService.updateById(field);
        AssertUtils.isTrue(rv, "field.updateById.failure");
        Result model = new Result("field.updateById.success");
        model.put("field", field);
        return model;
    }

    @DeleteMapping("/{fieldId}")
    public Result removeById(@PathVariable Integer fieldId) {
        boolean rv = fieldService.removeById(fieldId);
        AssertUtils.isTrue(rv, "field.deleteById.failure");
        return new Result("field.deleteById.success");
    }

    @GetMapping("/{fieldId}")
    public Result getById(@PathVariable Integer fieldId, String locale) {
        logger.debug("fieldId = " + fieldId);
        Field field = fieldService.getById(fieldId, locale);
        Result model = new Result("field.selectById.success");
        model.put("field", field);
        return model;
    }

}
