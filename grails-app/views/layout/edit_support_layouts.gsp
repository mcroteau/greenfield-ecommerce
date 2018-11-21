
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	
	<title>Greenfield : Shipping Settings</title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
</head>

<body>

	<div class="form-group" style="margin-top:30px">
		<g:link class="btn btn-default pull-right" controller="layout" action="create">New Layout</g:link>
		<g:link controller="layout" action="index" class="btn btn-default pull-right" style="display:inline-block;margin-right:10px;">Store Layouts</g:link>
	</div>

	<h2>Store Screen Layouts</h2>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	<g:if test="${flash.error}">
		<div class="alert alert-danger" role="status">${flash.error}</div>
	</g:if>
	
	
	<form action="save_support_layouts" class="form-horizontal" method="post">
		
		<p>This is where pages like the login page, check out screens, registration screen layouts are set. The only screen not set here is search results. Search results uses the default store layout</p>
		
		<p>Change layouts in <g:link controller="catalog" action="list">Catalogs</g:link>,
			&nbsp;<g:link controller="product" action="list">Products</g:link>, <g:link controller="page" action="list">Pages</g:link>
			
			<!--
			checkout preview
			checkout screen
			checkout success
			registration
			login
			-->
			

			
		<div class="form-row">
			<span class="form-label twohundred">Registration Screens</span>
			<span class="input-container">
				<g:select name="registration_screen"
			          from="${layouts}"
			          value="${registration_screen}"
			          optionKey="id" 
					  optionValue="name"
					  id="shipState"
					  class="form-control"
					  style="width:175px;"/>
			</span>
			<br class="clear"/>
		</div>
			
			
		<div class="form-row">
			<span class="form-label twohundred">Checkout Preview</span>
			<span class="input-container">
				<g:select name="checkout_preview"
			          from="${layouts}"
			          value="${checkout_preview}"
			          optionKey="id" 
					  optionValue="name"
					  id="shipState"
					  class="form-control"
					  style="width:175px;"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">Checkout Screen</span>
			<span class="input-container">
				<g:select name="checkout_screen"
			          from="${layouts}"
			          value="${checkout_screen}"
			          optionKey="id" 
					  optionValue="name"
					  id="shipState"
					  class="form-control"
					  style="width:175px;"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">Checkout Success</span>
			<span class="input-container">
				<g:select name="checkout_success"
			          from="${layouts}"
			          value="${checkout_success}"
			          optionKey="id" 
					  optionValue="name"
					  id="shipState"
					  class="form-control"
					  style="width:175px;"/>
			</span>
			<br class="clear"/>
		</div>
		
		
		



			
		<div class="buttons-container">
			<span class="form-label twohundred">&nbsp;</span>
			<g:link controller="layout" action="index" class="btn btn-default">Store Layouts</g:link>
			<g:submitButton value="Save Layouts" name="submit" class="btn btn-primary"/>
		</div>
		
	</form>
	

	
</body>
</html>