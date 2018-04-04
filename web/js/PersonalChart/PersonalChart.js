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
    jQuery("#Page2Box").hide();
    jQuery("#Page1").hide();
    jQuery("#Page2").hide();

    jQuery("#UnitRow").hide();
    jQuery("#TargetRow").hide();

    jQuery("input[name='DataType']").change(function(e){
        var input = jQuery(e.currentTarget);

        if (input.val() == 1) {
            jQuery("#UnitRow").hide();
            get_target_list(-1);
        } else {
            jQuery("#UnitRow").show();
        }
    });

    jQuery("input[name='Unit']").change(function(e){
        var input = jQuery(e.currentTarget);

        if (input.val()) {
            get_target_list(input.val());
        }
    });

    jQuery("#CheckBtn").click(function(){
        if (check_input()) {
            get_data();

            /*var unit = -1;
            if (jQuery("input[name='DataType']:checked").val() == 0) {
                unit = jQuery("input[name='Unit']:checked").val();
            }
            get_target_list(unit);*/

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

function get_target_list(unit) {

    var data = {
        "code": "d6",
        "id": jQuery.cookie("id"),
        "unit": unit
    };

    jQuery.ajax({
        type: "POST",
        url: server_url + "statisticsServlet",
        async: false,
        dataType: "json",
        data: JSON.stringify(data),
        success: function(data) {
            show_target_list(data.data);
        },
        error: function() {
            alert("服务器连接失败，请检查网络。");
        }
    });


    /*jQuery.getJSON("../resource/d6.json", function(data){
        show_target_list(data.data);
    });*/
}

function show_target_list(data) {
    var target_row = jQuery("#TargetRow");
    target_row.show();
    var target_select = target_row.children("td:nth-child(2)").children("select");
    target_select.html();
    target_select.children().remove();

    var code = "";
    for (var i = 0, j = data.length; i < j; ++i) {
        code += "<option value='" + data[i].id + "'>" + data[i].x + "</option>";
    }
    target_select.append(code);
}

function get_data() {
    get_histogram_chart_data();
    get_broken_line_chart_data();
}

function get_histogram_chart_data() {
    var data_type = jQuery("input[name='DataType']:checked").val();
    var target = jQuery("#TargetRow").children("td:nth-child(2)").children("select").val();

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
        "code": "d8",
        "id": jQuery.cookie("id"),
        "unit": unit,
        "target":target,
        "bg_time": bg_time,
        "ed_time": ed_time
    };

    jQuery.ajax({
        type: "POST",
        url: server_url + "statisticsServlet",
        async: false,
        dataType: "json",
        data: JSON.stringify(data),
        success: function(data) {
            show_histogram_chart_data(data.data);
        },
        error: function() {
            alert("服务器连接失败，请检查网络。");
        }
    });


    /*jQuery.getJSON("../resource/d8/d8_" + jQuery("#TargetRow").children("td:nth-child(2)").children("select").val() + ".json", function(data){
        show_histogram_chart_data(data.data);
    });*/
}

function show_histogram_chart_data(data) {
    if (!flag) {
        jQuery("#Page1").show();
        jQuery("#Page2").show();
        flag = true;
    }

    var tbody = jQuery(".HistogramChart").children("tbody");
    tbody.html("<tr>" +
        "<td>对象</td>" +
        "<td>数值</td>" +
        "<td>百分比</td>" +
        "<td>柱状图</td>" +
        "</tr>");

    var code = "";
    var j = data.length;
    for (var i = 0; i < j; ++i) {
        code += generate_histogram_chart_line_code(data[i].keyword, Number(data[i].number), data[i].percent, data[i].is_special);
    }
    tbody.append(code);

    turn_to_page(1);
    turn_to_page(1);
}

function generate_histogram_chart_line_code(x, y, percentage, is_special) {
    var code = "";
    if (is_special == 1) {
        code = "<tr class='Special'>";
    } else {
        code = "<tr>";
    }
    code += "<td>" + x + "</td>" +
        "<td>" + y + "</td>" +
        "<td>" + percentage + "%</td>" +
        "<td><div class='Histogram' style='width: " + percentage + "%;'></div></td>" +
        "</tr>";
    return code;
}

function get_broken_line_chart_data() {
    var data_type = jQuery("input[name='DataType']:checked").val();
    var target = jQuery("#TargetRow").children("td:nth-child(2)").children("select").val();

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
        "code": "d10",
        "id": jQuery.cookie("id"),
        "unit": unit,
        "target":target,
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

            //alert("data: "+ data);

            var pretreat_data = [];
            for (var i = 0, j = data.data.length; i < j; ++i) {

                pretreat_data.push([Number(data.data[i].x) * 1000, Number(data.data[i].y)]);
            }
            show_broken_line_chart_data(pretreat_data);
        },
        error: function() {
            alert("服务器连接失败，请检查网络。");
        }
    });

    /*jQuery.getJSON("../resource/d10/d10_" + jQuery("#TargetRow").children("td:nth-child(2)").children("select").val() + ".json", function(data){
        var pretreat_data = [];
        for (var i = 0, j = data.data.length; i < j; ++i) {
            pretreat_data.push([Number(data.data[i].x) * 1000, Number(data.data[i].y)]);
        }
        show_broken_line_chart_data(pretreat_data);
    });*/
}

function show_broken_line_chart_data(data) {
    jQuery('#Page2Box').highcharts({
        chart: {
            zoomType: 'x'
        },
        title: {
            text: '权值表'
        },
        subtitle: {
            text: document.ontouchstart === undefined ?
                '鼠标拖动可以进行缩放' : '手势操作进行缩放'
        },
        xAxis: {
            type: 'datetime',
            dateTimeLabelFormats: {
                millisecond: '%H:%M:%S.%L',
                second: '%H:%M:%S',
                minute: '%H:%M',
                hour: '%H:%M',
                day: '%m-%d',
                week: '%m-%d',
                month: '%Y-%m',
                year: '%Y'
            }
        },
        tooltip: {
            dateTimeLabelFormats: {
                millisecond: '%H:%M:%S.%L',
                second: '%H:%M:%S',
                minute: '%H:%M',
                hour: '%H:%M',
                day: '%Y-%m-%d',
                week: '%m-%d',
                month: '%Y-%m',
                year: '%Y'
            }
        },
        yAxis: {
            title: {
                text: '权值和'
            }
        },
        legend: {
            enabled: false
        },
        plotOptions: {
            area: {
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },
                marker: {
                    radius: 2
                },
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            }
        },
        series: [{
            type: 'area',
            name: '权值和',
            data: data
        }]
    });
}