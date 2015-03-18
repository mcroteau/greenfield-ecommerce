<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
	<title>Create Product</title>
</head>
<body>
	
	
	<div id="catalog-selection-backdrop"></div>
	
	<div id="catalog-selection-modal">
		<h3>Product Catalogs</h3>
		<p class="information secondary">Selecting a Subcatalog will automatically select all parent Catalogs up to the top level Catalog.</p>
		<div id="catalog-selection-container">
			${catalogIdSelectionList}
		</div>
		<a href="javascript:" class="btn btn-default pull-right" style="margin-top:15px;" id="close-catalogs-select-modal">Accept &amp; Close</a>
		<br class="clear"/>
	</div>
	
	
	
	
	<div class="form-outer-container">
				
		<div class="form-container" >
			
			<h2>Create Product
				<g:link controller="product" action="list" class="btn btn-default pull-right">Back</g:link>
				<br class="clear"/>
			</h2>
			
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
			</div>

			<g:uploadForm action="save" class="form-horizontal" >
				
				
				<div class="form-row">
					<span class="form-label full secondary">Name 
						<span class="information secondary block">Name must be unique</span>
					</span>
					<span class="input-container">
						<input name="name" type="text" class="form-control threefifty" value="${productInstance?.name}" id="name"/>
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
						<input name="price" type="text" class="form-control " style="width:150px" value="${productInstance?.price}" id="price"/>
					</span>
					<br class="clear"/>
				</div>
				
				
			
			
				
				<div class="form-row">
					<span class="form-label full secondary">Quantity</span>
					<span class="input-container">
						<input name="quantity" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.quantity}" id="quantity"/>
						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:150px;">Quantity of 0 will categorize product as “out of stock”</span>
					</span>
					<br class="clear"/>
				</div>
				
			
			
				<div class="form-row">
					<span class="form-label full secondary"></span>
					<span class="input-container">
						<img src="/${applicationService.getContextName()}/images/app/no-image.jpg" height="50" width="50" style="margin-bottom:0px !important"/>
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
						<input name="weight" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.weight}" id="weight"/>
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
				
				
				
				<div class="buttons-container">	
					<g:submitButton name="create" class="btn btn-primary" value="Save Product"/>
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
