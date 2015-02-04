<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<html lang="en">
<head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 	<meta name="layout" content="admin" />
	<title><g:message code="app.name"  /> </title>
</head>
<body>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info">${flash.message}</div>
	</g:if>	
	
	
	
	
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
		<g:form controller="admin" action="index" method="get" class="range-form">
			<span class="secondary">
				Date Range :&nbsp;
			</span>
			<input type="text" name="startDate" id="start-date" class="form-control" value="${startDate}"/>
			&nbsp;
			<span class="secondary">to</span>
			&nbsp;
			<input type="text"name="endDate" id="end-date" class="form-control" value="${endDate}"/>
			<a href="javascript:" class="btn btn-default" id="refresh"><span class="glyphicon glyphicon-refresh"></span></a>
			<g:link controller="admin" action="index" params="[ allData : true ]" class="btn btn-default">All Data</g:link>
		</g:form>
	</div>
	
	<br class="clear"/>
	
	<div id="refreshable-data">
		
		<div id="sales-stats-container">
			
			<div id="sales-stats" class="pull-left">
				
				<div class="sales-stat">
					<span class="stat">
						<span class="sales-stat-dollar secondary">$</span>
						<g:if test="${storeStatistics.salesStats.sales > 0}">
							<span class="sales-stat-value">${storeStatistics.salesStats.sales}</span>
						</g:if>
						<g:else>
							<span class="sales-stat-value">--</span>
						</g:else>
						<span class="sales-stat-label hint">TOTAL SALES</span>
					</span>
				</div>
				
				<div class="sales-stat">
					<span class="stat">
						<g:if test="${storeStatistics.salesStats.orderCount > 0}">
							<span class="sales-stat-value">${storeStatistics.salesStats.orderCount}</span>
						</g:if>
						<g:else>
							<span class="sales-stat-value">--</span>
						</g:else>
						<span class="sales-stat-label hint">TOTAL ORDERS</span>
					</span>
				</div>
				
				
				<div class="sales-stat">
					<span class="stat">
						<span class="sales-stat-dollar secondary">$</span>
						<g:if test="${storeStatistics.salesStats.averageOrder > 0}">
							<span class="sales-stat-value">${storeStatistics.salesStats.averageOrder}</span>
						</g:if>
						<g:else>
							<span class="sales-stat-value">--</span>
						</g:else>
						<span class="sales-stat-label hint">AVERAGE ORDER</span>
					</span>
				</div>
				

				<div class="sales-stat">
					<span class="stat">
						<span class="hint">${storeStatistics.salesStats.checkoutCarts}/${storeStatistics.salesStats.shoppingCarts}</span>
						<span class="sales-stat-value">${storeStatistics.salesStats.checkoutRate}</span>
						<span class="sales-stat-percent secondary">%</span>
						<span class="sales-stat-label hint">CHECKOUT RATE</span>
					</span>
				</div>
				
			</div>
			
			<div id="sales-chart-container" class="pull-left">
				<g:if test="${storeStatistics.salesStats.chartData.size() > 0}">
					<h3 class="secondary align-left">Sales</h3>
					<div id="sales-chart"></div>
				</g:if>
				<g:else>
					<img src="/${applicationService.getContextName()}/images/app/no-chart-data.jpg" id="no-chart-data" height="260" width="520" style="margin-left:40px;"/>
				</g:else>
			</div>
			
			<br class="clear"/>
			
		</div>
		<!-- end of sales-stats-container -->
		
		<br class="clear"/>
		
					
		<div id="activity-stats-container">
			
			<div class="activity-stats">
				
				<h4 class="secondary">Top viewed Products</h4>
				
				<div class="activity-stat-list">
				
					<g:if test="${storeStatistics.productStats}">
					
						<g:each in="${storeStatistics.productStats}" var="productStat" status="i">
							<g:if test="${i <= 4}">
								<div class="activity-stat-row">
									<span class="activity-stat-title">${productStat.value.product}</span>
									<span class="activity-stat-value">${productStat.value.count}</span>
									<br class="clear"/>
								</div>
							</g:if>
						</g:each>
    	            	
						<g:if test="${storeStatistics.productStats.size() > 4}">
							<div class="view-all">
								<a href="#">View All</a>
							</div>
						</g:if>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							No Data Available
						</div>
					</g:else>
				</div>
				
			</div>
			
			<div class="activity-stats">
				
				<h4 class="secondary">Top viewed Pages</h4>
				
				<div class="activity-stat-list">
				
					<g:if test="${storeStatistics?.pageStats}">
					
						<g:each in="${storeStatistics?.pageStats}" var="pageStat" status="i">
							<g:if test="${i <= 4}">
								<div class="activity-stat-row">
									<span class="activity-stat-title">${pageStat.value.page}</span>
									<span class="activity-stat-value">${pageStat.value.count}</span>
									<br class="clear"/>
								</div>
							</g:if>
						</g:each>
    	            	
						<g:if test="${storeStatistics.pageStats?.size() > 4}">
							<div class="view-all">
								<a href="#">View All</a>
							</div>
						</g:if>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							No Data Available
						</div>
					</g:else>
					
				</div>
				
			</div>
			
			<div class="activity-stats last">
				
				<h4 class="secondary">Top Search Terms</h4>
				
				<div class="activity-stat-list">
					
					<g:if test="${storeStatistics?.searchStats}">
					
						<g:each in="${storeStatistics?.searchStats}" var="searchStat" status="i">
							<g:if test="${i <= 4}">
								<div class="activity-stat-row">
									<span class="activity-stat-title">${searchStat.key}</span>
									<span class="activity-stat-value">${searchStat.value.count}</span>
									<br class="clear"/>
								</div>
							</g:if>
						</g:each>
    	            	
						<g:if test="${storeStatistics.searchStats?.size() > 4}">
							<div class="view-all">
								<a href="#">View All</a>
							</div>
						</g:if>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							No Data Available
						</div>
					</g:else>
					
				</div>
				
			</div>
		
			<br class="clear"/>
			
			<div class="activity-stats">
				
				<h4 class="secondary">Top viewed Catalogs</h4>
				
				<div class="activity-stat-list">
					
					<g:if test="${storeStatistics?.catalogStats}">
					
						<g:each in="${storeStatistics?.catalogStats}" var="catalogStat" status="i">
							<g:if test="${i <= 4}">
								<div class="activity-stat-row">
									<span class="activity-stat-title">${catalogStat.value.catalog}</span>
									<span class="activity-stat-value">${catalogStat.value.count}</span>
									<br class="clear"/>
								</div>
							</g:if>
						</g:each>
    	            	
						<g:if test="${storeStatistics.catalogStats?.size() > 4}">
							<div class="view-all">
								<a href="#">View All</a>
							</div>
						</g:if>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							No Data Available
						</div>
					</g:else>
			
				</div>
				
			</div>
			
			
			
			<div id="google-analytics-tip">
				<blockquote class="secondary">Greenfield leverages Google Analytics for detailed website statistics.  If you haven’t already done so, create an account and save your Analytics code in <strong class="secondary">Settings -> Store Settings</strong> to take advantage of this feature 
				
					<a href="http://www.google.com/analytics/" target="_blank">Visit Google Analytics</a>
				</blockquote>
			
				<br class="clear"/>
			</div>
			
			
			
		</div>
		<!-- end of activity stats container -->
		
		
	</div>
	
	
	<div id="calculating" style="margin-top:100px;display:none;text-align:center">
		<span class="secondary">Calculating, please wait&nbsp;&nbsp;</span>
		<img src="/${applicationService.getContextName()}/images/loading.gif" >
	</div>
	
	
	
