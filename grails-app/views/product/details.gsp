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
	
${raw(applicationService.getHeader(catalogInstance, "${productInstance?.name} Product Details", true, params))}


<% if(catalogInstance){ %>
	<div class="breadcrumbs">
		${raw(applicationService.getBreadcrumbs(catalogInstance))}
	</div>
<%}%>


	<div class="product_details_wrapper">

		<g:if test="${flash.message}">
			<div class="alert alert-info" role="status">${raw(flash.message)}</div>
		</g:if>
		
		<div class="product_photos_wrapper">
			
			<g:if test="${productInstance?.detailsImageUrl}">
				<a href="/${applicationService.getContextName()}/${productInstance.imageUrl}" id="main_preview_link">
					<img 
						src="/${applicationService.getContextName()}/${productInstance?.detailsImageUrl}" id="main_preview" 
							style="width:300px;border:solid 1px #ddd"/><br/>
					<span style="font-size:10px; color:#888">Click to see full sized image</span>
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
				<style type="text/css">
					.sales-price{
						font-size:17px;
						text-decoration: line-through;
					}
					.on-sale{
						color:#fff;
						background:#d7182b;
						display:inline-block;
						padding:3px 7px;
						-webkit-border-radius: 3px;
						-moz-border-radius: 3px;
						border-radius: 3px;
					}
				</style>
					
				<g:if test="${productInstance.salesPrice}">
					<span class="on-sale">Sale</span>
					<span class="sales-price">$${applicationService.formatPrice(productInstance.price)}</span>
					<h2 class="product_price" style="margin-top:0px;">$${applicationService.formatPrice(productInstance.salesPrice)}</h2>
				</g:if>
				<g:else>
					<h2 class="product_price" style="margin-top:0px;">$${applicationService.formatPrice(productInstance.price)}</h2>
				</g:else>
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
				


				<g:if test="${applicationService.getSocialMediaEnabled() == "true"}">
					
					<g:if env="production">

						<div id="social-media-container">

							<div id="fb-share-button"
							 	class="fb-share-button" 
							 	data-href="https://developers.facebook.com/docs/plugins/" 
							 	data-layout="button" 
							 	data-size="small" 
							 	data-mobile-iframe="false"
							 	style="top:-5px">
							 	<a class="fb-xfbml-parse-ignore" target="_blank" 
							 		href="https://www.facebook.com/sharer/sharer.php?u=https%3A%2F%2Fdevelopers.facebook.com%2Fdocs%2Fplugins%2F&amp;src=sdkpreparse">Share</a>
							</div>

							 
							<a id="twitter-share-button" 
								  class="twitter-share-button social-media-button"
							  href="https://twitter.com/intent/tweet"
							  data-url="replaced-below"
							  data-size="small">
							Tweet</a>

					
							<div id="g-plus" 
								class="g-plus social-media-button" 
								data-action="share" 
								data-height="24" 
								data-href="replaced-below">
							</div>
							
						</div>

					</g:if>
				</g:if>

			</g:form>

		</div>
		

		<div class="description" style="clear:both;"><p>${productInstance.description}</p></div>


        <g:if test="${productInstance.productSpecifications}">
		    <div class="product-specifications">
                <h2>Product Specifications</h2>
                <table class="table">
                    <g:each in="${productInstance.productSpecifications}" var="productSpecification">
                        <tr style="border-top:dashed 1px #ccc;border-bottom:dashed 1px #ccc">
                            <th style="border:none">${productSpecification.specification.name}</th>
                            <td style="border:none">${productSpecification.specificationOption.name}</td>
                        </tr>
                    </g:each>
                </table>
		    </div>
		</g:if>

		<br style="clear:both"/>
	</div>



<g:if test="${applicationService.getSocialMediaEnabled() == "true"}">
	<g:if env="production">
		<script src="https://apis.google.com/js/platform.js" async defer></script>
		<div id="fb-root"></div>
	</g:if>
</g:if>


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




	/** Beginning social network code **/
	<g:if test="${applicationService.getSocialMediaEnabled() == "true"}">
		
		<g:if env="production">

			//Social Media
			var title = "${productInstance?.name} Details"
			var description = "${productInstance?.description}"
			var shareUrl = pre + "product/details/${productInstance.id}";
			var shareImageUrl = pre + "/${productInstance?.imageUrl}";
			
			var $head = $('head'),
				$googlePlus = $("#g-plus"),
				$facebookShare = $("#fb-share-button"),
				$twitterTweet = $('#twitter-share-button')

			$facebookShare.attr('data-href', shareUrl)
			$twitterTweet.attr('data-url', shareUrl)
			$googlePlus.attr('data-href', shareUrl)

			var ogUrl   = '<meta property="og:url" content="${shareUrl}"/>'
	  		var ogType  = '<meta property="og:type" content="website" />';
	  		var ogTitle = '<meta property="og:title" content="${title}" />';
	  		var ogDesc  = '<meta property="og:description" content="${description}" />';
	  		var ogImage = '<meta property="og:image" content="${shareImageUrl}" />';

	  		$head.append(ogUrl)
	  		$head.append(ogType)
	  		$head.append(ogTitle)
	  		$head.append(ogDesc)
	  		$head.append(ogImage)







			//Twitter
			window.twttr = (function(d, s, id) {
			  var js, fjs = d.getElementsByTagName(s)[0],
			    t = window.twttr || {};
			  if (d.getElementById(id)) return t;
			  js = d.createElement(s);
			  js.id = id;
			  js.src = "https://platform.twitter.com/widgets.js";
			  fjs.parentNode.insertBefore(js, fjs);

			  t._e = [];
			  t.ready = function(f) {
			    t._e.push(f);
			  };

			  return t;
			}(document, "script", "twitter-wjs"));
			//End Twitter


			//Facebook
			(function(d, s, id) {
			  var js, fjs = d.getElementsByTagName(s)[0];
			  if (d.getElementById(id)) return;
			  js = d.createElement(s); js.id = id;
			  js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.9";
			  fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));
			//End Facebook

		</g:if>
	</g:if>
	/** End social network code **/


});
</script>


<br class="clear"/>


${raw(applicationService.getFooter())}
