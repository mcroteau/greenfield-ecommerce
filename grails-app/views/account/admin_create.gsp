<%@ page import="org.greenfield.Account" %>
<%@ page import="org.greenfield.State" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
	<title>Create Customer</title>	
</head>
	
<body>

	<div class="form-outer-container">
		
		<div class="form-container">

			<h2>Create Account					
				<g:link controller="account" action="admin_list" params="[admin:false]"class="btn btn-default pull-right">Back to Accounts</g:link>
				<br class="clear"/>
			</h2>
			

			<br class="clear"/>
			
			
			<div class="messages">
			
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
					
				<g:hasErrors bean="${accountInstance}">
					<div class="alert alert-danger">
						<ul>
							<g:eachError bean="${accountInstance}" var="error">
								<li><g:message error="${error}"/></li>
							</g:eachError>
						</ul>
					</div>
				</g:hasErrors>
				
			</div>
			
		
			
			<g:form action="admin_save" class="form-horizontal" role="form">



				<div class="form-row">
					<span class="form-label full">Username</span>
					<span class="input-container">
						<g:textField type="text" name="username" required="" value="${accountInstance?.username}" class="form-control twofifty"/>
					</span>
					<br class="clear"/>
				</div>
				

				<div class="form-row">
					<span class="form-label full">Email</span>
					<span class="input-container">
						<g:textField type="email" name="email" required="" value="${accountInstance?.email}" class="form-control twofifty"/>
					</span>
					<br class="clear"/>
				</div>
				
				

				<div class="form-row">
					<span class="form-label full">Password</span>
					<span class="input-container">
						<g:textField type="text" name="password" required="" value="${accountInstance?.password}" class="form-control twofifty"/>
					</span>
					<br class="clear"/>
				</div>
				
				


				<div class="form-row">
					<span class="form-label full">Name</span>
					<span class="input-container">
						<g:textField class="form-control twohundred"  name="name" value="${accountInstance?.name}"/>
					</span>
					<br class="clear"/>
				</div>


				<div class="form-row">
					<span class="form-label full">Address 1</span>
					<span class="input-container">
						<g:textField class="threehundred form-control"  name="address1" value="${accountInstance?.address1}"/>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				<div class="form-row ">
					<span class="form-label full">Address 2</span>
					<span class="input-container">
						<g:textField class="threehundred form-control"  name="address2" value="${accountInstance?.address2}"/>
					</span>
					<br class="clear"/>
				</div>




				<div class="form-row">
					<span class="form-label full">City</span>
					<span class="input-container">
						<g:textField class="twotwentyfive form-control"  name="city" value="${accountInstance?.city}"/>
					</span>
					<br class="clear"/>
				</div>
				
				

				<div class="form-row">
					<span class="form-label full">State</span>
					<span class="input-container">	
						<g:select name="state.id"
								from="${State.list()}"
								value="${accountInstance?.state?.id}"
								optionKey="id" 
								optionValue="name" 
								class="form-control"/>
					</span>
					<br class="clear"/>	
				</div>
				
				
				

				<div class="form-row">
					<span class="form-label full">Zip</span>
					<span class="input-container">
						<g:textField class="onefifty form-control"  name="zip" value="${accountInstance?.zip}"/>
					</span>
					<br class="clear"/>
				</div>
				


				<div class="form-row">
					<span class="form-label full">Phone</span>
					<span class="input-container">
						<g:textField class="twofifty form-control"  name="phone" value="${accountInstance?.phone}"/>
					</span>
					<br class="clear"/>
				</div>

				

				<div class="form-row">
					<span class="form-label full">Is Adminstrator</span>
					<span class="input-container">
						<g:checkBox name="admin" value="${accountInstance.hasAdminRole}" checked="${accountInstance.hasAdminRole}"/>
						<span class="information">All Accounts are customers by default.</span>		</span>
					<br class="clear"/>
				</div>	
				
				
				<div class="buttons-container">	
					<g:submitButton name="create" class="btn btn-primary" value="Create Account" />		
				</div>
				
			</g:form>
	
		</div>
	
	</div>	
	
</body>
</html>
