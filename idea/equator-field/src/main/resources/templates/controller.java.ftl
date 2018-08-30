package ${package.Controller};

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

import ${package.Entity}.${entity};
<#if (table.keyFields?size > 1)>
import ${package.Entity}.${entity}Key;
</#if>
<#--import com.equator.model.CostInfoKey;-->
import ${package.Service}.${table.serviceName};


<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!table.name} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 * @version ${cfg.gen_version}_1.0
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if cfg.moduleName?? && cfg.moduleName != "">/${cfg.moduleName}</#if>/${table.name}")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    /**
     * 查询
     * @param pageParam
     * @param ${entity?uncap_first}
     * @return
     */
    @GetMapping
    public Result page(BPCPage pageParam, ${entity} ${entity?uncap_first}) {
        Page<${entity}> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        Wrapper<${entity}> wrapper = new QueryWrapper<>();

        IPage<${entity}> fieldIPage = ${table.serviceName?uncap_first}.page(page, wrapper);
        return Result.success("${table.name}.page.success").put("page", fieldIPage);
    }

    /**
     * 添加
     * @param ${entity?uncap_first}
     * @return
     */
    @PostMapping
    public Result save(@RequestBody @Validated(${entity}.${entity}Add.class) ${entity} ${entity?uncap_first}) {
        boolean rv = ${table.serviceName?uncap_first}.save(${entity?uncap_first});
        AssertUtils.isTrue(rv, "${table.name}.save.failure");
        return Result.success("${table.name}.save.success").put("data", ${entity?uncap_first});
    }

    /**
     * 修改
     * @param ${entity?uncap_first}
     * @return
     */
    @PutMapping
    public Result update(@RequestBody @Validated(${entity}.${entity}Edit.class) ${entity} ${entity?uncap_first}) {
        boolean rv = ${table.serviceName?uncap_first}.updateById(${entity?uncap_first});
        AssertUtils.isTrue(rv, "${table.name}.update.failure");
        return Result.success("${table.name}.update.success").put("data", ${entity?uncap_first});
    }

    <#if (idKeyField.propertyName)??>
    /**
     * 删除
     * @param ${idKeyField.propertyName}
     * @return
     */
    @DeleteMapping("/{${idKeyField.propertyName}}")
    public Result remove(@PathVariable Long ${idKeyField.propertyName}) {
        <#if (table.keyFields?size > 1)>
        boolean rv = ${table.serviceName?uncap_first}.removeById(new ${entity}Key(${idKeyField.propertyName}, "szgl"));
        <#else>
        boolean rv = ${table.serviceName?uncap_first}.removeById(${idKeyField.propertyName});
        </#if>
        AssertUtils.isTrue(rv, "${table.name}.remove.failure");
        return Result.success("${table.name}.remove.success");
    }

    /**
     * 信息
     * @param ${idKeyField.propertyName}
     * @return
     */
    @GetMapping("/{${idKeyField.propertyName}}")
    public Result info(@PathVariable Long ${idKeyField.propertyName}) {
        <#if (table.keyFields?size > 1)>
        ${entity} ${entity?uncap_first} = ${table.serviceName?uncap_first}.getById(new ${entity}Key(${idKeyField.propertyName}, "szgl"));
        <#else>
        ${entity} ${entity?uncap_first} = ${table.serviceName?uncap_first}.getById(${idKeyField.propertyName});
        </#if>
        return Result.success("${table.name}.info.success").put("data", ${entity?uncap_first});
    }
    </#if>
}

</#if>
