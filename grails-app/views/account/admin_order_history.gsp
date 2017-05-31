
<%@ page import="org.greenfield.Transaction" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'transaction.label', default: 'Transaction')}" />
		<title>Greenfield : Orders</title>
	</head>
	<body>


		<div id="list-transaction" class="content scaffold-list" role="main">

		
			<g:link controller="account" action="account_activity" id="${accountInstance.id}" class="btn btn-default pull-right" name="Back to Accounts">Back to Account Activity</g:link>

			<h2>${accountInstance.username} Orders</h2>
			
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<g:if test="${transactionInstanceList}">
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="id" title="${message(code: 'transaction.id.label', default: 'Order #')}" />
										
						<g:sortableColumn property="orderDate" title="${message(code: 'transaction.orderDate.label', default: 'Order Date')}" />
						
						<g:sortableColumn property="subtotal" title="${message(code: 'transaction.subtotal.label', default: 'Sub Total')}" />
						
						<g:sortableColumn property="shipping" title="${message(code: 'transaction.shipping.label', default: 'Shipping')}" />
						
						<g:sortableColumn property="taxes" title="${message(code: 'transaction.taxes.label', default: 'Taxes')}" />
						
						<g:sortableColumn property="status" title="${message(code: 'transaction.taxes.label', default: 'Status')}" />
						
						<g:sortableColumn property="total" title="${message(code: 'transaction.total.label', default: 'Total')}" />
					
						<th></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${transactionInstanceList}" status="i" var="transactionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td>${transactionInstance.id}</td>
					
						<td><g:link controller="account" action="review_order" id="${transactionInstance.id}"><g:formatDate date="${transactionInstance.orderDate}" format="dd MMM yyyy hh:mm a" /></g:link></td>
						
						<td>$${applicationService.formatPrice(transactionInstance.subtotal)}</td>
					
						<td>$${applicationService.formatPrice(transactionInstance.shipping)}</td>
				
						<td>$${applicationService.formatPrice(transactionInstance.taxes)}</td>
		
						<td>${transactionInstance.status}</td>
				
						<td>$${applicationService.formatPrice(transactionInstance.total)}</td>
					
						<td><g:link controller="account" action="review_order" params="[id: transactionInstance.id]" class="show-transaction-${transactionInstance.id}">Show </g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${transactionInstanceTotal}" />
			</div>
			
			</g:if>
			<g:else>
				<p>No orders found...</p>
			</g:else>
		</div>
	</body>
</html>
