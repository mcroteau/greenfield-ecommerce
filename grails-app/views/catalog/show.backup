
<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-catalog" class="content scaffold-show" role="main">

			<h1><g:message code="default.show.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
		
			<div class="form-group">
				<label>URI</label>&nbsp;/${applicationService.getContextName()}/catalog/show/${catalogInstance.id} &nbsp;<a href="/${applicationService.getContextName()}/catalog/products/${URLEncoder.encode("${catalogInstance.id}", "UTF-8")}" class="btn btn-sm btn-default">Test</a>
			</div>
			
		
			<div class="form-group">
				<label>Name</label>&nbsp;${catalogInstance.name}
			</div>
			
			<div class="form-group">
				<label>Description</label>&nbsp;<p>${catalogInstance.description}</p>
			</div>
			
			
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${catalogInstance?.id}" />
					
					<g:actionSubmit class="btn btn-danger" action="delete" value="Delete" onclick="return confirm('Are you sure?');" />
					
					<g:link class="btn btn-default" action="edit" id="${catalogInstance.id}">Edit</g:link>
					
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
