
<%@ page import="org.greenfield.ApplicationService" %>
<%@ page import="org.greenfield.Product" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('org.greenfield.CurrencyService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title>Greenfield : Products</title>
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
			.floatleft{
				float:left;
			}
		</style>
		
	</head>
	<body>

		<div id="list-product" class="content scaffold-list" role="main">
			
			<h2 class="floatleft">
				<g:message code="products"/>
			</h2>
			
			
			<div style="float:right; width:570px; ">
				<g:link controller="product" action="create" class="btn btn-primary pull-right"><g:message code="new.product"/></g:link>
				<g:form action="admin_search" class="form-horizontal">
					<g:submitButton name="submit" value="${message(code:'search')}" id="search" class="btn btn-info"/>
					<input type="text" name="query" value="${query}" id="searchbox" class="form-control" style="width:250px;" placeholder="${message(code:'search.by.name')}"/>
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
							<th style="width:65px;"></th>
					
							<g:sortableColumn property="id" title="${message(code:'id')}" />
						
						
							<g:sortableColumn property="name" title="${message(code: 'name', default: 'Name')}" />
						
							<g:sortableColumn property="quantity" title="${message(code: 'quantity', default: 'Quantity')}" />
						
							<g:sortableColumn property="price" title="${message(code: 'price', default: "${currencyService.format('Price')}")}" />

							<g:sortableColumn property="salesPrice" title="${message(code: 'sales.price', default: "${currencyService.format('Sales Price')}")}" />
							
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
										style="height:30px;width:30px;"/>
									</g:if>
									<g:else>
										<img src="/${applicationService.getContextName()}/images/app/no-image.jpg"  
										style="height:30px;width:30px;border:solid 1px #ddd"/>
									</g:else>
								</g:link>
							</td>
							
							<td><g:link action="show" id="${productInstance.id}">${fieldValue(bean: productInstance, field: "id")}</g:link></td>
						
							<td>${fieldValue(bean: productInstance, field: "name")}</td>
						
							
							<td>${fieldValue(bean: productInstance, field: "quantity")}</td>
							
							
							<td>${currencyService.format(applicationService.formatPrice(productInstance.price))}</td>
							
						
							<td>${currencyService.format(applicationService.formatPrice(productInstance.salesPrice))}</td>
						
						
							<td><g:link controller="product" action="edit" params="[id: productInstance.id]" class="edit-product-${productInstance.id}"><g:message code="edit"/></g:link></td>
						</tr>
					</g:each>
					</tbody>
				</table>
				<div class="btn-group">
					<g:paginate total="${productInstanceTotal}" params="${[query:query]}" />
				</div>
				<g:link controller="product" action="outofstock" class="pull-right"><g:message code="out.of.stock"/></g:link>
			</g:if>
			<g:else>

				<div class="alert alert-info"><g:message code="no.products.found"/><br/><br/>
				</div>
			
				<g:if test="${catalogCount == 0}">

					<div class="alert alert-info">
						<strong><g:message code="no.catalogs.products"/></strong>
						<br/>
						<g:message code="create.catalog.products"/>
					
						<br/><br/>
					
						<g:link controller="catalog" action="create" class="btn btn-primary"><g:message code="create.catalog"/></g:link>
					</div>
					
				</g:if>
			</g:else>
		</div>
	</body>
</html>
