
<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : Catalog Administration</title>
	</head>
	<body>

		<div id="list-catalog" class="content scaffold-list" role="main">
			
			<h2 class="">Catalogs
				<g:link controller="catalog" action="create" class="btn btn-primary pull-right" >New Catalog</g:link>
			</h2>
			
			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<g:if test="${catalogsList?.size() > 0}">
				<table class="table">
					<thead>
						<tr>
							<th>Name</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${catalogsList}" var="catalogData">
							<tr>
								<td>${catalogData.name} <span class="information secondary" style="font-size:11px;">(${catalogData.path})</span>
								</td>
								<td>
									<g:link controller="catalog" action="edit" params="[id: catalogData.id]">Edit</g:link>
								</td>
							</tr>
						</g:each>
					</tbody>
				</table>
			</g:if>
			<g:else>
				<p>No Catalogs found..</p>
			</g:else>
		</div>	
	</body>
</html>
