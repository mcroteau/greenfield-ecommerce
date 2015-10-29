
<%@ page import="org.greenfield.ShoppingCart" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'shoppingCart.label', default: 'ShoppingCart')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>


		<div id="list-shoppingCart" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<table class="table">
				<thead>
					<tr>
					
						<g:sortableColumn property="id" title="Id" />
						
						<g:sortableColumn property="dateCreated" title="Date Created" />
						
						<g:sortableColumn property="account" title="Account" />
						
						<g:sortableColumn property="status" title="Status" />
						
						<th></th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${shoppingCartInstanceList}" status="i" var="shoppingCartInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${shoppingCartInstance.id}">${shoppingCartInstance.id}</g:link></td>
						
						<td>${shoppingCartInstance.dateCreated}</td>
				
						<td>${shoppingCartInstance.account.username}</td>
					
						<td>${shoppingCartInstance.status}</td>
						
						<td><g:link class="btn btn-default" action="show" id="${shoppingCartInstance.id}">Show</g:link></td>
					
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${shoppingCartInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
