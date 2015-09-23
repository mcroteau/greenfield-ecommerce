<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Add Specification</title>
	</head>
	<body>
		
		<div class="content">
		
			<h2>Add Specification
				<g:link controller="catalog" action="edit" name="edit" class="btn btn-default pull-right" id="${catalogInstance.id}">Back to Catalog</g:link>
			</h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<div style="width:400px;float:left">
			
				<g:form action="save" method="post">
					
					<input type="hidden" name="id" value="${catalogInstance.id}"/>
					
					<div class="form-row">
						<span class="form-label secondary">Catalog</span>
						<span class="input-container">
							<span class="label label-default">${catalogInstance?.name}</span>
						</span>
						<br class="clear"/>
					</div>
					
					<div class="form-row">
						<span class="form-label secondary">Name</span>
						<span class="input-container">
							<input type="text" name="name" class="form-control" value="${specification?.name}" style="width:200px;display:inline-block"/>
						</span>
						<br class="clear"/>
					</div>
					

					<g:if test="${catalogInstance.subcatalogs}">
						<div class="form-row">
							<span class="form-label secondary">Apply to subcatalogs</span>
							<span class="input-container">
								<input type="checkbox" ${specification?.applySubcatalogs} name="applySubcatalogs" id="applySubcatalogs"/>
								<br/>
								<g:each in="${catalogInstance.subcatalogs}" var="subcatalog">
									<span class="label label-default">${subcatalog.name}</span>
								</g:each>
							</span>
							<br class="clear"/>
						</div>
					</g:if>
					
					<g:submitButton name="save" class="btn btn-primary" value="Save Specification" />
					
				</g:form>
				
			</div>
			
			<div class="clear"></div>
				
		</div>

	
	</body>
</html>
