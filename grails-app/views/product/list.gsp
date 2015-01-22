
<%@ page import="org.greenfield.ApplicationService" %>
<%@ page import="org.greenfield.Product" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
		<style type="text/css">
			input#searchbox{
				float:right;
			}
			#search{
				float:right;
				margin-left:1px;
				margin-right:10px;
			}
			#list-product h2{
				float:left;
			}
		</style>
		
	</head>
	<body>

		<div id="list-product" class="content scaffold-list" role="main">
			
			<h2>Products</h2>
			
			
			<div style="margin-top:20px;float:right; width:600px; border:solid 0px #ddd">
				<g:link controller="product" action="create" class="btn btn-primary pull-right">New Product</g:link>
				<g:form action="admin_search" class="form-horizontal">
					<g:submitButton name="submit" value="Search" id="search" class="btn btn-info"/>
					<input type="text" name="query" value="" id="searchbox" class="form-control" style="width:250px;" placeholder="search by name and description"/>
				</g:form>
			</div>
			
			<br style="clear:both;"/>
			<br style="clear:both;"/>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:if test="${productInstanceList.size() > 0}">
			
				<table class="table" >
					<thead>
						<tr>
							<th></th>
					
							<g:sortableColumn property="id" title="Id" />
						
						
							<g:sortableColumn property="name" title="${message(code: 'product.name.label', default: 'Name')}" />
						
							<g:sortableColumn property="quantity" title="${message(code: 'product.quantity.label', default: 'Quantity')}" />
						
							<g:sortableColumn property="price" title="${message(code: 'product.price.label', default: 'Price')}" />
						
							<g:sortableColumn property="catalog" title="Catalog" />
							
							<th></th>
						
						</tr>
					</thead>
					<tbody>
					<g:each in="${productInstanceList}" status="i" var="productInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						
							<td>
								<g:link action="show" id="${productInstance.id}">
									<g:if test="${productInstance.detailsImageUrl}">
										<img src="/${applicationService.getContextName()}/${productInstance.detailsImageUrl}"  
										style="height:50px;width:50px;"/>
									</g:if>
									<g:else>
										<img src="/${applicationService.getContextName()}/images/app/no-image.jpg"  
										style="height:50px;width:50px;border:solid 1px #ddd"/>
									</g:else>
								</g:link>
							</td>
							
							<td><g:link action="show" id="${productInstance.id}">${fieldValue(bean: productInstance, field: "id")}</g:link></td>
						
							<td>${fieldValue(bean: productInstance, field: "name")}</td>
						
							
							<td>${fieldValue(bean: productInstance, field: "quantity")}</td>
							
							<td>$${applicationService.formatPrice(productInstance.price)}</td>
						
							<td>${productInstance.catalog.name}</td>
						
							<td><g:link controller="product" action="edit" params="[id: productInstance.id]" class="btn btn-default">Edit</g:link></td>
						</tr>
					</g:each>
					</tbody>
				</table>
				<div class="btn-group">
					<g:paginate total="${productInstanceTotal}" />
				</div>
				<g:link controller="product" action="outofstock" class="pull-right">Out of Stock</g:link>
			</g:if>
			<g:else>
				<p>No products found</p>
			</g:else>
		</div>
	</body>
</html>
