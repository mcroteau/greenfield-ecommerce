<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<html lang="en">
<head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 	<meta name="layout" content="admin" />
	<title>Greenfield  : ${message(code:"dashboard")}</title>
	
	<script type="text/javascript" src="${resource(dir:'js/lib/mustache/mustache.min.js')}"></script>
	
	<!-- jscrollpane assets -->
	<link rel="stylesheet" href="${resource(dir:'js/lib/jscrollpane/2.0.20', file:'jscrollpane.css')}" />
	<script type="text/javascript" src="${resource(dir:'js/lib/jscrollpane/2.0.20/jquery.mousewheel.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/jscrollpane/2.0.20/jscrollpane.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/jscrollpane/2.0.20/mwheelintent.js')}"></script>
	
	
<style type="text/css">	
	#backdrop{
		z-index:-3001;
		opacity:0;
		position:fixed;
		top:0px;
		bottom:0px;
		left:0px;
		right:0px;
		background:rgba(0,0,0,0.30);
	}
	
	#modal{
		height:380px;
		width:320px;
		padding:20px 10px 20px 30px;
		z-index:-2000;
		opacity:0;
		left:50%;
		top:50%;
		margin-top:-190px;
		margin-left:-160px;
		position:absolute;
		background:#ffffff;
		text-align:center;
		border-radius: 3px 3px 3px 3px;
		-moz-border-radius: 3px 3px 3px 3px;
		-webkit-border-radius: 3px 3px 3px 3px;
		-webkit-box-shadow: 0px 0px 30px 0px rgba(0,0,0,0.5);
		-moz-box-shadow: 0px 0px 30px 0px rgba(0,0,0,0.5);
		box-shadow: 0px 0px 30px 0px rgba(0,0,0,0.5);
	}
	#modal .activity-stats{
		height:auto;
		margin-bottom:0px;
	}
	
	#activity-stats-content{
		height:260px;
		width:280px;
		margin:10px auto;
		text-align:left;
		overflow: auto;
	}
	#close-activity-stats{
		font-size:12px;
		padding:5px 10px;
		margin-top:8px;
		margin-right:0px;
		outline:none;
		display:inline-block;
	}
	#close-activity-stats:hover{
		text-decoration:none;
		background:#efefef;
	}
	
	.jspContainer{
		-webkit-box-shadow: inset 0 3px 5px -4px rgba(0,0,0,0.2);
		-moz-box-shadow: inset 0 3px 5px -4px rgba(0,0,0,0.2);
	    box-shadow: inset 0px 3px 5px -4px rgba(0,0,0,0.2);
	}

	.jspVerticalBar{
		width:8px;
	}
	
	.jspPane{
		width:255px !important;
	}
	
	.jspTrack{
		background-color:#efefef;
		-webkit-box-shadow: inset 1px 1px 1px 0px rgba(0,0,0,0.15);
		-moz-box-shadow: inset 1px 1px 1px 0px rgba(0,0,0,0.15);
		box-shadow: inset 1px 1px 1px 0px rgba(0,0,0,0.15);
	}
	.jspDrag{
		background-color:#bbbbbb;
	}
</style>	
	
