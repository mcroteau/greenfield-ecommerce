<%@ page import="org.greenfield.Account" %>
<%@ page import="org.greenfield.common.RoleName" %>
<%@ page import="org.greenfield.State" %>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'accountInstance.label', default: 'Account')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="edit-account" class="content scaffold-edit" role="main">

			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>

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
			
			
			
				<div class="form-group ${hasErrors(bean: accountInstance, field: 'username', 'error')} required">
					<label for="username">
						<g:message code="accountInstance.username.label" default="Username" />
						<span class="required-indicator">*</span>
					</label>${accountInstance.username}
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'email', 'error')} required">
					<label for="email">
						<g:message code="accountInstance.email.label" default="Email" />
						<span class="required-indicator">*</span>
					</label>
					${accountInstance?.email}
				</div>
				


				<div class="form-group ${hasErrors(bean: accountInstance, field: 'name', 'error')} ">
					<label for="name">
						<g:message code="accountInstance.name.label" default="Name" />
		
					</label>
					${accountInstance?.name}
				</div>


				<div class="formgroup ${hasErrors(bean: accountInstance, field: 'address1', 'error')} ">
					<label for="address">
						<g:message code="accountInstance.address1.label" default="Address1" />
					</label>
					${accountInstance?.address1}
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'address2', 'error')} ">
					<label for="address2">
						<g:message code="accountInstance.address2.label" default="Address2" />
		
					</label>
					${accountInstance?.address2}
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'city', 'error')} ">
					<label for="city">
						<g:message code="accountInstance.city.label" default="City" />
		
					</label>
					${accountInstance?.city}
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'state', 'error')} ">
					<label for="state">
						State
					</label>
					${accountInstance?.state?.name}
				</div>


				<div class="form-group ${hasErrors(bean: accountInstance, field: 'zip', 'error')} ">
					<label for="zip">
						<g:message code="accountInstance.zip.label" default="Zip" />
					</label>
					${accountInstance?.zip}
				</div>


				<div class="form-group ${hasErrors(bean: accountInstance, field: 'phone', 'error')} ">
					<label for="phone">
						<g:message code="accountInstance.phone.label" default="Phone" />
					</label>
					${accountInstance?.phone}
				</div>
				
				
				<div class="form-group">
					<label>Password<label>					
					<g:link action="admin_edit_password" class="btn btn-default" id="${accountInstance.id}">Change Password</g:link>
				</div>

				
				<g:if test="${accountInstance.hasAdminRole}">
					<h3>Is Administrator</h3>
					<p>By default all Accounts are customers</p>
				</g:if>	
				
			
			<g:form>
	
				<g:hiddenField name="id" value="${accountInstance?.id}" />
				
				<g:link class="btn btn-default" action="admin_edit" id="${accountInstance.id}" >Edit</g:link>
				
				<g:actionSubmit class="btn btn-danger" action="admin_delete" value="Delete" onclick="return confirm('Are you sure?');" />
			</g:form>
		</div>
	</body>
</html>
