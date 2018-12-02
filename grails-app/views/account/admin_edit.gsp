<%@ page import="org.greenfield.Account" %>
<%@ page import="org.greenfield.common.RoleName" %>
<%@ page import="org.greenfield.State" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title>Edit Account</title>

	</head>
	<body>


	<div class="form-outer-container">
	
	
		<div class="form-container">
		

			<h2>Edit Account			

				<g:link controller="account" action="admin_list" params="[admin:false]" class="btn btn-default pull-right">Back to Accounts</g:link>
				
				<div style="display:inline-block;width:10px;height:10px;" class="pull-right"></div>
				<g:link controller="account" action="account_activity" id="${accountInstance?.id}" class="btn btn-default pull-right">Activity</g:link>
				<br class="clear"/>
			</h2>

			<br class="clear"/>
			
			
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
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
				
				
				<g:if test="${accountInstance.username == 'admin'}">
					<div class="form-row">
						<span class="form-label full">UUID</span>
						<span class="input-container">
							<g:textField type="uuid" name="uuid" value="${accountInstance?.uuid}" class="form-control twofifty"/>
						</span>
						<span class="information">You can manually set UUID to match exported data UUID for admin account</span>
						<br class="clear"/>
					</div>
				</g:if>

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
						<g:textField type="email" name="email" required="" value="${accountInstance?.email}" class="form-control twofifty"/>
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
				  	<label for="country" class="form-label full">Country</label>
					<span class="input-container">
						<g:select name="country.id"
								from="${countries}"
								value="${accountInstance?.state?.country?.id}"
								optionKey="id" 
								optionValue="name"
								class="form-control"
								id="countrySelect"/>
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
								id="stateSelect"/>
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
				
					<g:link action="admin_edit_password" class="btn btn-default" id="${accountInstance.id}" style="margin-right:5px;">Change Password</g:link>
					
					<g:if test="${accountInstance.username != 'admin'}">
						<g:actionSubmit class="btn btn-danger" action="admin_delete" value="Delete" formnovalidate="" onclick="return confirm('Are you sure?');" />
					</g:if>
					
					<g:actionSubmit class="btn btn-primary" action="admin_update" value="Update" />
					
		
				</div>
				
			</g:form>
	
		</div>
	
	</div>	
	
	
	<script type="text/javascript" src="${resource(dir:'js/country_states.js')}"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			//TODO:object orient this
			countryStatesInit("${applicationService.getContextName()}", ${accountInstance?.state?.id})
		})
	</script>
	
	</body>
</html>
