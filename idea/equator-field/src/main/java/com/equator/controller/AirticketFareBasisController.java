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

import com.equator.model.AirticketFareBasis;
import com.equator.service.AirticketFareBasisService;


import org.springframework.web.bind.annotation.RestController;
import com.equator.controller.BController;

/**
 * <p>
 * 机票基础票价表 前端控制器
 * </p>
 *
 * @author dau
 * @since 2018-08-28
 * @version 3.0-RC1_1.0_1.0
 */
@RestController
@RequestMapping("//airticket_fare_basis")
public class AirticketFareBasisController extends BController {

    @Autowired
    private AirticketFareBasisService airticketFareBasisService;

    /**
     * 查询
     * @param pageParam
     * @param airticketFareBasis
     * @return
     */
    @GetMapping
    public Result page(BPCPage pageParam, AirticketFareBasis airticketFareBasis) {
        Page<AirticketFareBasis> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        Wrapper<AirticketFareBasis> wrapper = new QueryWrapper<>();

        IPage<AirticketFareBasis> fieldIPage = airticketFareBasisService.page(page, wrapper);
        Result result = new Result("\"airticket_fare_basis.page.success\"");
        result.put("page", fieldIPage);
        return result;
    }

    /**
     * 添加
     * @param airticketFareBasis
     * @return
     */
    @PostMapping
    public Result save(@RequestBody @Validated(AirticketFareBasis.AirticketFareBasisAdd.class) AirticketFareBasis airticketFareBasis) {
        boolean rv = airticketFareBasisService.save(airticketFareBasis);
        AssertUtils.isTrue(rv, "airticket_fare_basis.save.failure");
        Result model = new Result("airticket_fare_basis.save.success");
        model.put("data", airticketFareBasis);
        return model;
    }

    /**
     * 修改
     * @param airticketFareBasis
     * @return
     */
    @PutMapping
    public Result update(@RequestBody @Validated(AirticketFareBasis.AirticketFareBasisEdit.class) AirticketFareBasis airticketFareBasis) {
        boolean rv = airticketFareBasisService.updateById(airticketFareBasis);
        AssertUtils.isTrue(rv, "airticket_fare_basis.update.failure");
        Result model = new Result("airticket_fare_basis.update.success");
        model.put("data", airticketFareBasis);
        return model;
    }

    /**
     * 删除
     * @param airticketFareBasisId
     * @return
     */
    @DeleteMapping("/{airticketFareBasisId}")
    public Result remove(@PathVariable Long airticketFareBasisId) {
        boolean rv = airticketFareBasisService.removeById(airticketFareBasisId);
        AssertUtils.isTrue(rv, "airticket_fare_basis.remove.failure");
        return new Result("airticket_fare_basis.remove.success");
    }

    /**
     * 信息
     * @param airticketFareBasisId
     * @return
     */
    @GetMapping("/{airticketFareBasisId}")
    public Result info(@PathVariable Long airticketFareBasisId) {
        AirticketFareBasis airticketFareBasis = airticketFareBasisService.getById(airticketFareBasisId);
        Result model = new Result("airticket_fare_basis.info.success");
        model.put("data", airticketFareBasis);
        return model;
    }
}

