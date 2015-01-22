<%@ page import="org.greenfield.Account" %>
<%@ page import="org.greenfield.common.RoleName" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Update Password</title>
	</head>
	<body>

		<div id="edit-account" class="content scaffold-edit" role="main">

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			
			<g:form method="post" >
			
				<h1>Update Password</h1>
				
				<div class="form-group">
					<label>Username</label>&nbsp;${accountInstance.username}
				</div>

				
				<div class="form-group">
					<label>Password</label>		
					<input type="password" class="form-control"  name="passwordHash" value=""/>	
				</div>


				<div class="form-group">
					<g:hiddenField name="id" value="${accountInstance?.id}" />
					<g:actionSubmit class="btn btn-primary" action="admin_update_password" value="Update Password" />		
				</div>
				
			</g:form>
		</div>
	</body>
</html>
