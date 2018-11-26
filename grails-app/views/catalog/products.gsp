<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


${raw(applicationService.getCatalogHeader(catalogInstance, "Products", false, params))}

<%
def pageParams = [:]
pageParams["id"] = catalogInstance?.id

Collection<?> keys = params.keySet()
for (Object param : keys) {
    def optionIdsString = params.get(param)
    if(param != "action" &&
            param != "controller" &&
            param != "id" &&
            param != "offset" &&
            param != "max" &&
            optionIdsString){
        pageParams[param] = optionIdsString
    }
}
%>

<div class="breadcrumbs">
	${raw(applicationService.getBreadcrumbs(catalogInstance))}
</div>


<h2>${catalogInstance?.name}</h2>

<p>${raw(catalogInstance?.description)}</p>


<g:if test="${products}">

	<div class="catalog-products-header">
	
		<div class="catalog-products-header-pagination">
			<g:paginate 
				max="12"
				maxsteps="5"
				total="${productsTotal}"
		        params="${pageParams}"
				class="pull-right" />
		</div>
		
		<div class="catalog-products-header-count">
			<% if(offset && max){
				def first = Integer.parseInt(request.offset) + 1
				def last = first + max - 1
				if(last > productsTotal)last = productsTotal
			%>
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
				<p class="product-name" id="product-name-${productInstance.id}">${productInstance.name}</p>
			</g:link>
	
			<style type="text/css">
				.price-details span{
					display:block;
					text-align:center;
					border:solid 0px #ddd;
				}
				.sales-price{
					font-size:12px;
					text-decoration: line-through;
				}
			</style>
				
			<g:link controller="product" action="details" id="${productInstance.id}">
				<g:if test="${productInstance.salesPrice}">
					<div class="price-details">
						<span class="sales-price">$${applicationService.formatPrice(productInstance.price)}</span>
						<span class="product-price">$${applicationService.formatPrice(productInstance.salesPrice)}</span>
					</div>
				</g:if>
				<g:else>
					<div class="price-details">
						<span class="product-price">$${applicationService.formatPrice(productInstance.price)}</span>
						<span class="sales-price"></span>
					</div>
				</g:else>
	        </g:link>
            
            <%--
            <g:if test="${productInstance.productSpecifications.size() > 0}">
                <g:each in="${productInstance.productSpecifications}" var="productSpecification">
                    <span class="label label-default">${productSpecification.specificationOption.specification.name}:&nbsp;${productSpecification.specificationOption.name}</span>
                </g:each>
            </g:if>
            --%>
            
		</div>
	</g:each>
	<br style="clear:both"/>
	
	
	<div style="width:auto;float:right;margin-bottom:20px;">
		<g:paginate 
			max="12"
			maxsteps="5"
			total="${productsTotal}"
		    params="${pageParams}"
			class="pull-right" />
	</div>
	<br class="clear"/>		

</g:if>
<g:else>
	<p>No products found for selected catalog</p>
</g:else>


<script type="text/javascript">
    var $filters = $('.catalog-filter-checkbox');

	var protocol = window.location.protocol,
	    host = window.location.host,
	    path = window.location.pathname,
	    href = window.location.href;

    var paramsIndex = href.indexOf("?");
    var post = "",
        postParams = [];

    if(paramsIndex > -1){
        post = href.substring(paramsIndex + 1, href.length);
        postParams = post.split('&')
    }


    if(!$.isEmptyObject(postParams)){
        $(postParams).each(function(index, fullParam){
            var index = fullParam.indexOf("=");
            var param = fullParam.substring(0, index);
            var paramValue = fullParam.substring(index + 1, fullParam.length)

            var values = paramValue.split("-")

            if(values){
                $(values).each(function(index, value){
                    var id = param + '-' + value;
                    if(id){
                        var $checkbox = $('#' + id);
                        if($checkbox){
                            $checkbox.prop('checked', true);
                        }
                    }
                });
            }
        })
    }


    if(!$.isEmptyObject($filters)){
        $filters.change(filterProducts)
    }


    function filterProducts(event){

        var filtersParams = "?";

        var count = 0;
        var checkedCount = $('#catalog-filter-container :checkbox:checked').length;

        var filtersMap = {};

        $filters.each(function(index, checkbox){
            var $checkbox = $(checkbox);
            if($checkbox.is(':checked')){

                var name = $checkbox.data('name');
                var id = $checkbox.data('option-id');

                if(name in filtersMap){
                    filtersMap[name].push(id)
                }else{
                    filtersMap[name] = []
                    filtersMap[name].push(id)
                }
            }
        });


        for(var filterKey in filtersMap){
            var ids = filtersMap[filterKey]

            var filterParam = filterKey + "=" + ids.join("-") + "&"
            filtersParams += filterParam

        }

        filtersParams = filtersParams.substring(0, filtersParams.length - 1);

        var pre = protocol + '//' + host;

        var url = pre + path + filtersParams;
        window.location = url;
    }

</script>

${raw(applicationService.getCatalogFooter(catalogInstance, false, params))}
