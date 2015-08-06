
<%@ page import="org.greenfield.Account" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title>Greenfield : Accounts</title>
	</head>
	<body>

		<div id="list-account" class="content scaffold-list" role="main">
		

			<g:if test="${admin}">
				<h2>Admin Accounts</h2>
			</g:if>
			<g:else>
				<h2>Customer Accounts</h2>
			</g:else>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			

			<g:if test="${admin}">
				<g:form action="admin_create">
					<input type="hidden" name="admin" value="true"/>
					<g:submitButton class="btn btn-primary pull-right" name="New Account" value="Create New Admin User"/>
				</g:form>
				<p class="information secondary">Admin Accounts have access to the store front and Administration area.</p>
			</g:if>
			<g:else>
				<g:form action="admin_create">
					<input type="hidden" name="admin" value="false"/>
					<g:submitButton class="btn btn-primary pull-right" name="Create New Account" value="New Customer"/>
				</g:form>
				<p class="information secondary">Customer accounts have access to the store front and are able to shop for products</p>
			</g:else>
			
						
			<ul class="nav nav-tabs">

				<g:if test="${admin}">
			  		<li class="inactive"><g:link uri="/account/admin_list?admin=false" class="btn btn-default">Customers</g:link></li>
			  	  	<li class="active"><g:link uri="/account/admin_list?admin=true" class="btn btn-default">Admin Users</g:link></li>
				</g:if>
				<g:else>
			  		<li class="active"><g:link uri="/account/admin_list?admin=false" class="btn btn-default">Customers</g:link></li>
			  	  	<li class="inactive"><g:link uri="/account/admin_list?admin=true" class="btn btn-default">Admin Users</g:link></li>
				</g:else>
			</ul>
			
			
			
			
			
			<table class="table">
				<thead>
					<tr>
						
						<g:sortableColumn property="username" title="Username" />
						
						<g:sortableColumn property="name" title="Name" />
						
						<g:sortableColumn property="email" title="Email" />
					
						<g:sortableColumn property="city" title="City" />
					
						<g:sortableColumn property="state" title="State" />
						
						<th></th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${accountInstanceList}" status="i" var="accountInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
				
						<td><g:link action="admin_show" id="${accountInstance.id}">${fieldValue(bean: accountInstance, field: "username")}</g:link></td>
					
						<td>${accountInstance.name}</td>
					
					
						<td>${fieldValue(bean: accountInstance, field: "email")}</td>
					
						<td>${fieldValue(bean: accountInstance, field: "city")}</td>
					
						<td>${accountInstance?.state?.name}</td>
				
						
						<td><g:link controller="account" action="admin_edit" params="[id: accountInstance.id]" class="">Edit</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			
			
		</div>
	</body>
</html>
