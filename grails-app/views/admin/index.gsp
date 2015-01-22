<html lang="en">
<head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 	<meta name="layout" content="admin" />
	<title><g:message code="app.name"  /> </title>
</head>
<body>
	
	
	<g:if test="${flash.message}">
		<div class="alert alert-info">${flash.message}</div>
	</g:if>	
	
	<div style="background:#f8f8f8; border:solid 1px #ddd; padding:0px 30px 30px; margin-top:20px;">
			
		<h1>Administration</h1>

		<p>Welcome to the administration section.  Steps to complete initial setup</p>
			
		<style type="text/css">
			ol li{
				margin:5px auto;
			}
		</style>
			
		<ol>
			<li>Set Store Settings : <strong>Settings -> Store Settings</strong></li>
			<li>Configure Stripe credentials : <strong>Settings -> Stripe Settings</strong> <span class="instructions">(<a href="http://www.stripe.com" target="_blank">Visit Stripe</a>)</span></li>
			<li>Configure Email Settings : <strong>Settings -> Email Settings</strong> <span class="instructions">(Recommend <a href="http://www.mandrill.com" target="_blank">Mandrill</a>)</span></li>
			<li>Setup Shipping : <strong>Settings -> Shipping Settings</strong> <span class="instructions">(<a href="http://www.easypost.com" target="_blank">EasyPost</a> integration available)</span></li>
			<li>Update Homepage : <strong>Configuration -> Edit Home Page</strong></li>
			<li>Create first catalog : <strong>Catalogs -> New Catalog</strong></li>
			<li>Create first product : <strong>Products -> New Product</strong></li>
			<li><strong>Start Selling!</strong>
		</ol>	
			
			
		<br/>
		<p style="margin-bottom:20px;">Greenfield leverages Google Analytics for web traffic statistics. <br/>Go to <strong>Settings -> Store Settings</strong> to set tracking code</p>
			
		<a href="http://www.google.com/analytics/" class="btn btn-primary" target="_blank">Visit Google Analytics</a>
		
		<br/>
		<br/>
		<br/>
		<p>This page can be personalized by editing <strong>index.gsp</strong> located in <strong>grails-app/views/admin</strong></p>
		
		
	</div>
	
	
</body>
</html>
