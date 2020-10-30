layui.define(function (exports) {
   layui.use(['layer'], function () {
       var sInfo={}
       $(document).on('click', "#submitSInfo", function () {
           var data = {"username": $("#username").val(), "password":$("#password"), "goodsId": $("#goodsId").val()};
           $.ajax({
               url: ctx + '/start',
                type: 'post',
               data: JSON.stringify(data),
               success: function (res) {
                   
               },
               error: function () {
                   
               }
           })
       });
       exports('sInfo', sInfo)
   }) ;
});