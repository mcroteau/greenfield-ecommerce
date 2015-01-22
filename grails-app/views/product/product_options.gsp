<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<div class="content">
		
			<h2>${productInstance.name} : Product Options
				<g:link action="add_product_option" name="edit" class="btn btn-primary pull-right" id="${productInstance.id}" style="margin-left:10px;">Add Product Option</g:link>
				
				<g:link action="edit" name="edit" class="btn btn-default pull-right" id="${productInstance.id}">Back</g:link>
			</h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:if test="${productInstance.productOptions.size() > 0}">
				<table class="table table-condensed">
					<thead>
					<tr>
						<th>Name</th>
						<th># Variants</th>
						<th></th>
					</tr>
					</thead>
					<tbody>
					<g:each in="${productInstance.productOptions}" status="i" var="productOption">
						<tr>
							<td>${productOption.name}</td>
							<td>${productOption.variants?.size()}</td>
							<td><g:link controller="productOption" action="edit" name="edit" class="btn btn-default pull-right" id="${productOption.id}">Edit</g:link></td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</g:if>			
			<g:else>
				<div class="alert alert-info">No Product Options added yet</div>
			</g:else>
			
			
		</div>

	
	</body>
</html>
