<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title>Edit Product</title>
	</head>
	<body>
	
	<div class="form-outer-container">
		
		
		<div class="form-container">
			
			<h1 class="maintenance-header">Edit Product
				<g:link controller="product" action="list" class="btn btn-default pull-right">Back</g:link>
				<br class="clear"/>
			</h1>
			
			<br class="clear"/>
			

			
			<div class="messages">
			
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
					
				<g:hasErrors bean="${productInstance}">
					<div class="alert alert-danger">
						<ul>
							<g:eachError bean="${productInstance}" var="error">
								<li><g:message error="${error}"/></li>
							</g:eachError>
						</ul>
					</div>
				</g:hasErrors>
				
				<g:if test="${productInstance.disabled}">
					<div class="alert alert-warning">Product is currently disabled</div>
				</g:if>
				
			</div>



			<g:uploadForm method="post" >

				<g:hiddenField name="id" value="${productInstance?.id}" />
				<g:hiddenField name="version" value="${productInstance?.version}" />
				
			
				<div class="form-row">
					<span class="form-label full secondary">Url</span>
					<span class="input-container">
						<span class="secondary">
							/${applicationService.getContextName()}/product/details/${productInstance?.id} &nbsp;
						</span>

						<a href="/${applicationService.getContextName()}/product/details/${URLEncoder.encode("${productInstance?.id}", "UTF-8")}" target="_blank">Test</a>
						
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label full secondary">Name 
						<span class="information secondary block">Name must be unique</span>
					</span>
					<span class="input-container">
						<input name="name" type="text" class="form-control threefifty" value="${productInstance?.name}"/>
					</span>
					<br class="clear"/>
				</div>
			
			
				
				<div class="form-row">
					<span class="form-label full secondary">Catalog</span>
					<span class="input-container">
						<g:select id="catalog" name='catalog.id' 
							value="${productInstance?.catalog?.id}"
						    noSelection="${['null':'Select One...']}"
						    from='${Catalog?.list()}'
						    optionKey="id" 
							optionValue="name"
							class="form-control autowidth"></g:select>
					</span>
					<br class="clear"/>
				</div>

			
			
				
				<div class="form-row">
					<span class="form-label full secondary">Price $</span>
					<span class="input-container">
						<input name="price" type="text" class="form-control " style="width:150px" value="${productInstance?.price}"/>
					</span>
					<br class="clear"/>
				</div>
				
				
			
			
				
				<div class="form-row">
					<span class="form-label full secondary">Quantity</span>
					<span class="input-container">
						<input name="quantity" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.quantity}"/>
						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:150px;">Quantity of 0 will categorize product as “out of stock”</span>
					</span>
					<br class="clear"/>
				</div>
				
			
			
				<div class="form-row">
					<span class="form-label full secondary"></span>
					<span class="input-container">
						<g:if test="${productInstance.detailsImageUrl}">
							<img src="/${applicationService.getContextName()}/${productInstance.detailsImageUrl}" height="50" width="50" style="margin-bottom:0px !important"/>
						</g:if>
						<g:else>
							<img src="/${applicationService.getContextName()}/images/app/no-image.jpg" height="50" width="50" style="margin-bottom:0px !important"/>
						</g:else>
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label full hint">Main Image</span>
					<span class="input-container">
						<input type="file" name="image" id="image" />
					</span>
					<br class="clear"/>
				</div>
			
			
				
				<div class="form-row">
					<span class="form-label full secondary">Weight</span>
					<span class="input-container">
						<input name="weight" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.weight}"/>
						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:300px;">Weight is required at a minimum to calculate shipping realtime via <a href="http://www.easypost.com" target="_blank">EasyPost</a>. </span>
					</span>
					<br class="clear"/>
				</div>
			
			
			
			
				
				<div class="form-row">
					
					<span class="form-label full secondary">Length
						<span class="information secondary" style="display:block">Length, Width &amp; Height are optional for rate calculation</span>
					</span>
					<span class="input-container">
						<input name="length" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.length}" />
					</span>
					
					<span class="form-label secondary" style="float:left">Height</span>
					<span class="input-container">
						<input name="height" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.height}" />
					</span>
					
					<span class="form-label secondary" style=>Width</span>
					<span class="input-container">
						<input name="width" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.width}" />
					</span>
					<br class="clear"/>
				</div>						
			
			
				
				
				<div class="form-row">
					<span class="form-label full hint">Description
						<span class="information secondary" style="display:block">Accepts HTML as well as plain text</span>	
					</span>
					
					<span class="input-container">
						
						<textarea  name="description" id="description" class="form-control" style="height:150px; width:380px;">${productInstance?.description}</textarea>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				
			
				
				<div class="form-row">
					<span class="form-label full secondary">Additional Photos
						<g:link controller="product" action="additional_photos" id="${productInstance.id}" class="information" style="display:block">Manage Additional Photos</g:link>
					</span>
					<span class="input-container sized">
						<g:if test="${productInstance.additionalPhotos.size() > 0}">
							<g:each in="${productInstance.additionalPhotos}" var="photo">
								<img src="/${applicationService.getContextName()}/${photo.imageUrl}"/>
							</g:each>
						</g:if>
						<g:else>
							<g:link controller="product" action="additional_photos" id="${productInstance.id}" class="btn btn-default">Add Additional Photos</g:link>
						</g:else>
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label full secondary">Product Options
						<g:link controller="product" action="product_options" id="${productInstance.id}" class="information" style="display:block">Manage Product Options</g:link>
					</span>
					<span class="input-container sized">
						<g:if test="${productInstance.productOptions.size() > 0}">
							<g:each in="${productInstance.productOptions}" var="option">
								<span class="label label-default">${option.name}</span>
							</g:each>
						</g:if>
						<g:else>
							<g:link controller="product" action="add_product_option" id="${productInstance.id}" class="btn btn-default">Add Product Options</g:link>
						</g:else>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				
				<div class="form-row">
					<span class="form-label full hint">Disabled</span>
					<span class="input-container">
						<g:checkBox name="disabled" value="${productInstance.disabled}" />
					</span>
					<br class="clear"/>
				</div>
				
				
				
				
				<div class="buttons-container">	
				
					<g:actionSubmit controller="product" action="delete" value="Delete" formnovalidate="" onclick="return confirm('Are you sure?');"  class="btn btn-danger" />
					
					<g:submitButton name="update" class="btn btn-primary" value="Update Product"/>
				</div>
				
			</g:uploadForm>
		
		</div>
		
	</div>
	
	</body>
</html>
