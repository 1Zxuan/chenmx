layui.define(function (exports) {
   layui.use(['layer', 'table'], function () {
       var layer = layui.layer;
       var table = layui.table;
       var index ={
           init: function () {
               var loadIndex = layui.layer.load(2);
               table.render({
                   elem: '#mainTable',
                   url: ctx + '/search?key=' + $("#search").val(),
                   toolbar : '#toolbar',
                   defaultToolbar: null,
                   method: 'get',
                   page:true,
                   event: true,
                   cols: [[{
                       type: 'numbers',
                       title: '序号'
                   },{
                       field: 'brandName',
                       title: '品牌'
                   },{
                       title: '商标',
                       templet: '<div><img src="{{d.logo}}" style="width:100%; height:100%;"/></div>'
                   },{
                       title: '操作',
                       toolbar: '#operation'
                   }]],
                   done:function (res) {
                       layer.close(loadIndex);
                   }
               });
           }
       };
       $(document).on('click', '#submit', function () {
           var loadIndex = layui.layer.load(2);
           table.reload('mainTable',{
              url:  ctx + '/search?key=' + $("#search").val(),
               done: function () {
                   layer.close(loadIndex)
               }
           });
       });
       table.on('tool(mainTable)',function (obj) {
           var data = obj.data;
           var event = obj.event;
           switch (event) {
               case 'list':
                   layer.open({
                       type: 2,
                       title: '选择',
                       content: ctx + '/page/brandInfo?brandId=' + data.brandId,
                       area: ['95%', '98%']
                   });break;
           }
       });

       exports('index', index);
   }) ;
});