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
			#css-textarea,
			#javascript-textarea{
				height:275px; 
				width:90%;
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
				
		<div style="margin-bottom:20px">
			
			<g:form controller="layout" action="apply_catalogs" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Catalogs" class="btn btn-default pull-right" style="display:inline-block;"/>
			</g:form>
			
			<g:form controller="layout" action="apply_products" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Products" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
			<g:form controller="layout" action="apply_pages" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Pages" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
		</div>
		<br class="clear"/>
		
		<div class="form-group" style="margin-top:30px">
			<g:link class="btn btn-primary pull-right" controller="layout" action="edit" id="${layoutInstance?.id}">Edit Store Layout</g:link>
			<g:link class="btn btn-default pull-right" controller="layout" action="index" style="display:inline-block;margin-right:10px;">Back to Layouts</g:link>
			<g:link class="btn btn-default pull-right" controller="layout" action="create" style="display:inline-block;margin-right:10px;">New Layout</g:link>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred secondary">Name 
			</span>
			<span class="input-container">
				<input name="name" type="text" class="form-control threefifty" value="${layoutInstance?.name}" id="name" disabled="true"/>
			</span>
			<br class="clear"/>
		</div>
		<div class="form-row">
			<span class="form-label twohundred secondary">Default 
			</span>
			<span class="input-container">
				<g:checkBox name="defaultLayout" value="${layoutInstance?.defaultLayout}" checked="${layoutInstance?.defaultLayout}" disabled="true"/>
			</span>
			<br class="clear"/>
		</div>

		
		<h3>Layout HTML</h3>
		
		<p class="instructions">Layout code will automatically be wrapped by necessary wrapper html that includes everything necessary for your store front. <g:link controller="layout" action="edit_wrapper">Edit Main HTML Wrapper</g:link>.</p><p class="instructions">Place all layout code below. Be sure to include <strong>[[CONTENT]]</strong> tag.  <g:link controller="layout" action="tags">View All Available Tags</g:link>.</p>  
		
		<p class="instructions">[[CONTENT]] tag is where all store front product, catalog, page and other content is displayed automatically</p>
		
		<p class="instructions"><g:link controller="layout" action="how">How the Layout Engine works</g:link></p>
		
		<textarea id="layout-textarea"
				name="content" 
				class="form-control"
				disabled>${layoutInstance?.content}</textarea>
		

		
		<h3>Layout CSS</h3>
		<textarea id="css-textarea" 
				name="css" 
				class="form-control"
				disabled>${layoutInstance?.css}</textarea>
		
		<br class="clear"/>
		
		
		<h3>Layout Javascript</h3>
		<textarea id="javascript-textarea" 
				name="javascript" 
				class="form-control"
				disabled>${layoutInstance?.javascript}</textarea>
				
		
		<div class="form-group" style="margin-top:30px">
			<g:link class="btn btn-primary pull-right" controller="layout" action="edit" id="${layoutInstance?.id}">Edit Store Layout</g:link>
		</div>
			
				
		<br class="clear"/>
			
		<div style="margin-top:20px">
		
			<g:form controller="layout" action="apply_catalogs" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Catalogs" class="btn btn-default pull-right" style="display:inline-block;"/>
			</g:form>
		
			<g:form controller="layout" action="apply_products" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Products" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
		
			<g:form controller="layout" action="apply_pages" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Pages" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
		
		</div>
			
		

<script type="text/javascript">
$(document).ready(function(){
	$("#layout-textarea").allowTabChar();
	$("#css-textarea").allowTabChar();
});
</script>		
		
	</body>
</html>
