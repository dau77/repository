package com.equator.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.equator.model.GeneratorParam;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 如果修改了代码生成器 或 模板，注意修改修改号
 * @version ersion
 */
public class MPGenerator {



    public static void main(String[] args) {

        //前后台是否需要分开？或可以分开？
        //前台代码输出路径？


        //初始化时，设置好配置对象。另有方法执行，这样可以自己改配置？乱改版本好像就没意义 了
//        new MPGenerator("com.mp.demo", "userinfo", "groupinfo")
        new MPGenerator("com.equator", "cost_info")
//                .setFileOverride(true)
                .setAuthor("lwd")
                .setOutputDir("G:\\IdeaProjects\\equator-field\\src\\main\\java")
//                .setExtraOutputDir("G:\\vue_project\\tmpsrc")
                .setDataSourceConfig("jdbc:postgresql://192.168.11.61:5432/fy_mtravel_gl_zs_180323", "mtravel", "mtravel")
                .execute();
        //TODO 考虑还是放在resource
        //TODO 传模块名 impl目录还是要，如果没有就少一层
        //TODO XML放JAVA还是放resources?
        //TODO page类移到model?   form 类？  复杂的实体（如有 userinfo, menu 等）
        //TODO 配置文件、配置类
        //TODO WAR的配置说明

        //@KeySequence时，ID的序列应该用Long还是Integer？如果是后者需要 @KeySequence(value="user_id", clazz=Integer.class)
    }

    private final static String CONFIG_VERSION = "1.0";

    protected CustomAutoGenerator autoGenerator;

    private String parentPackageName;

    private String superParentPackageName = "com.equator";

    private String[] tableNames;

    public MPGenerator(String parentPackageName, String... tableNames) {
        init(parentPackageName, tableNames);
    }

    protected void init(String parentPackageName, String... tableNames) {
        this.parentPackageName = parentPackageName;
        this.tableNames = tableNames;

        this.autoGenerator = new CustomAutoGenerator();
        //
        this.autoGenerator
                // 全局配置
                .setGlobalConfig(this.globalConfig())
                // 数据源配置
                .setDataSource(this.dataSourceConfig())
                // 策略配置
                .setStrategy(this.strategyConfig(tableNames))
                // 包配置
                .setPackageInfo(this.packageConfig(parentPackageName))
                //选择 freemarker 引擎，默认 Veloctiy
                .setTemplateEngine(new CustomFreemarkerTemplateEngine())
                //注入配置
                .setCfg(injectionConfig())
        ;

        System.out.println(version());
    }

    protected InjectionConfig injectionConfig() {
//        return new InjectionConfig() {
//            @Override
//            public void initMap() {
//                Map<String, Object> map = new HashMap<String, Object>();
////                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                map.put("gen_version", version());
//                this.setMap(map);
//            }
//        };
        CustomInjectionConfig config = new CustomInjectionConfig();
        config.setGeneratorVersion(version());
        return config;
    }

    private GlobalConfig globalConfig() {
        GlobalConfig config = new GlobalConfig();
        config
                .setOutputDir("d:\\codeGen") //InjectionConfig生成文件的输出目录
                .setFileOverride(false) //是否覆盖已有文件
                .setOpen(true) //是否打开输出目录
                .setEnableCache(false) //是否在xml中添加二级缓存配置
                .setActiveRecord(false) //不需要ActiveRecord特性
                .setAuthor("goldmaple") //开发人员
//                .setKotlin(true) //开启 Kotlin 模式
                .setActiveRecord(false) //开启 ActiveRecord 模式
                .setBaseResultMap(true) //开启 BaseResultMapt(为了自定义方便？)
                .setBaseColumnList(true) //开启 baseColumnList(为了自定义方便？)
//                .setDateType(DateType.TIME_PACK) //时间类型对应策略
//                //各层文件名称方式，例如： %Action 生成 UserAction
//                .setEntityName()
//                .setMapperName()
//                .setXmlName()
                .setServiceName("%sService") //不设置，类似：IUserService
//                .setServiceImplName()
//                .setControllerName()
//                .setIdType(IdType.INPUT) //指定生成的主键的ID类型  如果设置了这个,配置entityTableFieldAnnotationEnable(true)会产生@TableId，但没有import ?? 可以考虑在CustomAutoGenerator中处理
                ;
        return config;
    }

