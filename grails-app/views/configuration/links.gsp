<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		<style type="text/css">
			.section{
				margin:10px 20px 30px 0px;
			}
		</style>
	</head>
	<body>
		
		<div id="create-catalog" class="content scaffold-create" role="main">
		
			<h1>Storefront links</h1>
		
			<p>Below is a list of application links/uris.  The links below will allow the application adminstrator to customize the layout and link to important functionality.</p>
		
			<div class="section">	
				<p><strong>Shopping Cart : </strong> /shoppingCart</p>
				<p>Links to current customer's shopping cart</p>
				<g:link uri="/shoppingCart" class="btn btn-default">
					<span class="glyphicon glyphicon-shopping-cart"></span>
					Shopping Cart
				</g:link>
			</div>
			
			
			<div class="section">	
				<p><strong>Order History: </strong> /account/order_history</p>
				<p>Customer's store order history</p>
				<g:link uri="/transaction/history" class="btn btn-default">
					<span class="glyphicon glyphicon-th-list"></span>
					Order History
				</g:link>
			</div>
			

			<div class="section">	
				<p><strong>Account Profile: </strong> /account/profile</p>
				<p>Links to Customer's personal account information</p>
				<g:link uri="/account/profile" class="btn btn-default">
					<span class="glyphicon glyphicon-user"></span>
					Account
				</g:link>
			</div>
			
			

			<div class="section">	
				<p><strong>Login: </strong> /auth/customer_login</p>
				<p>Links to login screen</p>
				<g:link uri="/account/customer_login" class="btn btn-default">
					<span class="glyphicon glyphicon-share"></span>
					Login Screen
				</g:link>
			</div>
			
			

			<div class="section">	
				<p><strong>Register: </strong> /auth</p>
				<p>Customer's registration screen</p>
				<g:link uri="/account/registration" class="btn btn-default">
					<span class="glyphicon glyphicon-new-window"></span>
					Customer Registration
				</g:link>
			</div>

			<div class="section">	
				<p><strong>Catalog Products: </strong> /catalog/{catalog name}/products</p>
				<p>Will link to the specified catalog's products screen by catalog name</p>
			</div>

			<div class="section">	
				<p><strong>Catalog Products: </strong> /catalog/{id}/products</p>
				<p>Will link to the specified catalog's products screen by catalog id</p>
			</div>
			
		</div>
	</body>
</html>

						
	
	