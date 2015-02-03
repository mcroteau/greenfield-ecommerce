<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
	<title>Greenfield : Administration Prototype</title>

	<link rel="stylesheet" href="${resource(dir:'bootstrap/3.1.1/css', file:'bootstrap.min.css')}" />
	<script type="text/javascript" src="${resource(dir:'js/lib/jquery/1.11.0/jquery.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'bootstrap/3.1.1/js/bootstrap.js')}"></script>
	
	<script type="text/javascript" src="${resource(dir:'bootstrap/datepicker/datepicker.js')}"></script>
	<link rel="stylesheet" href="${resource(dir:'bootstrap/datepicker', file:'datepicker.css')}" />
	
	<script type="text/javascript" src="${resource(dir:'js/lib/dygraphs/1.1.0/dygraph-combined.min.js')}"></script>
	

	<link href="fonts/fonts.css" rel="stylesheet"/>
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	
	<g:layoutHead/>
	
	
<style type="text/css">	
	@font-face { 
		font-family: Roboto-Regular; 
		src: url("${resource(dir:'fonts/Roboto-Regular.ttf')}"); 
	} 
	@font-face { 
		font-family: Roboto-Bold;
		src: url("${resource(dir:'fonts/Roboto-Bold.ttf')}"); 
	}
	@font-face { 
		font-family: Roboto-Thin; 
		src: url("${resource(dir:'fonts/Roboto-Thin.ttf')}"); 
	}
	@font-face { 
		font-family: Roboto-Light; 
		src: url("${resource(dir:'fonts/Roboto-Light.ttf')}"); 
	}
	@font-face { 
		font-family: Roboto-Black; 
		src: url("${resource(dir:'fonts/Roboto-Black.ttf')}"); 
	}
	@font-face { 
		font-family: Roboto-Medium; 
		src: url("${resource(dir:'fonts/Roboto-Medium.ttf')}"); 
	}
</style>	

