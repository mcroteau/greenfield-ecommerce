<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<div class="content">
		
			<h1>${productOptionInstance.name} : Edit Variant
				<g:link controller="productOption" action="edit" name="edit" class="btn btn-default pull-right" id="${productOptionInstance.id}">Back</g:link>
			</h1>
			
			<div class="clear" style="margin-top:20px;"></div>


			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
		
			<g:uploadForm controller="productOption" action="update_variant" method="post" >
			
				<div style="width:500px; border:solid 1px #ddd; padding:20px; background:#f8f8f8">
				
					<input type="hidden" name="id" value="${variant.id}"/>
            	
					<div class="form-group">
						<label for="name" class="col-sm-3 control-label">Name</label>
						<g:field class="form-control" name="name" value="${variant.name}" style="width:250px;"/>
					</div>
					
					<div class="form-group">
						<label for="price" class="col-sm-3 control-label">Adjustment Price $</label>
						<g:field class="form-control" name="price" value="${applicationService.formatPrice(variant?.price)}" style="width:80px;"/>
					</div>
				
				
					<g:if test="${variant.imageUrl}">
						<div class="form-group">
							<label class="col-sm-3 control-label">Current Image</label>
							<a href="/${applicationService.getContextName()}/${variant.imageUrl}">
								<img src="/${applicationService.getContextName()}/${variant.imageUrl}" style="height:50px;width:50px;"/>
							</a>
						</div>
					</g:if>
				
				
					<div class="form-group">
						<label class="col-sm-3 control-label">New Image</label>
						<input type="file" name="image" id="image" style="display:inline-block" />	
					</div>
            	

            		<g:submitButton name="update" class="btn btn-primary pull-right" value="Update Variant" />

					<g:link controller="productOption" action="edit" name="edit" class="btn btn-default pull-right" id="${productOptionInstance.id}" style="margin-right:10px;">Back</g:link>
        	
					<div class="clear"></div>
					
				</div>
				
			</g:uploadForm>
			
			
			
			<div class="clear"></div>
			
				
		</div>

	
	</body>
</html>
