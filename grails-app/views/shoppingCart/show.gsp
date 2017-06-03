
<%@ page import="org.greenfield.ShoppingCart" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'shoppingCart.label', default: 'ShoppingCart')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-shoppingCart" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<ol class="property-list shoppingCart">
			
				<g:if test="${shoppingCartInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="shoppingCart.status.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="status-label">${shoppingCartInstance?.status}</span>
					
				</li>
				</g:if>
				
			
				<g:if test="${shoppingCartInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="shoppingCart.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${shoppingCartInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>


				<g:if test="${shoppingCartInstance?.account}">
				<li class="fieldcontain">
					<span id="account-label" class="property-label"><g:message code="shoppingCart.account.label" default="Account" /></span>
					
						<span class="property-value" aria-labelledby="account-label"><g:fieldValue bean="${shoppingCartInstance}" field="account"/></span>
					
				</li>
				</g:if>
			
			
				<g:if test="${shoppingCartInstance?.shipping}">
				<li class="fieldcontain">
					<span id="shippingCharge-label" class="property-label"><g:message code="shoppingCart.shippingCharge.label" default="Shipping Charge" /></span>
					
						<span class="property-value" aria-labelledby="shippingCharge-label"><g:fieldValue bean="${shoppingCartInstance}" field="shipping"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${shoppingCartInstance?.shoppingCartItems}">
				<li class="fieldcontain">
					<span>Products</span>
					<g:each in="${shoppingCartInstance.shoppingCartItems}" var="item">
						<span>${product.item.product.name}</span>
					</g:each>
				</li>
				</g:if>
	
			
			</ol>

		</div>
	</body>
</html>
