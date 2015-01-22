<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
		<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
		<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
	</head>
	<body>
	
		<div id="edit-catalog" class="content scaffold-edit" role="main">

			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
			
			<g:hasErrors bean="${catalogInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${catalogInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			
			<g:form method="post" >
				<g:hiddenField name="id" value="${catalogInstance?.id}" />
				<g:hiddenField name="version" value="${catalogInstance?.version}" />
			
				<div class="form-group">
					<label>URI</label>&nbsp;/${applicationService.getContextName()}/catalog/show/${catalogInstance.id} &nbsp;<a href="/${applicationService.getContextName()}/catalog/products/${URLEncoder.encode("${catalogInstance.id}", "UTF-8")}" class="btn btn-sm btn-default">Test</a>
				</div>
				
				<g:render template="form"/>
				
				<fieldset class="buttons">
			
					<g:actionSubmit class="btn btn-danger" action="delete" value="Delete" formnovalidate="" onclick="return confirm('Are you sure?');" />
				
					<g:actionSubmit class="btn btn-primary" action="update" value="Update" />
				
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
