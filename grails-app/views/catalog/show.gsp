<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	
	<title>Show Catalog</title>
	
</head>
<body>
	
	
	
	<div class="form-outer-container">
		
		
		<div class="form-container">
			
			<h2>Show Catalog
				<g:link controller="catalog" action="list" class="btn btn-default pull-right">Back to Catalogs</g:link>
				<br class="clear"/>
			</h2>
			
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
			
			
				<div class="form-row">
					<span class="form-label twohundred secondary">Url</span>
					<span class="input-container">
						<span class="secondary">
							/${applicationService.getContextName()}/catalog/products/${catalogInstance.id} &nbsp;
						</span>

						<a href="/${applicationService.getContextName()}/catalog/products/${URLEncoder.encode("${catalogInstance.id}", "UTF-8")}" target="_blank">Test</a>
						
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label twohundred secondary">Name 
					</span>
					<span class="input-container">
						<input name="name" type="text" class="form-control threefifty" value="${catalogInstance?.name}" disabled="disabled"/>
					</span>
					<br class="clear"/>
				</div>
				
				

				
				
				<div class="form-row">
					<span class="form-label twohundred secondary">Description 
					</span>
					<span class="input-container">
						<textArea class="form-control" name="description" id="description" cols="40" rows="10" maxlength="65535" disabled="disabled">${catalogInstance?.description}</textarea>
						<br class="clear"/>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				
				
				<div class="buttons-container">
				
					<g:link controller="catalog" action="list" class="btn btn-default" >Back to List</g:link>
				
					<g:link controller="catalog" action="edit" id="${catalogInstance?.id}" class="btn btn-default">Edit Catalog</g:link>
				</div>
				
				
		</div>
	</div>
					
</body>
</html>
