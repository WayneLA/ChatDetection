/**
 * Created by fuookami on 2017/5/16.
 */

PublicFun.include_js("../js/Public/SiderBarHandler.js");

var sider_bar_handler = null;
var server_url = null;

jQuery(document).ready(function(){
    sider_bar_handler = new SiderBarHandler();
    sider_bar_handler.init_sider_bar_handler();

    jQuery.getJSON("../resource/setting.json", function (data) {
        server_url = String(data.server_url);
        run();
    });
});

function run() {
    jQuery("#UploadBtn").click(function(){
        if (check_input()) {
            upload();
        }
    });
}

function check_input()
{
    // todo
    return true;
}

function upload()
{
    var data = {
        "code": "d11",
        "id": jQuery.cookie("id"),
        "data": {
            "code": "0",
            "timestamp": Date.parse(jQuery("input[name='DataDatetime']").val()) / 1000,
            "data": {
                "text": jQuery("#Text").val()
            }
        }
    };

    jQuery.ajax({
        type: "POST",
        url: server_url + "uploadServlet", // url
        async: false,
        dataType: "json",
        data: JSON.stringify(data),
        success: function(data) {
            jQuery("input[name='DataDatetime']").val("");
            jQuery("#Text").val("");
        },
        error: function() {
            alert("服务器连接失败，请检查网络。");
        }
    });
}