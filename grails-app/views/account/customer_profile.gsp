<%@ page import="org.greenfield.State" %>
<%@ page import="org.greenfield.Country %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

${raw(applicationService.getDefaultHeader("Account Info"))}
	
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

		<div style="padding:20px;">
			
			<h2 style="border-bottom:dashed 3px #ddd; padding-bottom:7px;">Customer Profile
			<g:link controller="account" class="btn btn-default pull-right" action="order_history">Order History</g:link>
			</h2>
			
			

				
  			<g:if test="${flash.message}">
    			<div class="alert alert-warning">${flash.message}</div>
  			</g:if>
				
  			<g:if test="${flash.error}">
    			<div class="alert alert-danger">${flash.error}</div>
  			</g:if>
			
			
			<g:hasErrors bean="${accountInstance}">
				<div class="alert alert-danger">
				<ul class="errors" role="alert">
					<g:eachError bean="${accountInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
				</div>
			</g:hasErrors>
			
			<g:form controller="account" action="customer_update" method="post" class="form-horizontal">

				<g:hiddenField name="id" value="${accountInstance?.id}" />
				<g:hiddenField name="version" value="${accountInstance?.version}" />
				
				<div class="form-group">
				  	<label for="username" class="col-sm-4 control-label">Username</label>
					${accountInstance?.username}
				</div>
				
				<div class="form-group">
				  	<label for="email" class="col-sm-4 control-label">Email Address</label>
				  	<input type="text" name="email" class="form-control" id="email" placeholder="" value="${accountInstance?.email}">
				</div>
				
				<div class="form-group">
				  	<label for="name" class="col-sm-4 control-label">Name</label>
				  	<input type="text" name="name" class="form-control" id="name" placeholder="" value="${accountInstance?.name}">
				</div>
				
				
				<div class="form-group">
				  	<label for="address1" class="col-sm-4 control-label">Address</label>
				  	<input type="text" name="address1" class="form-control" id="address1" placeholder="" value="${accountInstance?.address1}">
				</div>
				
				<div class="form-group">
				  	<label for="address2" class="col-sm-4 control-label">Address Continued</label>
				  	<input type="text" name="address2" class="form-control" id="address2" placeholder="" value="${accountInstance?.address2}">
				</div>
				
				<div class="form-group">
				  	<label for="city" class="col-sm-4 control-label">City</label>
				  	<input type="text" name="city" class="form-control" id="city" placeholder="" value="${accountInstance?.city}">
				</div>
				
				<div class="form-group">
				  	<label for="country" class="col-sm-4 control-label">Country</label>
					<g:select name="country.id"
							from="${countries}"
							value="${accountInstance?.country?.id}"
							optionKey="id" 
							optionValue="name"
							class="form-control"
							id="countrySelect"/>
				</div>
				
				<div class="form-group">
				  	<label for="state" class="col-sm-4 control-label">State</label>
					<g:select name="state.id"
							from="${State.list()}"
							value="${accountInstance?.state?.id}"
							optionKey="id" 
							optionValue="name"
							class="form-control" 
							id="stateSelect"/>
				</div>
				
				<div class="form-group">
				  	<label for="zip" class="col-sm-4 control-label">Zip</label>
				  	<input type="text" name="zip" class="form-control" id="zip" placeholder="" value="${accountInstance?.zip}">
				</div>

				
				<div class="form-group">
				  	<label for="phone" class="col-sm-4 control-label">Phone</label>
				  	<input type="text" name="phone" class="form-control" id="phone" placeholder="" value="${accountInstance?.phone}">
				</div>

				
				
	
				<div class="form-group">
					<input type="submit" class="btn btn-primary pull-right" value="Update Profile">
				</div>
				
			</g:form>
		</div>
		

		<script type="text/javascript" src="${resource(dir:'js/country_states.js')}"></script>
		
		<script type="text/javascript">
			$(document).ready(function(){
				//TODO:object orient this
				countryStatesInit("${applicationService.getContextName()}", ${accountInstance?.state?.id})
			})
		</script>

${raw(applicationService.getDefaultFooter())}	