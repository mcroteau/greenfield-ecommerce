
<%@ page import="org.greenfield.ShoppingCartItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'shoppingCartItem.label', default: 'ShoppingCartItem')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<div id="list-shoppingCartItem" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<table class="table">
				<thead>
					<tr>
					
						<g:sortableColumn property="quantity" title="${message(code: 'shoppingCartItem.quantity.label', default: 'Quantity')}" />
					
						<th><g:message code="shoppingCartItem.product.label" default="Product" /></th>
					
						<th><g:message code="shoppingCartItem.shoppingCart.label" default="Shopping Cart" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${shoppingCartItemInstanceList}" status="i" var="shoppingCartItemInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
				
						<td>${fieldValue(bean: shoppingCartItemInstance, field: "quantity")}</td>
					
						<td>${shoppingCartItemInstance?.product?.name}</td>
					
						<td>${shoppingCartItemInstance?.shoppingCart?.id}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${shoppingCartItemInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
