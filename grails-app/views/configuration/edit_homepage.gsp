<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
</head>
<body>

	<h1>Edit Home Page</h1>		

	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	
	<g:form controller="configuration" action="save_homepage">
		<div class="form-group">
			<label>Home Page</label>
			<textarea class="form-control" name="homepage" id="homepage">${homepage.content}</textarea>
		</div>		
		
		<script type="text/javascript">
		    CKEDITOR.replace( 'homepage' );
		</script>
		
		
		<div class="form-group pull-right">
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Homepage" name="submit" class="btn btn-primary"/>
		</div>
	</g:form>
	
</body>
</html>