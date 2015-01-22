<%@ page import="org.greenfield.Account" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		<style type="text/css">
			.form-group label{
				font-weight:normal;
			}
			.form-group .form-control{
				display:inline-block;
				width:300px;
			}
			.form-group label em{
				font-weight:normal;
				font-size:11px;
				color:#777
			}
			#information h3{
				margin:20px 0px 30px 0px;
				padding-bottom:7px;
				border-bottom:dashed 3px #ddd;
			}
		</style>
	
	</head>
	
	<body>
		<div>

			<h2>Create Customer</h2>
			
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
			
			<g:form action="admin_save" class="form-horizontal" role="form">


				<%@ page import="org.greenfield.Account" %>
				<%@ page import="org.greenfield.State" %>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'username', 'error')} required">
					<label for="username" class="col-sm-3 control-label">
						<g:message code="account.username.label" default="Username" />
						<span class="required-indicator">*</span>
					</label>
					<g:textField class="form-control"  name="username" required="" value="${accountInstance?.username}"/>
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'email', 'error')} required">
					<label for="email" class="col-sm-3 control-label">
						<g:message code="account.email.label" default="Email" />
						<span class="required-indicator">*</span>
					</label>
					<g:textField class="form-control" type="email" name="email" required="" value="${accountInstance?.email}"/>
				</div>


				<div class="form-group ${hasErrors(bean: accountInstance, field: 'passwordHash', 'error')} ">
					<label for="password" class="col-sm-3 control-label">Password</label>
					<input type="password" class="form-control"  name="passwordHash" value="${accountInstance?.passwordHash}"/>
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'name', 'error')} ">
					<label for="name" class="col-sm-3 control-label">
						<g:message code="account.name.label" default="Name" />
		
					</label>
					<g:textField class="form-control"  name="name" value="${accountInstance?.name}"/>
				</div>


				<div class="form-group ${hasErrors(bean: accountInstance, field: 'address1', 'error')} ">
					<label for="address1" class="col-sm-3 control-label">
						<g:message code="account.address1.label" default="Address1" />
		
					</label>
					<g:textField class="form-control"  name="address1" value="${accountInstance?.address1}"/>
				</div>


				<div class="form-group ${hasErrors(bean: accountInstance, field: 'address2', 'error')} ">
					<label for="address2" class="col-sm-3 control-label">
						<g:message code="account.address2.label" default="Address2" />
		
					</label>
					<g:textField class="form-control"  name="address2" value="${accountInstance?.address2}"/>
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'city', 'error')} ">
					<label for="city" class="col-sm-3 control-label">
						<g:message code="account.city.label" default="City" />
		
					</label>
					<g:textField class="form-control"  name="city" value="${accountInstance?.city}"/>
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'state', 'error')} ">
					<label for="state" class="col-sm-3 control-label">
						<g:message code="account.state.label" default="State" />
		
					</label><g:select name="state.id"
									  from="${State.list()}"
									  value="${accountInstance?.state?.id}"
									  optionKey="id" 
									  optionValue="name" 
									  class="form-control"/>
											  
				</div>

				<div class="form-group ${hasErrors(bean: accountInstance, field: 'zip', 'error')} ">
					<label for="zip" class="col-sm-3 control-label">
						<g:message code="account.zip.label" default="Zip" />
					</label>
					<g:textField class="form-control"  name="zip" value="${accountInstance?.zip}"/>
				</div>


				<div class="form-group ${hasErrors(bean: accountInstance, field: 'phone', 'error')} ">
					<label for="phone" class="col-sm-3  control-label">
						<g:message code="account.phone.label" default="Phone" />
					</label>
					<g:textField class="form-control"  name="phone" value="${accountInstance?.phone}"/>
				</div>


				<div class="form-group">
					<label class="col-sm-3 control-label">Is Adminstrator</label>
					<g:checkBox name="admin" value="${admin}" checked="${admin}"/>
					<span style="font-size:11px;">All Accounts are customers by default.</span>
				</div>	
				
				<div class="form-group">	
					<label class="col-sm-3"></label>
					<g:submitButton name="create" class="btn btn-primary" value="Create" />		
				</div>
			</g:form>
		</div>
	</body>
</html>
