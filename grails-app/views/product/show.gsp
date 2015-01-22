
<%@ page import="org.greenfield.ApplicationService" %>
<%@ page import="org.greenfield.Product" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
			
	<h1>Show Product</h1>
			
	<div style="float:left; width:300px; margin-right:30px;">
	
		<div id="show-product" class="content scaffold-show" role="main">
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			<g:if test="${productInstance.disabled}">
				<div class="alert alert-warning">This product is currently disabled</div>
			</g:if>
			
			<fieldset class="form">
			
				<div class="form-group">
					<label>Name</label>
					<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${productInstance}" field="name"/></span>
				</div>
				
			
				<div class="form-group">
					<label>Quantity</label>
					<span class="property-value" aria-labelledby="quantity-label"><g:fieldValue bean="${productInstance}" field="quantity"/></span>
				</div>
				
				<div class="form-group">
					<label>Price</label>
					<span class="property-value">$${applicationService.formatPrice(productInstance?.price)}</span>
					
				</div>
		
		
				<div class="form-group">
					<label>Catalog</label>
					<span class="property-value" aria-labelledby="catalog-label">${productInstance.catalog.name}</span>
				</div>
				
			
			
				<div class="form-group">
					<label>Length</label>
					<span class="property-value" aria-labelledby="length-label"><g:fieldValue bean="${productInstance}" field="length"/></span>
				</div>
			
			
				<div class="form-group">
					<label>Width</label>
					<span class="property-value" aria-labelledby="width-label"><g:fieldValue bean="${productInstance}" field="width"/></span>
				</div>
			
			
				<div class="form-group">
					<label>Height</label>
					<span class="property-value" aria-labelledby="height-label"><g:fieldValue bean="${productInstance}" field="height"/></span>
				</div>
				
			
				<div class="form-group">
					<label>Weight</label>
					<span class="property-value" aria-labelledby="weight-label"><g:fieldValue bean="${productInstance}" field="weight"/></span>
				</div>
				
				
				
				
				<div class="form-group">
					<label>Description</label>
					<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${productInstance}" field="description"/></span>
				</div>
				
			
				<div class="form-group">
					<label>Disabled/Hidden</label>
					<span class="property-value" aria-labelledby="productNo-label">${productInstance.disabled}</span>
				</div>
			
				<div class="clear"></div>
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${productInstance?.id}" />
					
					<g:actionSubmit class="btn btn-danger" action="delete" value="Delete" onclick="return confirm('Are you sure?');" />
					
					<g:link class="btn btn-default" action="edit" id="${productInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					
				</fieldset>
			</g:form>
		</div>
	</div>
	
	<div style="float:left;" >

		<g:if test="${productInstance.imageUrl}">
			<div class="form-group">
				<div class="image-background" style=" background:#efefef;text-align:center; padding:5px; border:solid 1px #ddd">
					<img src="/${applicationService.getContextName()}/${productInstance.imageUrl}" style="width:220px;"/>
				</div>
			
				<br/>
				<label>Photo Url</label>
				<br/>
				<span style="font-size:10px; color:#777;">
					${productInstance.imageUrl}
				</span>
				
			</div>
		</g:if>
		<g:else>
			<div class="form-group">
				<div class="image-background" style=" background:#efefef;text-align:center; padding:5px; border:solid 1px #ddd">
					no image
				</div>
			</div>
		</g:else>
		
	</div>

	<div class="clear"></div>
	
	</body>
</html>
