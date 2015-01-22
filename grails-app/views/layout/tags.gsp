<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Available Layout Tags</title>
		
		<style type="text/css">
			ul{
			}
			ul li{
				list-style:square;
			}
			
			.tag-summary{
				padding:20px;
				margin:20px auto;
				background:#f8f8f8;
				border-radius:3px;
				border:dashed 1px #ddd;
			}
			
			h4{
				color:#3381b9;
			}
			pre{
				font-size:12px;
			}
		</style>
		
	</head>
	<body>
		
		<h2>Available Tags
			<g:link action="index" class="btn btn-default">Back</g:link>
		</h2>
		
		<div class="clear"></div>
		
		<p class="instructions">Tags can be used within the layout html.  <strong>Context</strong> is referring to the application name when deployed</p>
		
		
		<p class="instructions">All available layout tags are below.  Click for details</p>
		
		<ol>
			<li><a href="#context">[[CONTEXT_NAME]]</a></li>
			<li><a href="#catalogs">[[CATALOGS]]</a></li>
			<li><a href="#search-box">[[SEARCH_BOX]]</a></li>
			<li><a href="#shopping-cart">[[SHOPPING_CART]]</a></li>
			<li><a href="#account">[[ACCOUNT]]</a></li>
			<li><a href="#greeting">[[GREETING]]</a></li>
			<li><a href="#login">[[LOGIN]]</a></li>
			<li><a href="#logout">[[LOGOUT]]</a></li>
			<li><a href="#register">[[REGISTER]]</a></li>
			<li><a href="#order-history">[[ORDER_HISTORY]]</a></li>
			<li><a href="#admin-link">[[ADMIN_LINK]]</a></li>
		</ol>
		
		
		
		<div class="tag-summary" id="context">
			<h4>[[CONTEXT_NAME]]</h4>
			<p>Important tag, will be replaced with the value of <strong>app.context</strong> defined in <strong>web-app -> settings -> settings.properties</strong>.</p>
			
		</div>
		
		

		<div class="tag-summary" id="catalogs">
			<h4>[[CATALOGS]]</h4>
			<p>Displays all Catalogs with products associated.</p>
			
			<p><strong>example output : </strong>
<pre>
&lt;ul class="catalogs-list"&gt;
	&lt;li class="catalog-link">
		&lt;a href="/context/catalog/products/1"&gt;Catalog 1&lt;/a&gt;
	&lt;/li&gt;
	&lt;li class="catalog-link">
		&lt;a href="/context/catalog/products/2"&gt;Catalog 2&lt;/a&gt;
	&lt;/li&gt;
	&lt;li class="catalog-link">
		&lt;a href="/context/catalog/products/3"&gt;Catalog 3&lt;/a&gt;
	&lt;/li&gt;
&lt;/ul&gt;
</pre>
			</p>
		</div>
		
		
		<div class="tag-summary" id="search-box">
			<h4>[[SEARCH_BOX]]</h4>
			
			<p>Displays a search form allowing customers to search products</p>
			
			<p><strong>output : </strong>
<pre>
&lt;form action="context/product/search"&gt; 
	&lt;div id="searchbox"&gt;
		&lt;input type="text" name="query" name="search"/&gt; 
		&lt;input type="submit" id="search-button" value="Search" name="search-button"/&gt;
	&lt;/div&gt;
&lt;/form&gt;
</pre>
			</p>
		</div>
		
		
		
		<div class="tag-summary" id="shopping-cart">
			<h4>[[SHOPPING_CART]]</h4>
			
			<p>Displays link to Store shopping cart</p>
			
			<p><strong>output : </strong>
<pre>
&lt;a href="/context/shoppingCart" id="shopping-cart"&gt;Shopping Cart&lt;/a&gt;
</pre>
			</p>
		</div>
		
		
		
		<div class="tag-summary" id="account">
			<h4>[[ACCOUNT]]</h4>
			
			<p>Displays link to Customer Account profile information</p>
			
			<p><strong>output : </strong>
<pre>
&lt;a href="/context/account/customer_profile" id="my-account"&gt;My Account&lt;/a&gt;
</pre>
			</p>
	
		</div>
		
		
		
		<div class="tag-summary" id="greeting">
			<h4>[[GREETING]]</h4>
			
			<p>Displays greeting to Customer if logged in.</p>
			
			<p><strong>output : </strong>
<pre>
&lt;span id="greeting"&gt;Welcome back &lt;a href="/context/account/customer_profile"&gt;username&lt;/a&gt;&lt;/span&gt;
</pre>			
			</p>
		</div>
		
		
		
		<div class="tag-summary" id="login">
			<h4>[[LOGIN]]</h4>
			
			<p>Displays link to log in</p>
			
			<p><strong>output : </strong>
<pre>
&lt;a href="/context/auth/customer_login" id="login"&gt;Login&lt;/a&gt;
</pre>
			</p>
			
		</div>
		
		
		
		<div class="tag-summary" id="logout">
			<h4>[[LOGOUT]]</h4>
			
			<p>Displays link to log out</p>
			
			<p><strong>output : </strong>
<pre>
&lt;a href="/context/auth/signOut" id="logout"&gt;Logout&lt;/a&gt;
</pre>
			</p>
			
		</div>
		
		
		
		<div class="tag-summary" id="register">
			<h4>[[REGISTER]]</h4>
			
			<p>Displays link to Register as Customer with Store</p>
			
			<p><strong>output : </strong>
<pre>
&lt;a href="/context/account/customer_registration" id="register"&gt;Register&lt;/a&gt;
</pre>
			</p>
		</div>
		
		
		
		<div class="tag-summary" id="order-history">
			<h4>[[ORDER_HISTORY]]</h4>
			
			<p>Displays link to Customer's Order History</p>
			
			<p><strong>output : </strong>
<pre>
&lt;a href="/context/account/order_history" id="account-order-history"&gt;Order History&lt;/a&gt;
</pre>
			</p>			
		</div>
		
		
		
		<div class="tag-summary" id="admin-link">
			<h4>[[ADMIN_LINK]]</h4>
			
			<p>Displays link to Adminstration section</p>
			
			<p><strong>output : </strong>
<pre>
&lt;a href="/context/admin" id="admin-link"&gt;Administration&lt;/a&gt;
</pre>
			</p>	
		</div>
		
		
	</body>
</html>
