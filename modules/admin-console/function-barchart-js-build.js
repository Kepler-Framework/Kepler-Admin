//图表构建
function build(title, categoriesData, seriesData, yAxisTitle, tooltipSuffix) {
	var chart = {
			chart: {                                                           
	            type: 'bar'                                                    
	        },                                                                 
	        title: {                                                           
	            text: title                    
	        },                                                                 
	        subtitle: {                                                        
	            text: ''                                  
	        },                                                                 
	        xAxis: {                                                           
	            categories: categoriesData,
	            title: {                                                       
	                text: null                                                 
	            }                                                              
	        },                                                                 
	        yAxis: {                                                           
	            min: 0,                                                        
	            title: {                                                       
	                text: yAxisTitle,                             
	                align: 'high'                                              
	            },                                                             
	            labels: {                                                      
	                overflow: 'justify'                                        
	            }                                                              
	        },                                                                 
	        tooltip: {                                                         
	            valueSuffix: tooltipSuffix                                       
	        },                                                                 
	        plotOptions: {                                                     
	            bar: {                                                         
	                dataLabels: {                                              
	                    enabled: true                                          
	                }                                                          
	            }                                                              
	        },                                                                 
	        legend: {                                                          
	            layout: 'vertical',                                            
	            align: 'right',                                                
	            verticalAlign: 'top',                                          
	            x: -40,                                                        
	            y: 100,                                                        
	            floating: true,                                                
	            borderWidth: 1,                                                
	            backgroundColor: '#FFFFFF',                                    
	            shadow: true                                                   
	        },                                                                 
	        credits: {                                                         
	            enabled: false                                                 
	        },                                                                 
	        series: seriesData                                                                
	    };         
	return chart;
}
