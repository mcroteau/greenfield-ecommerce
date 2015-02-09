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
			
			<h2>Additional Photos
				<g:link action="edit" name="edit" class="btn btn-default pull-right" id="${productInstance.id}">Back to Product</g:link>
			</h2>
			
			
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<div style="width:450px;">
			
				<g:uploadForm action="add_additional_photo" method="post" >
					
					<input type="hidden" name="id" value="${productInstance.id}"/>
					
					<div style="background:#f8f8f8;border:solid 1px #ddd; padding:15px; text-align:center">
						<div class="form-row" style="text-align:center">
							<span class="form-label medium secondary">Product</span>
							<span class="input-container">
								<span class="label label-default">${productInstance?.name}</span>
							</span>
							<br class="clear"/>
						</div>
						
						
						<div class="form-group">
							<input type="file" name="image" id="image" style="margin:auto" />	
							<span class="information secondary">Select Image to Upload.  File names may be changed on upload.</span>
						</div>
						
						<div class="form-group" style="margin-top:20px;">	
							
							<g:submitButton name="add" class="btn btn-primary" value="Upload Photo" />
							
						</div>
					</div>
				
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
										<img src="/${applicationService.getContextName()}/${photoInstance.detailsImageUrl}" style="height:30px;width:30px;"/>
									</a>
								</td>
								<td style="font-size:12px; ">${photoInstance.name}</td>
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
