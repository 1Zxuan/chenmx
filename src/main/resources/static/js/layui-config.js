layui.config({
    version: '20200424',
    base: ctx + '/js'
}).extend({
    index: '/specific/index',
    brandInfo: '/specific/brandInfo',
    sInfo: '/specific/sInfo'
});
$(function() {
    layui.use('element', function() {
        var element = layui.element;
        element.init();
    });
});