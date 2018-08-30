package com.equator.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.equator.util.AssertUtils;
import com.equator.web.Result;
import com.equator.service.BPCPage;

import com.equator.model.CostInfo;
import com.equator.model.CostInfoKey;
import com.equator.service.CostInfoService;


import org.springframework.web.bind.annotation.RestController;
import com.equator.controller.BController;

/**
 * <p>
 * 成本版本表 前端控制器
 * </p>
 *
 * @author dau
 * @since 2018-08-28
 * @version 3.0-RC1_1.0_1.0
 */
@RestController
@RequestMapping("//cost_info")
public class CostInfoController extends BController {

    @Autowired
    private CostInfoService costInfoService;

    /**
     * 查询
     * @param pageParam
     * @param costInfo
     * @return
     */
    @GetMapping
    public Result page(BPCPage pageParam, CostInfo costInfo) {
        Page<CostInfo> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        Wrapper<CostInfo> wrapper = new QueryWrapper<>();

        IPage<CostInfo> fieldIPage = costInfoService.page(page, wrapper);
        Result result = new Result("\"cost_info.page.success\"");
        result.put("page", fieldIPage);
        return result;
    }

    /**
     * 添加
     * @param costInfo
     * @return
     */
    @PostMapping
    public Result save(@RequestBody @Validated(CostInfo.CostInfoAdd.class) CostInfo costInfo) {
        boolean rv = costInfoService.save(costInfo);
        AssertUtils.isTrue(rv, "cost_info.save.failure");
        Result model = new Result("cost_info.save.success");
        model.put("data", costInfo);
        return model;
    }

    /**
     * 修改
     * @param costInfo
     * @return
     */
    @PutMapping
    public Result update(@RequestBody @Validated(CostInfo.CostInfoEdit.class) CostInfo costInfo) {
        boolean rv = costInfoService.updateById(costInfo);
        AssertUtils.isTrue(rv, "cost_info.update.failure");
        Result model = new Result("cost_info.update.success");
        model.put("data", costInfo);
        return model;
    }

    /**
     * 删除
     * @param costInfoId
     * @return
     */
    @DeleteMapping("/{costInfoId}")
    public Result remove(@PathVariable Long costInfoId) {
        boolean rv = costInfoService.removeById(new CostInfoKey(costInfoId, "szgl"));
        AssertUtils.isTrue(rv, "cost_info.remove.failure");
        return new Result("cost_info.remove.success");
    }

    /**
     * 信息
     * @param costInfoId
     * @return
     */
    @GetMapping("/{costInfoId}")
    public Result info(@PathVariable Long costInfoId) {
        CostInfo costInfo = costInfoService.getById(new CostInfoKey(costInfoId, "szgl"));
        Result model = new Result("cost_info.info.success");
        model.put("data", costInfo);
        return model;
    }
}

