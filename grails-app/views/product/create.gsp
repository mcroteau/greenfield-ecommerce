<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('org.greenfield.CurrencyService').newInstance()%>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<title>Greenfield : Create Product</title>
</head>
<body>
	
	<div id="catalog-selection-backdrop"></div>
	
	<div id="catalog-selection-modal">
		
		<h3><g:message code="product.catalogs"/></h3>
		
		<p class="information secondary"><g:message code="selecting.subcatalog"/></p>
		
		<div id="catalog-selection-container">
			${raw(catalogIdSelectionList)}
		</div>
		
		<a href="javascript:" class="btn btn-default pull-right" style="margin-top:15px;" id="close-catalogs-select-modal"><g:message code="accept.close"/></a>
		<br class="clear"/>
	</div>
	
	
	
	
	<div class="form-outer-container">
				
		<div class="form-container" >
			
			<h2><g:message code="create.product"/>
				<g:link controller="product" action="list" class="btn btn-default pull-right"><g:message code="back"/></g:link>
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
					<span class="form-label full secondary"><g:message code="name"/> 
						<span class="information secondary block"><g:message code="name.unique"/></span>
					</span>
					<span class="input-container">
						<input name="name" type="text" class="form-control threefifty" value="${productInstance?.name}" id="name"/>
					</span>
					<br class="clear"/>
				</div>
				
				
			
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="catalogs"/><br/>
						<a href="javascript:" id="catalog-selection-modal-link"><g:message code='add.remove.catalogs'/></a>
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
						<input name="price" type="text" class="form-control " style="width:150px" value="${productInstance?.price}" id="price"/>
					</span>
					<br class="clear"/>
				</div>
				
				

		
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="sales.price"/> ${currencyService.getCurrencySymbol()}</span>
					<span class="input-container">
						<input name="salesPrice" type="text" class="form-control " style="width:150px; float:left;" value="${productInstance?.salesPrice ? productInstance.salesPrice : '-'}" id="salesPrice"/>

						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:150px;"><g:message code="leave.blank"/></span>
					</span>
					<br class="clear"/>
				</div>
				
			
			
				
				<div class="form-row">
					<span class="form-label full secondary"><g:message code="quantity"/></span>
					<span class="input-container">
						<%
							def quantity =  productInstance?.quantity
							if(quantity == 0)quantity = 1
						%>
						<input name="quantity" type="text" class="form-control" style="width:75px;float:left" value="${quantity}" id="quantity"/>
						<span class="information secondary" style="float:left; display:inline-block; margin-left:15px;width:150px;"><g:message code="quantity.zero"/></span>
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
					<span class="form-label full secondary"><g:message code="product.item"/> # 
						<span class="information secondary block"><g:message code="product.optional"/></span>
					</span>
					<span class="input-container">
						<input name="productNo" type="text" class="form-control threefifty" value="${productInstance?.productNo}" id="productNo" style="width:150px"/>
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
								class="form-control threehundred"/>
	 				</span>
	 				<br class="clear"/>
	 			</div>
				
			
				
				<div class="buttons-container">	
					<g:submitButton name="create" class="btn btn-primary" value="Save Product"/>
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
