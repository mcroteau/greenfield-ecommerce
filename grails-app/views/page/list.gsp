
<%@ page import="org.greenfield.Page" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
	
		<div id="list-page" class="content scaffold-list" role="main">

			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:link controller="page" action="create" class="btn btn-primary pull-right">New Custom Page</g:link>
			
			<br style="clear:both">
			
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="dateCreated" title="${message(code: 'page.dateCreated.label', default: 'Date Created')}" />
						<g:sortableColumn property="title" title="Title" />
						
						<th>url</th>
						
						<th></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${pageInstanceList}" status="i" var="pageInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
				
						<td><g:link action="show" id="${pageInstance.id}"><g:formatDate date="${pageInstance.dateCreated}" format="dd MMM yyyy" /></g:link></td>
					
						<td>${pageInstance.title}</td>
					
						<td>/${applicationService.getContextName()}/page/store_view/${pageInstance.title} &nbsp;&nbsp;
						<a href="/${applicationService.getContextName()}/page/store_view/${URLEncoder.encode("${pageInstance.title}", "UTF-8")}" class="btn btn-sm btn-default">Test</a></td>
					
						<td><g:link action="edit" id="${pageInstance.id}" class="btn btn-default">Edit</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${pageInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
