<%@ page import="org.greenfield.State" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Greenfield : Shipping Settings</title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />
</head>

<body>

	<h2>Shipping Settings</h2>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	<g:if test="${flash.error}">
		<div class="alert alert-danger" role="status">${flash.error}</div>
	</g:if>
	
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="inactive"><g:link uri="/configuration/settings" class="btn btn-default">Store Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/email_settings" class="btn btn-default">Email Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/stripe_settings" class="btn btn-default">Stripe/Payment Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
		<li class="active"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Additional Layouts </g:link></li>
	</ul>
	
	
	
	<form action="save_support_layouts" class="form-horizontal">
		
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
			<span class="form-label twohundred">Login Screen</span>
			<span class="input-container">
				<g:select name="login_screen"
			          from="${State.list()}"
			          value="${shipping_settings?.state}"
			          optionKey="id" 
					  optionValue="name"
					  id="shipState"
					  class="form-control"
					  style="width:175px;"/>
			</span>
			<br class="clear"/>
		</div>
		
			
		<div class="form-row">
			<span class="form-label twohundred">Registration Screens</span>
			<span class="input-container">
				<g:select name="registration_screen"
			          from="${State.list()}"
			          value="${shipping_settings?.state}"
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
			          from="${State.list()}"
			          value="${shipping_settings?.state}"
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
			          from="${State.list()}"
			          value="${shipping_settings?.state}"
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
			          from="${State.list()}"
			          value="${shipping_settings?.state}"
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
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Layouts" name="submit" class="btn btn-primary"/>
		</div>
		
	</form>
	

	
</body>
</html>