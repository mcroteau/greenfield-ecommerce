<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('org.greenfield.CurrencyService').newInstance()%>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<title>Greenfield : Edit Product</title>
</head>
<body>
	
	
	<div id="catalog-selection-backdrop"></div>
	
	<div id="catalog-selection-modal">
		
		<h3><g:message code="product.catalogs"/></h3>
		
		<p class="information secondary"><g:message code="select.subcatalog"/></p>
		
		<div id="catalog-selection-container">
			${raw(catalogIdSelectionList)}
		</div>
        
		<div class="alert alert-danger pull-left" style="margin-top: 8px; padding:5px 10px;width:385px">
            <strong><g:message code="warning"/>:</strong> <g:message code="changing.catalogs.remove"/>
        </div>
		
		<a href="javascript:" class="btn btn-default pull-right" style="margin-top:15px;" id="close-catalogs-select-modal"><g:message code="accept.close"/></a>
		<br class="clear"/>
	</div>
	
	
	<div class="form-outer-container">
	
		<div class="form-container">
		
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
					<div class="alert alert-warning"><g:message code="product.disabled"/></div>
				</g:if>
				
			</div>
			
			
			
			<h2><g:message code="edit.product"/>
				<g:link controller="product" action="list" class="btn btn-default pull-right"><g:message code="back.to.list"/></g:link>
				<br class="clear"/>
			</h2>
			
			<br class="clear"/>
			

			


			<g:uploadForm method="post" >

				<g:hiddenField name="id" value="${productInstance?.id}" />
				<g:hiddenField name="version" value="${productInstance?.version}" />
				
			
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="url"/></span>
					<span class="input-container">
						<span class="secondary">
							/${applicationService.getContextName()}/product/details/${productInstance?.id} &nbsp;
						</span>

						<a href="/${applicationService.getContextName()}/product/details/${URLEncoder.encode("${productInstance?.id}", "UTF-8")}" target="_blank"><g:message code="test"/></a>
						
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="name"/> 
						<span class="information secondary block"><g:message code="name.unique"/></span>
					</span>
					<span class="input-container">
						<input name="name" type="text" class="form-control threefifty" value="${productInstance?.name}"/>
					</span>
					<br class="clear"/>
				</div>
			
			
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="catalogs"/><br/>
						<a href="javascript:" id="catalog-selection-modal-link"><g:message code="add.remove.catalogs"/></a>
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
					<span class="form-label full secondary"><g:message code="price"/> ${currencyService.getCurrencySymbol()}</span>
					<span class="input-container">
						<input name="price" type="text" class="form-control " style="width:150px" value="${productInstance?.price}"/>
					</span>
					<br class="clear"/>
				</div>
				
			
			
				
				
			
			
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="sales.price"/> ${currencyService.getCurrencySymbol()}</span>
					<span class="input-container">
						<input name="salesPrice" type="text" class="form-control " style="width:150px; float:left;" value="${productInstance?.salesPrice ?productInstance?.salesPrice : '-'}" id="salesPrice"/>

						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:150px;"><g:message code="leave.blank"/></span>
					</span>
					<br class="clear"/>
				</div>
				
			
			
				
				
			
			
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="quantity"/></span>
					<span class="input-container">
						<input name="quantity" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.quantity}"/>
						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:150px;"><g:message code="quantity.zero"/></span>
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
					<span class="form-label full hint"><g:message code="main.image"/></span>
					<span class="input-container">
						<input type="file" name="image" id="image" />
					</span>
					<br class="clear"/>
				</div>
			
			
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="weight"/>
						<span class="information secondary" style="display:block">* <g:message code="weight.ounces"/></span>
					</span>
					<span class="input-container">
						<input name="weight" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.weight}"/>
						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:300px;"><g:message code="weight.calculations"/> <a href="http://www.easypost.com" target="_blank">EasyPost</a>. </span>
					</span>
					<br class="clear"/>
				</div>
			
			
			
			
				
				<div class="form-row">
					
					<span class="form-label full secondary"><g:message code="length"/>
						<span class="information secondary" style="display:block"><g:message code="length.width.height"/></span>
					</span>
					<span class="input-container">
						<input name="length" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.length}" />
					</span>
					
					<span class="form-label secondary" style="float:left"><g:message code="height"/></span>
					<span class="input-container">
						<input name="height" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.height}" />
					</span>
					
					<span class="form-label secondary" style=""><g:message code="width"/></span>
					<span class="input-container">
						<input name="width" type="text" class="form-control" style="width:75px;float:left" value="${productInstance?.width}" />
					</span>
					<br class="clear"/>
				</div>						
			
			
				
				
				<div class="form-row">
					<span class="form-label full hint"><g:message code="description"/>
						<span class="information secondary" style="display:block"><g:message code="accepts.html"/></span>	
					</span>
					
					<span class="input-container">
						
						<textarea  name="description" id="description" class="form-control" style="height:150px; width:380px;">${productInstance?.description}</textarea>
					</span>
					<br class="clear"/>
				</div>
				

				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="additional.photos"/>
						<g:link controller="product" action="additional_photos" id="${productInstance.id}" class="information" style="display:block"><g:message code="manage.additional.photos"/></g:link>
					</span>
					<span class="input-container sized">
						<g:if test="${productInstance.additionalPhotos.size() > 0}">
							<g:each in="${productInstance.additionalPhotos}" var="photo">
								<img src="/${applicationService.getContextName()}/${photo.imageUrl}"/>
							</g:each>
						</g:if>
						<g:else>
							<g:link controller="product" action="additional_photos" id="${productInstance.id}" class="btn btn-default"><g:message code="add.additional.photos"/></g:link>
						</g:else>
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="product.options"/>
						<g:link controller="product" action="product_options" id="${productInstance.id}" class="information" style="display:block"><g:message code="manage.product.options"/></g:link>
					</span>
					<span class="input-container sized">
						<g:if test="${productInstance.productOptions.size() > 0}">
							<g:each in="${productInstance.productOptions}" var="option">
								<span class="label label-default">${option.name}</span>
							</g:each>
						</g:if>
						<g:else>
							<g:link controller="product" action="add_product_option" id="${productInstance.id}" class="btn btn-default"><g:message code="add.product.options"/></g:link>
						</g:else>
					</span>
					<br class="clear"/>
				</div>
				



				<div class="form-row">
					<span class="form-label full secondary"><g:message code="product.specifications"/>
						<g:link controller="productSpecification" action="manage" id="${productInstance.id}" class="information" style="display:block"><g:message code="manage.product.specifications"/></g:link>
					</span>
					<span class="input-container sized">
						<g:if test="${productInstance.productSpecifications.size() > 0}">
							<g:each in="${productInstance.productSpecifications}" var="productSpecification">
								<span class="label label-default">${productSpecification.specificationOption.specification.name}:&nbsp;${productSpecification.specificationOption.name}</span>
							</g:each>
						</g:if>
						<g:else>
							<g:link controller="productSpecification" action="manage" id="${productInstance.id}" class="btn btn-default"><g:message code="add.product.specification"/></g:link>
						</g:else>
					</span>
					<br class="clear"/>
				</div>



					
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="product.item"/> # 
						<span class="information secondary block"><g:message code="product.optional"/></span>
					</span>
					<span class="input-container">
						<input name="productNo" type="text" class="form-control threefifty"
						style="width:150px;" 
						value="${productInstance?.productNo}"/>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="harmonized.code"/> 
						<span class="information secondary block"><g:message code="product.optional.harmonized"/></span>
					</span>
					<span class="input-container">
						<input name="harmonizedCode" type="text" class="form-control threefifty" value="${productInstance?.harmonizedCode}" id="harmonizedCode" style="width:209px"/>
					</span>
					<br class="clear"/>
				</div>
			
				
				  
	 			<div class="form-row">
	 				<span class="form-label full secondary"><g:message code="layout"/></span>
	 				<span class="input-container">
						<g:select name="layout.id"
								from="${layouts}"
								value="${productInstance?.layout?.id}"
								optionKey="id" 
								optionValue="name" 
								class="form-control"/>
	 				</span>
	 				<br class="clear"/>
	 			</div>
				
				
				
				<div class="form-row">
					<span class="form-label full hint"><g:message code="disabled"/></span>
					<span class="input-container">
						<g:checkBox name="disabled" value="${productInstance.disabled}" />
					</span>
					<br class="clear"/>
				</div>
				

				
				<div class="form-row">
					<span class="form-label full hint"><g:message code="purchaseable"/></span>
					<span class="input-container">
						<g:checkBox name="purchaseable" value="${productInstance.purchaseable}" />
					</span>
					<br class="clear"/>
				</div>
				
				
				
				<div class="buttons-container">	
					<g:set var="sure" value="${message(code:'are.you.sure')}"/>
	
					<g:actionSubmit controller="product" action="delete" value="${message(code:'delete')}" formnovalidate="" onclick="return confirm('${sure}');"  class="btn btn-danger" />
					
					<g:actionSubmit action="update" name="update" class="btn btn-primary" value="${message(code:'update.product')}"/>
				</div>
				
			</g:uploadForm>
		
		</div>
		
	</div>

<script type="text/javascript">
	var BUTTON_TEXT = "${message(code:'add.catalogs')}";
	var MESSAGE = "${message(code:'no.catalogs.selected')}"
</script>	
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
