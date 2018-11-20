<%@ page import="org.greenfield.Page" %>
<%@ page import="org.greenfield.Layout %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
	<title><g:message code="default.edit.label" args="[entityName]" /></title>
	
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
	
</head>
<body>

	<div class="form-outer-container">
	
	
		<div class="form-container">
		
			<h2>Edit Page
				<g:link controller="page" action="list" class="btn btn-default pull-right">Back to Pages</g:link>
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
				
				<g:hasErrors bean="${pageInstance}">
					<div class="alert alert-danger">
						<ul>
							<g:eachError bean="${pageInstance}" var="error">
								<li><g:message error="${error}"/></li>
							</g:eachError>
						</ul>
					</div>
				</g:hasErrors>
			
			</div>
			
			
			
		
			<g:form action="save" >
				
				<g:hiddenField name="id" value="${pageInstance?.id}" />
				<g:hiddenField name="version" value="${pageInstance?.version}" />
				

				<div class="form-row">
					<span class="form-label full secondary">Url</span>
					<span class="input-container">
						<span class="information">
							/${applicationService.getContextName()}/page/store_view/${pageInstance.title} &nbsp;&nbsp;
							<a href="/${applicationService.getContextName()}/page/store_view/${URLEncoder.encode("${pageInstance.title}", "UTF-8")}" target="_blank">Test</a>
						</span>
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label full secondary">Title 
						<span class="information secondary block">Title must be unique</span>
					</span>
					<span class="input-container">
						<input name="title" type="text" class="form-control threefifty" value="${pageInstance?.title}"/>
					</span>
					<br class="clear"/>
				</div>
				
				
						  

		 		<div class="form-row">
		 			<span class="form-label full secondary">Layout</span>
		 			<span class="input-container">
						<g:select name="layout.id"
								from="${layouts}"
								value="${pageInstance?.layout?.id}"
								optionKey="id" 
								optionValue="name" 
								class="form-control"/>
		 			</span>
		 			<br class="clear"/>
		 		</div> 
						  
						  
			
				<div class="form-row">
					<span class="form-label full secondary">Description 
					</span>
					<span class="input-container">
						<span class="information secondary block">Editor below allows to switch between HTML source and plain text</span>
					</span>
					<br class="clear"/>
				</div>
			
			
				<div class="form-row">
					<g:textArea class="form-control ckeditor" name="content" id="content" cols="40" rows="15" maxlength="65535" value="${pageInstance?.content}"/>
					<br class="clear"/>
				</div>
			
			
			
				<div class="buttons-container">

					<g:if test="${pageInstance.title != "Home"}">
						<g:actionSubmit class="btn btn-danger" action="delete" value="Delete" formnovalidate="" onclick="return confirm('Are you sure?');" />
					</g:if>
				
					<g:actionSubmit class="btn btn-primary" action="update" value="Update" />

					
				</div>
				
			</g:form>
			
		</div>
	</div>
	
</body>
</html>