    private DataSourceConfig dataSourceConfig() {
        return new CustomDataSourceConfig() //new DataSourceConfig()
                .setDbType(DbType.POSTGRE_SQL)
                .setDriverName("org.postgresql.Driver")
                .setTypeConvert(new PostgreSqlTypeConvert() {

                    @Override
                    public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType){
                        String t = fieldType.toLowerCase();
                        if(t.contains("numeric")) {
                            return DbColumnType.BIG_DECIMAL;
                        }
                        return super.processTypeConvert(globalConfig, fieldType);
                    }

                })
                ;
    }


    private StrategyConfig strategyConfig(String... tableNames) {
        return new StrategyConfig()
                .setDbColumnUnderline(true) //表名、字段名、是否使用下划线命名（默认 false）

//                .setCapitalMode(true) //是否大写命名。全局大写命名 ORACLE 注意
//                .setSkipView(true) //是否跳过视图
                .setNaming(NamingStrategy.underline_to_camel) //数据库表映射到实体的命名策略
//                .setColumnNaming() //数据库表字段映射到实体的命名策略。未指定按照 naming 执行
//                .setTablePrefix() //表前缀
//                .setFieldPrefix() //字段前缀
                .setSuperEntityClass(superPackageName("model.BModel")) //自定义继承的Entity类全称，带包名
//                .setSuperEntityColumns()  // 自定义基础的Entity类，公共字段
//                .setSuperMapperClass(ConstVal.SUPER_MAPPER_CLASS) //自定义继承的Mapper类全称，带包名
                .setSuperMapperClass(superPackageName("dao.BMapper")) //自定义继承的Mapper类全称，带包名
//                .setSuperServiceClass(ConstVal.SUPER_SERVICE_CLASS) //自定义继承的Service类全称，带包名
                .setSuperServiceClass(superPackageName("service.BService")) //自定义继承的Service类全称，带包名
//                .setSuperServiceImplClass(ConstVal.SUPER_SERVICE_IMPL_CLASS) //自定义继承的ServiceImpl类全称，带包名
                .setSuperServiceImplClass(superPackageName("service.BServiceImpl")) //自定义继承的ServiceImpl类全称，带包名
                .setSuperControllerClass(superPackageName("controller.BController")) //自定义继承的Controller类全称，带包名
                .setInclude(tableNames) //需要包含的表名（与exclude二选一配置）。修改替换成你需要的表名，多个表名传数组
//                .setExclude() //需要排除的表名
//                .setEntityColumnConstant(true) //【实体】是否生成字段常量（默认 false）。public static final String ID = "test_id";
//                .setEntityBuilderModel(true) //【实体】是否为构建者模型（默认 false）。public User setName(String name) { this.name = name; return this; }
                .setEntityLombokModel(true) //【实体】是否为lombok模型（默认 false）
//                .setEntityBooleanColumnRemoveIsPrefix(true) //Boolean类型字段是否移除is前缀（默认 false）。比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
                .setRestControllerStyle(true) //生成 <code>@RestController</code> 控制器
                .setControllerMappingHyphenStyle(true) //驼峰转连字符。<code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>

                .entityTableFieldAnnotationEnable(true) //是否生成实体时，生成字段注解 ??????
//                .setVersionFieldName() //设置乐观锁字段
//                .setLogicDeleteFieldName() //设置逻辑删除字段
//                .setTableFillList() //表填充字段
                ;
    }

    private PackageConfig packageConfig(String packageName) {
        return new PackageConfig()
                .setParent(packageName)
                .setMapper("dao")
                .setXml("dao.xml")
                .setEntity("model");
    }


    /**
     * 返回DataSourceConfig,用于设置数据源
     * @return
     */
    public DataSourceConfig getDataSourceConfig() {
        return this.autoGenerator.getDataSource();
    }

    /**
     * 设置数据源
     * @param url
     * @param username
     * @param password
     */
    public MPGenerator setDataSourceConfig(String url, String username, String password) {
        getDataSourceConfig()
                .setUrl(url)
                .setUsername(username)
                .setPassword(password);
        return this;
    }

    protected GlobalConfig getGlobalConfig() {
        return this.autoGenerator.getGlobalConfig();
    }

    protected InjectionConfig getInjectionConfig() {
        return this.autoGenerator.getCfg();
    }

    /**
     * 生成文件的输出目录 输出路径
     * @return
     */
    public MPGenerator setOutputDir(String outputDir) {
        //默认的JAVA代码
        this.getGlobalConfig().setOutputDir(outputDir);
        return this;
    }

//    /**
//     * 生成额外文件（如vue）的输出目录 输出路径
//     * @return
//     */
//    public MPGenerator setExtraOutputDir(String outputDir) {
//        InjectionConfig iCfg = this.getInjectionConfig();
//        if(CustomInjectionConfig.class.isInstance(iCfg)) {
//            ((CustomInjectionConfig) iCfg).setOutputParentDir(outputDir);
//        }
//        return this;
//    }

    /**
     * 是否覆盖已有文件
     * @return
     */
    public MPGenerator setFileOverride(boolean fileOverride) {
        this.getGlobalConfig().setFileOverride(fileOverride);
        return this;
    }

    /**
     * 是否打开输出目录
     * @return
     */
    public MPGenerator setOpen(boolean open) {
        this.getGlobalConfig().setOpen(open);
        return this;
    }

    /**
     * 开发人员
     * @return
     */
    public MPGenerator setAuthor(String author) {
        this.getGlobalConfig().setAuthor(author);
        return this;
    }

    /**
     * 前端配置
     * @param builder
     * @return
     */
    public MPGenerator setFrontEndConfigBuilder(FrontEndConfigBuilder builder) {
        this.autoGenerator.setFrontEndConfigBuilder(builder);
        return this;
    }

    /**
     *
     * @param param
     * @return
     */
    public MPGenerator setParam(GeneratorParam param) {
        ((CustomInjectionConfig) this.autoGenerator.getCfg()).setGeneratorParam(param);
        return this;
    }

//    public MPGenerator putInjection(String key, String value) {
//        this.autoGenerator.getCfg().getMap().put(key, value);
//        return this;
//    }

    /**
     * 生成代码
     */
    public void execute() {
        this.autoGenerator.execute();
    }

    /**
     *
     * @param sub
     * @return
     */
    private String packageName(String sub) {
        return this.parentPackageName + "." + sub;
    }

    /**
     *
     * @param sub
     * @return
     */
    private String superPackageName(String sub) {
        return this.superParentPackageName + "." + sub;
    }

    /**
     * MP版本_配置版本
     * @return
     */
    protected String version() {
        return myBatisPlusVersion() + "_" + CONFIG_VERSION;
    }


    public static String myBatisPlusVersion() {
        String content = AutoGenerator.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        content = content.substring(content.lastIndexOf("/") + 1);

        // 按指定模式在字符串查找
        String pattern = "\\D+(\\d.*)\\.jar"; //.+(\d+.*)\.jar

        System.out.println(content);

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        return m.find() && m.groupCount() == 1 ? m.group(1) : "unknow";
    }
}