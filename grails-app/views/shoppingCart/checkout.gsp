<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Checkout"))}

	<h4>Successfully Processed Order</h4>
	
	<h2>Order # : ${transaction.id}</h2>
	
	<table class="table table-bordered">
		<thead>
			<tr style="background:#efefef">
				<th style="text-align:center">ID</th>
				<th>Name</th>
				<th style="text-align:center">Price</th>
				<th style="text-align:center">Quantity</th>
				<th style="text-align:center">Extended Price</th>
			</tr>
		</thead>		
		<tbody>
			<g:each in="${transaction.shoppingCart?.shoppingCartItems}" status="i" var="item">
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
					<td style="text-align:center">
						$${applicationService.formatPrice(productTotal)}
					</td>
					<td style="text-align:center">${item.quantity}</td>
					<td id="extended_price" style="text-align:center">
						$${applicationService.formatPrice(extendedPrice)}
					</td>
				</tr>
			</g:each>
			<tr>
				<td colspan="4" style="text-align:right;">Subtotal</td>
				<td style="text-align:center; ">
					$${applicationService.formatPrice(transaction.subtotal)}
				</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right; font-size:12px">Taxes (${applicationService.getTaxRate()}%)</td>
				<td style="text-align:center; font-size:12px;">$${applicationService.formatPrice(transaction.taxes)}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right;font-size:12px">Shipping</td>
				<td  style="text-align:center;font-size:12px">$${applicationService.formatPrice(transaction.shipping)}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right;font-weight:bold;">Total</td>
				<td style="font-weight:bold; font-size:17px;text-align:center">$${applicationService.formatPrice(transaction.total)}</td>
			</tr>
			
		</tbody>
	</table>
	
	<style type="text/css">
		.float{
			float:left;
		}
	</style>
	
	<div class="float">
		<h3>Shipping Address</h3>
		<address>
			${transaction?.shipName}<br/>
			${transaction?.shipAddress1}<br/>
			<g:if test="${transaction.shipAddress2}">
				${transaction.shipAddress2}<br/>
			</g:if>
			${transaction.shipCity}, ${transaction?.shipState?.name}<br/>
			${transaction.shipZip}		
		</address>
	</div>
	
	<br style="clear:both;"/>
	
${raw(applicationService.getFooter())}