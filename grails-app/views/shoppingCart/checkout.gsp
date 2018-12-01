<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>
<% def currencyService = grailsApplication.classLoader.loadClass('org.greenfield.CurrencyService').newInstance()%>

${raw(applicationService.getScreenHeader("Checkout Success"))}

	<h4>Successfully Processed Order. Thank you!</h4>
	
	<h2>Order # : ${transaction.id}</h2>
	
	<table class="table table-bordered">
		<thead>
			<tr style="background:#efefef">
				<th style="text-align:center">Id</th>
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
							optionsTotal += option.checkoutPrice
						}	
					}
                    def productTotal = item.checkoutPrice + optionsTotal
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
										(${currencyService.format(applicationService.formatPrice(option.checkoutPrice))})
									</span>
									<br/>
								</g:each>
							</div>
						</g:if>
					</td>
					<td style="text-align:center">
                        <g:if test="${item.regularPrice != item.checkoutPrice}">
							${currencyService.format(applicationService.formatPrice(item.checkoutPrice))}
                            <span class="regular-price">${currencyService.format(applicationService.formatPrice(item.regularPrice))}</span>
                        </g:if>
						<g:else>
							${currencyService.format(applicationService.formatPrice(item.checkoutPrice))}
						</g:else>
					</td>
					<td style="text-align:center">${item.quantity}</td>
					<td id="extended_price" style="text-align:center">
						${currencyService.format(applicationService.formatPrice(extendedPrice))}
					</td>
				</tr>
			</g:each>
			<tr>
				<td colspan="4" style="text-align:right;">Subtotal</td>
				<td style="text-align:center; ">
					${currencyService.format(applicationService.formatPrice(transaction.subtotal))}
				</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right; font-size:12px">Taxes (${applicationService.getTaxRate()}%)</td>
				<td style="text-align:center; font-size:12px;">${currencyService.format(applicationService.formatPrice(transaction.taxes))}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right;font-size:12px">Shipping</td>
				<td  style="text-align:center;font-size:12px">${currencyService.format(applicationService.formatPrice(transaction.shipping))}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right;font-weight:bold;">Total</td>
				<td style="font-weight:bold; font-size:17px;text-align:center">${currencyService.format(applicationService.formatPrice(transaction.total))}</td>
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
			
			<br/>
			<br/>
			<strong>email:</strong>&nbsp;${transaction?.account.email}<br/>
			<g:if test="${transaction.account.phone}">
				<strong>phone</strong>:&nbsp;${transaction?.account.phone}
			</g:if>
		</address>
		
	</div>
	
	<br style="clear:both;"/>
	
${raw(applicationService.getScreenFooter("Checkout Success"))}