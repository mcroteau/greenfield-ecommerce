<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

${applicationService.getHeader(catalogInstance, "Products", false)}


<%

println request

println params

def pageParams = [:]

Collection<?> keys = params.keySet()
for (Object param : keys) {

    def optionIds = params.list(param)
    if(param != "action" &&
            param != "controller" &&
            param != "id" &&
            param != "offset" &&
            param != "max" &&
            optionIds){


    }
}

%>



<div class="breadcrumbs">
	${applicationService.getBreadcrumbs(catalogInstance)}
</div>


<h2>${catalogInstance?.name}</h2>

<p>${catalogInstance?.description}</p>


<g:if test="${products}">

	<div class="catalog-products-header">
	
		<div class="catalog-products-header-pagination">
			<g:paginate 
				max="12"
				maxsteps="5"
				total="${productsTotal}"
		        params="${[id: catalogInstance?.id, "test": "test" ]}"
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
	
			<g:link controller="product" action="details" id="${productInstance.id}">
				<p class="product-price">$${applicationService.formatPrice(productInstance.price)}</p>
	        </g:link>	
		</div>
	</g:each>
	<br style="clear:both"/>
	
	
	<div style="width:auto;float:right;margin-bottom:20px;">
		<g:paginate 
			max="12"
			maxsteps="5"
			total="${productsTotal}"
	        params="${[id: catalogInstance?.id ]}"
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

            var id = param + '-' + paramValue;

            if(id){
                var $checkbox = $('#' + id);
                if($checkbox){
                    $checkbox.prop('checked', true);
                }
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

                /**
                var filter = name + '=' + id

                var postparam = "&";
                if(checkedCount == count){
                    postparam = ""
                }
                filter += postparam
                filters += filter
                **/
            }
        });


        console.log("filtersMap", filtersMap);
        for(var filterKey in filtersMap){
            console.log('here...');
            var ids = filtersMap[filterKey]
            console.log(filterKey, ids)

            var filterParam = filterKey + "=" + ids.join("-") + "&"
            filtersParams += filterParam

        }

        filtersParams = filtersParams.substring(0, filtersParams.length - 1);
        console.log(filtersParams);


        var pre = protocol + '//' + host;

        var url = pre + path + filtersParams;
        window.location = url;
    }

</script>

${applicationService.getFooter()}
