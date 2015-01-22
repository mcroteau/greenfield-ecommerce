<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<link rel="stylesheet" href="${resource(dir:'bootstrap/3.1.1/css', file:'bootstrap.min.css')}" />
	
	<script type="text/javascript" src="${resource(dir:'js/lib/jquery/1.11.0/jquery.js')}"></script>
	
	<script type="text/javascript" src="${resource(dir:'bootstrap/3.1.1/js/bootstrap.js')}"></script>
	
	<g:layoutHead/>
	
<style type="text/css">
@font-face { 
	font-family: Roboto-Regular; 
	src: url("${resource(dir:'fonts/Roboto-Regular.ttf')}"); 
} 
@font-face { 
	font-family: Roboto-Bold;
	src: url("${resource(dir:'fonts/Roboto-Bold.ttf')}"); 
}
@font-face { 
	font-family: Roboto-Thin; 
	src: url("${resource(dir:'fonts/Roboto-Thin.ttf')}"); 
}
@font-face { 
	font-family: Roboto-Light; 
	src: url("${resource(dir:'fonts/Roboto-Light.ttf')}"); 
}
@font-face { 
	font-family: Roboto-Black; 
	src: url("${resource(dir:'fonts/Roboto-Black.ttf')}"); 
}
@font-face { 
	font-family: Roboto-Medium; 
	src: url("${resource(dir:'fonts/Roboto-Medium.ttf')}"); 
}
html,body{
	background:#efefef;
}	

#admin-marker{
	height:5px;
	margin-top:15px;
	margin-bottom:0px;
	background:#ffa247;
	background:#999;
	-webkit-border-top-left-radius: 5px;
	-webkit-border-top-right-radius: 5px;
	-moz-border-radius-topleft: 5px;
	-moz-border-radius-topright: 5px;
	border-top-left-radius: 5px;
	border-top-right-radius: 5px;
}

#admin-nav-container{
	font-family:Roboto-Regular;
	padding-bottom:15px;
	margin-top:0px;
	background:#3b3e41;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
}

#admin-nav-container h4{
	font-size:12px;
	text-align:center;
	color:bbb;
}

#admin-nav{
	margin:0px;
	padding:0px;
	list-style:none;
}

#admin-nav li{
	padding:0px;
	margin:0px;
	height:58px;
	display:block;
	background:ddd;
}

#admin-nav li a{
	color:#bbb;
	display:block;
	font-size:17px;
	height:58px;
	padding:15px 10px 10px 10px;
	vertical-align:middle;
	font-family:Roboto-Thin;
	background:#3b3e41;
	border-top:solid 1px #575859;
	border-bottom:solid 1px #2b2b2b;
	outline:none;
	text-decoration:none;
}

#admin-nav li a:hover{
	color:#ddd;
	background:#595b5d;
	text-decoration:none;
}

#admin-nav li a:active{
	color:#efefef;
	background:#656667;
	text-decoration:none;
}

#admin-nav li a:visited, 
#admin-nav li a:link{
	text-decoration:none;
}

#admin-nav li a.active{
	color:#f8f8f8;
	background:#25292c;
}

#admin-content{
	padding:20px 10px 30px 0px;
	position:relative;
}

.container{	
	background:#fff;
	min-height:500px;
	margin-top:15px;
	-webkit-border-radius: 10px;
	-moz-border-radius: 10px;
	border-radius: 10px;
	position:relative;
	-webkit-box-shadow: 0px 0px 25px 0px rgba(204,204,204,1);
	-moz-box-shadow: 0px 0px 25px 0px rgba(204,204,204,1);
	box-shadow: 0px 0px 25px 0px rgba(204,204,204,1);
}	

table{
	empty-cells:show;
	border-collapse:separate;
	border:none !important;
	border-spacing:2px 2px;
}
table tr{
	border:none !important;
}
table td{
	border-top:none !important;
	border-bottom: 1px dashed #ddd;
	border-right : 1px dashed #ddd;
}
.nav{
	margin-bottom:10px;
}
.inactive a{
	display:inline-block;
	background:#f8f8f8;
	border:solid 1px #ddd !important;
}
.form-group label{
	font-weight:normal;
}
.form-group .form-control{
	display:inline-block;
	width:300px;
}
.form-group label em{
	font-weight:normal;
	font-size:11px;
	color:#777
}
#information h3{
	margin:20px 0px 30px 0px;
	padding-bottom:7px;
	border-bottom:dashed 3px #ddd;
}

.instructions{
	color:#777;
	font-size:12px;
}

.clear{
	clear:both;
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

</head>
<body>

	<div class="container">
	
		<div class="row">
		
			<div class="col-xs-2">
			
				<div id="admin-nav-container">
			
					<div id="admin-marker"></div>
					
					
					<ul id="admin-nav">
						<li><g:link uri="/admin">Home</g:link></li>
						<li><g:link uri="/product/list">Products</g:link></li>
						<li><g:link uri="/catalog/list">Catalogs</g:link></li>
						<li><g:link uri="/transaction/list">Orders</g:link></li>
						<li><g:link uri="/page/list">Pages</g:link></li>
						<li><g:link uri="/account/admin_list?admin=false">Accounts</g:link></li>
						<li><g:link uri="/configuration/settings">Settings</g:link></li>
						<li><g:link uri="/configuration">Configuration</g:link></li>
						<li><g:link uri="/layout">Store Layout</g:link></li>
					</ul>
				</div>
			</div>
			
			<div class="col-xs-10">
			
				<div id="admin-content">
				
					<div style="background:#efefef; padding:5px 10px; color:#555; font-size:12px; border-radius:3px; border:solid 1px #ddd;">
						Welcome back <strong><shiro:principal/></strong>! 
						
						<g:link controller="auth" action="signOut" class="pull-right">logout</g:link>

						<span class="pull-right">&nbsp;&nbsp;|&nbsp;&nbsp;</span>
						
						<g:link uri="/" class="pull-right">Storefront</g:link>
						
						<div class="clear"></div>
					</div>
					
					
					<shiro:hasRole name="ROLE_SALESMAN">
						<h2>Salesman</h2>
					</shiro:hasRole>
					
					<shiro:hasRole name="ROLE_CUSTOMER_SERVICE">
						<h2>Customer Service</h2>
					</shiro:hasRole>
					
					<shiro:hasRole name="ROLE_AFFILIATE">
						<h2>Affiliate</h2>
					</shiro:hasRole>
                	
					<g:layoutBody/>

					
				</div>
			</div>
			
		</div>
		

		<div class="footer" style="margin:30px 20px; color:#777; font-size:11px;text-align:right">
			<!--
			<li><g:link uri="/admin/shoppingCart/list">Shopping Carts</g:link></li>
			<li><g:link uri="/admin/shoppingCartItem/list">Shopping Cart Items</g:link></li>
			-->
			Greenfield &copy; 2015
		</div>
			
	</div>
	
	
	<br style="margin-top:100px;"/>
	
	
</body>
</html>
