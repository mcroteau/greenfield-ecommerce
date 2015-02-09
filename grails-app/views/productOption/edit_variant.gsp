<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Edit Variant</title>
	</head>
	<body>
	
		<div class="form-outer-container">
		
		
			<div class="form-container">
		
				<h2>Edit Variant
					<g:link controller="productOption" action="edit" name="edit" class="btn btn-default pull-right" id="${productOptionInstance.id}">Back to Product Option</g:link>
				</h2>
				
				<div class="clear" style="margin-top:20px;"></div>
            	
            	
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
				
		    	
				<g:uploadForm controller="productOption" action="update_variant" method="post" >
				
					<div style="width:550px;">
					
						<input type="hidden" name="id" value="${variant.id}"/>
		    	
						<div class="form-row">
							<span class="form-label medium">Product</span>
							<span class="input-container">
								<span class="label label-default">${productOptionInstance.product.name}</span>	
							</span>
							<br class="clear"/>
						</div>
					
				
						<div class="form-row">
							<span class="form-label medium">Product Option</span>
							<span class="input-container">
								<span class="label label-default">${productOptionInstance.name}</span>	
							</span>
							<br class="clear"/>
						</div>
					
					
						<div class="form-row">
							<span class="form-label medium">Variant Name</span>
							<span class="input-container">
								<g:field class="form-control" name="name" value="${variant.name}" style="width:250px;"/>
							</span>
							<br class="clear"/>
						</div>
						
						
						<div class="form-row">
							<span class="form-label medium">Adjustment Price $</span>
							<span class="input-container">
								<g:field class="form-control" name="price" value="${applicationService.formatPrice(variant?.price)}" style="width:80px;"/>
							</span>
							<br class="clear"/>
						</div>
					
					
						<g:if test="${variant.imageUrl}">
							<div class="form-row">
								<span class="form-label medium">Current Image</span>
								<span class="input-container">
									<a href="/${applicationService.getContextName()}/${variant.imageUrl}">
										<img src="/${applicationService.getContextName()}/${variant.imageUrl}" style="height:30px;width:30px;"/>
									</a>
								</span>
								<br class="clear"/>
							</div>
						</g:if>
					
					
						<div class="form-row">
							<span class="form-label medium">New Image</span>
							<span class="input-container">
								<input type="file" name="image" id="image" style="display:inline-block" />	
							</span>
							<br class="clear"/>
						</div>
            		
            	
						<div class="buttons-container">
            	        	
							<g:link controller="productOption" action="edit" name="edit" class="btn btn-default" id="${productOptionInstance.id}" style="margin-right:10px;">Back</g:link>
							
            				<g:submitButton name="update" class="btn btn-primary" value="Update Variant" />
        				</div>
				
						<div class="clear"></div>
						
					</div>
					
				</g:uploadForm>
				
				
				
				<div class="clear"></div>
				
			</div>
				
		</div>

	
	</body>
</html>
