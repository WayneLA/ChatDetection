/**
 * Created by fuookami on 2017/3/22.
 */

PublicFun.include_js("../js/Public/SiderBarHandler.js");

var sider_bar_handler = null;

jQuery(document).ready(function(){
    sider_bar_handler = new SiderBarHandler();
    sider_bar_handler.init_sider_bar_handler();
});