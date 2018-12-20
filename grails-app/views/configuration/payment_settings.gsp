<%@ page import="org.greenfield.Catalog" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Greenfield : Payment Settings</title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
		.payment-configured{
			font-size:19px;
			padding:3px 10px;
			margin:0px 0px 10px 0px;
			display:inline-block;
			background:#efefef;
			border:solid 1px #ddd;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	
</head>
<body>

	<h2>Payment Settings</h2>
	

	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="inactive"><g:link uri="/configuration/settings" class="btn btn-default">Store Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/email_settings" class="btn btn-default">Email Settings</g:link></li>
		<li class="active"><g:link uri="/configuration/payment_settings" class="btn btn-default">Payment Settings</g:link></li>
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default">Shipping Settings</g:link></li>
	</ul>
	
	
	
	<form action="save_payment_settings" class="form-horizontal">

		<%
		def usd = payment_settings?.storeCurrency == "USD" ? "selected" : ""
		def gbp = payment_settings?.storeCurrency == "GBP" ? "selected" : ""
		def nzd = payment_settings?.storeCurrency == "NZD" ? "selected" : ""
		def cad = payment_settings?.storeCurrency == "CAD" ? "selected" : ""
		def eur = payment_settings?.storeCurrency == "EUR" ? "selected" : ""
		def brl = payment_settings?.storeCurrency == "BRL" ? "selected" : ""
		def inr = payment_settings?.storeCurrency == "INR" ? "selected" : ""
		def hkd = payment_settings?.storeCurrency == "HKD" ? "selected" : ""
		%>
	
		<div id="stripePaymentSettings">
	
			<div class="form-row">
				<div class="form-label twohundred pull-left" style="display:inline-block">Current Configuration</div>
				
				<div class="pull-left" style="display:inline-block; width:500px; margin-left:20px;">
					<a href="http://www.stripe.com" alt="Visit Stripe : Payments API Gateway" style="border:none;text-decoration:none"><img src="${resource(dir:'images/app', file:'stripe-logo.png')}" style="height:20%; width:20%;outline:none"/></a>
		
					<p class="information secondary" style="margin-top:0px !important;">Stripe is a developer-friendly way to accept payments online.  To continue, you will need a Stripe account to accept payments with Greenfield. <a href="http://stripe.com" target="_blank">Visit Stripe</a>.</p>
				</div>
				<br class="clear">
			</div>
			
			<div class="form-row">
				<div class="form-label twohundred pull-left" style="display:inline-block">Base Currency</div>
								
				<div class="input-container pull-left" style="width:500px;">
					<select name="storeCurrency" class="form-control" style="width:423px;" id="currencySelectStripe">
						<option value="USD" <%=usd%>>$&nbsp;&nbsp;USD - United States</option>
						<option value="GBP" <%=gbp%>>£&nbsp;&nbsp;GBP - United Kingdom</option>
						<option value="NZD" <%=nzd%>>$&nbsp;&nbsp;NZD - New Zealand</option>
						<option value="CAD" <%=cad%>>C $&nbsp;&nbsp;CAD - Canada</option>
						<option value="EUR" <%=eur%>>€&nbsp;&nbsp;EUR - Germany, France, Netherlands, Ireland</option>
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
					<select name="storeCountryCode" class="form-control" style="width:230px;" id="countrySelectStripe">
					</select>
					<br/>
					<br/>
					<p class="information secondary">Store country must match the currency selected. Used for shipping international shipping calculations</p>
				</div>
				<br class="clear"/>
			</div>
			
    	
			<h4>Development Settings</h4>
		
			<div class="form-row">
				<span class="form-label twohundred">Development Secret Key</span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="stripeDevApiKey" value="${payment_settings.stripeDevApiKey}"/>
				</span>
				<br class="clear"/>
			</div>
    	
			<div class="form-row">
				<span class="form-label twohundred">Development Publishable Key</span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="stripeDevPublishableKey" value="${payment_settings.stripeDevPublishableKey}"/>
				</span>
				<br class="clear"/>
			</div>
    	
    	
			<h4>Live/Production Settings</h4>
			
			<div class="form-row">
				<span class="form-label twohundred">Live Secret Key</span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="stripeProdApiKey" value="${payment_settings.stripeProdApiKey}"/>
				</span>
				<br class="clear"/>
			</div>
			
			<div class="form-row">
				<span class="form-label twohundred">Live Publishable Key</span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="stripeProdPublishableKey" value="${payment_settings.stripeProdPublishableKey}"/>
				</span>
				<br class="clear"/>
			</div>		
			
			
			
			<div class="buttons-container">
				<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
				<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
			</div>
			
		</div>
		
		
		<div id="braintreePaymentSettings">
			
	
			<div class="form-row">
				<div class="form-label twohundred pull-left" style="display:inline-block">Current Configuration</div>
				
				<div class="pull-left" style="display:inline-block; width:500px; margin-left:20px;">
					<a href="http://www.braintreepayments.com" alt="Visit Braintree : Payments API Gateway" style="border:none;text-decoration:none"><img src="${resource(dir:'images/app', file:'braintree-logo.png')}" style="height:30%; width:30%;outline:none"/></a>
		
					<p class="information secondary" style="margin-top:0px !important;">Braintree is a developer-friendly way to accept payments online.  To continue, you will need a Braintree account to accept payments with Greenfield. <a href="http://braintreepayments.com" target="_blank">Visit Braintree</a>.</p>
				</div>
				<br class="clear">
			</div>
			

			
			<div class="form-row">
				<div class="form-label twohundred pull-left" style="display:inline-block">Base Currency</div>

				
				<div class="input-container pull-left" style="width:500px;">
					<select name="storeCurrency" class="form-control" style="width:423px;" id="currencySelectBraintree">
						<option value="USD" <%=usd%>>$&nbsp;&nbsp;USD - United States</option>
						<option value="GBP" <%=gbp%>>£&nbsp;&nbsp;GBP - United Kingdom</option>
						<option value="NZD" <%=nzd%>>$&nbsp;&nbsp;NZD - New Zealand</option>
						<option value="CAD" <%=cad%>>C $&nbsp;&nbsp;CAD - Canada</option>
						<option value="EUR" <%=eur%>>€&nbsp;&nbsp;EUR - Germany, France, Netherlands, Ireland, Greece</option>
						<option value="HKD" <%=hkd%>>HK$ HKD - Hong Kong</option>
						<option value="BRL" <%=brl%>>(R$&nbsp;&nbsp;BRL - Brazil: Stripe by invite only)</option>
						<option value="INR" <%=inr%>>(₹ INR - India: Stripe by invite only)</option>
					</select>
					<br/>
					
					<br/>
					<p class="information secondary">Braintree serves most of Europe. Listed are the countries tested for using shipping apis</p>
					
					<p class="information secondary">The currency and country you select should match the country in which your business is located and used when setting up your Braintree account unless you are setting up a site in another country</p>
					
					<p class="information secondary">Greenfield, DataTundra, MGi Data Source or Mike Croteau are not liable for your configuration.</p>
				</div>
				<br class="clear"/>			
			</div>
			
			
			<div class="form-row">
				<div class="form-label twohundred pull-left" style="display:inline-block">Store Country</div>
			
				<div class="input-container pull-left" style="width:500px;">
					<select name="storeCountryCode" class="form-control" style="width:230px;" id="countrySelectBraintree">
					</select>
					<br/>
					<br/>
					<p class="information secondary">Store country must match the currency selected. Used for shipping international shipping calculations</p>
				</div>
				<br class="clear"/>
			</div>
			


			
			<div class="form-row">
				<span class="form-label twohundred">Merchant Id</span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="braintreeMerchantId" value="${payment_settings.braintreeMerchantId}"/>
				</span>
				<br class="clear"/>
			</div>
			

			<div class="form-row">
				<span class="form-label twohundred">Public Key</span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="braintreePublicKey" value="${payment_settings.braintreePublicKey}"/>
				</span>
				<br class="clear"/>
			</div>
			
			<div class="form-row">
				<span class="form-label twohundred">Private</span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="braintreePrivateKey" value="${payment_settings.braintreePrivateKey}"/>
				</span>
				<br class="clear"/>
			</div>		
			
			
			<div class="buttons-container">
				<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
				<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
			</div>
			
		</div>
		
		<style type="text/css">
			#select-gateway{
				text-align:right;
			}
			#select-gateway a{
				opacity:0.3;
			}
			#select-gateway a:hover{
				opacity:0.92 !important;
				text-decoration:none;
			}

			#select-gateway img{
				outline:none;
			}
		</style>
			
		<div id="select-gateway">
			<g:link action="select_gateway">
				<span id="select-gateway-text">Set Payment Gateway</span>
				<img src="${resource(dir:'images/app', file:'gear.png')}"/>
			</g:link>
		</div>
		
	</form>
	
	<script type="text/javascript">
		
		var currenciesMapStripe = {
			"USD" : [{ "code": "us", "name" : "United States" }],
			"GBP" : [{ "code": "gb", "name" : "United Kingdom" }],
			"NZD" : [{ "code": "nz", "name" : "New Zealand" }],
			"CAD" : [{ "code": "ca", "name" : "Canada" }],
			"EUR" : [
					{ "code": "de", "name" : "Germany" },
					{ "code": "fr", "name" : "France" },
					{ "code": "nl", "name" : "Netherlands" },
					{ "code": "ie", "name" : "Ireland" }
			],
			"BRL" : [{ "code": "br", "name" : "Brazil" }],
			"INR" : [{ "code": "in", "name" : "India" }],
			"HKD" : [{ "code": "hk", "name" : "Hong Kong" }]
		}
		
		var countryMapStripe = {
			"us" : { "code": "us", "name" : "United States" },
			"gb" : { "code": "gb", "name" : "United Kingdom" },
			"nz" : { "code": "nz", "name" : "New Zealand" },
			"ca" : { "code": "ca", "name" : "Canada" },
			"de" : { "code": "de", "name" : "Germany" },
			"fr" : { "code": "fr", "name" : "France" },
			"nl" : { "code": "nl", "name" : "Netherlands" },
			"ie" : { "code": "ie", "name" : "Ireland" },
			"br" : { "code": "br", "name" : "Brazil" },
			"in" : { "code": "in", "name" : "India" },
			"hk" : { "code": "hk", "name" : "Hong Kong" }
		}
		
		
		
		var currenciesMapBraintree = {
			"USD" : [{ "code": "us", "name" : "United States" }],
			"GBP" : [{ "code": "gb", "name" : "United Kingdom" }],
			"NZD" : [{ "code": "nz", "name" : "New Zealand" }],
			"CAD" : [{ "code": "ca", "name" : "Canada" }],
			"EUR" : [
					{ "code": "de", "name" : "Germany" },
					{ "code": "fr", "name" : "France" },
					{ "code": "nl", "name" : "Netherlands" },
					{ "code": "ie", "name" : "Ireland" },
					{ "code": "gr", "name" : "Greece" }
			]
		}
		
		var countryMapBraintree = {
			"us" : { "code": "us", "name" : "United States" },
			"gb" : { "code": "gb", "name" : "United Kingdom" },
			"nz" : { "code": "nz", "name" : "New Zealand" },
			"ca" : { "code": "ca", "name" : "Canada" },
			"de" : { "code": "de", "name" : "Germany" },
			"fr" : { "code": "fr", "name" : "France" },
			"nl" : { "code": "nl", "name" : "Netherlands" },
			"ie" : { "code": "ie", "name" : "Ireland" },
			"gr" : { "code": "gr", "name" : "Greece" }
		}
		
		
		$(document).ready(function(){
			var countryCode = "${payment_settings?.storeCountryCode}"
			//console.log("here...", countryCode)

			var $stripePaymentSettings = $("#stripePaymentSettings");
			var $braintreePaymentSettings = $("#braintreePaymentSettings");

			var $countrySelectStripe = $("#countrySelectStripe"),
				$currencySelectStripe = $("#currencySelectStripe"),
				$countrySelectBraintree = $("#countrySelectBraintree"),
				$currencySelectBraintree = $("#currencySelectBraintree");
			
			$currencySelectStripe.change(updateCountrySelect(currenciesMapStripe, $countrySelectStripe, $currencySelectStripe))
			$currencySelectBraintree.change(updateCountrySelect(currenciesMapBraintree, $countrySelectBraintree, $currencySelectBraintree))
			
			function updateCountrySelect(currenciesMap, $countrySelect, $currencySelect){
				return function(event){
					$countrySelect.find("option").remove()
					
					var currency = $currencySelect.val()
					//console.log(currency)
					var countries = currenciesMap[currency]
					
					$(countries).each(function(o, q){
						//console.log(o, q)
						$countrySelect.append("<option value=\"" + q.code + "\">" + q.name + "</option>");
					})
				}
			}
			
			function setCountryCode($countrySelect, countryCodeMap){
				var selected = countryCodeMap[countryCode]
				$countrySelect.append("<option value=\"" + selected.code + "\">" + selected.name + "</option>");
			}
			
			
			<%if(applicationService.getBraintreeEnabled() == "true"){%>
				setCountryCode($countrySelectBraintree, countryMapBraintree);
				//setCurrencyCode($currencySelectBraintree, currenciesMapBraintree);
				$stripePaymentSettings.hide()
				$braintreePaymentSettings.show()
				$currencySelectStripe.prop("disabled", true)
			<%}else{%>
				setCountryCode($countrySelectStripe, countryMapStripe);
				//setCurrencyCode($currencySelectStripe, currenciesMapStripe);
				$stripePaymentSettings.show()
				$braintreePaymentSettings.hide()
				$currencySelectBraintree.prop("disabled", true)
			<%}%>
		})
	</script>
		
</body>
</html>