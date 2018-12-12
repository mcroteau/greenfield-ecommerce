<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Greenfield : Email Settings</title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
</head>
<body>

	<h2>Email Settings</h2>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="inactive"><g:link uri="/configuration/settings" class="btn btn-default">Store Settings</g:link></li>
		<li class="active"><g:link uri="/configuration/email_settings" class="btn btn-default">Email Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/payment_settings" class="btn btn-default">Payment Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
	</ul>
	
	
	<form action="save_email_settings" class="form-horizontal">
	
		<div class="form-row">
			<span class="form-label twohundred">Username</span>
			<span class="input-container">
				<input type="text" class="form-control" name="username" value="${email_settings.username}" style="width:300px"/>
			</span>
			<br class="clear"/>
		</div>

		<div class="form-row">
			<span class="form-label twohundred">Password</span>
			<span class="input-container">
				<input type="password" class="form-control" name="password" value="${email_settings.password}" style="width:300px"/>
			</span>
			<br class="clear"/>
		</div>

		<div class="form-row">
			<span class="form-label twohundred">Host</span>
			<span class="input-container">
				<input type="text" class="form-control" name="host" value="${email_settings.host}" style="width:300px"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">Port</span>
			<span class="input-container">
				<input type="text" class="form-control" name="port" value="${email_settings.port}" style="width:100px"/>
			</span>
			<br class="clear"/>
		</div>		
		
		
		<div class="form-row">
			<span class="form-label twohundred">SMTP Auth</span>
			<span class="input-container">
				<input type="checkbox" ${email_settings.auth} name="auth"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">STARTTLS Enabled</span>
			<span class="input-container">
				<input type="checkbox" ${email_settings.startTls} name="startTls"/>
			</span>
			<br class="clear"/>
		</div>
		
	
		<div class="form-row">
			<span class="form-label twohundred">Admin/Notification Email Addresses
				<p class="information secondary">Comma separated list of emails that will receive notification emails for registrations, orders & other updates</p>
			</span>
			<span class="input-container">
				<textarea class="form-control threefifty" style="height:100px;" name="adminEmail">${email_settings.adminEmail}</textarea>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">Support Email Address
				<p class="information secondary">The "From" address on all outgoing emails</p>
			</span>
			<span class="input-container">
				<input type="text" class="form-control twohundred" name="supportEmail" value="${email_settings.supportEmail}"/>
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