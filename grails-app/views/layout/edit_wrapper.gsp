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
				height:475px; 
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
		
		<g:form controller="layout" action="update_wrapper">
			
			<div class="form-group" style="margin-top:30px">
				<g:submitButton class="btn btn-primary pull-right" name="updateLayout" value="Update Store Layout & CSS" />
				<g:link controller="layout" action="restore_wrapper" class="btn btn-default pull-right" style="margin-right:10px;">Restore Base Wrapper HTML</g:link>
			</div>
			
			<h2>Store Layout</h2>
			<div class="alert alert-danger" style="margin-top:10px;"><strong>Caution:</strong><br/>
				This is the base layout wrapper with javascript, css and meta information includes that operate the store. Here are the following tags to include to have a working store front. [[TITLE]], [[META_KEYWORDS]], [[META_DESCRIPTION]], [[STORE_LAYOUT]], [[STORE_JAVASCRIPT]], [[STORE_CSS]], [[GOOGLE_ANALYTICS]]
			</div>
				
			<p class="instructions">Be sure to include <strong>[[STORE_LAYOUT]], [[STORE_JAVASCRIPT]], [[STORE_CSS]]</strong> tags.  <g:link controller="layout" action="tags">View All Available Tags</g:link>.</p>  
			
			<p class="instructions"><g:link controller="layout" action="how">How the Layout Engine works</g:link></p>
			
			<textarea id="layout-textarea"
					name="layoutWrapper" 
					class="form-control">${layoutWrapper}</textarea>
			
			
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
