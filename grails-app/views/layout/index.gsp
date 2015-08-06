<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : Store Layout</title>
		
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
		
		<g:form controller="layout" action="update">
			
			<div class="form-group" style="margin-top:30px">
				<g:submitButton class="btn btn-primary pull-right" name="updateLayout" value="Update Store Layout & CSS" />
			</div>
			
			<h2>Store Layout</h2>
			<p class="instructions">Place all layout code below.  Be sure to include <strong>[[CONTENT]]</strong> tag.  <g:link controller="layout" action="tags">View All Available Tags</g:link>.</p>  
			
			<p class="instructions"><g:link controller="layout" action="how">How the Layout Engine works</g:link></p>
			
			
			<textarea id="layout-textarea"
					name="layout" 
					class="form-control">${layout}</textarea>
			

			
			<h3>Store CSS</h3>
			<p class="instructions">Place all supporting css code below</p>
			
			<textarea id="css-textarea" 
					name="css" 
					class="form-control">${css}</textarea>
			
			<br class="clear"/>
			
			<g:submitButton class="btn btn-primary pull-right" name="updateLayout" value="Update Store Layout & CSS" style="margin-bottom:30px;margin-top:30px;" />
			
		</g:form>
		
		

<script type="text/javascript">
$(document).ready(function(){
	$("#layout-textarea").allowTabChar();
	$("#css-textarea").allowTabChar();
});
</script>		
		
	</body>
</html>
