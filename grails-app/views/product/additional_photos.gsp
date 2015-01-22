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
	
		<div id="create-product" class="content scaffold-create" role="main">
			
			<h1>${productInstance.name} : Additional Photos</h1>
			
			
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<div style="width:500px;">
			
				<g:uploadForm action="add_additional_photo" method="post" >
				
					<input type="hidden" name="id" value="${productInstance.id}"/>
					
					<div class="form-group">
						<label>Additional Image</label>
						<input type="file" name="image" id="image" />	
					</div>
					
					<div class="form-group" style="margin-top:20px;">	
						<g:link action="edit" name="edit" class="btn btn-default" id="${productInstance.id}">Cancel</g:link>
						<g:submitButton name="add" class="btn btn-primary" value="Upload Photo" />
						
					</div>
					<p>File names will be changed when uploaded.</p>
				</g:uploadForm>
				
				<g:if test="${productInstance.additionalPhotos.size() > 0}">
					<table class="table">
						<thead>
							<tr>	
								<th></th>
								<th>Name</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<g:each in="${productInstance.additionalPhotos}" status="i" var="photoInstance">
							<tr>
								<td>
									<a href="/${applicationService.getContextName()}/${photoInstance.imageUrl}">
										<img src="/${applicationService.getContextName()}/${photoInstance.detailsImageUrl}" style="height:50px;width:50px;"/>
									</a>
								</td>
								<td>${photoInstance.name}</td>
								<td>
									<g:link action="remove_additional_photo" class="btn btn-default" id="${photoInstance.id}">Remove</g:link>
								</td>
							</tr>
							</g:each>
						</tbody>
					</table>
				</g:if>
			</div>
		</div>
	</body>
</html>
