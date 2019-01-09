<%@ 
page import="grails.util.Environment" 
page import="org.greenfield.Account"
page import="org.greenfield.Product"
page import="org.greenfield.Catalog"
page import="org.greenfield.ProductOption"
page import="org.greenfield.ProductSpecification"
page import="org.greenfield.ShoppingCart"
page import="org.greenfield.Transaction"
page import="org.greenfield.Page"
page import="org.greenfield.Upload"
page import="org.greenfield.log.CatalogViewLog"
page import="org.greenfield.log.ProductViewLog"
page import="org.greenfield.log.PageViewLog"
page import="org.greenfield.log.SearchLog"
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
		<title>Greenfield : Import/Export</title>
	</head>
	<body>
		
		<style type="text/css">
			.btn{
				margin-bottom:20px;
				width:175px;
			}
			.borderless td, .borderless th {
			    border: none !important;
			}

		</style>
		
		<div id="configuration" class="content scaffold-create" role="main">
		
			<h2>Import/Export</h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:hasErrors bean="${catalogInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${catalogInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
	
			<br/>
			
			<pre style="float:left; width:300px; font-family:Arial !important; padding:0px 10px 0px 10px !important" class="">
				<table class="table borderless" style="width:260px; margin:-5px 30px -20px 10px !important;">
					<tr>
						<td>Accounts</td>
						<td><strong>${Account.count()}</strong></td>
					</tr>
					<tr>
						<td>Catalogs</td>
						<td><strong>${Catalog.count()}</strong></td>
					</tr>
					<tr>
						<td>Products</td>
						<td><strong>${Product.count()}</strong></td>
					</tr>
					<tr>
						<td>Product Options</td>
						<td><strong>${ProductOption.count()}</strong></td>
					</tr>
					<tr>
						<td>Product Specifications</td>
						<td><strong>${ProductSpecification.count()}</strong></td>
					</tr>
					<tr>
						<td>Shopping Carts</td>
						<td><strong>${ShoppingCart.count()}</strong></td>
					</tr>
					<tr>
						<td>Orders</td>
						<td><strong>${Transaction.count()}</strong></td>
					</tr>
					<tr>
						<td>Pages</td>
						<td><strong>${Page.count()}</strong></td>
					</tr>
					<tr>
						<td>Uploads</td>
						<td><strong>${Upload.count()}</strong></td>
					</tr>
					<tr>
						<td>Catalog View Logs</td>
						<td><strong>${CatalogViewLog.count()}</strong></td>
					</tr>
					<tr>
						<td>Product View Logs</td>
						<td><strong>${ProductViewLog.count()}</strong></td>
					</tr>
					<tr>
						<td>Page View Logs</td>
						<td><strong>${PageViewLog.count()}</strong></td>
					</tr>
					<tr>
						<td>Search Logs</td>
						<td><strong>${SearchLog.count()}</strong></td>
					</tr>
				</table>
			</pre>
			
			
			
			<div style="float:left; width:300px; margin-left:57px;">
				<!--
				<g:link uri="/configuration/import_products_view" class="btn btn-default">
					<span class="glyphicon glyphicon-import"></span>
					Import Products
				</g:link>
				<br/>
				-->
				
				<g:link uri="/export/view_export" class="btn btn-default">
					<span class="glyphicon glyphicon-export"></span>
					Export Data
				</g:link>
				<br/>
				
				<g:link uri="/import/view_import" class="btn btn-default">
					<span class="glyphicon glyphicon-import"></span>
					Import Data
				</g:link>
				<br/>
            	
	        	
				<g:link uri="/configuration/uploads" class="btn btn-default">
					<span class="glyphicon glyphicon-upload"></span>
					Uploads
				</g:link>
				<br/>
	        	
	        	<g:if env="development">
					<g:link uri="/development/generate_development_data" class="btn btn-default">
						<span class="glyphicon glyphicon-retweet"></span>
						Generate Data
					</g:link>
					<br/>
					<g:link uri="/development/generate_customers" class="btn btn-default">
						<span class="glyphicon glyphicon-retweet"></span>
						++Dev Customers
					</g:link>
					<br/>
				</g:if>
				
			</div>
			
			
			<br class="clear"/>
			
			<g:link uri="/configuration/manage_key" class="pull-right">
				Manage Data Export Key
			</g:link>
			
			<br class="clear"/>
			
		</div>
	</body>
</html>

						
	
	