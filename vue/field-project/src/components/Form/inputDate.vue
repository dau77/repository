<template>
  <div class="fy-calendar">
    <input type="text" @click="openByDrop($event)" v-model="currentValue" :placeholder="placeholder" :readonly="is_readonly">
    <transition name="fade">
      <div class="calendar-dropdown" v-if="show">
        <calendar :multi="multi" :zero="zero" :lunar="lunar" :value="date_value" :begin="begin" :end="end" :range="range" :disabled="disabled" :events="events" @select="select">
        </calendar>
      </div>
    </transition>
  </div>
</template>

<script>
import calendar from '@/components/calendar/calendar.vue'
import util from '../utils/util.js'
export default {
    name: 'FyInputDate',
    componentName: 'FyInputDate',
    mixins: [util], //这些混入实例对象可以像正常的实例对象一样包含选项（可以直接this.*使用混入对象中的方法）
    inject: {
        //获取父元素的provide中的值
        fyForm: {
            default: ''
        },
        fyFormItem: {
            default: ''
        }
    },
    components: {
        calendar
    },
    props: {
        placeholder: {
            type: String,
            default: '请选择日期'
        },
        is_readonly: {
            type: Boolean,
            default: true
        },

        //为了简化使用组件时属性的传递，将calendar组件中的props全部放在calendar_prop对象中
        calendar_prop: {},

        //时间戳格式
        value: {
            type: Number,
            default: 0
        }
    },
    data() {
        return {
            is_click: true, //离开时的点击事件被重复触发，加判断参数
            currentValue: this.timestampToDate(this.value),

            //以下数据全部封装到calendar中传入
            show: false,
            //选择了日期后执行的事件
            select: value => {
                this.show = false
                this.date_value = value
                this.currentValue = value.join('-')
            },

            // :value Array default:[] * 日历的默认值
            // :begin Array default:[] * 限制开始选择日期
            // :end Array default:[] * 限制结束选择日期
            // :range Bool default:false * 您可以选择一个时间段
            // :zero Bool default:false * 是否小于10补零
            // :lunar Bool default:false * 显示中国农历
            // :weeks Array * 根据系统语言的变化或自定义
            // :months Array * 根据系统语言的变化或自定义
            // :events Object * 自定义日历事件
            //以下是calendar组件中的属性
            // 多选模式
            multi: false,
            // 范围模式
            range: false,
            // 默认日期
            date_value: [],
            // 开始选择日期
            begin: [],
            // 结束选择日期
            end: [],
            // 是否小于10补零
            zero: false,
            // 屏蔽的日期
            disabled: [],
            // 是否显示农历
            lunar: false,
            // 自定义事件
            events: {}
        }
    },
    mounted() {
        this.dispatch('FyFormItem', 'formItem.addChildVc', [this]) //发送添加formitem对象的信息
        for (var key in this.calendar_prop) {
            if (this.calendar_prop[key]) {
                this[key] = this.calendar_prop[key]
            }
        }
    },
    watch: {
        value(value) {
            this.setCurrentValue(this.value)
        },
        currentValue(value) {
            this.$emit('input', this.dateToTimestamp(value))
            this.dispatch('FyFormItem', 'el.form.change', [
                this.dateToTimestamp(value)
            ])
        }
    },
    computed: {},
    methods: {
        //设置calendar组件的显示和隐藏
        openByDrop(e) {
            this.is_click = true
            this.show = true
            e.stopPropagation()
            window.setTimeout(() => {
                document.addEventListener(
                    'click',
                    e => {
                        if (this.is_click) {
                            this.is_click = false
                            this.show = false
                            document.removeEventListener(
                                'click',
                                () => {},
                                false
                            )
                            this.dispatch('FyFormItem', 'el.form.change', [
                                this.dateToTimestamp(this.currentValue)
                            ])
                        }
                    },
                    false
                )
            }, 1000)
        },
        setCurrentValue(value) {
            if (value === this.currentValue) return // 如果新旧值一致直接返回
            this.currentValue = this.timestampToDate(this.value) // 改变当前值
        },
        resetField() {
            this.currentValue = ''
        },
        //日期转时间戳
        dateToTimestamp(value) {
            const reg = /^(\d{4})-(\d{2})-(\d{2})$/
            if (!reg.test(value)) {
                return 0
            } else {
                return new Date(value).getTime()
            }
        },
        //时间戳转日期
        timestampToDate(value) {
            if (value == 0 || value.constructor != Number) {
                return ''
            }
            const date = new Date(value)
            var y = date.getFullYear()
            var m = date.getMonth() + 1
            m = m < 10 ? '0' + m : m
            var d = date.getDate()
            d = d < 10 ? '0' + d : d
            const s = y + '-' + m + '-' + d
            return y + '-' + m + '-' + d
        }
    }
}
</script>

<style>
.fy-calendar {
    position: relative;
}
.fy-calendar input {
    box-sizing: border-box;
    -webkit-appearance: none;
    background-color: #fff;
    background-image: none;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    color: #606266;
    display: inline-block;
    font-size: inherit;
    height: 40px;
    line-height: 30px;
    outline: none;
    padding: 0 15px;
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    width: 100%;
    background: url('../../assets/images/date_ico.png') no-repeat 8px 10px;
    background-size: 15px;
    padding-left: 36px;
    color: #666;
}
.fy-calendar input::-webkit-input-placeholder {
    color: #dbdbdb;
}
.calendar-dropdown {
    background: #fff;
    position: absolute;
    left: 5%;
    top: 120%;
    padding: 2%;
    border: 1px solid #eee;
    border-radius: 2px;
    z-index: 10;
}
.calendar-dropdown:before {
    position: absolute;
    left: 30px;
    top: -10px;
    content: '';
    border: 5px solid rgba(0, 0, 0, 0);
    border-bottom-color: #dedede;
}
.calendar-dropdown:after {
    position: absolute;
    left: 30px;
    top: -9px;
    content: '';
    border: 5px solid rgba(0, 0, 0, 0);
    border-bottom-color: #fff;
}
</style>
