<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Greenfield : Stripe Settings</title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	
</head>
<body>

	<h2>Stripe Settings</h2>
	

	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="inactive"><g:link uri="/configuration/settings" class="btn btn-default">Store Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/email_settings" class="btn btn-default">Email Settings</g:link></li>
		<li class="active"><g:link uri="/configuration/stripe_settings" class="btn btn-default">Stripe/Payment Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
	</ul>
	
	
	
	<form action="save_stripe_settings" class="form-horizontal">

	
		<p class="information secondary" style="margin-top:0px !important; margin-bottom:20px;">Stripe is a developer-friendly way to accept payments online.  You will need a Stripe account to accept payments with Greenfield. <a href="http://stripe.com" target="_blank">Stripe's Website</a>.</p>
		

		<h4>Development Settings</h4>
	
		<div class="form-row">
			<span class="form-label twohundred">Development API Key/Secret Key</span>
			<span class="input-container">
				<input type="text" class="form-control threehundred" name="devApiKey" value="${stripe_settings.devApiKey}"/>
			</span>
			<br class="clear"/>
		</div>

		<div class="form-row">
			<span class="form-label twohundred">Development Publishable Key</span>
			<span class="input-container">
				<input type="text" class="form-control threehundred" name="devPublishableKey" value="${stripe_settings.devPublishableKey}"/>
			</span>
			<br class="clear"/>
		</div>


		<h4>Live/Production Settings</h4>
		
		<div class="form-row">
			<span class="form-label twohundred">Live API Key/Secret Key</span>
			<span class="input-container">
				<input type="text" class="form-control threehundred" name="prodApiKey" value="${stripe_settings.prodApiKey}"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">Live Publishable Key</span>
			<span class="input-container">
				<input type="text" class="form-control threehundred" name="prodPublishableKey" value="${stripe_settings.prodPublishableKey}"/>
			</span>
			<br class="clear"/>
		</div>		
		
		
		
		<div class="buttons-container">
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
		</div>
		
		
	</form>
</body>
</html>