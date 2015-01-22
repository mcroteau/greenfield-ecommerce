<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${applicationService.getHeader("Order History")}

<h1>Order History : <shiro:principal/></h1>
			
<g:if test="${flash.message}">
	<div class="alert alert-info" role="status">${flash.message}</div>
</g:if>
		
<g:if test="${transactions}">		
	<table class="table table-condensed table-bordered">
			<tr style="background:#efefef">
				<th>Id</th>
				<th>Order Date</th>
				<th>Shipping</th>
				<th>Taxes</th>
				<th>Total</th>
				<th>Status</th>
				<th></th>
			</tr>
		<g:each in="${transactions}" status="i" var="transaction">
			<tr>
				<td>${transaction.id}</td>
				<td><g:link controller="transaction" action="details" id="${transaction.id}" ><g:formatDate format="dd MMM yyyy hh:mm z" date="${transaction.orderDate}"/></g:link></td>
				<td>$${applicationService.formatPrice(transaction.shipping)}</td>
				<td>$${applicationService.formatPrice(transaction.taxes)}</td>
				<td>$${applicationService.formatPrice(transaction.total)}</td>
				<td>${transaction.status}</td>
				<td><g:link controller="transaction" action="details" id="${transaction.id}" class="btn btn-default">Details</g:link></td>
			</tr>
		</g:each>
	</table>
</g:if>
<g:else>
	<h2>No orders found</h2>
</g:else>
	

${applicationService.getFooter()}
