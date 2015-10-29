<%@ page import="org.greenfield.Account" %>
<%@ page import="org.greenfield.common.RoleName" %>
<%@ page import="org.greenfield.State" %>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'accountInstance.label', default: 'Account')}" />
		<title>Show Account</title>
	</head>
	<body>
	
		<div class="form-outer-container">
	
	
			<div class="form-container">
		

				<h2>Show Account					
				<g:link controller="account" action="admin_list" params="[admin:false]"class="btn btn-default pull-right">Back to Accounts</g:link>
						<br class="clear"/>
				</h2>

				<br class="clear"/>
			
			
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
			
				<g:hasErrors bean="${accountInstance}">
				<ul class="errors" role="alert">
					<g:eachError bean="${accountInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
				</g:hasErrors>
			
			
				<g:form method="post" class="form-horizontal" >
			
					<g:hiddenField name="id" value="${accountInstance?.id}" />
				

					<div class="form-row">
						<span class="form-label full">Username</span>
						<span class="input-container">
							${accountInstance.username}
						</span>
						<br class="clear"/>
					</div>
				

					<div class="form-row">
						<span class="form-label full">Email</span>
						<span class="input-container">
							<g:textField type="email" name="email" required="" value="${accountInstance?.email}" class="form-control twofifty" disabled="disabled"/>
						</span>
						<br class="clear"/>
					</div>
				


					<div class="form-row">
						<span class="form-label full">Name</span>
						<span class="input-container">
							<g:textField class="form-control twohundred"  name="name" value="${accountInstance?.name}" disabled="disabled"/>
						</span>
						<br class="clear"/>
					</div>


					<div class="form-row">
						<span class="form-label full">Address 1</span>
						<span class="input-container">
							<g:textField class="threehundred form-control"  name="address1" value="${accountInstance?.address1}" disabled="disabled"/>
						</span>
						<br class="clear"/>
					</div>
				
				
				
					<div class="form-row ">
						<span class="form-label full">Address 2</span>
						<span class="input-container">
							<g:textField class="threehundred form-control"  name="address2" value="${accountInstance?.address2}" disabled="disabled"/>
						</span>
						<br class="clear"/>
					</div>




					<div class="form-row">
						<span class="form-label full">City</span>
						<span class="input-container">
							<g:textField class="twotwentyfive form-control"  name="city" value="${accountInstance?.city}" disabled="disabled"/>
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
									class="form-control"
									disabled="disabled"/>
						</span>
						<br class="clear"/>	
					</div>
				
				
				

					<div class="form-row">
						<span class="form-label full">Zip</span>
						<span class="input-container">
							<g:textField class="onefifty form-control"  name="zip" value="${accountInstance?.zip}" disabled="disabled"/>
						</span>
						<br class="clear"/>
					</div>
				


					<div class="form-row">
						<span class="form-label full">Phone</span>
						<span class="input-container">
							<g:textField class="twofifty form-control"  name="phone" value="${accountInstance?.phone}" disabled="disabled"/>
						</span>
						<br class="clear"/>
					</div>

				

					<div class="form-row">
						<span class="form-label full">Is Adminstrator</span>
						<span class="input-container">
							<g:checkBox name="admin" value="${accountInstance.hasAdminRole}" checked="${accountInstance.hasAdminRole}" disabled="disabled"/>
							<span class="information">All Accounts are customers by default.</span>		</span>
						<br class="clear"/>
					</div>	
				
				

					<div class="buttons-container">
				
						<g:form>
	        			
							<g:hiddenField name="id" value="${accountInstance?.id}" />
							
							
							<g:link class="btn btn-default" action="admin_edit" id="${accountInstance.id}">Edit Account</g:link>
							
						</g:form>
		
					</div>
				
				</g:form>
	
			</div>
	
		</div>
		
	</body>
</html>
