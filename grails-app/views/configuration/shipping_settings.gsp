<%@ page import="org.greenfield.State" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Shipping Settings</title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />
</head>

<body>

	<h1>Shipping Settings</h1>
	
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
		<li class="active"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
	</ul>
	
	
	<style type="text/css">
		.form-group .form-control{
			display:inline-block;
			width:350px;
		}
	</style>
	
	
	<form action="save_shipping_settings" class="form-horizontal">
		
		<h3>Store Address</h3>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Address 1</label>
			<input type="text" class="form-control" name="address1" value="${shipping_settings?.address1}" style="width:275px;"/>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Address 2</label>
			<input type="text" class="form-control" name="address2" value="${shipping_settings?.address2}" style="width:275px;"/>
		</div>
		
		
		<div class="form-group">
			<label class="col-sm-3 control-label">City</label>
			<input type="text" class="form-control" name="city" value="${shipping_settings?.city}" style="width:275px;"/>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">State</label>
			<g:select name="state"
			          from="${State.list()}"
			          value="${shipping_settings?.state}"
			          optionKey="id" 
					  optionValue="name"
					  id="shipState"
					  class="form-control"
					  style="width:175px;"/>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Zip</label>
			<input type="text" class="form-control" name="zip" value="${shipping_settings?.zip}" style="width:175px;"/>
		</div>
		
		
		
		<h3>Rate Settings</h3>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Flat Rate Shipping $
				<p style="color:#777; font-style:italic;">Used when EasyPost is not enabled.</p>
			</label>
			<input type="text" class="form-control" name="shipping" value="${shipping_settings?.shipping}" style="width:100px;"/>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">Enable <strong>EasyPost</strong> Integration</label>
			<input type="checkbox" ${shipping_settings.easypostEnabled} name="easypostEnabled" id="easypostEnabled"/>
		</div>
		
		<p><strong>EasyPost</strong> is allows you to integrate USPS, UPS, DHL and FedEx into Greenfield for realtime shipping calculations.  <a href="http://easypost.com" target="_blank">EasyPost's Website</a>.</p>
		
		


		<br/>
		<div id="easypostSettings">
			<h3>Easy Post Settings</h3>
			
			<div class="form-group">
				<label class="col-sm-3 control-label">Test Secret API Key</label>
				<input type="text" class="form-control" name="testApiKey" value="${shipping_settings.testApiKey}"/>
			</div>
    		
			<div class="form-group">
				<label class="col-sm-3 control-label">Live Secret API Key</label>
				<input type="text" class="form-control" name="liveApiKey" value="${shipping_settings.liveApiKey}"/>
			</div>		
				
			<br/>
		</div>
		
			
		<div class="form-group">
			<label class="col-sm-3 control-label">&nbsp;</label>
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
		</div>
		
	</form>
	
	
	<script type="text/javascript">
		$(document).ready(function(){
			var $easypost = $('#easypostEnabled');
			var $easypostSettings = $('#easypostSettings');
			
			$easypost.click(toggleEasyPost);
			
			function toggleEasyPost(){
				$easypostSettings.toggle();
			}
			
			if(!$easypost.prop('checked')){
				$easypostSettings.hide();
			}
			
		});
	</script>
	
</body>
</html>