</head>
<body>
	 
	<div id="modal">							
		<div class="activity-stats">
			<h4 class="secondary" id="activity-stats-title">{{title}}</h4>
			<div id="activity-stats-content"></div>
		</div>
		<a href="javascript:" id="close-activity-stats" class="pull-right"><g:message code="close"/></a>
		<br class="clear"/>
	</div>
	


	<div id="backdrop"></div>
	
	
	<g:if test="${flash.message}">
		<div class="alert alert-info">${flash.message}</div>
	</g:if>	
	
	
	
	
	<div id="store-stats">
		
		<div class="store-stat">
			<span class="store-stat-value"><g:link controller="product" action="list">${storeStatistics.generalStats.products}</g:link></span>
			<span class="store-stat-label secondary"><g:message code="products"/></span>
		</div>

		
		<div class="store-stat">
			<span class="store-stat-value"><g:link controller="product" action="outofstock">${storeStatistics.generalStats.outOfStock}</g:link></span>
			<span class="store-stat-label secondary"><g:message code="out.of.stock"/></span>
		</div>
		
		
		<div class="store-stat">
			<span class="store-stat-value"><g:link controller="catalog" action="list">${storeStatistics.generalStats.catalogs}</g:link></span>
			<span class="store-stat-label secondary"><g:message code="catalogs"/></span>
		</div>
		
		
		<div class="store-stat">
			<span class="store-stat-value"><g:link controller="account" action="admin_list" params="${[admin:false]}">${storeStatistics.generalStats.customers}</g:link></span>
			<span class="store-stat-label secondary"><g:message code="customers"/></span>
		</div>
		
		
		<div class="store-stat">
			<span class="store-stat-value"><g:link controller="shoppingCart" action="active">${storeStatistics.generalStats.abandonedCarts}</g:link></span>
			<span class="store-stat-label secondary"><g:message code="abandoned.carts"/></span>
		</div>
		
		
	</div>
	<!-- end of store-stats -->
	
	
	
	<h1 class="dashboard pull-left"><g:message code="store.overview"/></h1>

	<div id="date-selectors" class="pull-right align-right" style="display:block">
		<g:form controller="admin" action="index" method="get" class="range-form">
			<span class="secondary">
				<g:message code="date.range"/> :&nbsp;
			</span>
			<input type="text" name="startDate" id="start-date" class="form-control" value="${startDate}"/>
			&nbsp;
			<span class="secondary"><g:message code="to"/></span>
			&nbsp;
			<input type="text" name="endDate" id="end-date" class="form-control" value="${endDate}"/>
			<a href="javascript:" class="btn btn-default" id="refresh" title="${message(code:'refresh')}"><span class="glyphicon glyphicon-refresh"></span></a>
			<g:link controller="admin" action="index" params="[ allData : true ]" class="btn btn-default all-data"><g:message code="all.data"/></g:link>
		</g:form>
	</div>
	
	<br class="clear"/>
	
	<div id="refreshable-data">
		
		<div id="sales-stats-container">
			
			<div id="sales-stats" class="pull-left">
				
				<%
				def jsCurrencySymbol = currencySymbol
				def appendSymbol
				if(currencySymbol == "€"){
					appendSymbol = currencySymbol
					currencySymbol = ""
				}
				%>
				
				<div class="sales-stat">
					<span class="stat">
						<span class="sales-stat-dollar secondary">${currencySymbol}</span>
						<g:if test="${storeStatistics.salesStats.sales != "0"}">
							<span class="sales-stat-value">${storeStatistics.salesStats.sales}</span>
							<span class="sales-stat-dollar secondary">${appendSymbol}</span>
						</g:if>
						<g:else>
							<span class="sales-stat-value">--</span>
						</g:else>
						<span class="sales-stat-label hint"><g:message code="total.sales"/></span>
					</span>
				</div>
				
				<div class="sales-stat">
					<span class="stat">
						<g:if test="${storeStatistics.salesStats.orderCount != "0"}">
							<span class="sales-stat-value">${storeStatistics.salesStats.orderCount}</span>
						</g:if>
						<g:else>
							<span class="sales-stat-value">--</span>
						</g:else>
						<span class="sales-stat-label hint"><g:message code="total.orders"/></span>
					</span>
				</div>
				
				
				<div class="sales-stat">
					<span class="stat">
						<span class="sales-stat-dollar secondary">${currencySymbol}</span>
						<g:if test="${storeStatistics.salesStats.averageOrder != "0"}">
							<span class="sales-stat-value">${storeStatistics.salesStats.averageOrder}</span>
							<span class="sales-stat-dollar secondary">${appendSymbol}</span>
						</g:if>
						<g:else>
							<span class="sales-stat-value">--</span>
						</g:else>
						<span class="sales-stat-label hint"><g:message code="average.order"/></span>
					</span>
				</div>
				

				<div class="sales-stat">
					<span class="stat">
						<span class="hint">${storeStatistics.salesStats.checkoutCarts}/${storeStatistics.salesStats.shoppingCarts}</span>
						<span class="sales-stat-value">${storeStatistics.salesStats.checkoutRate}</span>
						<span class="sales-stat-percent secondary">%</span>
						<span class="sales-stat-label hint"><g:message code="checkout.rate"/></span>
					</span>
				</div>
				
			</div>
			
			<div id="sales-chart-container" class="pull-left">
				<g:if test="${storeStatistics.salesStats.chartData.size() > 0}">
					<h3 class="secondary align-left"><g:message code="sales"/></h3>
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
				
				<h4 class="secondary"><g:message code="top.viewed.products"/></h4>
				
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
    	            	
						<g:if test="${storeStatistics.productStats.size() > 5}">
							<div class="view-all-container">
								<a href="javascript:" class="view-all" data-type="products" data-title="${message(code:'top.viewed.products')}"><g:message code="view.all"/></a>
							</div>
						</g:if>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							<g:message code="no.data.available"/>
						</div>
					</g:else>
				</div>
				
			</div>
			
			<div class="activity-stats">
				
				<h4 class="secondary"><g:message code="top.viewed.pages"/></h4>
				
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
    	            	
						<g:if test="${storeStatistics.pageStats?.size() > 5}">
							<div class="view-all-container">
								<a href="javascript:" class="view-all" data-type="pages" data-title="${message(code:'top.viewed.pages')}"><g:message code="view.all"/></a>
							</div>
						</g:if>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							<g:message code="no.data.available"/>
						</div>
					</g:else>
					
				</div>
				
			</div>
			
			<div class="activity-stats last">
				
				<h4 class="secondary"><g:message code="top.search.terms"/></h4>
				
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
    	            	
						<g:if test="${storeStatistics.searchStats?.size() > 5}">
							<div class="view-all-container">
								<a href="javascript:" class="view-all" data-type="searches" data-title="${message(code:'top.search.terms')}"><g:message code="view.all"/></a>
							</div>
						</g:if>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							<g:message code="no.data.available"/>
						</div>
					</g:else>
					
				</div>
				
			</div>
		
			<br class="clear"/>
			
			<div class="activity-stats">
				
				<h4 class="secondary"><g:message code="top.viewed.catalogs"/></h4>
				
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
						
						<g:if test="${storeStatistics.catalogStats?.size() > 5}">
							<div class="view-all-container">
								<a href="javascript:" class="view-all" data-type="catalogs" data-title="${message(code:'top.viewed.catalogs')}"><g:message code="view.all"/></a>
							</div>
						</g:if>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							<g:message code="no.data.available"/>
						</div>
					</g:else>
			
				</div>
				
			</div>
			
			
			
			<div id="google-analytics-tip">
				<blockquote class="secondary">
					<g:message code="greenfield.google.analytics"/>
				
				
					<a href="http://www.google.com/analytics/" target="_blank"><g:message code="visit.google.analytics"/></a>
				</blockquote>
			
				<br class="clear"/>
			</div>
			
			
			
		</div>
		<!-- end of activity stats container -->
		
		
	</div>
	
	
	<div id="calculating" style="margin-top:100px;display:none;text-align:center">
		<span class="secondary"><g:message code="calculating.please.wait"/>&nbsp;&nbsp;</span>
		<img src="/${applicationService.getContextName()}/images/loading.gif" >
	</div>
	
	<input type="hidden" id="startDateHidden" value="${startDate}"/>
	<input type="hidden" id="endDateHidden" value="${endDate}"/>
	
	
