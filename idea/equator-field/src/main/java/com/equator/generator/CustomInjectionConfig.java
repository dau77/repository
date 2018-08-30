package com.equator.generator;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.equator.model.GeneratorParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomInjectionConfig extends InjectionConfig {

    private GeneratorParam generatorParam;

    private String generatorVersion;

//    private String outputParentDir;

    public CustomInjectionConfig() {
        this.setMap(new HashMap<String, Object>());
    }

    /**
     * 在产生代码前由AbstractTemplateEngine.batchOutput()方法调用
     */
    @Override
    public void initMap() {
        Map<String, Object> map = this.getMap();//new HashMap<String, Object>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
        map.put("gen_version", getGeneratorVersion());
        map.put("moduleName", generatorParam.getModuleName());

        if(generatorParam != null && getConfig() != null && getConfig().getPathInfo() != null
                && StringUtils.isNotEmpty(generatorParam.getFrontEndOutputDir())) {
            setPathInfo(GenConsts.FRONT_END_PATH, ""); //前端项目根目录
            setNoEmptyPathInfo(GenConsts.MENU_PATH, generatorParam.getMenuFile()); //菜单文件路径
            setNoEmptyPathInfo(GenConsts.ROUTER_PATH, generatorParam.getRouterFile()); //路由文件路径
        }
    }

    private void setNoEmptyPathInfo(String pathKey, String path) {
        if(StringUtils.isNotEmpty(path)) {
            Map<String, String> pathInfo = getConfig().getPathInfo();
            pathInfo.put(pathKey, joinPath(path)); //菜单文件路径
        }
    }

    private void setPathInfo(String pathKey, String path) {
        Map<String, String> pathInfo = getConfig().getPathInfo();
        pathInfo.put(pathKey, joinPath(path)); //菜单文件路径
    }

    private String joinPath(String path) {
        String output = generatorParam.getFrontEndOutputDir();
        return output + path;
    }
}
