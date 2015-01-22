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
		
			<h1>${productInstance.name} : Edit Product Option
				<g:link controller="product" action="product_options" name="edit" class="btn btn-default pull-right" id="${productInstance.id}">Back</g:link>
			</h1>
			
			<div class="clear" style="margin-top:20px;"></div>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<div style="width:400px;float:left">
			
				<g:form action="update" method="post">
			
					<input type="hidden" name="id" value="${productOptionInstance.id}"/>
					
					<span>Name</span>&nbsp;&nbsp;
					<input type="text" name="name" class="form-control" value="${productOptionInstance?.name}" style="width:200px;display:inline-block"/>
					
					<g:submitButton name="update" class="btn btn-primary" value="Update Name" />
					
				</g:form>
				
			
				<g:if test="${variants?.size() > 0}">
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
											<img src="/${applicationService.getContextName()}/${variant.imageUrl}" style="height:50px;width:50px;"/>
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
			
				<div style="width:400px; float:left; margin-left:40px; border:solid 1px #ddd; padding:20px; background:#f8f8f8">
				
					<g:if test="${flash.variantMessage}">
						<div class="alert alert-info" role="status">${flash.variantMessage}</div>
					</g:if>
				
					<h3 style="margin:0px auto 20px;">Add Option Variant</h3>
					
					<input type="hidden" name="id" value="${productOptionInstance.id}"/>
            	
					<div class="form-group">
						<label for="name" class="col-sm-3 control-label">Name</label>
						<g:field class="form-control" name="name" value="${name}" style="width:250px;"/>
					</div>
					
					<div class="form-group">
						<label for="price" class="col-sm-3 control-label">Adjustment Price $</label>
						<g:field class="form-control" name="price" value="${price}" style="width:80px;"/>
					</div>
				
					<div class="form-group">
						<label class="col-sm-3 control-label">Image</label>
						<input type="file" name="image" id="image" style="display:inline-block" />	
					</div>
            	
            		<g:submitButton name="add" class="btn btn-primary pull-right" value="Add Variant" />
				
				</div>
				
			</g:uploadForm>
			
			
			
			<div class="clear"></div>
			
				
		</div>

	
	</body>
</html>
