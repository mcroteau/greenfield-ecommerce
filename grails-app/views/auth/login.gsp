<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<title>
${applicationService.getStoreName()} : Admin Login</title>
		<link rel="stylesheet" href="${resource(dir:'bootstrap/3.1.1/css', file:'bootstrap.min.css')}" />
	
	</head>
	<body>
		
<style type="text/css">
	body{ 
		padding:0px;
		height:100%;
		text-align:center;
		background:#efefef;
		background:#1c695b;
	}
	.container{
		padding:20px 50px 50px 50px;
		width:400px;
		margin:50px auto;
		background:#fff;
		text-align:left !important;
		border-radius: 3px 3px 3px 3px;
		-moz-border-radius: 3px 3px 3px 3px;
		-webkit-border-radius: 3px 3px 3px 3px;
		box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.38);
		-moz-box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.38);
		-webkit-box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.38);
	}

</style>
	
	<div class="container">
	
		<g:if test="${flash.message}">
			<div class="alert alert-info">${flash.message}</div>
		</g:if>
		
		<div>
			<h2 class="hint" style="color:rgba(0,0,0,0.26) !important;">Administration Login</h2>
			<g:form controller="auth" action="signIn" class="form">
				<div class="form-group">
				  	<label for="username">Username</label>
				  	<input type="text" name="username" class="form-control" id="username" placeholder="">
				</div>
				<div class="form-group">
				  	<label for="password">Password</label>
				  	<input type="password" name="password" class="form-control" id="password" placeholder="******">
				</div>
				<div class="checkbox">
				  	<label>
				    	<g:checkBox name="rememberMe" value="${rememberMe}" />&nbsp;Remember Me
				  	</label>
				</div>
			
				<div class="form-group">
				  	<label><g:link controller="account" action="forgot">Forgot password?</g:link></label>
				</div>
				
				<input type="hidden" name="targetUri" value="${targetUri}" />
				
				<button type="submit" class="btn btn-default btn-primary">Login</button>
				
			</g:form>
		</div>
		
	</div>
			
	</body>
</html>

