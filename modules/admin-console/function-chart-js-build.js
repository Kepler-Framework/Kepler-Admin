//图表构建
function buildTotal(title, period, name, data) {
	var chartTotal = {
		title : {
			text : title,
			x : -20
		},
		xAxis : {
			type : 'datetime',
			labels:{
				step:1,
				rotation:30
			},
			tickPixelInterval:30
		},
		yAxis : {
			title : {
				text : "请求"
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
	return chartTotal;
}

function buildError(title, period, name, data) {
	var chartError = {
		title : {
			text : title,
			x : -20
		},
		xAxis : {
			type : 'datetime',
			labels:{
				step:1,
				rotation:30
			},
			tickPixelInterval:30
		},
		yAxis : {
			title : {
				text : "错误"
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
		colors: [ '#FF0033'],
		series : [ {
			name : name,
			data : data

		} ]
	};
	return chartError;
}

function buildElapse(title, period, name, data) {
	var chartElapse = {
		title : {
			text : title,
			x : -20
		},

		xAxis : {
			type : 'datetime',
			labels:{
				step:1,
				rotation:30
			},
			tickPixelInterval:30
		},
		yAxis : {
			title : {
				text : '耗时'
			},
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#80080'
			} ]
		},
		tooltip : {
			valueSuffix : ' ms'
		},
		legend : {
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'middle',
			borderWidth : 0
		},
		colors:["#FF9933"],
		series : [ {
			name : name,
			data : data

		} ]
	};
	return chartElapse;
}