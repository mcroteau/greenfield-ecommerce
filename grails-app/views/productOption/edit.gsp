<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Edit Product Option</title>
	</head>
	<body>
		
		<div class="content">
		
			<h2>Edit Product Option
				<g:link controller="product" action="product_options" name="edit" class="btn btn-default pull-right" id="${productInstance.id}">Product Options</g:link>
			</h2>
			
			<br class="clear"/>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<div style="width:355px;float:left">
			
				<g:form action="update" method="post">
			
					<input type="hidden" name="id" value="${productOptionInstance.id}"/>
					
				
					<div class="form-row">
						<span class="form-label secondary">Name 
						</span>
						<span class="input-container">
							<input type="text" name="name" class="form-control" value="${productOptionInstance?.name}" style="width:150px;display:inline-block"/>
							<g:submitButton name="update" class="btn btn-primary" value="Update Name" />
						</span>
						<br class="clear"/>
					</div>
					

					
					
				</g:form>
				
			
				<g:if test="${variants?.size() > 0}">
					
					<h4 style="margin-top:20px">Current Variants</h4>
					
					<table class="table table-condensed">
						<thead>
							<tr>
								<th></th>
								<th>Name</th>
								<th>Adjustment<br/> Price</th>
								<th><g:link action="edit_variant_positions" id="${productOptionInstance.id}" class="btn btn-default btn-xs pull-right">Edit Ordering <span class="glyphicon glyphicon-sort"></span></g:link></th>
							</tr>
						</thead>
						<tbody>
						<g:each in="${variants}" status="i" var="variant">
							<tr id="variant_${variant.id}">
								<td>
									<g:if test="${variant.imageUrl}">
										<a href="/${applicationService.getContextName()}/${variant.imageUrl}">
											<img src="/${applicationService.getContextName()}/${variant.imageUrl}" style="height:30px;width:30px;"/>
										</a>
									</g:if>
								</td>
								<td>${variant.name}</td>
								<td>$${applicationService.formatPrice(variant?.price)}</td>
								<td>
									<g:link class="btn btn-default" action="edit_variant" id="${variant.id}">Edit</g:link>
									<g:form action="remove_variant" method="post" id="${variant.id}" style="display:inline-block;">
										<g:actionSubmit class="btn btn-default" controller="productOption" action="remove_variant" value="Delete" id="${variant.id}" formnovalidate="" onclick="return confirm('Are you sure?');" />
									</g:form>
								</td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</g:if>			
				<g:else>
					<div class="alert alert-info">No variants added yet</div>
				</g:else>
				
			</div>	
			
			
		
			<g:uploadForm controller="productOption" action="add_variant" method="post" >
			
				<div style="width:350px; float:left; margin-left:40px; border:solid 1px #ddd; padding:15px; background:#f8f8f8">
				
					<g:if test="${flash.variantMessage}">
						<div class="alert alert-info" role="status">${flash.variantMessage}</div>
					</g:if>
				
					<h3 style="margin:0px auto 20px; text-align:center">Add Variant</h3>
					
					<input type="hidden" name="id" value="${productOptionInstance.id}"/>
            	
				
				
					<div class="form-row">
						<span class="form-label minimum secondary">Name 
						</span>
						<span class="input-container">
							<g:field class="form-control" name="name" value="${name}" style="width:175px;"/>
						</span>
						<br class="clear"/>
					</div>
					
				
					<div class="form-row">
						<span class="form-label minimum secondary">Price Adjustment 
						</span>
						<span class="input-container">
							<g:field class="form-control" name="price" value="${price}" style="width:80px;"/>
						</span>
						<br class="clear"/>
					</div>
					
					
				
					<div class="form-row">
						<span class="form-label secondary" style="width:75px">Image</span>
						<span class="input-container">
							<input type="file" name="image" id="image" style="display:inline-block" />	
						</span>
						<br class="clear"/>
					</div>
					
					
            	
            		<g:submitButton name="add" class="btn btn-primary pull-right" value="Add Variant" />
				
				</div>
				
			</g:uploadForm>
			
			
			
			<div class="clear"></div>
			
				
		</div>

	
	</body>
</html>
