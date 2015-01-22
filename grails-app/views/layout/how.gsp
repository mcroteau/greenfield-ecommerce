<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>How the Layout Engine Works</title>
		
		<style type="text/css">
			ul{
			}
			ul li{
				list-style:square;
			}
			
			.summary{
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
		
		<h2>How the Layout Engine Works
			<g:link action="index" class="btn btn-default">Back</g:link>
		</h2>
		
		<div class="clear"></div>
		
		
		<p>When editing the layout there are 5 places/files to remember</p>
		
		<ol>
			<li><a href="#store-layout-editor">Store Layout Editor</a></li>
			<li><a href="#css-editor">CSS Editor</a></li>
			<li><a href="#layout-wrapper">layout-wrapper.html</a></li>
			<li><a href="#store-css">store.css</a></li>
			<li><a href="#app-css">app.css</a></li>
		</ol>
		
		
		<div class="summary" id="store-layout-editor">
			<h4>Store Layout Editor</h4>
			<p><strong>Store Layout -> Store Layout Editor</strong>
			
			<p>Store layout code will be saved here.  Dynamic store content (catalogs, products, pages etc..) will be rendered within a section defined with the <strong>[[CONTENT]]</strong> tag.  This tag is required and will not save without it.</p>
			
			<p>Store layout html will replace <strong>[[STORE_LAYOUT]]</strong> within the <strong>layout-wrapper.html</strong> file located <strong>web-app -> templates -> storefront</strong>.  </p>
			
		</div>
		
		
		
		<div class="summary" id="layout-wrapper">
			<h4>CSS Editor</h4>
			
			<p>The css code entered here gets saved to <strong>store.css</strong> located in the <strong>web-app -> css</strong> directory.  This file is referenced in <strong>layout-wrapper.html</strong></p>
			
		</div>
		
		
		
		<div class="summary" id="layout-wrapper">
			
			<h4>layout-wrapper.html</h4>
			
			<p><strong>web-app -> templates -> storefront</strong></p>
			
			<p>This is the main layout file for Greenfield storefront.  This file gets read and <strong>[[STORE_LAYOUT]]</strong> gets replaced by the html code entered in the <strong>Store Layout Editor</strong></p>
			
			<p><strong>current layout-wrapper.html : </strong>
<pre>&lt;!DOCTYPE html&gt;
&lt;html lang="en"&gt;
&lt;/head&gt;
	&lt;title&gt;[[TITLE]]&lt;/title&gt;
	&lt;meta name="keywords" content="[[META_KEYWORDS]]"&gt;
	&lt;meta name="description" content="[[META_DESCRIPTION]]"&gt;
	
	&lt;link rel="shortcut icon" type="image/png" href="/[[CONTEXT_NAME]]/images/favicon.png"/&gt;
	
	&lt;script src="/[[CONTEXT_NAME]]/js/lib/jquery/1.11.0/jquery.js"&gt;&lt;/script&gt;
	&lt;script src="/[[CONTEXT_NAME]]/bootstrap/3.1.1/js/bootstrap.min.js"&gt;&lt;/script&gt;
	&lt;link rel="stylesheet" href="/[[CONTEXT_NAME]]/bootstrap/3.1.1/css/bootstrap.min.css" type="text/css"&gt;
	&lt;link rel="stylesheet" href="/[[CONTEXT_NAME]]/css/app.css" type="text/css"&gt;
	&lt;link rel="stylesheet" href="/[[CONTEXT_NAME]]/css/store.css" type="text/css"&gt;
&lt;head&gt;
&lt;body&gt;
	
[[STORE_LAYOUT]]


&lt;script&gt;
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', '[[GOOGLE_ANALYTICS]]', 'auto');
  ga('send', 'pageview');

&lt;/script&gt;

&lt;/body&gt;
&lt;/html&gt;
</pre>
			</p>
		</div>
		
		
		
		
		
		
		<div class="summary" id="store-css">
			<h4>store.css</h4>
			
			<p>The css code entered in the CSS Editor <strong>Store Layout</strong> get gets saved to this file.  It is located in the <strong>web-app -> css</strong> directory.  This file is referenced in <strong>layout-wrapper.html</strong></p>
		
		</div>
		
		
		
		
		<div class="summary" id="app-css">
			<h4>app.css</h4>
			
			<p>Additional place to enter custom css code.  Located in the <strong>web-app -> css</strong> directory.  This file is referenced in <strong>layout-wrapper.html</strong></p>
		
		</div>
		
		
		
	</body>
</html>
