<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getDefaultHeader("Login"))}
  

	<g:if test="${flash.message}">
		<div class="alert alert-info">${flash.message}</div>
	</g:if>
	
	<div style="float:left; width:40%; border-right:dashed 1px #ddd; padding:20px;">
		<g:form controller="login" action="authenticate" class="form">
			<div class="form-group">
			  	<label for="username">Username</label>
			  	<input type="text" name="username" class="form-control" id="username" placeholder="">
			</div>
			<div class="form-group">
			  	<label for="password">Password</label>
			  	<input type="password" name="password" class="form-control" id="password" placeholder="***">
			</div>
				
			<div class="checkbox">
				<input type="checkbox" class="chk" name="remember-me" id="remember_me" />
				<label for="remember_me">Remember me</label>
			</div>
		
			<div class="form-group">
			  	<label><g:link controller="account" action="customer_forgot">Forgot password?</g:link></label>
			</div>
			
			<input type="hidden" name="targetUri" value="${targetUri}" />
			<button type="submit" class="btn btn-default" id="customer_login">Login</button>
			
		</g:form>
	</div>
	
	
	<div style="float:left; padding:20px;">
		<h5>Dont have an account?</h5>
		<p>Signup to continue shopping</p>
		<g:link controller="account" action="customer_registration">Register</g:link>
	</div>
	
	<br style="clear:both">
			
			
${raw(applicationService.getDefaultFooter())}
