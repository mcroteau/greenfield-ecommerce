<%@ page import="org.greenfield.State" %>
<%@ page import="grails.util.Environment" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>
	

	
${applicationService.getHeader("Shopping Cart")}

	<script src="/${applicationService.getContextName()}/js/lib/stripe/stripe.js"></script>
	
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

			    <div class="form-group">
			      	<label class="col-sm-4 control-label">Card Number</label>
			        <input type="text" class="form-control creditcard_input" size="20" id="creditcard_number"/>
			    </div>

			    <div class="form-group">
			      	<label class="col-sm-4 control-label">CVC <br/><em>(located on the back of credit card)</em></label>
			        <input type="text" class="form-control creditcard_input" size="2" style="width:70px" id="creditcard_cvc"/>
			    </div>

				
			    <div class="form-group">
			    	<label class="col-sm-4 control-label">Expiration <em>(MM/YYYY)</em></label>
			    	<input type="text" class="form-control creditcard_input"  style="display:inline-block;width:100px;margin-right:0px;" id="creditcard_exp_month"/>
			      	<span>/</span>
			      	<input type="text" class="form-control creditcard_input" style="display:inline-block;width:100px;" id="creditcard_exp_year"/>
					<br />
			    </div>
				
			</form>
			
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
				<span class="pull-right" id="processing" style="display:none">
					Processing payment, please wait&nbsp;
					<img src="/${applicationService.getContextName()}/images/loading.gif" >
				</span>
				<br/>
				<br/>
				<button id="submit" class="btn btn-primary pull-right">Checkout</button>
				
			</div>
			
		</div>
	</g:if>
	<g:else>
		<h2>Shopping Cart is empty...</h2>
	</g:else>
	
<script type="text/javascript">
			
$(document).ready(function(){

	var $tokenInput = $('#stripeToken'),
		$cardNumberInput = $('#creditcard_number'),
		$cardCvcInput = $('#creditcard_cvc'),
		$cardExpMonth = $('#creditcard_exp_month'),
		$cardExpYear = $('#creditcard_exp_year'),
		$checkoutForm = $('#checkout_form'),
		$processing = $('#processing');

	
	<g:if env="development">
		<g:set var="publishableKey" value="${applicationService.getStripeDevelopmentPublishableKey()}"/>
	</g:if>
	
	<g:if env="production">
		<g:set var="publishableKey" value="${applicationService.getStripeLivePublishableKey()}"/>
	</g:if>
	
	Stripe.setPublishableKey("${publishableKey}");

	$('#submit').click(checkCreditCardValues);

	function checkout(){
		//console.info('submit form, process payment, create transaction', $checkoutForm);
		$checkoutForm.submit();
	}

	function setStripeTokenInput(code, token){
		$tokenInput.val(token.id)
		checkout();
	}

	function getStripeToken(){
		//TODO : catch stripe payment errors
		Stripe.card.createToken({
		    number    : $cardNumberInput.val(),
		    cvc       : $cardCvcInput.val(),
		    exp_month : $cardExpMonth.val(),
		    exp_year  : $cardExpYear.val()
		}, setStripeTokenInput);
	}


	function checkCreditCardValues(){
		if($cardNumberInput.val() != "" &&
				$cardCvcInput.val() != "" &&
				$cardExpMonth.val() != "" &&
				$cardExpYear.val() != ""){
			getStripeToken();
			$processing.show();
		}else{
			alert('Please make sure all credit card information is provided')
		}
	}
	
	
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

})
</script>			


${applicationService.getFooter()}	