
<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-catalog" class="content scaffold-list" role="main">
			
			<h2 class="">Catalogs
				<g:link controller="catalog" action="create" class="btn btn-primary pull-right" >New Catalog</g:link>
			</h2>
			
			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<g:if test="${catalogInstanceList.size() > 0}">
				<table class="table">
					<thead>
						<tr>
					
							<g:sortableColumn property="id" title="Id" />
					
							<g:sortableColumn property="name" title="${message(code: 'catalog.name.label', default: 'Name')}" />
						
							<th>url</th>
							<th></th>
							
						</tr>
					</thead>
					<tbody>
					<g:each in="${catalogInstanceList}" status="i" var="catalogInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						
							<td>${fieldValue(bean: catalogInstance, field: "id")}</td>
						
							<td><g:link controller="catalog" action="edit" params="[id: catalogInstance.id]" >${fieldValue(bean: catalogInstance, field: "name")}</g:link></td>
							
							<td>/${applicationService.getContextName()}/catalog/show/${catalogInstance.id} &nbsp;<a href="/${applicationService.getContextName()}/catalog/products/${URLEncoder.encode("${catalogInstance.id}", "UTF-8")}" target="_blank" class="information">Test</a></td>
							
							<td><g:link controller="catalog" action="edit" params="[id: catalogInstance.id]">Edit</g:link></td>
							
						</tr>
					</g:each>
					</tbody>
				</table>
				<div class="pagination">
					<g:paginate total="${catalogInstanceTotal}" />
				</div>
			</g:if>
			<g:else>
				<p>No Catalogs found..</p>
			</g:else>
		</div>	
	</body>
</html>
