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
	
		<div id="file-upload" class="content scaffold-create" role="main">
			
			<h1>Product Import</h1>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info">${flash.message}</div>
			</g:if>
			
			<g:if test="${flash.error}">
				<div class="alert alert-danger">${flash.error}</div>
			</g:if>
			
			
			
			<div style="width:600px;">
			
				<g:uploadForm action="import_products" method="post" >
				
					<div class="form-group">
						<label>Select Import File</label>
						<input type="file" name="file" id="file" />	
					</div>
					
					<div class="form-group" style="margin-top:20px;">	
						<g:link action="index" name="cancel" class="btn btn-default">Cancel</g:link>
						<g:submitButton name="add" class="btn btn-primary" value="Import Products" />
					</div>
					
					<p style="font-size:12px;" class="secondary">Products will need to be manually added to Catalogs after import</p>
					
					<p style="font-size:12px;" class="secondary">File should be comma separated value (csv) formatted with the following values</p>
					
					<p style="font-size:12px;" class="secondary">
						1. Name, 2. Quantity, 3. Price, 4. Weight, 5. Description
					</p>
					
					<ul style="font-size:12px;" class="secondary">
						<li>All columns must contain values</li>
						<li>No Headings</li>
						<li>No $ signs or commas in values</li>
						<li>Weight is in ounces</li>
					</ul>
					
				</g:uploadForm>
				
			</div>
		</div>
	</body>
</html>
