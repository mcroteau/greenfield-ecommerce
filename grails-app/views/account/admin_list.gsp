
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
			
			
			<g:if test="${accountInstanceList}">
			
			
				<table class="table">
					<thead>
						<tr>
							<!-- TODO: make sortable, may require refactoring Account hasMany to include hasMany roles/authorities -->
							<g:sortableColumn property="username" title="Username" />
							
							<g:sortableColumn property="name" title="Name" />
							
							<g:sortableColumn property="email" title="Email" />
						
							<!--<g:sortableColumn property="city" title="City" />-->
						
							<!--<g:sortableColumn property="state" title="State" />-->

							<g:sortableColumn property="pageViews" title="Searches" />

							<g:sortableColumn property="pageViews" title="Page Views" />

							<g:sortableColumn property="catalogViews" title="Catalog Views" />

							<g:sortableColumn property="productViews" title="Product Views" />

							<g:sortableColumn property="orders" title="Orders" />
							
							<th></th>
						</tr>
					</thead>
					<tbody>
					<g:each in="${accountInstanceList}" status="i" var="accountInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
							<td>
								<g:link action="admin_show" id="${accountInstance.id}">
								${fieldValue(bean: accountInstance, field: "username")}</g:link>
							</td>
						
							<td>${accountInstance.name}</td>
						
						
							<td>${fieldValue(bean: accountInstance, field: "email")}</td>
						
							<!--<td>${fieldValue(bean: accountInstance, field: "city")}</td>-->
						
							<!--<td>${accountInstance?.state?.name}</td>-->

							<td align="center">
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">
									${fieldValue(bean: accountInstance, field: "searches")}
								</g:link>
							</td>

							<td align="center">
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">
									${fieldValue(bean: accountInstance, field: "pageViews")}
								</g:link>
							</td>

							<td align="center">
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">
									${fieldValue(bean: accountInstance, field: "catalogViews")}</g:link>
							</td>

							<td align="center">
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">
									${fieldValue(bean: accountInstance, field: "productViews")}
								</g:link>
							</td>
								
					
							<td align="center">
								<g:link controller="account" action="admin_order_history" params="[id: accountInstance.id]" class="">${accountInstance.transactions.size()}
								</g:link>
							</td>

							<td>
								<g:link controller="account" action="admin_edit" params="[id: accountInstance.id]" class="">Edit</g:link>&nbsp;&nbsp;
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">Activity</g:link>
							</td>
						
						</tr>
					</g:each>
					</tbody>
				</table>
				
				<div class="btn-group">
					<g:paginate total="${accountInstanceTotal}" />
				</div>
			</g:if>
			<g:else>
				<br/>
				<p style="color:#333;padding:0px 40px;">No Accounts created yet, you just opened up a store on undeveloped land on the internet. You will have customer accounts. Hang tight...</p>
			</g:else>
		</div>
	</body>
</html>
