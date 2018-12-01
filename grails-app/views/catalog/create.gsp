<%@ page import="org.greenfield.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
		<title>Create Catalog</title>
		<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
		<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
		<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
	
		<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	</head>
	<body>
		
		
		
		<div class="form-outer-container">
		
		
			<div class="form-container">
			
				<h2>Create Catalog
					<g:link controller="catalog" action="list" class="btn btn-default pull-right">Back to Catalogs</g:link>
					<br class="clear"/>
				</h1>
			
				<br class="clear"/>
			

			
				<div class="messages">
			
					<g:if test="${flash.message}">
						<div class="alert alert-info" role="status">${flash.message}</div>
					</g:if>
			
					<g:if test="${flash.error}">
						<div class="alert alert-danger" role="status">${flash.error}</div>
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
				
				
				
			
				<g:form action="save" >
					<div class="form-row">
						<span class="form-label twohundred secondary">Name 
							<span class="information secondary block">Name must be unique</span>
						</span>
						<span class="input-container">
							<input name="name" type="text" class="form-control threefifty" value="${catalogInstance?.name}" id="name"/>
						</span>
						<br class="clear"/>
					</div>
					
					

					<div class="form-row">
						<span class="form-label twohundred secondary">Location</span>
						<span class="input-container">
							<select name="location" class="form-control" style="width:auto">
								<option value="">-- Top Level --</option>
								${raw(catalogOptions)}
							</select>
						</span>
						<br class="clear"/>
					</div>
					
						  

			 		<div class="form-row">
			 			<span class="form-label twohundred secondary">Layout</span>
			 			<span class="input-container">
							<g:select name="layout.id"
									from="${layouts}"
									value="${catalogInstance?.layout?.id}"
									optionKey="id" 
									optionValue="name" 
									class="form-control"/>
			 			</span>
			 			<br class="clear"/>
			 		</div>
					
				
					<div class="form-row">
						<span class="form-label twohundred secondary">Description 
						</span>
						<span class="input-container">
							<span class="information secondary block">Editor below allows to switch between HTML source and plain text</span>
						</span>
						<br class="clear"/>
					</div>
				
				
					<div class="form-row">
						<g:textArea class="form-control ckeditor" name="description" id="description" cols="40" rows="15" maxlength="65535" value="${catalogInstance?.description}"/>
						<br class="clear"/>
					</div>
				
				
				
					<div class="buttons-container">
						<g:submitButton name="create" class="btn btn-primary" value="Save Catalog" />
					</div>
					
				</g:form>
				
			</div>
		</div>
		
		
		
	</body>
</html>
