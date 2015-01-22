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

	<h1>Email Settings</h1>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="inactive"><g:link uri="/configuration/settings" class="btn btn-default">Store Settings</g:link></li>
		<li class="active"><g:link uri="/configuration/email_settings" class="btn btn-default">Email Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/stripe_settings" class="btn btn-default">Stripe/Payment Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
	</ul>
	
	
	<form action="save_email_settings" class="form-horizontal">
	
		<div class="form-group">
			<label class="col-sm-3 control-label">Username</label>
			<input type="text" class="form-control" name="username" value="${email_settings.username}" style="width:300px"/>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">Password</label>
			<input type="password" class="form-control" name="password" value="${email_settings.password}" style="width:300px"/>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label">Host</label>
			<input type="text" class="form-control" name="host" value="${email_settings.host}" style="width:300px"/>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Port</label>
			<input type="text" class="form-control" name="port" value="${email_settings.port}" style="width:100px"/>
		</div>		
		
		
		<div class="form-group">
			<label class="col-sm-3 control-label">SMTP Auth</label>
			<input type="checkbox" ${email_settings.auth} name="auth"/>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">STARTTLS Enabled</label>
			<input type="checkbox" ${email_settings.startTls} name="startTls"/>
		</div>
		
	
		<div class="form-group">
			<label class="col-sm-3 control-label">Admin Email Address
			<p style="font-size:11px;font-style:italic">Email addresses to receive notifications when there are new orders and registrations</p>
			</label>
			<input type="text" class="form-control" name="adminEmail" value="${email_settings.adminEmail}"/>
			
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Support Email Address
				<p style="font-size:11px;font-style:italic">The "From" address on all outgoing emails</p>
			</label>
			<input type="text" class="form-control" name="supportEmail" value="${email_settings.supportEmail}"/>
			
		</div>
		
		
		
		<div class="form-group">
			<label  class="col-sm-3 control-label">&nbsp;</label>
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
		</div>
		
		
	</form>
</body>
</html>