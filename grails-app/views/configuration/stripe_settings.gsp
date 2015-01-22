<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Stripe Settings</title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
</head>
<body>

	<h1>Stripe Settings</h1>
	
	<p>Stripe is a developer-friendly way to accept payments online.  You will need a Stripe account to accept payments with Greenfield. <a href="http://stripe.com" target="_blank">Stripe's Website</a>.</p>


	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="inactive"><g:link uri="/configuration/settings" class="btn btn-default">Store Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/email_settings" class="btn btn-default">Email Settings</g:link></li>
		<li class="active"><g:link uri="/configuration/stripe_settings" class="btn btn-default">Stripe/Payment Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
	</ul>
	
	
	<style type="text/css">
		.form-group .form-control{
			display:inline-block;
			width:350px;
		}
	</style>
	
	
	<form action="save_stripe_settings" class="form-horizontal">


		<h3>Development Settings</h3>
	
		<div class="form-group">
			<label class="col-sm-3 control-label">Development API Key/Secret Key</label>
			<input type="text" class="form-control" name="devApiKey" value="${stripe_settings.devApiKey}"/>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">Development Publishable Key</label>
			<input type="text" class="form-control" name="devPublishableKey" value="${stripe_settings.devPublishableKey}"/>
		</div>


		<h3>Live/Production Settings</h3>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Live API Key/Secret Key</label>
			<input type="text" class="form-control" name="prodApiKey" value="${stripe_settings.prodApiKey}"/>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Live Publishable Key</label>
			<input type="text" class="form-control" name="prodPublishableKey" value="${stripe_settings.prodPublishableKey}"/>
		</div>		
		
		
		
		
		
		<div class="form-group">
			<label class="col-sm-3 control-label">&nbsp;</label>
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
		</div>
		
		
	</form>
</body>
</html>