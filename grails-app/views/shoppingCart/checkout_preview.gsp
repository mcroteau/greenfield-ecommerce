<%@ page import="org.greenfield.State" %>
<%@ page import="grails.util.Environment" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
	

	
${raw(applicationService.getHeader("Shopping Cart"))}

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
											($${applicationService.formatPrice(option.variant.price)})
										</span>
										<br/>
									</g:each>
								</div>
							</g:if>
						</td>
						<td>$${applicationService.formatPrice(productTotal)}</td>
						<td style="text-align:center">${item.quantity}</td>
						<td id="extended_price" style="text-align:right;">
							$${applicationService.formatPrice(extendedPrice)}
						</td>
						
					</tr>
				</g:each>
				<tr>
					<td colspan="4" style="text-align:right;">Subtotal</td>
					<td style="text-align:right; ">$${applicationService.formatPrice(shoppingCart.subtotal)}</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align:right; font-size:12px">Taxes (${applicationService.getTaxRate()}%)</td>
					<td style="text-align:right; font-size:12px;">$${applicationService.formatPrice(shoppingCart.taxes)}</td>
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
						$${applicationService.formatPrice(shoppingCart.shipping)}
						<g:if test="${shoppingCart.shipmentId != 'BASE'}">
							<g:link controller="shipping" action="select" id="${shoppingCart.id}" style="display:block; font-size:11px;">Change Shipping</g:link>
						</g:if>
					</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align:right;font-weight:bold;">Total</td>
					<td style="font-weight:bold; font-size:17px;text-align:right">$${applicationService.formatPrice(shoppingCart.total)}</td>
				</tr>
			</tbody>
		</table>
		

		
		
		<div id="information">
			
			<form name="checkout" action="/${applicationService.getContextName()}/shoppingCart/checkout" method="post" id="checkout_form" class="form-horizontal">
				
				<h3>Shipping Address</h3>

				<g:link controller="account" action="customer_profile" class="btn btn-default pull-right">Change Address</g:link>
				
				<div class="clear"></div>
				
				<div class="form-group">
					<label class="col-sm-4 control-label">Name <em>(first & last)</em></label>
					<input type="text" class="form-control shipping-info" name="shipName" value="${accountInstance?.name}" id="shipName" disabled="true"/>
				</div>
		
				<div class="form-group">
					<label class="col-sm-4 control-label">Shipping Address</label>
					<input type="text" class="form-control shipping-info" name="shipAddress1" value="${accountInstance?.address1}" id="shipAddress1" disabled="true"/>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">Shipping Address Continued</label>
					<input type="text" class="form-control shipping-info" name="shipAddress2" value="${accountInstance?.address2}" id="shipAddress2" disabled="true"/>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">City</label>
					<input type="text" class="form-control shipping-info" name="shipCity" value="${accountInstance?.city}" id="shipCity" disabled="true"/>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">State</label>
					<g:select name="shipState"
					          from="${State.list()}"
					          value="${accountInstance?.state?.id}"
					          optionKey="id" 
							  optionValue="name"
							  id="shipState"
							  disabled="true"
							  class="form-control"
							  style="width:150px;"/>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">Zip</label>
					<input type="text" class="form-control shipping-info" name="shipZip" id="shipZip" value="${accountInstance?.zip}" disabled="true"/>
				</div>

				<div class="form-group">
					<label class="col-sm-4 control-label">Phone</label>
					<input type="text" class="form-control shipping-info" name="shipPhone" id="shipPhone" value="${accountInstance?.phone}" disabled="true"/>
				</div>

				
				<style type="text/css">
					h3 em{ 
						font-size:12px;
						margin-left:7px;
						display:inline-block;
					}
				</style>
				
				<!--
				<h3>Billing Address <input type="checkbox" id="sameas"/><em>Same as Shipping Address</em></h3>
		
				<div class="billing-address">
					<div class="form-group">
						<label class="col-sm-4 control-label">Name <em>(first & last)</em></label>
						<input type="text" class="form-control" name="billName" value="${accountInstance?.name}" id="billName"/>
					</div>
		        	
					<div class="form-group">
						<label class="col-sm-4 control-label">Billing Address</label>
						<input type="text" class="form-control" name="billAddress1" value="${accountInstance?.address1}" id="billAddress1"/>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label">Billing Address Continued</label>
						<input type="text" class="form-control" name="billAddress2" value="${accountInstance?.address2}" id="billAddress2"/>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label">City</label>
						<input type="text" class="form-control" name="billCity" value="${accountInstance?.city}" id="billCity"/>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label">State</label>
						<g:select name="billState"
						          from="${State.list()}"
						          value="${accountInstance?.state?.id}"
						          optionKey="id"
								  optionValue="name"
								  id="billState" />
			    	
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label">Zip</label>
						<input type="text" class="form-control" name="billZip" value="${accountInstance?.zip}" id="billZip"/>
					</div>
				</div>
				-->
				
				
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
				<button id="submit" class="btn btn-primary btn-lg pull-right" style="margin:20px 20px; background:#3276B1 !important">Pay $${applicationService.formatPrice(shoppingCart.total)}</button>
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
	
<script type="text/javascript">
			
$(document).ready(function(){

	var $submitBtn = $('#submit'),
		$tokenInput = $('#stripeToken'),
		$checkoutForm = $('#checkout_form'),
		$processing = $('#processing');

	
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

	console.log(Stripe)

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



	function process_checkout(){
		$processing.show()
		stripe.createToken(card).then(function(result) {
			console.log("submit form")
			console.log(result)
			$tokenInput.val(result.token.id)
			$checkoutForm.submit();
		});
	}

	
	initialize()


		
	/** TODO: consider removing
	var $sameas = $('#sameas'),
		$shipName = $('#shipName'),
		$billName = $('#billName'),
		$shipAddress1 = $('#shipAddress1'),
		$billAddress1 = $('#billAddress1'),
		$shipAddress2 = $('#shipAddress2'),
		$billAddress2 = $('#billAddress2'),
		$shipCity = $('#shipCity'),
		$billCity = $('#billCity'),
		$shipState = $('#shipState'),
		$billState = $('#billState'),
		$shipZip = $('#shipZip'),
		$billZip = $('#billZip'),
		$shippingInfo = $('.shipping-info');
		
	
	
	$sameas.click(checkSetBillingInfo)	
	
	$shippingInfo.blur(checkSetBillingInfo)
	
	$shipState.change(function(){
		checkSetBillingInfo();
	});
	
	function checkSetBillingInfo(){
		if($sameas.is(':checked')){
			$billName.val($shipName.val())
			$billAddress1.val($shipAddress1.val())
			$billAddress2.val($shipAddress2.val())
			$billCity.val($shipCity.val())
			$billState.val($shipState.val())
			$billZip.val($shipZip.val())
		}
	}
	$sameas.click()
	**/

})
</script>			


${raw(applicationService.getFooter())}	