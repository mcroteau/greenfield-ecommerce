<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

${applicationService.getHeader(catalogInstance, "Products")}



<div class="breadcrumbs">
	${applicationService.getBreadcrumbs(catalogInstance)}
</div>


<h2>${catalogInstance?.name}</h2>

<p>${catalogInstance?.description}</p>


<g:if test="${products}">

	<div class="catalog-products-header">
	
		<div class="catalog-products-header-pagination">
			<g:paginate total="${productsTotal}"
		        params="${[id: catalogInstance?.id ]}"
				class="pull-right" />
		</div>
		
		<div class="catalog-products-header-count">
			<% if(offset && max){
				def first = Integer.parseInt(request.offset) + 1
				def last = first + max - 1%>
				Showing <strong>${first}-${last}</strong> of <strong>${productsTotal}</strong> Results
			<%}else{%>
				<strong>${productsTotal}</strong> Total Results
			<%}%>
		</div>
		<br class="clear"/>	
			
	</div>
	
		
	<g:each in="${products}" status="i" var="productInstance">
		
		<div class="catalog-product">
			<g:link controller="product" action="details" id="${productInstance.id}">
				<div class="product-image">
					<g:if test="${productInstance.detailsImageUrl}">
						<img src="/${applicationService.getContextName()}/${productInstance.detailsImageUrl}" />
					</g:if>
					<g:else>
						<img src="/${applicationService.getContextName()}/images/app/no-image.jpg" style="border:solid 1px #ddd">
					</g:else>
				</div>
			</g:link>
	
			<g:link controller="product" action="details" id="${productInstance.id}">
				<p class="product-name">${productInstance.name}</p>
			</g:link>
	
			<g:link controller="product" action="details" id="${productInstance.id}">
				<p class="product-price">$${applicationService.formatPrice(productInstance.price)}</p>
	        </g:link>	
		</div>
	</g:each>
	<br style="clear:both"/>
	
	
	<div style="width:auto;float:right;margin-bottom:20px;">
		<g:paginate total="${productsTotal}"
	        params="${[id: catalogInstance?.id ]}"
			class="pull-right" />
	</div>
	<br class="clear"/>		

</g:if>
<g:else>
	<p>No products found for selected catalog</p>
</g:else>
		
${applicationService.getFooter()}
