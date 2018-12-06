<%@ page import="org.greenfield.State" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('org.greenfield.CurrencyService').newInstance()%>

${raw(applicationService.getScreenHeader("Checkout Preview"))}

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

	<h2>Shopping Cart</h2>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	<g:if test="${shoppingCartInstance?.shoppingCartItems}">

		<table class="table table-bordered">
			<thead>
				<tr style="background:#efefef">
					<th style="text-align:center">ID</th>
					<th>Name</th>
					<th style="text-align:center">Price</th>
					<th style="text-align:center">Quantity</th>
					<th style="text-align:center">Extended Price</th>
					<th></th>
				</tr>
			</thead>		
			<tbody>
				<g:each in="${shoppingCartInstance?.shoppingCartItems}" status="i" var="item">
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
						<td><g:link controller="product" action="details" id="${item.product.id}">${item.product.name}</g:link>
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
						<td style="text-align:center">
							${currencyService.format(applicationService.formatPrice(productTotal))}
						    <g:if test="${item.product.salesPrice}">
						        <span class="regular-price">${currencyService.format(applicationService.formatPrice(item.product.price))}</span>
						    </g:if>
						</td>
						<td style="text-align:center">${item.quantity}</td>
						<td  style="text-align:center" id="extended_price">
							${currencyService.format(applicationService.formatPrice(extendedPrice))}
						</td>
						<td>
							<g:form controller="shoppingCart" action="remove_item" method="get" id="${shoppingCartInstance.id}">
								<input type="hidden" name="itemId" value="${item.id}"/>
								<input type="hidden" name="id" value="${shoppingCartInstance.id}"/>
								<input type="submit" class="btn btn-sm btn-default remove-${item.id}" name="submit" value="Remove Item"/>
							</g:form>
						</td>
					</tr>
				</g:each>
				<tr>
					<td colspan="4" style="text-align:right;">Subtotal</td>
					<td style="text-align:center; ">${currencyService.format(applicationService.formatPrice(shoppingCartInstance.subtotal))}</td>
					<td></td>
				</tr>
			</tbody>
		</table>
		
		
		<g:if test="${shippingApiEnabled == true}">
			<div id="information">
				
				<form name="anonymous_preview" action="/${applicationService.getContextName()}/shoppingCart/anonymous_preview" method="post" id="anonymousForm" class="form-horizontal">
					
					<h3>Calculate Shipping</h3>
        	
					<p class="secondary information">Please complete the form below to complete to calculate shipping and continue...</p>
					
					<div class="clear"></div>
        	
					
					<div class="form-group">
						<label class="col-sm-4 control-label">Name <em>(first &amp; last)</em></label>
						<input type="text" class="form-control shipping-info" name="name" value="${accountInstance?.name}" id="name"/>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label">Email</label>
						<input type="text" class="form-control shipping-info" name="email" value="${accountInstance?.email}" id="email" style="width:350px"/>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label">Shipping Address</label>
						<input type="text" class="form-control shipping-info" name="address1" value="${accountInstance?.address1}" id="address1"/>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label">Shipping Address Continued</label>
						<input type="text" class="form-control shipping-info" name="address2" value="${accountInstance?.address2}" id="address2"/>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label">City</label>
						<input type="text" class="form-control shipping-info" name="city" value="${accountInstance?.city}" id="city"/>
					</div>
					
        	
					<div class="form-group">
					  	<label for="country" class="col-sm-4 control-label">Country</label>
						<g:select name="country"
								from="${countries}"
								value=""
								optionKey="id" 
								optionValue="name"
								class="form-control"
								id="countrySelect"/>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label">State</label>
						<g:select name="state"
						          from="${State.list()}"
						          value=""
						          optionKey="id" 
								  optionValue="name"
								  class="form-control"
								  style="width:150px;"
								  id="stateSelect"/>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label">Zip</label>
						<input type="text" class="form-control shipping-info" name="zip" id="zip" value="${accountInstance?.zip}"/>
					</div>
        	
					<div class="form-group">
						<label class="col-sm-4 control-label">Phone</label>
						<input type="text" class="form-control shipping-info" name="phone" id="phone" value="${accountInstance?.phone}"/>
					</div>

					<input type="hidden" name="id" value="${shoppingCartInstance?.id}"/>
					
				</form>
			</div>
		</g:if>
	
		
		<div>
			<button name="submit" class="btn btn-primary pull-right btn-lg" id="checkoutBtn">Calculate Checkout</button>
		</div>
	</g:if>
	<g:else>
		<p>Your Shopping Cart is currently empty...</p>
	</g:else>

	<br class="clear"/>

	<script type="text/javascript" src="${resource(dir:'js/country_states.js')}"></script>
	
<script type="text/javascript">

	
$(document).ready(function(){

	var $submitBtn    = $('#checkoutBtn'),
		$checkoutForm = $('#anonymousForm');

	var $email    = $('#email'),
		$name     = $('#name'),
		$address1 = $('#address1'),
		$address2 = $('#address2'),
		$city     = $('#city'),
		$state    = $('#state')
		$zip      = $('#zip')
		$phone    = $('#phone');


	$submitBtn.click(process_checkout);
	function process_checkout(){
		if(validForm()){
			$checkoutForm.submit();
		}
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

		if($phone.val() == ""){
			alert("Please enter a valid phone...")
			return false
		}



		return true
	}



	countryStatesInit("${applicationService.getContextName()}", ${accountInstance?.state})


})
</script>			


${raw(applicationService.getScreenFooter("Checkout Preview"))}	