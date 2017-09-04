
<%@ page import="org.greenfield.Transaction" %>
<%@ page import="org.greenfield.ApplicationService" %>
<%@ page import="org.greenfield.common.OrderStatus" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'transaction.label', default: 'Transaction')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
	
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" style="margin-top:20px;">${flash.message}</div>
	</g:if>
	
	

	
	<h1>Order # : ${transactionInstance.id}</h1>
	
	<g:form>
		<g:hiddenField name="id" value="${transactionInstance?.id}" />

		<g:actionSubmit class="btn btn-danger pull-right" action="delete" value="Delete" onclick="return confirm('Are you sure?');" />

		<div style="height:20px; width:10px; display:inline-block" class="pull-right"></div>

		<g:link controller="transaction" action="list" class="btn btn-default pull-right" name="Order History">Order History</g:link>
		
		
		<g:if test="${transactionInstance.shoppingCart?.shipmentId &&
				transactionInstance.shoppingCart?.shipmentRateId &&
				transactionInstance.status != 'REFUNDED'}">
			<g:if test="${transactionInstance.postageId}">
				<g:link action="print_shipping_label" id="${transactionInstance.id}" class="btn btn-default pull-right" style="margin-right:10px;" target="_blank">Print Shipping Label</g:link>
			</g:if>
			<g:else>
				<g:link action="confirm_purchase_shipping_label" id="${transactionInstance.id}" class="btn btn-default pull-right" style="margin-right:10px;">Buy Shipping Label</g:link>
			</g:else>
		</g:if>
		
	</g:form>
	
	
	
	
	
	<div class="form-row">
		<span class="form-label minimum hint">Order Date:&nbsp;</span>
		<span class="input-container">
			<g:formatDate format="dd MMM yyyy hh:mm z" date="${transactionInstance.orderDate}"/>
		</span>
		<br class="clear"/>
	</div>
	
	
	<g:form controller="transaction" action="update_status" id="${transactionInstance.id}" method="post">
		<input type="hidden" id="current-status" value="${transactionInstance.status}" />
		<div class="form-row">
			<span class="form-label minimum hint">Status:&nbsp;</span>
			<span class="input-container">
				<g:if test="${transactionInstance.status != 'REFUNDED'}">
					<g:select from="${OrderStatus.values()}" 
						optionKey="description" 
						name="status" value="${transactionInstance.status}" 
						class="form-control" 
						style="width:150px; display:inline-block" 
						id="status-select"/>
					<input type="submit" value="Update Status" id="update-status" class="btn btn-primary"> 
				</g:if>
				<g:else>
					${transactionInstance.status}
				</g:else>
			</span>
			<br class="clear"/>
		</div>
	</g:form>
	
	
	
	<table class="table table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">Item #</th>
				<th>Name</th>
				<th style="text-align:center">Price</th>
				<th style="text-align:center">Quantity</th>
				<th style="text-align:center">Extended Price</th>
			</tr>
		</thead>		
		<tbody>
			<% total = 0 %>
			<g:each in="${transactionInstance.shoppingCart?.shoppingCartItems}" status="i" var="item">
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
					<td style="text-align:center"><g:link controller="product" action="edit" id="${item.product.id}">${item.product.id}</g:link></td>
					<td><g:link controller="product" action="edit" id="${item.product.id}">${item.product.name}</g:link>
						<br/>
						<g:if test="${item.shoppingCartItemOptions?.size() > 0}">
							<div style="font-size:11px; color:#777">
								<strong>options :&nbsp;</strong>
								<g:each in="${item.shoppingCartItemOptions}" var="option">
									<span class="option" style="font-size: 11px;color: #777;">${option.variant.name}
										($${applicationService.formatPrice(option.variant.price)})
									</span>
									<br/>
								</g:each>
							</div>
						</g:if>
					</td>
					<td style="text-align:center">$${applicationService.formatPrice(productTotal)}</td>
					<td style="text-align:center">${item.quantity}</td>
					<td id="extended_price" style="text-align:center">$${applicationService.formatPrice(extendedPrice)}</td>
				</tr>
			</g:each>
			<tr>
				<td colspan="4" style="text-align:right;">Subtotal</td>
				<td style="text-align:center; ">$${applicationService.formatPrice(transactionInstance.subtotal)}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right; font-size:12px">Taxes</td>
				<td style="text-align:center; font-size:12px;">$${applicationService.formatPrice(transactionInstance.taxes)}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right;font-size:12px">Shipping</td>
				<td  style="text-align:center;font-size:12px">$${applicationService.formatPrice(transactionInstance.shipping)}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right;font-weight:bold;">Total</td>
				<td style="font-weight:bold; font-size:17px;text-align:center">$${applicationService.formatPrice(transactionInstance.total)}</td>
			</tr>
		</tbody>
	</table>
	
	<style type="text/css">
		.billing-address,
		.shipping-address{
			float:left;
			margin:20px 50px 20px 0px ;
		}
	</style>
	
	<div class="shipping-address">
		<h3>Shipping</h3>
		<address>
			<strong>${transactionInstance?.shipName}</strong><br/>
			${transactionInstance?.shipAddress1}<br/>
			<g:if test="${transactionInstance.shipAddress2}">
				${transactionInstance.shipAddress2}<br/>
			</g:if>
			${transactionInstance?.shipCity},
			${transactionInstance.shipState.name}
			${transactionInstance?.shipZip}<br/>
			${transactionInstance?.account.email}<br/>
			<g:if test="${transactionInstance.account.phone}">
				phone:${transactionInstance?.account.phone}
			</g:if>
		</address>
	</div>
	
	<g:if test="${transactionInstance.status != 'REFUNDED'}">
		<g:link controller="transaction" action="confirm_refund" class="btn btn-default pull-right" id="${transactionInstance.id}">Refund Order</g:link>
	</g:if>
	
	<br style="clear:both"/>
	
<script type="text/javascript">
$(document).ready(function(){
	var $statusSel = $('#status-select'),
		$updateBtn = $('#update-status');
	
	var status = $('#current-status').val();
		
	$updateBtn.attr('disabled', 'disabled');
	$statusSel.change(checkEnableDisableSelect)


	function checkEnableDisableSelect(event){
		var $target = $(event.target);
		var selectedStatus = $target.val()
		if(selectedStatus == status){
			$updateBtn.attr('disabled', 'disabled');
		}else{
			$updateBtn.removeAttr('disabled');
		}
	}
	
});	
	
</script>

</body>
</html>
