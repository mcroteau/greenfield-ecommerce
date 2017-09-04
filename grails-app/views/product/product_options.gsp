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
		
			<h2>Product Options
				<g:link action="add_product_option" name="edit" class="btn btn-primary pull-right" id="${productInstance.id}" style="margin-left:10px;">Add Product Option</g:link>
				
				<g:link controller="productOption" action="manage_positions" name="edit" class="btn btn-default pull-right" id="${productInstance.id}" style="margin-left:10px;">Order Options</g:link>
				
				<g:link action="edit" name="edit" class="btn btn-default pull-right" id="${productInstance.id}">Back to Product</g:link>
			</h2>
		
			
			<div class="form-row">
				<span class="form-label secondary">Product</span>
				<span class="input-container">
					<span class="label label-default">${productInstance?.name}</span>
				</span>
				<br class="clear"/>
			</div>
			
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:if test="${productOptions.size() > 0}">
				<table class="table table-condensed" style="width:550px;">
					<thead>
					<tr>
						<th>Name</th>
						<th>Variants</th>
						<th></th>
					</tr>
					</thead>
					<tbody>
					<g:each in="${productOptions}" status="i" var="productOption">
						<tr>
							<td>${productOption.name}</td>
							<td><g:if test="${productOption.variants.size() > 0 }">
									<g:each in="${productOption.variants}" var="variant">
										<span class="label label-default">${variant.name}</span>
									</g:each>
								</g:if>
								<g:else>
									<span class="information">No Variants Added Yet</span>
								</g:else>
							</td>
							<td><g:link controller="productOption" action="edit" name="edit" class="" id="${productOption.id}">Edit</g:link></td>
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
