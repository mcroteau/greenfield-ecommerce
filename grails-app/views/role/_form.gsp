<%@ page import="org.greenfield.Role" %>



<div class="fieldcontain ${hasErrors(bean: roleInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="role.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${roleInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: roleInstance, field: 'accounts', 'error')} ">
	<label for="accounts">
		<g:message code="role.accounts.label" default="Accounts" />
		
	</label>
	
</div>

<div class="fieldcontain ${hasErrors(bean: roleInstance, field: 'permissions', 'error')} ">
	<label for="permissions">
		<g:message code="role.permissions.label" default="Permissions" />
		
	</label>
	
</div>

