<%@ page import="org.greenfield.State" %>
<% def currencyService = grailsApplication.classLoader.loadClass('org.greenfield.CurrencyService').newInstance()%>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


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
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	
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
		<li class="active"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
	</ul>
	
	
	
	<form action="save_shipping_settings" class="form-horizontal" method="post">
		
		<p class="information secondary">Address below is used when EasyPost or another shipping api is enabled for realtime shipping calculations and printing shipping labels.</p>
		
		<h4>Store Address</h4>
		
		<div class="form-row">
			<span class="form-label twohundred">Address 1</span>
			<span class="input-container">
				<input type="text" class="form-control" name="address1" value="${shipping_settings?.address1}" style="width:275px;"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">Address 2</span>
			<span class="input-container">
				<input type="text" class="form-control" name="address2" value="${shipping_settings?.address2}" style="width:275px;"/>
			</span>
			<br class="clear"/>
		</div>
		
		
		<div class="form-row">
			<span class="form-label twohundred">City</span>
			<span class="input-container">
				<input type="text" class="form-control" name="city" value="${shipping_settings?.city}" style="width:275px;"/>
			</span>
			<br class="clear"/>
		</div>

		
		<div class="form-row">
		  	<label class="form-label twohundred">Country</label>
			<span class="input-container">
				<g:select name="country"
						from="${countries}"
						value="${shipping_settings?.country}"
						optionKey="id" 
						optionValue="name"
					    class="form-control"
						id="countrySelect"/>
			</span>
			<br class="clear"/>
		</div>
		
		
		<div class="form-row">
			<span class="form-label twohundred">State</span>
			<span class="input-container">
				<g:select name="state"
			          from="${State.list()}"
			          value="${shipping_settings?.state}"
			          optionKey="id" 
					  optionValue="name"
					  id="stateSelect"
					  class="form-control"
					  style="width:175px;"/>
			</span>
			<br class="clear"/>
		</div>
		
		
		<div class="form-row">
			<span class="form-label twohundred">Zip</span>
			<span class="input-container">
				<input type="text" class="form-control" name="zip" value="${shipping_settings?.zip}" style="width:175px;"/>
			</span>
			<br class="clear"/>
		</div>

		
			
		<div class="form-row">
			<span class="form-label twohundred">Phone
				<br/>
				<span class="information secondary">Same in Store Settings</span>
			</span>
			<span class="input-container">
				<input type="text" class="form-control" name="storePhone" value="${shipping_settings?.storePhone}" style="width:273px;"/>
			</span>
			<br class="clear"/>
		</div>
		
		<br/>
		<h4>Rate Settings</h4>
		
		<div class="form-row">
			<span class="form-label twohundred">Flat Rate Shipping ${currencyService.getCurrencySymbol()}
				<p class="information secondary">Used when EasyPost is not enabled.</p>
			</span>
			<span class="input-container">
				<input type="text" class="form-control" name="shipping" value="${shipping_settings?.shipping}" style="width:100px;"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">Enable <strong>EasyPost</strong></span>
			<span class="input-container">
				<input type="checkbox" ${shipping_settings.easypostEnabled} name="easypostEnabled" id="easypostEnabled"/>
			</span>
			<br class="clear"/>
		</div>
		
		<p class="information secondary"><strong>EasyPost</strong> integrates USPS, UPS, DHL and FedEx with Greenfield for realtime shipping calculations and shipping labels.  You will need an account with EasyPost to take advantage of these features.  <a href="http://easypost.com" target="_blank">EasyPost Website</a>.</p>
		
		


		<br/>
		<div id="easypostSettings">
			<h4>Easy Post Settings</h4>
			
			<div class="form-row">
				<span class="form-label twohundred">Test Secret API Key</span>
				<span class="input-container">
					<input type="text" class="form-control" name="testApiKey" value="${shipping_settings.testApiKey}"/>
				</span>
				<br class="clear"/>
			</div>
    		
			<div class="form-row">
				<span class="form-label twohundred">Live Secret API Key</span>
				<span class="input-container">
					<input type="text" class="form-control" name="liveApiKey" value="${shipping_settings.liveApiKey}"/>
				</span>
				<br class="clear"/>
			</div>		
				
			<br/>
		</div>
		
			
		<div class="buttons-container">
			<span class="form-label twohundred">&nbsp;</span>
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
		</div>
		
	</form>
	

	<script type="text/javascript" src="${resource(dir:'js/country_states.js')}"></script>
	
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
			
				
			countryStatesInit("${applicationService.getContextName()}", ${shipping_settings?.state})
			
		});
	</script>
	
</body>
</html>