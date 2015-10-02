<%@ page import="org.greenfield.ApplicationService" %>
<%@ page import="java.util.Map" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<%
	def catalogInstance = null
	def productCatalogIds = productInstance.catalogs.collect{ it.id }
	if(session.catalogInstance && productCatalogIds.contains(session?.catalogInstance?.id)){
		catalogInstance = session.catalogInstance
	}
%>
	
${applicationService.getHeader(catalogInstance, "Product Details", true)}


<% if(catalogInstance){ %>
	<div class="breadcrumbs">
		${applicationService.getBreadcrumbs(catalogInstance)}
	</div>
<%}%>


	<div class="product_details_wrapper">

		<g:if test="${flash.message}">
			<div class="alert alert-info" role="status">${flash.message}</div>
		</g:if>
		
		<div class="product_photos_wrapper">
			
			<g:if test="${productInstance?.detailsImageUrl}">
				<a href="/${applicationService.getContextName()}/${productInstance.imageUrl}" id="main_preview_link">
					<img 
						src="/${applicationService.getContextName()}/${productInstance?.detailsImageUrl}" 
						id="main_preview" 
						style="width:300px;border:solid 1px #ddd"/>
				</a>
			</g:if>
			<g:else>
				<img src="/${applicationService.getContextName()}/images/app/no-image.jpg" 
					id="main_preview" 
					style="width:300px;border:solid 1px #ddd"/>
			</g:else>
			
			
			
			<div class="images_preview">
				
				<%
					Map<String, Boolean> images = new HashMap<String, Boolean>();
					if(productInstance?.imageUrl){
						images.put("${productInstance?.imageUrl}", true);
					}
				%>
				
				<g:if test="${productInstance?.detailsImageUrl}">
					<img src="/${applicationService.getContextName()}/${productInstance?.detailsImageUrl}" 
						data-link="/${applicationService.getContextName()}/${productInstance?.imageUrl}" 
						style="height:50px;" 
						class="preview_image"/>
				</g:if>
				
				
				<g:if test="${productInstance.additionalPhotos.size() > 0 }">
					<g:each in="${productInstance.additionalPhotos}" status="i" var="photo">
						<img src="/${applicationService.getContextName()}/${photo.detailsImageUrl}" data-link="/${applicationService.getContextName()}/${photo?.imageUrl}" style="height:50px;" class="preview_image"/>	
						
						<%
							if(productInstance?.imageUrl &&
							 	!images.containsKey("${photo?.imageUrl}")){
								images.put("${photo?.imageUrl}", true);
							}
						%>
					</g:each>
				</g:if>
				
				
				
				<g:if test="${productOptions?.size() > 0}">
					<g:each in="${productOptions}" var="productOption">
						<g:if test="${productOption.variants?.size() > 0}">
							<g:each in="${productOption.variants}" var="variant">
								<%
									if(variant?.imageUrl &&
									 	!images.containsKey("${variant.imageUrl}")){
										images.put("${variant?.imageUrl}", true);
										%>
											<img src="/${applicationService.getContextName()}/${variant.imageUrl}" data-link="/${applicationService.getContextName()}/${variant?.imageUrl}" style="height:50px;" class="preview_image"/>
										<%
									}
								%>
							</g:each>
						</g:if>
					</g:each>
				</g:if>
			
			</div>
		</div>
		
		
		
		
		<div class="product_details">
			<g:form controller="shoppingCart" action="add" class="form-inline">
				<h1 class="product_name">${productInstance.name}</h1>
				<h2 class="product_price" style="margin-top:0px;">$${applicationService.formatPrice(productInstance.price)}</h2>
				
				<div style="text-align:left; margin-top:20px;">
				
					<g:each in="${productOptions}" var="productOption">
						<g:if test="${productOption.variants?.size() > 0}">
							<div class="form-group" style="margin-bottom:10px;">
								<label style="display:inline-block; width:100px; text-align:right; margin-right:10px;">${productOption.name}</label>
								<select name="product_option_${productOption.id}" class="product_option">
									<g:each in="${productOption.variants}" var="variant">
										<option value="${variant.id}" data-image="${variant.imageUrl}">
											${variant.name} 
											<g:if test="${variant.price > 0}">
												($${applicationService.formatPrice(variant.price)})
											</g:if>
										</option>
									</g:each>
								</select>
							</div>
						</g:if>
					</g:each>
				
					<div class="form-group">
						<label style="display:inline-block; width:100px; text-align:right; margin-right:10px;">Quantity</label>
						<input type="text" name="quantity" value="1" style="width:70px;" class="form-control" style="margin:5px auto !important"/>
					</div>
					
				</div>
				
				
				<br/>
				<br/>
				<input type="hidden" name="id" value="${productInstance.id}"/>

				<g:submitButton name="submit" class="btn btn-primary" id="submit" value="Add To Cart"/>
				
				
			</g:form>

		</div>
		

		<div class="description" style="clear:both;"><p>${productInstance.description}</p></div>
		
		<br style="clear:both"/>
	</div>

<script type="text/javascript">
$(document).ready(function(){
	
	var $main = $('#main_preview'),
		$mainLink = $('#main_preview_link'),
		$options = $('.product_option');
	
	var protocol = window.location.protocol,
		host = window.location.host;
	
	
	var context = "/${applicationService.getContextName()}/";
	var pre = protocol + '//' + host + context;
	
	var urlPre = pre + 'productOption/variant_image/';
	
	
	$options.change(displayImage);
	$('.preview_image').click(preview);
	
	
	function displayImage(event){
       	var $selected = $(this).find('option:selected');
		var image = $selected.data('image');
		if(image != ""){
			var src = pre + image;
			$main.attr('src', src);
		}
	}

	
	function preview(event){
		var $target = $(event.target);
		var source = $main.attr('src');
		var preSource = $target.attr('src');
		
		var link = $target.attr('data-link');
		
		$main.attr('src', preSource)
		$(this).attr('src', preSource)
		$mainLink.attr('href', link)
	}	

});
</script>


${applicationService.getFooter()}
