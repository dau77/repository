package com.equator.controller;

import com.equator.model.GeneratorParam;
import com.equator.service.GeneratorService;
import com.equator.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO cost_info有source但不是联合主键，如何处理？联合主键加一个构造方法？
 * TODO 如果字段是plan_time_2，产生的是planTime2，默认会对应plan_time2，需要用@TableField指定
 * TODO 添加时ID会返回，为什么？
 * 可以选择产生add/list ?
 *
 * 联合主键在产生控制类时，参数处理可能有所不同？
 *
 * 菜单入口用SQL控制 ？
 *
 * SQL:
 * 1. 消息，主要控制器上用的
 * 2.
 *
 * 移动端和手机端的模板还不一样?
 * 移动端（对外）用VO，自己后端用实体？
 *
 *  public R page(JQPageInfo jqPageInfo,${className}Model ${classname}){  ?????? TODO 测试一下？
 *
 *
 *  需要构造的数据：一次只处理一张数据表
 *  {
 *      表名 -> 表数据
 *      fields -> {
 *          页面名 -> [ Field ], ...
 *      }
 *  }
 *
 *
 *
 * 入参：
 * 模块名（）
 *      camelToHyphen ？   /模块名/cost-info
 * 是否覆盖：默认否
 * 作者
 * 后端输出路径
 * 前端输出路径
 *      SQL输出路径和前端一起？？
 *
 *  配置：
 *  数据源，从配置文件取，或者MP GEN改用MP而非JDBC？
 *
 *
 *
 *
 *
 * pageName：默认可改, 可能有多个？add,list
 *              取field信息时，只取指定表的指定pageName信息
 * 前端输出目录：
 * API版本：有默认值
 * 请求地址：默认表名
 *          /user   GET
 *
 * 后端输出目录
 *
 * 消息提交的完善（文件已存在，产生了哪些等）
 */
@RestController
@RequestMapping("/generator")
public class GeneratorController {

    @Autowired
    GeneratorService generatorService;

    /**
     * {
     *         "author": "lwd",
     *         "front_end_output_dir": "G:\\IdeaProjects\\test2",
     *         "back_end_output_dir": "G:\\IdeaProjects\\equator-field\\src\\main\\java",
     *         "file_override": true,
     *         "parent_package_name": "com.equator",
     * 		"table_name": "cost_info",
     * 		"page_name_map": {"component":"costInfo","fieldlist":""}
     *
     *         "page_name_map": {"component":"costInfo","fieldlist":""}
     *         "page_file_map": {"component":"costInfo","fieldlist":""}
     *
     *
     *         "page_name_map": {"component":["costInfo","fieldlist"], }
     * }
     * @param generatorParam
     * @return
     */
    @PostMapping("/all")
    public Result all(@RequestBody GeneratorParam generatorParam) {
        generatorService.all(generatorParam);
        return new Result("generator.ok");
    }

    @PostMapping("/frontend")
    public Result frontEnd() {
        return null;
    }

    @PostMapping("/backend")
    public Result backEnd() {
        return null;
    }
}
