<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('org.greenfield.CurrencyService').newInstance()%>

${raw(applicationService.getDefaultHeader("Order History"))}

<h1>Order History : <sec:username/></h1>
			
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
				<td>${currencyService.format(applicationService.formatPrice(transaction.shipping))}</td>
				<td>${currencyService.format(applicationService.formatPrice(transaction.taxes))}</td>
				<td>${currencyService.format(applicationService.formatPrice(transaction.total))}</td>
				<td>${transaction.status}</td>
				<td><g:link controller="transaction" action="details" id="${transaction.id}" class="btn btn-default">Details</g:link></td>
			</tr>
		</g:each>
	</table>
</g:if>
<g:else>
	<p>No orders found...</p>
</g:else>
	

${raw(applicationService.getDefaultFooter())}
