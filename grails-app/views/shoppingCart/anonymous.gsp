<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Shopping Cart"))}

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
											($${applicationService.formatPrice(option.variant.price)})
										</span>
										<br/>
									</g:each>
								</div>
							</g:if>
						</td>
						<td style="text-align:center">
							$${applicationService.formatPrice(productTotal)}
						</td>
						<td style="text-align:center">${item.quantity}</td>
						<td  style="text-align:center" id="extended_price">
							$${applicationService.formatPrice(extendedPrice)}
						</td>
						<td>
							<g:form controller="shoppingCart" action="remove_item" method="get" id="${shoppingCartInstance.id}">
								<input type="hidden" name="itemId" value="${item.id}"/>
								<input type="hidden" name="id" value="${shoppingCartInstance.id}"/>
								<input type="submit" class="btn btn-sm btn-default" name="submit" value="Remove Item"/>
							</g:form>
						</td>
					</tr>
				</g:each>
				<tr>
					<td colspan="4" style="text-align:right;">Subtotal</td>
					<td style="text-align:center; ">$${applicationService.formatPrice(shoppingCartInstance.subtotal)}</td>
					<td></td>
				</tr>
			</tbody>
		</table>
		
		<div>
			<g:form controller="shoppingCart" action="anonymous_checkout_preview" id="${shoppingCartInstance?.id}">
				<input type="hidden" name="id" value="${shoppingCartInstance?.id}"/> 
				<g:submitButton name="submit" value="Checkout" class="btn btn-primary pull-right" id="checkout-btn"/>
			</g:form>
		</div>
	</g:if>
	<g:else>
		<p>Your Shopping Cart is currently empty...</p>
	</g:else>
	

${raw(applicationService.getFooter())}	