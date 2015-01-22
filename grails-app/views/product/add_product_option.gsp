<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">\
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		
		<div class="content">
		
			<h1>${productInstance.name} : Add Product Option</h1>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<div style="width:400px;float:left">
			
				<g:form action="save_option" method="post">
			
					<input type="hidden" name="id" value="${productInstance.id}"/>
					
					<span>Name</span>&nbsp;&nbsp;
					<input type="text" name="name" class="form-control" value="${productOption?.name}" style="width:200px;display:inline-block"/>
					
					<g:submitButton name="add" class="btn btn-primary" value="Save" />
					
				</g:form>
				
			</div>
			
			<div class="clear"></div>
				
		</div>

	
	</body>
</html>
