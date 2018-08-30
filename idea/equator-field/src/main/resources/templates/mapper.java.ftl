package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};

/**
 * <p>
 * ${table.comment!table.name} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 * @version ${cfg.gen_version}_1.0
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