</head>
<body>
	
	<div id="greenfield-header"></div>
	
	<div id="outer-container">
		
		<div id="admin-nav-container">
	
			<div id="admin-marker"></div>
			
			<ul id="admin-nav">
				<li><g:link uri="/admin">Dashboard</g:link></li>
				<li><g:link uri="/product/list">Products</g:link></li>
				<li><g:link uri="/catalog/list">Catalogs</g:link></li>
				<li><g:link uri="/transaction/list">Orders</g:link></li>
				<li><g:link uri="/page/list">Pages</g:link></li>
				<li><g:link uri="/account/admin_list?admin=false">Accounts</g:link></li>
				<li><g:link uri="/configuration/settings">Settings</g:link></li>
				<li><g:link uri="/configuration">Configuration</g:link></li>
				<li><g:link uri="/layout">Store Layout</g:link></li>
			</ul>
			
		</div>
		
		<div id="content-container">
			
			<div id="header">
				<span class="header-info pull-left align-left">Welcome Back <strong><shiro:principal/></strong>!</span>
				
				<span class="header-info pull-right align-right">
					<g:link uri="/">Store Front</g:link>
					&nbsp;|&nbsp;
					<g:link controller="auth" action="signOut">Logout</g:link>
				</span>
					
				<br class="clear"/>
			</div>
			
			
			<div id="content">
				
				<div id="store-stats">
					
					<div class="store-stat">
						<span class="store-stat-value">${storeStatistics.generalStats.products}</span>
						<span class="store-stat-label secondary">Products</span>
					</div>

					
					<div class="store-stat">
						<span class="store-stat-value">${storeStatistics.generalStats.outOfStock}</span>
						<span class="store-stat-label secondary">Out of Stock</span>
					</div>
					
					
					<div class="store-stat">
						<span class="store-stat-value">${storeStatistics.generalStats.catalogs}</span>
						<span class="store-stat-label secondary">Catalogs</span>
					</div>
					
					
					<div class="store-stat">
						<span class="store-stat-value">${storeStatistics.generalStats.customers}</span>
						<span class="store-stat-label secondary">Customers</span>
					</div>
					
					
					<div class="store-stat">
						<span class="store-stat-value">${storeStatistics.generalStats.abandonedCarts}</span>
						<span class="store-stat-label secondary">Abandoned Carts</span>
					</div>
					
					
				</div>
				<!-- end of store-stats -->
				
				
				
				<h1 class="dashboard pull-left">Store Overview</h1>

				<div id="date-selectors" class="pull-right align-right" style="display:block">
					<span class="secondary">
						Date Range :&nbsp;
					</span>
					<input type="text" id="start-date" class="form-control"/>
					&nbsp;
					<span class="secondary">to</span>
					&nbsp;
					<input type="text" id="end-date" class="form-control"/>
					<a href="javascript:" class="btn btn-default" id="refresh"><span class="glyphicon glyphicon-refresh"></span></a>
					<a href="javascript:" class="btn btn-default">All Data</a>
				</div>
				
				<br class="clear"/>
				
				<div id="refreshable-data">
					
					<div id="sales-stats-container">
						
						<div id="sales-stats" class="pull-left">
							
							<div class="sales-stat">
								<span class="stat">
									<span class="sales-stat-dollar secondary">$</span>
									<span class="sales-stat-value">12,045.50</span>
									<span class="sales-stat-label hint">TOTAL SALES</span>
								</span>
							</div>
							
							<div class="sales-stat">
								<span class="stat">
									<span class="sales-stat-value">10</span>
									<span class="sales-stat-label hint">TOTAL ORDERS</span>
								</span>
							</div>
							
							
							<div class="sales-stat">
								<span class="stat">
									<span class="sales-stat-dollar secondary">$</span>
									<span class="sales-stat-value">1,204.50</span>
									<span class="sales-stat-label hint">AVERAGE ORDER</span>
								</span>
							</div>
							

							<div class="sales-stat">
								<span class="stat">
									<span class="hint">13/23</span>
									<span class="sales-stat-value">53</span>
									<span class="sales-stat-percent secondary">%</span>
									<span class="sales-stat-label hint">CHECKOUT RATE</span>
								</span>
							</div>
							
						</div>
						
						<div id="sales-chart-container" class="pull-left">
							<h3 class="secondary align-left">Sales</h3>
							<div id="sales-chart"></div>
						</div>
						
						<br class="clear"/>
						
					</div>
					<!-- end of sales-stats-container -->
					
					<br class="clear"/>
					
								
					<div id="activity-stats-container">
						
						<div class="activity-stats">
							
							<h4 class="secondary">Top viewed Products</h4>
							
							<div class="activity-stat-list">
								<div class="activity-stat-row">
									<span class="activity-stat-title">Walrus Ivory Whale</span>
									<span class="activity-stat-value">4</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Walrus Ivory Walrus</span>
									<span class="activity-stat-value">2</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Jade Beaded Necklace</span>
									<span class="activity-stat-value">2</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Jade Earrings</span>
									<span class="activity-stat-value">1</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Black Diamond Necklace</span>
									<span class="activity-stat-value">1</span>
									<br class="clear"/>
								</div>
								

    							<div class="view-all">
									<a href="#">View All</a>
								</div>
								
							</div>
							
						</div>
						
						<div class="activity-stats">
							
							<h4 class="secondary">Top viewed Pages</h4>
							
							<div class="activity-stat-list">
								<div class="activity-stat-row">
									<span class="activity-stat-title">Home</span>
									<span class="activity-stat-value">114</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">About Us</span>
									<span class="activity-stat-value">110</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Contact Us</span>
									<span class="activity-stat-value">98</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Native Artwork</span>
									<span class="activity-stat-value">95</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Privacy Policy</span>
									<span class="activity-stat-value">92</span>
									<br class="clear"/>
								</div>

    							
								<div class="view-all">
									<a href="#">View All</a>
								</div>
								
							</div>
							
						</div>
						
						<div class="activity-stats last">
							
							<h4 class="secondary">Top Search Terms</h4>
							
							<div class="activity-stat-list">
								<div class="activity-stat-row">
									<span class="activity-stat-title">walrus ivory</span>
									<span class="activity-stat-value">13</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">black diamond</span>
									<span class="activity-stat-value">10</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">earrings</span>
									<span class="activity-stat-value">10</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">ivory carving</span>
									<span class="activity-stat-value">7</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">baleen</span>
									<span class="activity-stat-value">4</span>
									<br class="clear"/>
								</div>

    							
								<div class="view-all">
									<a href="#">View All</a>
								</div>
								
							</div>
							
						</div>
					
						<br class="clear"/>
						
						<div class="activity-stats">
							
							<h4 class="secondary">Top viewed Catalogs</h4>
							
							<div class="activity-stat-list">
								<div class="activity-stat-row">
									<span class="activity-stat-title">Oosik Carvings</span>
									<span class="activity-stat-value">142</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Native Masks</span>
									<span class="activity-stat-value">103</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Baleen Carvings</span>
									<span class="activity-stat-value">87</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Baleen Baskets</span>
									<span class="activity-stat-value">58</span>
									<br class="clear"/>
								</div>
                	
								<div class="activity-stat-row">
									<span class="activity-stat-title">Grass Baskets</span>
									<span class="activity-stat-value">42</span>
									<br class="clear"/>
								</div>


    							<div class="view-all">
									<a href="#">View All</a>
								</div>
								
							</div>
							
						</div>
						
						
						
						<div id="google-analytics-tip">
							<blockquote class="secondary">Greenfield leverages Google Analytics for detailed website statistics.  If you haven’t already done so, create an account and save your Analytics code in <strong class="secondary">Settings -> Store Settings</strong> to take advantage of this feature 
							
								<a href="#">Visit Google Analytics</a>
							</blockquote>
						
							<br class="clear"/>
						</div>
						
						
						
					</div>
					<!-- end of activity stats container -->
					
					
				</div>
				
				
			</div>
			<!-- end of content -->
			
			
			
		</div>
		<!-- end of content-container -->
		
		
		<br class="clear"/>
		
	</div>
	
	