<script type="text/javascript">
	
	var salesData = [];
	var dates = [];
	var totals = [];
	
	<g:if test="${storeStatistics.salesStats.chartData}">
		<g:each in="${storeStatistics.salesStats.chartData}" var="data">
			dates.push("${data.key}");
			var total = ${applicationService.formatPrice(data.value)}
			totals.push(total);
		</g:each>
		
		for(var m = 0; m < dates.length; m++){
			var dataPoint = [new Date(dates[m]), totals[m]];
			salesData.push(dataPoint);
		}
	</g:if>
	
	$(document).ready(function(){
		
		var $noChartDataImg = $('#no-chart-data');
		var $refreshDataDiv = $('#refreshable-data');
		var $calculatingDiv = $('#calculating');
		var $form = $('.range-form');
		
		var $refreshBtn = $('#refresh');
		$refreshBtn.click(refreshData);
		
		function refreshData(){
			$refreshDataDiv.fadeOut(100);
			$calculatingDiv.fadeIn(100);
			$form.submit();
		}
		
		var $startDate = $('#start-date'),
			$endDate = $('#end-date');
			
		$startDate.datepicker();
		$endDate.datepicker();
		
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
		
		if(salesData.length > 0){
			var chart = new Dygraph( $chartDiv.get(0), salesData, options );
		}
		
	});

	
</script>		
	
	
</body>
</html>
