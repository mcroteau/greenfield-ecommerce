<%@ page import="grails.util.Environment" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
	<title><g:layoutTitle default="Greenfield : ${message(code: "administration")}" /></title>


	<link rel="stylesheet" href="${resource(dir:'bootstrap/3.1.1/css', file:'bootstrap.min.css')}" />
	<script type="text/javascript" src="${resource(dir:'js/lib/jquery/1.11.0/jquery.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'bootstrap/3.1.1/js/bootstrap.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/datepicker/datepicker.js')}"></script>
	
	<script type="text/javascript" src="${resource(dir:'js/lib/datepicker/bootstrap-datepicker.js')}"></script>
	<link rel="stylesheet" href="${resource(dir:'js/lib/datepicker', file:'datepicker.css')}" />
	
	<script type="text/javascript" src="${resource(dir:'js/lib/dygraphs/1.1.0/dygraph-combined.min.js')}"></script>
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />

	
	<g:layoutHead/>
	
	
<style type="text/css">	
	
	html,body{
		background:#e9f6f2;
		background:#f0faf7;
	}
		
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

	#developer-mode{
		/**
		position:absolute; 
		top:1px; 
		left:13px;**/ 
		width:962px;
		line-height:1.0;
		margin:0px 0px 0px 13px; 
		padding:7px 0px 7px 0px; 
		color:#97c4b6;
		font-size:13px;
		background:#fff !important;
		border:solid 1px #bcddd3 !important;
		-webkit-border-radius: 0px;
		-moz-border-radius: 0px;
		border-radius: 0px;
	}

	#seeking-developers-container{
		width:200px;
		left:1000px;
		position:absolute;
	}
	
	#seeking-developers{
	}

	.table{
		border-collapse:collapse !important;
	}
	
</style>	

</head>
<body>
	
	<div id="seeking-developers-container" class="alert alert-info">
		<g:message code="interested.contributing"/>
	</div>
	
	<div id="greenfield-header"></div>
	
	<div id="outer-container" style="position:relative">
		
		<div id="admin-nav-container">
	
			<div id="admin-marker"></div>
			
			<ul id="admin-nav">
				<li><g:link uri="/admin" class="${dashboardActive}"><g:message code="dashboard" /></g:link></li>
				<li><g:link uri="/product/list" class="${productsActive}"><g:message code="products" /></g:link></li>
				<li><g:link uri="/catalog/list" class="${catalogsActive}"><g:message code="catalogs" /></g:link></li>
				<li><g:link uri="/transaction/list" class="${ordersActive}"><g:message code="orders" /></g:link></li>
				<li><g:link uri="/page/list" class="${pagesActive}"><g:message code="pages" /></g:link></li>
				<li><g:link uri="/account/admin_list?admin=false" class="${accountsActive}"><g:message code="accounts" /></g:link></li>
				<li><g:link uri="/configuration/index" class="${importActive}"><g:message code="import.export" /></g:link></li>
				<li><g:link uri="/layout/index" class="${layoutActive}"><g:message code="store.layouts" /></g:link></li>
				<li><g:link uri="/configuration/settings" class="${settingsActive}"><g:message code="settings" /></g:link></li>
			</ul>
				
		</div>
		
		
		
		<div id="content-container">
			
			<!--<img src="${resource(dir:'images/app/mgi-emblem.png')}" style="height:27px; width:34px; position:absolute; top:5px; left:753px;"/>-->
			
			<a href="http://www.mgidatasource.com" target="_blank" id="mgi-link"><img src="${resource(dir:'images/app/mgi-emblem.png')}" style="height:inherit;width:inherit;outline:none;"/></a>
			
			
			<div id="header">
				<span class="header-info pull-left align-left"><g:message code="welcome.back"/> <strong><sec:username/></strong>!</span>
				
				<span class="header-info pull-right align-right">
					<g:link uri="/" target="_blank"><g:message code="store.front"/></g:link>
					&nbsp;|&nbsp;
					<g:link controller="logout" action="index"><g:message code="logout"/></g:link>
				</span>
					
				<br class="clear"/>
			</div>
			
			
			<div id="content">
				
				<g:layoutBody/>
				
			</div>
			<!-- end of content -->
			
			
		
			<a href="http://www.mgidatasource.com" target="_blank" style="position:absolute; left:43px; bottom:0px; display:block; height:33px; width:27px;"><img src="${resource(dir:'images/app/mgi-emblem-bottom.png')}" style="height:inherit;width:inherit;outline:none;"/></a>
		
			
			
		</div>
		<!-- end of content-container -->
		
		
		<br class="clear"/>
	
	
	</div>
	
	<div id="third-party-integrations" style="position:relative; text-align:left;">
		<a href="http://www.stripe.com" target="_blank" style="display:inline-block;width:100px;margin-left:334px"><img src="${resource(dir:'images/app/stripe-logo.png')}" style="width:inherit;"/></a>
		<a href="http://www.braintreepayments.com" target="_blank" style="display:inline-block;width:100px;margin:0px 50px 0px 50px"><img src="${resource(dir:'images/app/braintree-logo.png')}" style="width:inherit;"/></a>
		<a href="http://www.easypost.com" target="_blank" style="display:inline-block;width:100px;"><img src="${resource(dir:'images/app/easypost-logo.png')}" style="width:inherit;"/></a>
	</div>
	
	<div id="bottom-padding"></div>
	
	
	<style type="text/css">
		#q{
			z-index:1000;
			opacity:1;
			background:#F0FAF7;
			position:absolute;
			top:0px;
			bottom:0px;
			left:0px;
			right:0px;
			color:#222;
			font-size:332px;
		}
		#third-party-integrations img{
			
		}
	</style>
	
	<!--<div id="q">Q</div>-->
	
	<script>
		//$("#q").fadeOut( 3000, function(){});
	</script>
	
</body>
</html>
