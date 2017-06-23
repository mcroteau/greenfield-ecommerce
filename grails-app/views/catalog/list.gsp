
<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : Catalogs</title>
	</head>
	<body>

		<div id="list-catalog" class="content scaffold-list" role="main">
			
			<h2 class="">Catalogs
				<g:link controller="catalog" action="create" class="btn btn-primary pull-right" >New Catalog</g:link>
				<g:link controller="catalog" action="menu_view" class="btn btn-default pull-right" style="display:inline-block;margin-right:5px">Menu View</g:link>
            	<g:link controller="specification" action="list" class="btn btn-default pull-right" style="margin-right:5px">Specifications</g:link>
            </h2>
			
			<p class="instructions" style="margin-bottom:0px;">Click <strong>"Menu View"</strong> to see how catalogs will display on store front</p>


			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<g:if test="${catalogsList?.size() > 0}">
				<table class="table" style="margin-top:0px !important; padding-top:0px">
					<thead>
						<tr>
							<th>Name</th>
							<th># Products</th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${catalogsList}" var="catalogData">
							<tr>
								<td>${catalogData.name} &nbsp;
									<span class="secondary" style="font-size:12px;color:rgba(0,0,0,0.54)">(${raw(catalogData.path)})</span>
								</td>
								<td align="center">${catalogData.productsCount}</td>
								<td>
									<g:link controller="catalog" action="manage_positions" id="${catalogData.id}">Update Position</g:link>
								</td>
								<td>
									<g:link controller="catalog" action="edit" params="[id: catalogData.id]" class="${catalogData.name}-edit">Edit</g:link>
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
