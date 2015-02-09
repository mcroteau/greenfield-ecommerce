<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	
	<title>Edit Catalog</title>
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
</head>
<body>
	
	
	
	<div class="form-outer-container">
		
		
		<div class="form-container">
			
			<h2>Edit Catalog
				<g:link controller="catalog" action="list" class="btn btn-default pull-right">Back to Catalogs</g:link>
				<br class="clear"/>
			</h1>
			
			<br class="clear"/>
			

			
			<div class="messages">
			
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
					
				<g:hasErrors bean="${catalogInstance}">
					<div class="alert alert-danger">
						<ul>
							<g:eachError bean="${catalogInstance}" var="error">
								<li><g:message error="${error}"/></li>
							</g:eachError>
						</ul>
					</div>
				</g:hasErrors>
				
			</div>
			
			
			
			<g:form method="post" >
			
				<g:hiddenField name="id" value="${catalogInstance?.id}" />
				<g:hiddenField name="version" value="${catalogInstance?.version}" />
			
				
			
				<div class="form-row">
					<span class="form-label full secondary">Url</span>
					<span class="input-container">
						<span class="secondary">
							/${applicationService.getContextName()}/catalog/products/${catalogInstance.id} &nbsp;
						</span>

						<a href="/${applicationService.getContextName()}/catalog/products/${URLEncoder.encode("${catalogInstance.id}", "UTF-8")}" target="_blank">Test</a>
						
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label full secondary">Name 
						<span class="information secondary block">Name must be unique</span>
					</span>
					<span class="input-container">
						<input name="name" type="text" class="form-control threefifty" value="${catalogInstance?.name}"/>
					</span>
					<br class="clear"/>
				</div>
				
				

				
				
				<div class="form-row">
					<span class="form-label full secondary">Description 
					</span>
					<span class="input-container">
						<span class="information secondary block">Editor below allows both HTML source and plain text</span>
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<g:textArea class="form-control ckeditor" name="description" id="description" cols="40" rows="15" maxlength="65535" value="${catalogInstance?.description}"/>
					<br class="clear"/>
				</div>
				
				
				
				<div class="buttons-container">
				
					<g:actionSubmit class="btn btn-danger" action="delete" value="Delete" formnovalidate="" onclick="return confirm('Are you sure you want to delete this Catalog?');" />
				
					<g:actionSubmit class="btn btn-primary" action="update" value="Update" />
				</div>
				
				
			</g:form>
		</div>
	</div>
					
</body>
</html>
