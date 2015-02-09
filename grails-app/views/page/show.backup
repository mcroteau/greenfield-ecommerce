
<%@ page import="org.greenfield.Page" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>


		<div id="show-page" class="content scaffold-show" role="main">
			
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<div class="form-group">
				<label>URL</label>/${applicationService.getContextName()}/page/store_view/${pageInstance.title} &nbsp;&nbsp;
					<a href="/${applicationService.getContextName()}/page/store_view/${URLEncoder.encode("${pageInstance.title}", "UTF-8")}" class="btn btn-sm btn-default">Test</a>
			</div>
			
			
			<ol class="property-list page">
			
				<g:if test="${pageInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="page.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${pageInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${pageInstance?.content}">
				<li class="fieldcontain">
					<span id="content-label" class="property-label"><g:message code="page.content.label" default="Content" /></span>
					
						<span class="property-value" aria-labelledby="content-label"><g:fieldValue bean="${pageInstance}" field="content"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${pageInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="page.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${pageInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${pageInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="page.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${pageInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${pageInstance?.id}" />
					
					<g:link class="btn btn-default" action="edit" id="${pageInstance?.id}">Edit</g:link>
					

					<g:if test="${pageInstance.title != "Home"}">
						<g:actionSubmit class="btn btn-danger" action="delete" value="Delete" onclick="return confirm('Are you sure?');" />
					</g:if>
					
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

