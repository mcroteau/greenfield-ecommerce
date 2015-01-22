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
				<div class="alert alert-info" role="status">${flash.message}</div>
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
					
					<p style="font-size:12px; color:#777">Product import will create catalogs if they do not already exist</p>
					
					<p style="font-size:12px; color:#777">File should be comma separated value (csv) formatted with the following values</p>
					
					<p style="font-size:12px; color:#777">
						1. Name, 2. Catalog, 3. Quantity, 4. Price, 5. Description
					</p>
					
					<ul style="font-size:12px; color:#777">
						<li>All columns must contain values</li>
						<li>No Headings</li>
						<li>No $ signs or commas in values</li>
					</ul>
					
				</g:uploadForm>
				
			</div>
		</div>
	</body>
</html>
