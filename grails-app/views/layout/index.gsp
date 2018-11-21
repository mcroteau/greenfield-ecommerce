<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : Store Layouts</title>
		
		<script type="text/javascript" src="${resource(dir:'js/allow-tab.js')}"></script>
		
		<style type="text/css">
			#css-textarea,
			#layout-textarea{
				height:350px; 
				width:100%;
				font-size:12px;
				background:#f8f8f8;
				font-family: Monaco,"MonacoRegular",monospace;
			}
		</style>
		
	</head>
	<body>

		<g:if test="${flash.message}">
			<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
		</g:if>
		
		<g:if test="${flash.error}">
			<div class="alert alert-danger" style="margin-top:10px;">${flash.error}</div>
		</g:if>
		
		
		<div class="form-group" style="margin-top:30px">
			<g:link class="btn btn-primary pull-right" controller="layout" action="create">New Layout</g:link>
			<g:link class="pull-right" controller="layout" action="edit_support_layouts"  style="display:inline-block;margin-right:30px">Other Pages</g:link>
			<g:link class="pull-right" controller="page" action="list"  style="display:inline-block;margin-right:30px">Pages</g:link>
			<g:link class="pull-right" controller="catalog" action="list"  style="display:inline-block;margin-right:30px">Catalogs</g:link>
			<g:link class="pull-right" controller="product" action="list" style="display:inline-block;margin-right:30px">Products</g:link>
		</div>
		
		<h2>Store Layouts</h2>
		<p class="instructions"><g:link controller="layout" action="edit_wrapper">Edit Main HTML Wrapper</g:link></p>  
			
		
		<p>Each page, whether it be a product page, custom page, catalog or other supporting page can have its own distinct layout.</p>
		
			
		<g:if test="${layouts?.size() > 0}">
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="name" title="Name" />
						<g:sortableColumn property="defaultLayout" title="Default" />
						<th></th>
					</tr>
				</thead>
				<g:each in="${layouts}" var="layoutInstance">
					<tr>
						<td>${layoutInstance.name}</td>
						<td>
							<g:if test="${layoutInstance.defaultLayout}">
								Default
							</g:if>
						</td>
						<td>
							<g:link controller="layout" action="edit" params="[id: layoutInstance?.id]" class="${layoutInstance.name}-edit">Edit</g:link>
						</td>
					</tr>
				</g:each>
			</table>

			<div class="btn-group">
				<g:paginate total="${layoutInstanceTotal}" />
			</div>
		</g:if>	
		<g:else>
			<div class="alert alert-info" style="margin-top:10px;"><strong>How did you do that?</strong>
			<br/><br/>
			You must have deleted layouts straight from the database. <g:link controller="layout" action="create" class="btn btn-primary">New Layout</g:link>
			</div>
		</g:else>
	
		
	</body>
</html>
