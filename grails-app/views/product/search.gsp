<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${applicationService.getHeader("Search Results")}
<style type="text/css">
.catalog-product{
	width:170px;
	float:left;
	height:265px;
	margin:10px 15px;
}
.catalog-product img{
	width:170px;
	text-align:center;
	border:solid 1px #ddd;
}
.catalog-product p{
	margin:0px;
	padding:0px;
}
.catalog-product a{
	color:#222;
}

.product-name,
.product-price{
	text-align:center
}

.product-name{
	color:#222;
}
.product-price{
	font-size:17px;
	font-weight:bold;
}
</style>

<h1>Search Results</h1>
			
<g:if test="${flash.message}">
	<div class="alert alert-info" role="status">${flash.message}</div>
</g:if>
			
<g:if test="${products?.size() > 0}">
	<g:each in="${products}" status="i" var="productInstance">
		
		<div class="catalog-product">
			<g:link controller="product" action="details" id="${productInstance.id}">
				<div class="product-image">
					<img src="/${applicationService.getContextName()}/${productInstance.detailsImageUrl}" style="width:200px;" />
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
</g:if>
<g:else>
		<p>No search results found</p>
</g:else>

${applicationService.getFooter()}
