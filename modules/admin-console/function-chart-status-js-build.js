//图表构建
function buildChart(title, yAxisTitle, name, data) {
	var chart = {
		title : {
			text : title,
			x : -20
		},
		xAxis : {
			type : 'datetime',
			labels : {
				step : 1,
				rotation : 30
			},
			tickPixelInterval : 30
		},
		yAxis : {
			title : {
				text : yAxisTitle
			},
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#80080'
			} ]
		},
		legend : {
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'middle',
			borderWidth : 0
		},
		series : [ {
			name : name,
			data : data

		} ]
	};
	return chart;
}
