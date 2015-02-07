<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
</head>
<body>

	<h2>Store Settings</h2>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="active"><g:link uri="/configuration/settings" class="btn btn-default">Store Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/email_settings" class="btn btn-default">Email Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/stripe_settings" class="btn btn-default">Stripe/Payment Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
	</ul>
	
	
	<style type="text/css">
		.form-group .form-control{
			display:inline-block;
			width:300px;
		}
	</style>
	
	<form action="save_settings" class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label">Company Name</label>
			<input type="text" class="form-control" name="storeName" value="${settings?.storeName}"/>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">Tax Rate % <br/><em>example : 7.5% enter 7.5</em></label>
			<input type="text" class="form-control" name="taxRate" value="${settings?.taxRate}" style="width:100px;"/>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">Shipping $</label>
			<input type="text" class="form-control" name="shipping" value="${settings?.shipping}" style="width:100px;"/>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">Meta Keywords</label>
			<input type="text" class="form-control" name="keywords" value="${settings?.keywords}"/>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">Meta Description</label>
			<textarea class="form-control" name="description" >${settings?.description}</textarea>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">Google Analytics</label>
			<input type="text" class="form-control" name="googleAnalytics" value="${settings?.googleAnalytics}"/>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">&nbsp;</label>
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
		</div>
		
	</form>
</body>
</html>