<script type="text/javascript">
	
	
	$(document).ready(function(){
		
		var $refreshDataDiv = $('#refreshable-data');
		var $refreshBtn = $('#refresh');
		$refreshBtn.click(refreshData);
		
		function refreshData(){
			$refreshDataDiv.fadeOut(500);
			$refreshDataDiv.fadeIn(500);
		}
		
		var $startDate = $('#start-date'),
			$endDate = $('#end-date');
			
		$startDate.datepicker();
		$endDate.datepicker();
		
		$startDate.val('01/01/2015');
		$endDate.val('01/31/2015');
		
		function generateData(){
			var days = 32;
			var data = [];
			var today = new Date();
			
			var past = new Date(today.setDate(today.getDate() - days));
			console.log(past)
			
			for(var i = 0; i < days; i ++){
				
				past.setDate(past.getDate() + i);
				var value = Math.floor(Math.random() * 100) + 1;
				
				var day = past.getDate();
				var month = past.getMonth() + 1;
				var year = past.getFullYear();
				
				var date = new Date(month + "/" + day + "/" + year)
				
				var element = [date, value]
				data.push(element);
			}
			return data
		}
		
		
		var data3 = generateData();
		//console.log(data3)
		
		
		var data2 = [
			[new Date("01/01/2015"), 24.54],
			[new Date("01/02/2015"), 43.10],
			[new Date("01/03/2015"), 13.50],
			[new Date("01/04/2015"), 65],
			[new Date("01/05/2015"), 56],
			[new Date("01/06/2015"), 75],
			[new Date("01/07/2015"), 23],
			[new Date("01/08/2015"), 45],
			[new Date("01/09/2015"), 58],
			[new Date("01/10/2015"), 14],
			[new Date("01/11/2015"), 17],
			[new Date("01/12/2015"), 24],
			[new Date("01/13/2015"), 38],
			[new Date("01/14/2015"), 34],
			[new Date("01/15/2015"), 24.54],
			[new Date("01/16/2015"), 43.10],
			[new Date("01/17/2015"), 13.50],
			[new Date("01/18/2015"), 65],
			[new Date("01/19/2015"), 56],
			[new Date("01/20/2015"), 75],
			[new Date("01/21/2015"), 23],
			[new Date("01/22/2015"), 45],
			[new Date("01/23/2015"), 58],
			[new Date("01/24/2015"), 14],
			[new Date("01/25/2015"), 17],
			[new Date("01/26/2015"), 24],
			[new Date("01/27/2015"), 38],
			[new Date("01/28/2015"), 34],
			[new Date("01/29/2015"), 38],
			[new Date("01/30/2015"), 34],
			[new Date("01/31/2015"), 75]
		];
		
		var options = {
			axes : {
				y : {
					axisLabelFormatter : function(y){
						return '$' + y;
					}
				}
			},
			highlightCircleSize: 4,
			width:555,
			height:240,                   
			legend: 'always',
			axisLineColor : "#ddd",
			fillGraph : true,
			drawPoints : false,//small dataset true
			pointSize : 4,
			strokeWidth : 2,
			color : "#9abbcf",
			drawGrid : true,
			gridLineColor : "#ddd",
			drawGapEdgePoints : true,
			labels : ["Date", "Sales"]
		}
		
		var $chartDiv = $('#sales-chart');
		
		var chart = new Dygraph( $chartDiv.get(0), data3, options );
		
	});
	

	
</script>	

</body>
</html>
