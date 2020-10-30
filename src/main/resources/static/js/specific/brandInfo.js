layui.define(function (exports) {
   layui.use(['layer', 'table'], function () {
       var layer = layui.layer;
       var table = layui.table;
       var brandInfo = {
            init:function () {
                var loadIndex = layui.layer.load(2);
                table.render({
                    elem: '#brandInfo',
                    url: ctx + '/brandInfo?key=' + $("#brandId").val(),
                    toolbar : '#toolbar',
                    defaultToolbar: null,
                    method: 'get',
                    page:true,
                    event: true,
                    cols: [[
                    //     {
                    //     type: 'numbers',
                    //     title: '序号'
                    // },
                        {
                        field: 'goodsName',
                        title: '商品名',
                        width: '30%'
                    },{
                        title: '图片',
                        templet: '#imgTpl',
                        field: 'goodsImg'
                    },
                        {
                        title: '市场价',
                        field: 'marketPrice'
                    },
                    //     {
                    //     title: '团购价',
                    //     field: 'grouponPrice'
                    // },
                        {
                        title: '销售价',
                        field: 'salePrice'
                    }, {
                        title: '成本',
                        field: 'costPrice'
                    },
                    //     {
                    //     title: '创建时间',
                    //     field: 'createTime'
                    // },
                        {
                        title: '状态',
                        templet: function (d) {
                            if (d.goodsStatus == 1) {
                                return '缺货'
                            }
                            return '有货'
                        }
                    }, {
                        title: '优惠',
                        field: 'marketingDesc'
                    }, {
                        title: '操作',
                        toolbar:'#operation'
                    }]],
                    done:function (res) {
                        layer.close(loadIndex);
                    }
                })
            }
       };

       $(document).on('click', '#kcd', function () {
            table.reload('brandInfo', {
               url: ctx + '/brandInfo?sortFlag=11&key='  + $("#brandId").val(),
                page: {
                   curr: 1
                }
            });
       });
       $(document).on('click', '#kcu', function () {
           table.reload('brandInfo', {
               url: ctx + '/brandInfo?sortFlag=10&key='  + $("#brandId").val(),
               page: {
                   curr: 1
               }
           });
       });

       table.on('tool(brandInfo)',function (obj) {
           var data = obj.data;
           var event = obj.event;
           switch (event) {
               case 's':
                   layer.open({
                       type: 2,
                       title: '信息',
                       content: ctx + '/page/sInfo?goodsId=' + data.goodsId,
                       area: ['60%', '70%']
                   });
               break;
           }
       });
       exports('brandInfo', brandInfo)
   });
});