<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Add Product Option</title>
	</head>
	<body>
		
		<div class="content">
		
			<h2>Add Product Option
				<g:link action="edit" name="edit" class="btn btn-default pull-right" id="${productInstance.id}">Back to Product</g:link>
			</h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<div style="width:400px;float:left">
			
				<g:form action="save_option" method="post">
				
					
					<input type="hidden" name="id" value="${productInstance.id}"/>
					
				
			
					<div class="form-row">
						<span class="form-label secondary">Product</span>
						<span class="input-container">
							<span class="label label-default">${productInstance?.name}</span>
						</span>
						<br class="clear"/>
					</div>
					
				
			
					<div class="form-row">
						<span class="form-label secondary">Name</span>
						<span class="input-container">
							<input type="text" name="name" class="form-control" value="${productOption?.name}" style="width:200px;display:inline-block"/>
						</span>
						<br class="clear"/>
					</div>
					
					
					<g:submitButton name="add" class="btn btn-primary" value="Save Product Option" />
					
				</g:form>
				
			</div>
			
			<div class="clear"></div>
				
		</div>

	
	</body>
</html>
