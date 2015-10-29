<%@ page import="org.greenfield.Account" %>
<%@ page import="org.greenfield.common.RoleName" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<title>Update Password</title>
</head>
<body>

	
	<div class="form-outer-container">
	
	
		<div class="form-container">
			
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			
			<g:form method="post">
			
				<h2>Update Password
					<g:link controller="account" action="admin_edit" id="${accountInstance?.id}" class="btn btn-default pull-right">Back to Account</g:link>
				</h2>

				
				<br class="clear"/>
				
				
				<div class="form-row">
					<span class="form-label full">Username</span>
					<span class="input-container">${accountInstance.username}
					</span>
					<br class="clear"/>
				</div>
				

				
				<div class="form-row">
					<span class="form-label full">New Password</span>		
					<span class="input-container">
						<input type="password" class="form-control"  name="passwordHash" value=""/>			
					</span>
					<br class="clear"/>
				</div>


				<div class="buttons-container">
					<g:hiddenField name="id" value="${accountInstance?.id}" />
					<g:actionSubmit class="btn btn-primary" action="admin_update_password" value="Update Password" />		
				</div>
				
			</g:form>
			
		</div>
		
	</div>
	
</body>
</html>
