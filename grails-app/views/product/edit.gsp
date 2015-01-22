<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="edit-product" class="content scaffold-edit" role="main">
			
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:if test="${productInstance.disabled}">
				<div class="alert alert-warning">
					This product is currently disabled.
				</div>
			</g:if>
			
			
			<g:hasErrors bean="${productInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${productInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			
			
			
			<g:uploadForm method="post" >
			
				<div style="width:280px; margin-right:50px; float:left">
					<g:hiddenField name="id" value="${productInstance?.id}" />
					<g:hiddenField name="version" value="${productInstance?.version}" />
					
					<g:render template="form"/>
					
				</div>
				
				<div style="float:left;">
                	
					<div class="form-group">
						<div class="image-background" style=" background:#efefef;text-align:center; padding:5px; border:solid 1px #ddd">
							<g:if test="${productInstance.detailsImageUrl}">
								<img 
									src="/${applicationService.getContextName()}/${productInstance.detailsImageUrl}" style="width:220px;"/>	
							</g:if>
							<g:else>
								<img 
									src="/${applicationService.getContextName()}/images/app/no-image.jpg" style="width:220px;"/>	
							</g:else>
						</div>
					
						<br/>
						Upload new file ?
						<input type="file" name="image" id="image" />	
						<br/>
						<strong>${productInstance?.additionalPhotos?.size()}</strong> Additional Photos
						<g:link action="additional_photos" id="${productInstance.id}" class="btn btn-default">Manage Additional Photos</g:link>

						<br/>
						<br/>
						<strong>${productInstance?.productOptions?.size()}</strong> Product Options
						<g:link action="product_options" id="${productInstance.id}" class="btn btn-default">Manage Options</g:link>
						
					</div>
                </div>
				
				<br style="clear:both;"/>
				
				<div class="form-group">
					  		
					<g:actionSubmit class="btn btn-danger" action="delete" value="Delete" formnovalidate="" onclick="return confirm('Are you sure?');" />
					
					<g:actionSubmit class="btn btn-primary" action="update" value="Update" />
		
				</div>
			</g:uploadForm>
		</div>
	</body>
</html>
