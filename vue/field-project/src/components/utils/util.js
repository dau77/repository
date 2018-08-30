export default {
  methods: {
    /**
     * 查找指定名称的父级组件后，触发自定义事件
     * @param {*} componentName 父级组件名称
     * @param {*} eventName //自定义事件名称
     * @param {*} params //参数
     */
    dispatch(componentName, eventName, params) {
      var parent = this.$parent || this.$root;
      var name = parent.$options.componentName;
      while (parent && (!name || name !== componentName)) {
        parent = parent.parentNode;

        if (parent) {
          name = parent.$options.componentName;
        }
      }
      if (parent) {
        parent.$emit.apply(parent, [eventName].concat(params));
      }
    },
    /**
     * 根据rules自定义验证方法
     * @param {*} rules 验证规则
     * @param {*} model form中的model
     */
    customValidator(rules, model) {
      if (rules) {
        rules = rules.filter(rule => {
          //规则中有confirmProp代表要和其他字段比较，如确认密码的情况，confirmProp的值为要比较值的keyName
          if (rule.confirmProp) {
            rule.validator = (rule, value, callback) => { 
              if (value !== model[rule.confirmProp]) {
                callback(new Error(rule.message));
              } else {
                callback();
              }
            };
          }
          return rule;
        });
      }
      return rules;
    },
    // isJson(str) {
    //   if(!str){
    //     return true;
    //   }
    //   if (typeof str == "string") {
    //     try {
    //       var obj = JSON.parse(str);
    //       if (typeof obj == "object" && obj) {
    //         return true;
    //       } else {
    //         return false;
    //       }
    //     } catch (e) {
    //       console.log("error：" + str + "!!!" + e);
    //       return false;
    //     }
    //   } else {
    //     return false;
    //   }
    // },
  }
};