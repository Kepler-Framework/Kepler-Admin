//图表主题
var GrayTheme = {
	    colors: ["#DDDF0D", "#7798BF", "#55BF3B", "#DF5353", "#aaeeee", "#ff0066", "#eeaaee", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
	    chart: {
	        backgroundColor: {
	            linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	            stops: [
					[0, 'rgb(96, 96, 96)'],
					[1, 'rgb(16, 16, 16)']
				]
	        },
	        borderWidth: 0,
	        borderRadius: 0,
	        plotBackgroundColor: null,
	        plotShadow: false,
	        plotBorderWidth: 0
	    },
	    title: {
	        style: {
	            color: '#FFF',
	            font: '16px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
	        }
	    },
	    subtitle: {
	        style: {
	            color: '#DDD',
	            font: '12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
	        }
	    },
	    xAxis: {
	        gridLineWidth: 0,
	        lineColor: '#999',
	        tickColor: '#999',
	        labels: {
	        	step:1,
				rotation:30,
	            style: {
	                color: '#FFF'
	            },
				tickPixelInterval:30
	        },
	        title: {
	            style: {
	                color: '#AAA',
	                font: 'bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
	            }
	        }
	    },
	    yAxis: {
	        alternateGridColor: null,
	        minorTickInterval: null,
	        gridLineColor: 'rgba(255, 255, 255, .1)',
	        minorGridLineColor: 'rgba(255,255,255,0.07)',
	        lineWidth: 0,
	        tickWidth: 0,
	        labels: {
	            style: {
	                color: '#FFF'
	            }
	        },
	        title: {
	            style: {
	                color: '#FFF',
	                font: 'bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
	            }
	        }
	    },
	    legend: {
	        itemStyle: {
	            color: '#CCC'
	        },
	        itemHoverStyle: {
	            color: '#FFF'
	        },
	        itemHiddenStyle: {
	            color: '#333'
	        }
	    },
	    labels: {
	        style: {
	            color: '#CCC'
	        }
	    },
	    tooltip: {
	        backgroundColor: {
	            linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	            stops: [
					[0, 'rgba(96, 96, 96, .8)'],
					[1, 'rgba(16, 16, 16, .8)']
				]
	        },
	        borderWidth: 0,
	        style: {
	            color: '#FFF'
	        }
	    },
	    credits: {                                                         
            enabled: false                                                 
        },  
	    plotOptions: {
	        series: {
	            nullColor: '#444444'
	        },
	        line: {
	            dataLabels: {
	                color: '#CCC'
	            },
	            marker: {
	                lineColor: '#333'
	            }
	        },
	        spline: {
	            marker: {
	                lineColor: '#333'
	            }
	        },
	        scatter: {
	            marker: {
	                lineColor: '#333'
	            }
	        },
	        candlestick: {
	            lineColor: 'white'
	        }
	    }
};
Highcharts.setOptions(GrayTheme);
