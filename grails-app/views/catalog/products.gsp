<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

${applicationService.getHeader(catalogInstance, "Products")}

<style type="text/css">
.catalog-product{
	width:180px;
	height:265px;
	float:left;
	margin:10px 10px;
}

.catalog-product img{
	width:180px;
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

.nextLink,
.step,
.currentStep,
.prevLink {
    background-color: #fff;
    border: 1px solid #ddd;
    color: #428bca;
    float: left;
    line-height: 1.42857;
    margin-left: -1px;
    padding: 6px 12px;
    position: relative;
    text-decoration: none !important;
}
.nextLink:hover,
.step:hover,
.currentStep:hover,
.prevLink:hover{
	text-decoration:none;
}
.currentStep {
    background-color: #428bca;
    border-color: #428bca;
    color: #fff;
    cursor: default;
    z-index: 2;
}

</style>

<h2>${catalogInstance?.name}</h2>

<p>${catalogInstance?.description}</p>

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


<g:paginate total="${productsTotal}"
        params="${[id: catalogInstance?.id ]}"
		class="pull-right" />
		
${applicationService.getFooter()}
