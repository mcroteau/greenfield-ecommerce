<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
		<title>Greenfield : Import/Upload</title>
	</head>
	<body>
		
		<style type="text/css">
			.btn{
				margin-bottom:20px;
				width:175px;
			}
		</style>
		
		<div id="create-catalog" class="content scaffold-create" role="main">
		
			<h2>Configuration</h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:hasErrors bean="${catalogInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${catalogInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
	
			<br/>
			
			<g:link uri="/configuration/uploads" class="btn btn-default">
				<span class="glyphicon glyphicon-upload"></span>
				Uploads
			</g:link>
			<br/>
			
			<!--
			<g:link uri="/configuration/import_products_view" class="btn btn-default">
				<span class="glyphicon glyphicon-import"></span>
				Import Products
			</g:link>
			<br/>
			-->
			
			<g:link uri="/importExportData/export_data_view" class="btn btn-default">
				<span class="glyphicon glyphicon-import"></span>
				Export All Data
			</g:link>
			<br/>
			
			<g:link uri="/importExportData/import_data_view" class="btn btn-default">
				<span class="glyphicon glyphicon-import"></span>
				Import Data
			</g:link>
			<br/>
			
		</div>
	</body>
</html>

						
	
	