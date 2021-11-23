<!DOCTYPE html>
<html style="height: 100%">
    <head>
        <meta charset="utf-8">
    </head>
    <body style="height: 100%; margin: 0">
        <div id="container" style="height: 100%"></div>

        <script type="text/javascript" src="lib/echarts.min.js"></script>

        <script type="text/javascript">

var dom = document.getElementById("container");
var myChart = echarts.init(dom);
var app = {};
var option;

var url = "tree-of-life-output.json";
//var url = "small.json";

fetch(url).then(response => response.json()).then(x =>
{
    option =
    {
        series:
        {
            type: 'sunburst',
            /*emphasis:
            {
                focus: 'ancestor'
            },*/
            data: x,
            radius: [0, '100%'],
            label:
            {
                rotate: 'radial'
            }
        }
    };
    myChart.setOption(option);
});

        </script>
    </body>
</html>
    