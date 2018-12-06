<%@ page import="org.greenfield.State" %>
<%@ page import="grails.util.Environment" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('org.greenfield.CurrencyService').newInstance()%>	

${raw(applicationService.getScreenHeader("Checkout"))}

	<!--<script src="/${applicationService.getContextName()}/js/lib/stripe/stripe.js"></script>

	<!--
	<script src="https://js.stripe.com/v2/"></script>
	-->
	<script src="https://js.stripe.com/v3/"></script>

	<style type="text/css">
		.form-group label{
			font-weight:normal;
		}
		.form-group .form-control{
			display:inline-block;
			width:200px;
		}
		.form-group label em{
			font-weight:normal;
			font-size:11px;
			color:#777
		}
		#information h3{
			margin:20px 0px 30px 0px;
			padding-bottom:7px;
			border-bottom:dashed 3px #ddd;
		}
	</style>
	
	
	<g:if test="${flash.message}">
		<div class="alert alert-warning" id="">${flash.message}</div>		
	</g:if>
	
	
	
	<g:if test="${shoppingCart}">
		
		
		<table class="table table-bordered">
			<thead>
				<tr style="background:#efefef">
					<th style="text-align:center">ID</th>
					<th>Name</th>
					<th>Price</th>
					<th style="text-align:center">Quantity</th>
					<th style="text-align:right;">Extended Price</th>
				</tr>
			</thead>		
			<tbody>
				<% total = 0 %>
				<g:each in="${shoppingCart?.shoppingCartItems}" status="i" var="item">
					<%
						def optionsTotal = 0
						if(item.shoppingCartItemOptions?.size() > 0){
							item.shoppingCartItemOptions.each(){ option ->
								optionsTotal += option.variant.price
							}	
						}
						def productTotal = item.product.price + optionsTotal
						if(item.product.salesPrice && item.product.salesPrice != 0){
							productTotal = item.product.salesPrice + optionsTotal
						}
						def extendedPrice = productTotal * item.quantity
					%>
					
					<tr>
						<td style="text-align:center">${item.product.id}</td>
						<td>
							<g:link controller="product" action="details" id="${item.product.id}">${item.product.name}</g:link>
							<br/>
							<g:if test="${item.shoppingCartItemOptions?.size() > 0}">
								<div style="font-size:11px; color:#777">
									<strong>options :&nbsp;</strong>
									<g:each in="${item.shoppingCartItemOptions}" var="option">
										<span class="option">${option.variant.name}
											(${currencyService.format(applicationService.formatPrice(option.variant.price))})
										</span>
										<br/>
									</g:each>
								</div>
							</g:if>
						</td>
						<td>
						    ${currencyService.format(applicationService.formatPrice(productTotal))}
						    <g:if test="${item.product.salesPrice}">
						        <span class="regular-price">${currencyService.format(applicationService.formatPrice(item.product.price))}</span>
						    </g:if>
						</td>
						<td style="text-align:center">${item.quantity}</td>
						<td id="extended_price" style="text-align:right;">
							${currencyService.format(applicationService.formatPrice(extendedPrice))}
						</td>
						
					</tr>
				</g:each>
				<tr>
					<td colspan="4" style="text-align:right;">Subtotal</td>
					<td style="text-align:right; ">${currencyService.format(applicationService.formatPrice(shoppingCart.subtotal))}</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align:right; font-size:12px">Taxes (${applicationService.getTaxRate()}%)</td>
					<td style="text-align:right; font-size:12px;">${currencyService.format(applicationService.formatPrice(shoppingCart.taxes))}</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align:right;font-size:12px">
						Shipping
						<g:if test="${shoppingCart.shipmentId != 'BASE'}"> 
							<span style="font-size:11px; color:#777; display:block">
								${shoppingCart.shipmentCarrier} ${shoppingCart.shipmentService}&nbsp;&nbsp;|&nbsp;&nbsp; 
								Est Days : ${shoppingCart.shipmentDays} 
							</span>
						</g:if>	
					</td>
					<td  style="text-align:right;font-size:12px">
						${shoppingCart.shipmentCurrency}&nbsp;${applicationService.formatPrice(shoppingCart.shipping)}
						<g:if test="${shoppingCart.shipmentId != 'BASE'}">
							<g:link controller="shipping" action="select" params="[anonymous: 'true']"
							style="display:block; font-size:11px;">Change Shipping</g:link>
						</g:if>
					</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align:right;font-weight:bold;">Total</td>
					<td style="font-weight:bold; font-size:17px;text-align:right">${currencyService.format(applicationService.formatPrice(shoppingCart.total))}</td>
				</tr>
			</tbody>
		</table>
		

		
		
		<div id="information">
			
			<form name="checkout" action="/${applicationService.getContextName()}/shoppingCart/checkout" method="post" id="checkout_form" class="form-horizontal">
				
				<h3>Shipping Address</h3>
				
				
				<input type="hidden" name="name" value="${accountInstance?.name}" id="name"/>
				<input type="hidden" name="email" value="${accountInstance?.email}" id="email"/>
				<input type="hidden" name="address1" value="${accountInstance?.address1}" id="address1"/>
				<input type="hidden" ame="address2" value="${accountInstance?.address2}" id="address2"/>
				<input type="hidden" name="city" value="${accountInstance?.city}" id="city"/>
				<input type="hidden" name="country" value="${accountInstance?.country?.id}" id="country"/>
				<input type="hidden" name="state" value="${stateValue}" id="state"/>
				<input type="hidden" name="zip" id="zip" value="${accountInstance?.zip}"/>
				<input type="hidden" name="phone" id="phone" value="${accountInstance?.phone}"/>
				
				
				<div class="clear"></div>
				
				<address>
					${accountInstance.name}<br/>
					${accountInstance.address1}<br/>
					<g:if test="${accountInstance.address2}">
					${accountInstance.address2}<br/>
					</g:if>
					${accountInstance.city}<br/>
					${accountInstance.country.name}, 
					<g:if test="${accountInstance.state}">
						${accountInstance.state.name},
					</g:if>
					${accountInstance.zip}<br/>
					<strong>phone</strong> ${accountInstance.phone}<br/>
					<strong>email</strong> ${accountInstance.email}<br/>
				</address>
				
				<g:link controller="shoppingCart" action="anonymous" class="btn btn-default pull-right">Change Address</g:link>
				
				<!--
				<div class="form-group">
					<label class="col-sm-4 control-label">Name <em>(first &amp; last)</em></label>
					<input type="text" class="form-control shipping-info" name="name" value="${accountInstance?.name}" id="name"/>
					<input type="hidden" name="name" value="${accountInstance?.name}" id="name"/>
				</div>
				
				<div class="form-group">
					<label class="col-sm-4 control-label">Email</label>
					<input type="text" class="form-control shipping-info" name="email" value="${accountInstance?.email}" id="email"/>		<input type="hidden" name="email" value="${accountInstance?.email}" id="email"/>
				</div>
				
				<div class="form-group">
					<label class="col-sm-4 control-label">Shipping Address</label>
					<input type="text" class="form-control shipping-info" name="address1" value="${accountInstance?.address1}" id="address1"/>
					<input type="hidden" name="address1" value="${accountInstance?.address1}" id="address1"/>
				</div>
				
				<div class="form-group">
					<label class="col-sm-4 control-label">Shipping Address Continued</label>
					<input type="text" class="form-control shipping-info" name="address2" value="${accountInstance?.address2}" id="address2"/>
					<input type="hidden" ame="address2" value="${accountInstance?.address2}" id="address2"/>
				</div>
				
				<div class="form-group">
					<label class="col-sm-4 control-label">City</label>
					<input type="text" class="form-control shipping-info" name="city" value="${accountInstance?.city}" id="city"/>
					<input type="hidden" name="city" value="${accountInstance?.city}" id="city"/>
				</div>
				

				
				<div class="form-group">
				  	<label for="country" class="col-sm-4 control-label">Country</label>
					<g:select name="country"
							from="${countries}"
							value="${accountInstance?.country?.id}"
							optionKey="id" 
							optionValue="name"
							class="form-control"
							id="countrySelect"/>
					<input type="hidden" name="country" value="${accountInstance?.country?.id}" id="country"/>
				</div>
				
				<%
				def stateValue = null
				if(accountInstance.state){
					
					stateValue = accountInstance?.state?.id
				}
				%>
				${accountInstance?.state?.id}
				<div class="form-group" id="stateSelectRow">
					<label class="col-sm-4 control-label">State</label>
					<g:select name="state.id"
					          from="${State.list()}"
					          value="${accountInstance?.state?.id}"
					          optionKey="id" 
							  optionValue="name"
							  id="stateSelect"
							  class="form-control"
							  style="width:150px;"/>
					
					<input type="hidden" name="state" value="${stateValue}" id="state"/>
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-4 control-label">Zip</label>
					<input type="text" class="form-control shipping-info" name="zip" id="zip" value="${accountInstance?.zip}"/>
					<input type="hidden" name="zip" id="zip" value="${accountInstance?.zip}"/>
				</div>

				<div class="form-group">
					<label class="col-sm-4 control-label">Phone</label>
					<input type="text" class="form-control shipping-info" name="phone" id="phone" value="${accountInstance?.phone}"/>
					<input type="hidden" name="phone" id="phone" value="${accountInstance?.phone}"/>
				</div>
				-->
				
				<style type="text/css">
					h3 em{ 
						font-size:12px;
						margin-left:7px;
						display:inline-block;
					}
				</style>
				
				
				
				<input type="hidden" name="stripeToken" value="" id="stripeToken"/>
	   			<input type="hidden" name="total" value="${total}"/> 
				<input type="hidden" name="id" value="${shoppingCart?.id}"/> 
		
				
				<br/>
				
				<h3 style="margin-top:20px !important;">Credit Card Information</h3>

			
				<style type="text/css">
					#processing{
						display:inline-block; 
						text-align:right; 
						margin-right:20px;
					}
					#processing img{
						margin:0px 10px;
					}
				</style>

				<div class="form-group">
					<label class="col-sm-4 control-label">Credit Card with Zip Code</label>
					<div id="credit-card-information" class="form-control" style="width:300px; height:43px; padding-top:12px;"></div>
				</div>
				
			</form>
			
			<div class="form-group" style="position:relative; text-align:center;">
				<button id="submit" class="btn btn-primary btn-lg pull-right" style="margin:20px 20px; background:#3276B1 !important">Pay ${currencyService.format(applicationService.formatPrice(shoppingCart.total))}</button>
				<br/>
				<span class="pull-right" id="processing" style="display:none">
					Processing checkout, please wait&nbsp;
					<img src="/${applicationService.getContextName()}/images/loading.gif" >
				</span>
				
			</div>
			
		</div>
	</g:if>
	<g:else>
		<h2>Shopping Cart is empty...</h2>
	</g:else>

	<br class="clear"/>

		