<script type="text/template" id="modal-template">
{{#rows}}
	<div class="activity-stat-row">
		<span class="activity-stat-title">{{title}}</span>
		<span class="activity-stat-value">{{value}}</span>
		<br class="clear"/>
	</div>
{{/rows}}
</script>
	
	
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
		
		var baseUrl = "/${applicationService.getContextName()}/admin/data?"
		
		var MODAL_TEMPLATE = $('#modal-template').text();
		
		var $viewAllBtn = $('.view-all');
		
		
		var $modal = $('#modal'),
			$backdrop = $('#backdrop'),
			$activityStatsTitle = $('#activity-stats-title'),
			$activityStatsContent = $('#activity-stats-content'),
			$activityStatsJscroll = $activityStatsContent.jScrollPane().data('jsp'),
			$activityStatsJscrollPane = $activityStatsJscroll.getContentPane();
			$closeActivityStats = $('#close-activity-stats'),
			$hiddenStartDate = $('#startDateHidden'),//prevent user from changing dates in input box
			$hiddenEndDate = $('#endDateHidden');
		
		
		$viewAllBtn.click(retreiveAllActivityData);
		$backdrop.click(hideModal);
		$closeActivityStats.click(hideModal);
		setupHoverEvents();
		
		
		function retreiveAllActivityData(event){
			var $target = $(event.target);
			var type = $target.data('type');
			var title = $target.data('title');
			var startDate = $hiddenStartDate.val();
			var endDate = $hiddenEndDate.val();
			getData(type, startDate, endDate).then(displayAllData(type, title));
		}
		
		function getData(type, startDate, endDate){
			var typeParam = "type=" + type;
			var dateParams = "&startDate=" + startDate + "&endDate=" + endDate;
			var url = baseUrl + typeParam + dateParams
			return $.ajax({
				url : url,
				type : 'get',
				dataType : 'json',
				contentType : 'application/json'
			})
		}
		
		function displayAllData(type, title){
			return function(response){
				$activityStatsJscrollPane.empty();
				$activityStatsTitle.html(title);
				
				var html = Mustache.to_html(MODAL_TEMPLATE, { rows : response.data.rows });
				$activityStatsJscrollPane.append(html);
				reinitializeDetailsJscrollPane();
				
				showModal();
			}
		}
				
		
		
		function reinitializeDetailsJscrollPane(){
			$activityStatsJscroll.reinitialise();
			$activityStatsContent.find('.jspVerticalBar').stop(true,true).animate({"opacity" : "0.4"}, 200);
		};
		
	
		function setupHoverEvents(){
			$activityStatsContent.hover(function(){
				$activityStatsContent.find('.jspVerticalBar').stop(true,true).animate({"opacity" : "0.9"}, 200);
			},
			function(){
				$activityStatsContent.find('.jspVerticalBar').stop(true,true).animate({"opacity" : "0.4"}, 200);
			});
		};
		

		
		
		var $chartDiv = $('#sales-chart'),
			$noChartDataImg = $('#no-chart-data'),
			$refreshDataDiv = $('#refreshable-data'),
			$calculatingDiv = $('#calculating'),
			$form = $('.range-form');
		
		var $refreshBtn = $('#refresh'),
			$allDataBtn = $('.all-data');
			
		$refreshBtn.click(refreshData);
		$allDataBtn.click(hideDashboard);
		
		function hideDashboard(){
			$refreshDataDiv.fadeOut(50);
			$calculatingDiv.fadeIn(50);
		}
		
		function refreshData(){
			$refreshDataDiv.fadeOut(50);
			$calculatingDiv.fadeIn(50);
			$form.submit();
		}
		
		var $startDate = $('#start-date'),
			$endDate = $('#end-date');
		
		//TODO:fix	
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
		
		if(salesData.length > 0){
			var chart = new Dygraph( $chartDiv.get(0), salesData, options );
		}
		
		
		
		function showModal(){
			$backdrop.animate({
				"opacity" : 1,
				"z-index" : 1000
			}, 150, function(){
				$modal.animate({
					"opacity" : 1,
					"z-index" : 1010
				}, 50, function(){
				});
			});	
		};
				
		
		function hideModal(){
			$modal.animate({
				"opacity" : 0,
				"z-index" : -1
			}, 0, function(){
				$backdrop.animate({
					"opacity" : 0,
					"z-index" : -2
				}, 0, function(){
				});	
			}); 
		};
		
		
		function numberWithCommas(x) {
    		return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
		
		var appendSymbol = "${appendSymbol}"
		var currencySymbol = "${jsCurrencySymbol}";
		$('.dygraph-axis-label-y').each(function(index){
			$(this).html($(this).html().replace(/\$/g, ''))
			if(appendSymbol != ""){
				console.log(appendSymbol)
				$(this).append(appendSymbol)
			}else{
				$(this).prepend(currencySymbol)
			}
			
		})
		
	});
	
	
</script>		


	
</body>
</html>
