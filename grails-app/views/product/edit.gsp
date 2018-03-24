<%@ page import="org.greenfield.Product" %>
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
	
	
	<div id="catalog-selection-backdrop"></div>
	
	<div id="catalog-selection-modal">
		<h3>Product Catalogs</h3>
		<p class="information secondary">Selecting a Subcatalog will automatically select all parent Catalogs up to the top level Catalog.</p>
		<div id="catalog-selection-container">
			${raw(catalogIdSelectionList)}
		</div>
        <div class="alert alert-danger pull-left" style="margin-top: 8px; padding:5px 10px;width:385px">
            <strong>Warning:</strong> Changing catalogs will remove any product specifications for this product
        </div>
		<a href="javascript:" class="btn btn-default pull-right" style="margin-top:15px;" id="close-catalogs-select-modal">Accept &amp; Close</a>
		<br class="clear"/>
	</div>
	
	
	<div class="form-outer-container">
	
		<div class="form-container">
			
			<h2>Edit Product
				<g:link controller="product" action="list" class="btn btn-default pull-right">Back to List</g:link>
				<br class="clear"/>
			</h2>
			
			<br class="clear"/>
			

			
			<div class="messages">
			
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${raw(flash.message)}</div>
				</g:if>
					
			
				<g:if test="${flash.error}">
					<div class="alert alert-danger" role="status">${raw(flash.error)}</div>
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
					<span class="form-label full secondary">Catalogs<br/>
						<a href="javascript:" id="catalog-selection-modal-link">Add/Remove Catalogs</a>
					</span>
					<span class="input-container threefifty" id="selected-catalogs-span">
						<g:each in="${productInstance?.catalogs}" var="catalog">
							<span class="label label-default">${catalog.name}</span>
						</g:each>
					</span>
					<input type="hidden" value="" id="catalogIds" name="catalogIds"/>
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
					<span class="form-label full secondary">Sale Price $</span>
					<span class="input-container">
						<input name="salesPrice" type="text" class="form-control " style="width:150px; float:left;" value="${productInstance?.salesPrice ?productInstance?.salesPrice : 0}" id="salesPrice"/>

						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:150px;">Leave blank or enter same price for no sale price</span>
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
							<a href="/${applicationService.getContextName()}/${productInstance.detailsImageUrl}" target="_blank"><img src="/${applicationService.getContextName()}/${productInstance.detailsImageUrl}" height="50" width="50" style="margin-bottom:0px !important"/></a>
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
					<span class="form-label full secondary">Weight
						<span class="information secondary" style="display:block">* Important : Weight is in ounces</span>
					</span>
					<span class="input-container">
						<input name="weight" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.weight}"/>
						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:300px;">Weight is in ounces & is required at a minimum to calculate shipping realtime via <a href="http://www.easypost.com" target="_blank">EasyPost</a>. </span>
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
					<span class="form-label full secondary">Product Specifications
						<g:link controller="productSpecification" action="manage" id="${productInstance.id}" class="information" style="display:block">Manage Product Specifications</g:link>
					</span>
					<span class="input-container sized">
						<g:if test="${productInstance.productSpecifications.size() > 0}">
							<g:each in="${productInstance.productSpecifications}" var="productSpecification">
								<span class="label label-default">${productSpecification.specificationOption.specification.name}:&nbsp;${productSpecification.specificationOption.name}</span>
							</g:each>
						</g:if>
						<g:else>
							<g:link controller="productSpecification" action="manage" id="${productInstance.id}" class="btn btn-default">Add Product Specification</g:link>
						</g:else>
					</span>
					<br class="clear"/>
				</div>



					
				<div class="form-row">
					<span class="form-label full secondary">Product No. 
						<span class="information secondary block">Optional identifier used else where</span>
					</span>
					<span class="input-container">
						<input name="productNo" type="text" class="form-control threefifty"
						style="width:150px;" 
						value="${productInstance?.productNo}"/>
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
					
					<g:actionSubmit action="update" name="update" class="btn btn-primary" value="Update Product"/>
				</div>
				
			</g:uploadForm>
		
		</div>
		
	</div>
	

<script type="text/javascript" src="${resource(dir:'js/product_catalogs.js')}"></script>
		
<script type="text/javascript">
	
	var catalogIds = [];
	<g:if test="${catalogIdsArray}">
		catalogIds = ${catalogIdsArray};
	</g:if>
	var catalogIdsString = catalogIds.join();

</script>	
	
</body>
</html>
