/**
 * Created by fuookami on 2017/3/22.
 */

var SiderBarHandler = function() {
    var sider_bar = jQuery(".SideBar");
    var sider_container = jQuery(".SideBarContainer");
    var main_container = jQuery(".MainContainer");
    var switch_btn = jQuery(".SideBarSwitchBtn");

    var sider_bar_show = 0;
    var sider_bar_close = 1;
    var status = sider_bar_show;
    // var status = sider_bar_close;

    this.init_sider_bar_handler = function() {
        sider_container.hide();
        // sider_bar.css("left", "0");
        // main_container.css("left", "30%");

        switch_btn.click(function(){
            if (status == sider_bar_show) {
                sider_bar.css("left", "0");
                main_container.css("left", "30%");
                sider_container.show();
                status = sider_bar_close;
            } else {
                sider_bar.css("left", "-25%");
                main_container.css("left", "5%");
                sider_container.hide();
                status = sider_bar_show;
            }
        });
    };

    return this;
};