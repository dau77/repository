export default {
    defaultActive: '1',
    errorType: 2,
    locale: 'zh-cn',
    setDefaultActive(v) {
        this.defaultActive = v
        console.log('defaultActive已经改变' + v)
    }
}