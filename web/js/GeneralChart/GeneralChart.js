/**
 * Created by fuookami on 2017/3/22.
 */

PublicFun.include_js("../js/Public/SiderBarHandler.js");

var sider_bar_handler = null;
var flag = false;

var curr_page = 0;
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
    jQuery("#Page1Box").hide();
    jQuery("#Page1").hide();

    jQuery("#UnitRow").hide();

    jQuery("input[name='DataType']").change(function(e){
        var input = jQuery(e.currentTarget);

        if (input.val() == 1) {
            jQuery("#UnitRow").hide();
        } else {
            jQuery("#UnitRow").show();
        }
    });

    jQuery("#CheckBtn").click(function(){
        if (check_input()) {
            get_data();
        }
    });

    jQuery(".PageTurnBox").on("click", "div", function(e){
        if(!jQuery(e.currentTarget).hasClass("CurrPage")){
            turn_to_page(jQuery(e.currentTarget).prevAll('div').length);
        }
    });
}

function turn_to_page(i) {
    jQuery("#Page" + curr_page + "Box").hide();
    jQuery("#Page" + curr_page).removeClass("CurrPage");

    curr_page = i;
    jQuery("#Page" + i + "Box").show();
    jQuery("#Page" + i).addClass("CurrPage");
}

function check_input() {
    return true;
}

function get_data() {
    var data_type = jQuery("input[name='DataType']:checked").val();
    
    var beginTime = jQuery("input[name='BeginDate']").val();
    var endTime = jQuery("input[name='EndDate']").val();
    var bg_time = Date.parse(beginTime);
    bg_time = bg_time / 1000;
    var ed_time = Date.parse(endTime);
    ed_time = ed_time / 1000;

    var unit = -1;
    if (data_type == 0) {
        unit = jQuery("input[name='Unit']:checked").val();
    }

    var data = {
        "code": "d9",
        "id": jQuery.cookie("id"),
        "unit": unit,
        "bg_time": bg_time,
        "ed_time": ed_time,
        "type": jQuery("input[name='Type']:checked").val()
    };

    jQuery.ajax({
        type: "POST",
        url: server_url + "statisticsServlet",
        async: false,
        dataType: "json",
        data: JSON.stringify(data),
        success: function(data) {
            //alert("data:"+JSON.stringify(data));
            show_histogram_chart_data(data.data);
        },
        error: function() {
            alert("服务器连接失败，请检查网络。");
        }
    });


    /*jQuery.getJSON("../resource/d9.json", function(data) {
        show_histogram_chart_data(data.data);
    });*/
}

function show_histogram_chart_data(data) {
    if (!flag) {
        jQuery("#Page1").show();
        flag = true;
    }

    var tbody = jQuery(".HistogramChart").children("tbody");
    tbody.html("<tr>" +
        "<td>对象</td>" +
        "<td>数值</td>" +
        "<td>百分比</td>" +
        "<td>柱状图</td>" +
        "</tr>");

    var max = 0;
    var j = data.length;
    for (var i = 0; i < j; ++i) {
        if (Number(data[i].y) > max) {
            max = Number(data[i].y);
        }
    }

    var code = "";
    for (var i = 0; i < j; ++i) {
        code += generate_histogram_chart_line_code(data[i].x, Number(data[i].y), max);
    }
    tbody.append(code);

    turn_to_page(1);
}

function generate_histogram_chart_line_code(x, y, max) {
    var percentage;
    if(max == 0){
        percentage = 0;
    }else{
        percentage  = y / max * 100;
    }
    return "<tr>" +
        "<td>" + x + "</td>" +
        "<td>" + y + "</td>" +
        "<td>" + percentage + "%</td>" +
        "<td><div class='Histogram' style='width: " + percentage + "%;'></div></td>" +
        "</tr>";
}