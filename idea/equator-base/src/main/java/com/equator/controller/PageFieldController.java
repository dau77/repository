package com.equator.controller;

import com.equator.service.field.PageFieldService;
import com.equator.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用于返回前端指定页面所需字段相关信息：字段名、验证规则等
 */
@RestController
@RequestMapping("/page_field")
public class PageFieldController extends BController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PageFieldService pageFieldService;

    @GetMapping("/{pageEnname}")
    public Result selectByPageEnname(@PathVariable String pageEnname) {
        logger.debug("pageEnname = " + pageEnname);
        Result model = new Result("page_field.selectByPageEnname.success");
        model.put("field_map", pageFieldService.getMapByPageEnname(pageEnname));
        return model;
    }
}
