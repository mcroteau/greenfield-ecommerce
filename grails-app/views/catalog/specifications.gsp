<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Specifications</title>
	</head>
	<body>
		
		<div class="content">
		
			<h2>Specifications
				<g:link controller="specification" action="create" name="edit" class="btn btn-primary pull-right" id="${catalogInstance.id}" style="margin-left:10px;">Add Specification</g:link>
				
				<g:link action="edit" name="edit" class="btn btn-default pull-right" id="${catalogInstance.id}">Back to Catalog</g:link>
			</h2>
		
			
			<div class="form-row">
				<span class="form-label secondary">Catalog</span>
				<span class="input-container">
					<span class="label label-default">${catalogInstance?.name}</span>
				</span>
				<br class="clear"/>
			</div>
			
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:if test="${catalogInstance.specifications.size() > 0}">
				<table class="table table-condensed" style="width:550px;">
					<thead>
					<tr>
						<th>Name</th>
						<th># Options</th>
						<th></th>
					</tr>
					</thead>
					<tbody>
					<g:each in="${catalogInstance.specifications}" status="i" var="specification">
						<tr>
							<td>${specification.name}</td>
							<td><g:if test="${specification.specificationOptions.size() > 0 }">
									<g:each in="${specification.specificationOptions}" var="option">
										<span class="label label-default">${option.name}</span>
									</g:each>
								</g:if>
								<g:else>
									<span class="information">No Specification Options Added Yet</span>
								</g:else>
							</td>
							<td><g:link controller="specification" action="edit" name="edit" class="" id="${specification.id}">Edit</g:link></td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</g:if>			
			<g:else>
				<div class="alert alert-info">No Specifications added yet</div>
			</g:else>
			
			
		</div>

	
	</body>
</html>