<script type="text/javascript" src="${resource(dir:'js/country_states.js')}"></script>
		
				
<script type="text/javascript">
			
$(document).ready(function(){

	var $submitBtn    = $('#submit'),
		$tokenInput   = $('#stripeToken'),
		$checkoutForm = $('#checkout_form'),
		$processing   = $('#processing'),
		$inputs       = $('.form-control');

	var $email    = $('#email'),
		$name     = $('#name'),
		$address1 = $('#address1'),
		$address2 = $('#address2'),
		$city     = $('#city'),
		$state    = $('#state')
		$zip      = $('#zip')
		$phone    = $('#phone');
		
		
	<g:if env="development">
		<g:set var="publishableKey" value="${applicationService.getStripeDevelopmentPublishableKey()}"/>
	</g:if>
	
	<g:if env="production">
		<g:set var="publishableKey" value="${applicationService.getStripeLivePublishableKey()}"/>
	</g:if>
	
	
	<g:if test="${publishableKey == ""}">
		alert("Error\nThis site has not been properly configured with Stripe Account information.  Please make sure you have created a Stripe Account and successfully entered API Keys in the Greenfield Stripe Settings area");
		$submitBtn.attr("disabled", "disabled");
	</g:if>
	
	var stripe = {},
		elements = {},
		card = {};

	//console.log(Stripe)

	var processingHtml = "Processing checkout, please wait&nbsp;<img src=\"/${applicationService.getContextName()}/images/loading.gif\"/>"


	$submitBtn.click(process_checkout);


	function initialize(){
		stripe = Stripe("${raw(publishableKey)}");
		elements = stripe.elements()
		card = elements.create('card', {
			base : {
	    		fontSize: '23px',
	    		lineHeight: '48px'
			}
		})

		card.mount('#credit-card-information')

		card.addEventListener('change', function(event) {
	  		var displayError = document.getElementById('card-errors');
	  		if (event.error) {
	    		//displayError.textContent = event.error.message;
	    		$processing.html(event.error.message)
				$processing.css({ "font-weight" : "bold" });
				$processing.show()
	  		} else {
	  			$processing.hide()
				$processing.css({ "font-weight" : "normal" })
				$processing.html(processingHtml)
	  		}
		});
	}
	

	function validForm(){
		
		var testEmail = /^[A-Z0-9._%+-]+@([A-Z0-9-]+\.)+[A-Z]{2,4}$/i;
		if($email.val() == ""){
			alert("Please enter a valid email address")
			return false
		}
		
		if(!testEmail.test($email.val())){
			alert("Please enter a valid email address...")
			return false
		}
		
		if($name.val() == ""){
			alert("Please enter a valid name...")
			return false
		}
		
		if($address1.val() == ""){
			alert("Please enter a valid address...")
			return false
		}
		
		if($city.val() == ""){
			alert("Please enter a valid city...")
			return false
		}
		
		if($zip.val() == ""){
			alert("Please enter a valid zip")
			return false
		}
		
		return true
	}


	function process_checkout(){
		
		if(validForm()){
			$processing.show()
			stripe.createToken(card).then(function(result) {
				$tokenInput.val(result.token.id)
				$checkoutForm.submit();
			});
		}
	}

	
	initialize()


	countryStatesInit("${applicationService.getContextName()}", 1000)

	$inputs.prop('disabled', true);
	
	var stateValue = "${accountInstance?.state?.id}"
	console.log(stateValue);
	
	if(stateValue == "" || stateValue == "null"){
		$('#stateSelectRow').hide()
	}else{
		$stateSelect.val(${accountInstance.state.id})	
	}
	

})
</script>			


${raw(applicationService.getScreenFooter("Checkout"))}	