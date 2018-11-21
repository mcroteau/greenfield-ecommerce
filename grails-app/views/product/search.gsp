<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getDefaultHeader("Search Results"))}

<h3>Search Results for "${query}"</h3>
			
<g:if test="${flash.message}">
	<div class="alert alert-info" role="status">${flash.message}</div>
</g:if>
			
<g:if test="${products?.size() > 0}">
	

	<div class="catalog-products-header-pagination">
		<g:paginate 
			max="12"
			maxsteps="5"
			total="${products.totalCount}"
	        params="${[ query : query ]}"
			class="pull-right" />
	</div>
	
	
	<div class="catalog-products-header-count">
		<% if(offset && max){
			def first = Integer.parseInt(request.offset) + 1
			def last = first + max - 1
			if(last > products.totalCount)last = products.totalCount
		%>
			Showing <strong>${first}-${last}</strong> of <strong>${products.totalCount}</strong> Results
		<%}else{%>
			<strong>${products.totalCount}</strong> Total Results
		<%}%>
	</div>
	
	
	<br class="clear"/>
	
	
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
				<p class="product-name" id="product-name-${productInstance.id}">${productInstance.name}</p>
			</g:link>
	
			<g:link controller="product" action="details" id="${productInstance.id}">
				<p class="product-price">$${applicationService.formatPrice(productInstance.price)}</p>
	        </g:link>	
		</div>
	</g:each>
	<br style="clear:both"/>
	<br style="clear:both"/>
	
	<div class="catalog-products-header-pagination">
		<g:paginate 
			max="12"
			maxsteps="5"
			total="${products.totalCount}"
	        params="${[ query : query ]}"
			class="pull-right" />
	</div>
	
	
	<script type="text/javascript">
		$(document).ready(function(){
			$('#search-input').val("${query}");
		})
	</script>
	
</g:if>
<g:else>
		<p>No search results found</p>
</g:else>

${raw(applicationService.getDefaultFooter())}
