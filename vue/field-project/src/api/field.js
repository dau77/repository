import {get, post, put } from "./base"

const pageField = {
    init(obj, pageEntity, pageName) {
        let promise = new Promise((resolve, reject) => {
            const url = 'page_field/' + pageName
            const params = {}
            var fields = get(url, params)
            fields.then(
                    res => {
                        obj[pageEntity] = res.response.data.field_map;
                        for (var key in obj[pageEntity]) {
                            var value = obj.fields[key];
                            if (value.field_rules != null && value.field_rules != undefined && value.field_rules != '') {
                                var rulelist = JSON.parse(value.field_rules).rule_list
                                for (var n = 0; n < rulelist.length; n++) { // 处理自定义验证方法
                                    if (rulelist[n].hasOwnProperty("validator")) {
                                        rulelist[n].validator = this[rulelist[n].validator];
                                    }
                                }
                                obj.rules[key] = rulelist;
                            }
                            if (value.category_group != null && value.category_group != undefined) {
                                obj[key] = value.category_group;
                            }
                        }
                        resolve()
                    })
                .catch(
                    err => {
                        console.log('err:' + err);
                        obj.$message.error('[' + err.status + ']' + err.message);
                        reject(err)
                    }
                );
        })
        return promise
    },
    isJson(rule, value, callback) {
        var isJson = false;
        if (!value) {
            isJson = true;
        }
        if (typeof value == "string") {
            try {
                var obj = JSON.parse(value);
                if (typeof obj == "object" && obj) {
                    isJson = true;
                }
            } catch (e) {
                console.log("error：" + value + "!!!" + e);
            }
        }
        if (!isJson) {
            callback(new Error(rule.message));
        } else {
            callback();
        }
    },
    getLanguages() {
        return [{ key_name: '中文', key_value: 'zh_CN' }, { key_name: '英文', key_value: 'en_US' }];
    },
    //时间格式化
    dateFormat: function(row, column) {
        var moment = require('moment');
        var date = row[column.property];
        if (date == undefined) {
            return "";
        }
        return moment(date).format("YYYY-MM-DD");
    }

}

export default pageField