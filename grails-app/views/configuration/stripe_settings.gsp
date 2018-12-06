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

	
		<p class="information secondary" style="margin-top:0px !important; margin-bottom:20px;">Stripe is a developer-friendly way to accept payments online.  You will need a Stripe account to accept payments with Greenfield. <a href="http://stripe.com" target="_blank">Visit Stripe</a>.</p>
		

		<div class="form-row">
			<div class="form-label twohundred pull-left" style="display:inline-block">Base Currency</div>
			
			<%
			def usd = stripe_settings?.storeCurrency == "USD" ? "selected" : ""
			def gbp = stripe_settings?.storeCurrency == "GBP" ? "selected" : ""
			def eur = stripe_settings?.storeCurrency == "EUR" ? "selected" : ""
			def brl = stripe_settings?.storeCurrency == "BRL" ? "selected" : ""
			def inr = stripe_settings?.storeCurrency == "INR" ? "selected" : ""
			def hkd = stripe_settings?.storeCurrency == "HKD" ? "selected" : ""
			%>
			
			<div class="input-container pull-left" style="width:500px;">
				<select name="storeCurrency" class="form-control" style="width:323px;" id="currencySelect">
					<option value="USD" <%=usd%>>$&nbsp;&nbsp;USD - US Dollar</option>
					<option value="GBP" <%=gbp%>>£&nbsp;&nbsp;GBP - United Kingdom</option>
					<option value="EUR" <%=eur%>>€&nbsp;&nbsp;EUR - Germany, France, Netherlands</option>
					<option value="HKD" <%=hkd%>>HK$ HKD - Hong Kong</option>
					<option value="BRL" <%=brl%>>(R$&nbsp;&nbsp;BRL - Brazil: Stripe by invite only)</option>
					<option value="INR" <%=inr%>>(₹ INR - India: Stripe by invite only)</option>
				</select>
				<br/>
				
				<br/>
				<p class="information secondary">Greenfield is configured to handle the country currencies listed.<br/> You can add more by editing CountryStatesHelper, the CurrencyService and this view</p>
				
				<p class="information secondary">Stripe is currently supported in 26 countries.</p>
				
				<p class="information secondary">The currency and country you select should match the country in which your business is located and used when setting up your Stripe account unless you are setting up a site in another country</p>
				
				<p class="information">
					<a href="https://stripe.com/br/pricing" target="_blank">Stripe: Request an Invite (Brazil)</a><br/>
					<a href="https://stripe.com/global#signup-form" target="_blank">Stripe: Request an Invite (India)</a><br/>
					<!--<a href="https://stripe.com/global#signup-form" target="_blank">Request an Invite (Mexico)</a>-->
				</p>

				<p class="information secondary">No matter what country you’re based in, you can use 
					<a href="https://stripe.com/atlas" target="_blank">Atlas</a> to easily incorporate a U.S. company, set up a U.S. bank account, and start accepting payments with Stripe. <a href="https://stripe.com/atlas" target="_blank">Request an invite</a></p>
		
				<p class="information secondary">Greenfield, DataTundra, MGi Data Source or Mike Croteau are not liable for your configuration.</p>
			</div>
			<br class="clear"/>			
		</div>
		<div class="form-row">
			<div class="form-label twohundred pull-left" style="display:inline-block">Store Country</div>
		
			<div class="input-container pull-left" style="width:500px;">
				<select name="storeCountryCode" class="form-control" style="width:230px;" id="countrySelect">
				</select>
				<br/>
				<br/>
				<p class="information secondary">Store country must match the currency selected. Used for shipping international shipping calculations</p>
			</div>
			<br class="clear"/>
		</div>
		

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
	
	<script type="text/javascript">
		$(document).ready(function(){
			var countryCode = "${stripe_settings?.storeCountryCode}"
			//console.log("here...", countryCode)
			
			var countriesCurrenciesMap = {
				"USD" : [{ "code": "us", "name" : "United States" }],
				"GBP" : [{ "code": "gb", "name" : "United Kingdom" }],
				"EUR" : [
						{ "code": "de", "name" : "Germany" },
						{ "code": "fr", "name" : "France" },
						{ "code": "nl", "name" : "Netherlands" },
				],
				"BRL" : [{ "code": "br", "name" : "Brazil" }],
				"INR" : [{ "code": "in", "name" : "India" }],
				"HKD" : [{ "code": "hk", "name" : "Hong Kong" }]
			}
			
			var countryCodeMap = {
				"us" : { "code": "us", "name" : "United States" },
				"gb" : { "code": "gb", "name" : "United Kingdom" },
				"de" : { "code": "de", "name" : "Germany" },
				"fr" : { "code": "fr", "name" : "France" },
				"nl" : { "code": "nl", "name" : "Netherlands" },
				"br" : { "code": "br", "name" : "Brazil" },
				"in" : { "code": "in", "name" : "India" },
				"hk" : { "code": "hk", "name" : "Hong Kong" }
			}
			
			var $countrySelect = $("#countrySelect"),
				$currencySelect = $("#currencySelect")
			
			$currencySelect.change(updateCountrySelect)
			
			function updateCountrySelect(event){
				$countrySelect.find("option").remove()
				
				var currency = $currencySelect.val()
				//console.log(currency)
				var countries = countriesCurrenciesMap[currency]
				
				$(countries).each(function(o, q){
					//console.log(o, q)
					$countrySelect.append("<option value=\"" + q.code + "\">" + q.name + "</option>");
				})
			}
			
			function setCountryCode(){
				//console.log(countryCode)
				var selected = countryCodeMap[countryCode]
				$countrySelect.append("<option value=\"" + selected.code + "\">" + selected.name + "</option>");
			}
			
			setCountryCode();
			
		})
	</script>
		
</body>
</html>