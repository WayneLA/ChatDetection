/**
 * Created by fuookami on 2017/3/21.
 */

PublicFun.include_js("../js/Public/InputTextPlaceholderHandler.js");

var account_input_placeholder_handler = null;
var password_input_placeholder_handler = null;
var outline_color = "93,149,242";

var admin_login = -1;
var user_login = 1;
var login_error = 0;

var server_url = "";

jQuery(document).ready(function(){
    account_input_placeholder_handler = new InputTextPlaceholderHandler("#AccountInput");
    password_input_placeholder_handler = new InputTextPlaceholderHandler("#PasswordInput");
    account_input_placeholder_handler.init_input_text_placeholder_handler();
    password_input_placeholder_handler.init_input_text_placeholder_handler();

    var ret_code = check_login_cookie();
    if (ret_code == admin_login) {
        location.href = "AdminIndex.html";
    } else if (ret_code == user_login) {
        location.href = "UserIndex.html";
    } else {
        jQuery.getJSON("../resource/setting.json", function (data) {
            server_url = String(data.server_url);
            //alert("server_url: "+server_url);
            run();
        });
    }

});

function run() {
    var account_input = jQuery("#AccountInput");
    var password_input = jQuery("#PasswordInput");
    var login_btn = jQuery("#LoginBtn");

    jQuery(".InputRow").each(function(i, ele) {
        var target = jQuery(ele);
        target.on("focus", "input", function(){
            target.css({
                "border-color" : "rgba(" + outline_color + ",.75)",
                "box-shadow": "0 0 8px rgba(" + outline_color + ",.105)",
                "-moz-box-shadow": "0 0 8px rgba(" + outline_color + ",.5)",
                "-webkit-box-shadow": "0 0 8px rgba(" + outline_color + ",3)"
            });
        });

        target.on("blur", "input", function(){
            target.css({
                "border-color": "black",
                "box-shadow": "none",
                "-moz-box-shadow": "none",
                "-webkit-box-shadow": "none"
            });
        });
    });

    get_login_assertion();

    account_input.change(function(){
        if (account_input.val() && password_input.val()) {
            login_btn.addClass("EnabledButtonBox");
        } else {
            login_btn.removeClass("EnabledButtonBox");
        }
    });

    password_input.change(function(){
        if (account_input.val() && password_input.val()) {
            login_btn.addClass("EnabledButtonBox");

        } else {
            login_btn.removeClass("EnabledButtonBox");
        }
    });

    login_btn.click(function(){
        if (login_btn.hasClass("EnabledButtonBox")) {
            get_login_assertion();
        }
    });
}

function check_login_cookie() {
    if (jQuery.cookie("id")) {
        if (jQuery.cookie("is_admin") == 1) {
            return admin_login;
        } else {
            return user_login;
        }
    }
    return login_error;
}

function get_login_assertion() {
    var data = {
        "code": "d1",
        "data": {
            /*"account": jQuery("#AccountInput").val(),
            "password": jQuery("#PasswordInput").val()*/
            "account": "root",
            "password": "123"
        }
    };

    //alert(JSON.stringify(data));

    jQuery.ajax({
        type: "POST",
        url: server_url + "userManageServlet",
        async: true,
        dataType: "json",
        data: JSON.stringify(data),
        success: function(returnstr) {
            save_user_info(returnstr);
            if (returnstr.is_admin == 1) {
                location.href = "AdminIndex.html";
            }
            else {
                location.href = "UserIndex.html";
            }
        },
        error: function() {
            alert("服务器连接失败，请检查网络。");
        }
    });
}

function save_user_info(data) {
    if (jQuery("#AutoLoginCheckBox").is(':checked')) {
        jQuery.cookie("account", "root", 7);
        jQuery.cookie("id", data.id, 7);
        jQuery.cookie("is_admin", data.is_admin, 7);
    } else {
        jQuery.cookie("account", "root");
        jQuery.cookie("id", data.id);
        jQuery.cookie("is_admin", data.is_admin);
    }
}