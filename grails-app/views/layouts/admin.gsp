<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
	<title><g:layoutTitle default="Greenfield : Administration" /></title>


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

	
	.table{
		border-collapse:collapse !important;
	}
	
</style>	

</head>
<body>
	
	<div id="greenfield-header"></div>
	
	<div id="outer-container">
		
		<div id="admin-nav-container">
	
			<div id="admin-marker"></div>
			
			<ul id="admin-nav">
				<li><g:link uri="/admin" class="${dashboardActive}">Dashboard</g:link></li>
				<li><g:link uri="/product/list" class="${productsActive}">Products</g:link></li>
				<li><g:link uri="/catalog/list" class="${catalogsActive}">Catalogs</g:link></li>
				<li><g:link uri="/transaction/list" class="${ordersActive}">Orders</g:link></li>
				<li><g:link uri="/page/list" class="${pagesActive}">Pages</g:link></li>
				<li><g:link uri="/account/admin_list?admin=false" class="${accountsActive}">Accounts</g:link></li>
				<li><g:link uri="/configuration/index" class="${importActive}">Import/Export</g:link></li>
				<li><g:link uri="/layout/index" class="${layoutActive}">Store Layouts</g:link></li>
				<li><g:link uri="/configuration/settings" class="${settingsActive}">Settings</g:link></li>
			</ul>
			
		</div>
		
		<div id="content-container">
			
			<div id="header">
				<span class="header-info pull-left align-left">Welcome Back <strong><sec:username/></strong>!</span>
				
				<span class="header-info pull-right align-right">
					<g:link uri="/">Store Front</g:link>
					&nbsp;|&nbsp;
					<g:link controller="logout" action="index">Logout</g:link>
				</span>
					
				<br class="clear"/>
			</div>
			
			
			<div id="content">
				
				<g:layoutBody/>
				
			</div>
			<!-- end of content -->
			
			
			
		</div>
		<!-- end of content-container -->
		
		
		<br class="clear"/>
		
	</div>


</body>
</html>
