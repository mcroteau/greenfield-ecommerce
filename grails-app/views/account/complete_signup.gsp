<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

<!-- changing the name Registration to something else will cause issues -->
${raw(applicationService.getScreenHeader("Registration"))}
  
	
	<div id="registration_form" style="width:350px;">
	
		<h1>Complete Signup</h1>
			
	
		<g:if test="${flash.message}">
			<div class="alert alert-info">${flash.message}</div>
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
		
			
		<p>Complete an account and start shopping.</p>
		

		<g:form action="complete" class="form" method="post">
		
			<input type="hidden" name="id" value="${accountInstance.id}"/>

			<div class="alert alert-warning" id="match-alert" style="display:none">Passwords Don't Match</div>
			<div class="alert alert-warning" id="length-alert" style="display:none">Passwords Must be at least 5 characters long</div>
		
	
			<div class="form-group">
			  	<label for="email">Email</label>
			  	${accountInstance?.email}
			</div>
			

			<div class="form-group">
			  	<label for="username">Username</label>
			  	<input type="text" name="username" class="form-control" id="username" placeholder="" value="" style="width:225px;">
			</div>

			
			<div class="form-group">
			  	<label for="password">Password</label>
			  	<input type="password" name="password" class="form-control" id="password" placeholder="***" style="width:225px;">
			</div>


			<div class="form-group">
			  	<label for="passwordRepeat">Re-Enter Password</label>
			  	<input type="password" name="passwordRepeat" class="form-control" id="passwordRepeat" placeholder="***" style="width:225px;">
			</div>
					

			<div class="form-group">
				<button type="submit" class="btn btn-primary pull-right" id="complete-registration">Register</button>
			</div>
			
		</g:form>
		
	</div>
	
			
<script type="text/javascript">
	$(document).ready(function(){
		var $password = $('#password'),
		    $passwordRepeat = $('#passwordRepeat'),
			$matchAlert = $('#match-alert')
			
		$passwordRepeat.blur(validatePasswords)
		
		function validatePasswords(data){
			if($password.val() == $passwordRepeat.val()){
				$matchAlert.hide();
			}else{
				$matchAlert.show();
			}
		
		}
		
	
	})
</script>

${raw(applicationService.getScreenFooter("Registration"))}