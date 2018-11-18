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
		</div>
		
		<h2>Store Layouts</h2>
		<p class="instructions"><g:link controller="layout" action="edit_wrapper">Edit Main HTML Wrapper</g:link></p>  
			
			
		<g:if test="${layouts?.size() > 0}">
			<table class="table">
				<tr>
					<th>Name</th>
					<th>Default</th>
					<th></th>
				</tr>
				<g:each in="${layouts}" var="layout">
					<tr>
						<td>${layout.name}</td>
						<td>${layout.default}</td>
						<td>
							
						</td>
					</tr>
				</g:each>
			</table>
		</g:if>	
		<g:else>
			<div class="alert alert-info" style="margin-top:10px;"><strong>How did you do that?</strong>
			<br/><br/>
			You must have deleted layouts straight from the database. <g:link controller="layout" action="create" class="btn btn-primary">New Layout</g:link>
			</div>
		</g:else>
	
		
	</body>
</html>
