<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title>Greenfield : Active Shopping Carts</title>
		<style type="text/css">
			input#searchbox{
				float:right;
			}
			#search{
				float:right;
				margin-left:1px;
				margin-right:10px;
			}
			#list-product h2{
				float:left;
			}
			.floatleft{
				float:left;
			}
		</style>
		
	</head>
	<body>

	<h2>Active Shopping Carts</h2>
	
	
	<g:if test="${activeShoppingCarts}">
		<table class="table">
			<tr>
				<th>Date</th>
				<th>Products</th>
			</tr>
			<g:each in="${activeShoppingCarts}" var="activeShoppingCart">
				<tr>
					<td><g:formatDate format="dd MMM yyyy hh:mm z" date="${activeShoppingCart.dateCreated}"/>
					</td>
					<td>
						<g:if test="${activeShoppingCart.shoppingCartItems}">
							<table>
							<g:each in="${activeShoppingCart.shoppingCartItems}" var="shoppingCartItem">
								<tr>
									<td>${shoppingCartItem?.product?.name}</td>
								</tr>
							</g:each>
							</table>
						</g:if>
						<g:else>
							No products in this cart
						</g:else>
					</td>
			</g:each>
		</table>	

		<div class="btn-group">
			<g:paginate total="${totalActiveShoppingCarts}" />
		</div>
	</g:if>
	
	</body>
</html>