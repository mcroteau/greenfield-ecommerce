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
	}
	.container{
		padding:50px;
		width:500px;
		margin:20px auto;
		background:#fff;
		text-align:left !important;
		-webkit-border-radius: 5px;
		-moz-border-radius: 5px;
		border-radius: 5px;
		-webkit-box-shadow: 0px 0px 25px 0px rgba(204,204,204,1);
		-moz-box-shadow: 0px 0px 25px 0px rgba(204,204,204,1);
		box-shadow: 0px 0px 25px 0px rgba(204,204,204,1);
	}
</style>
	
	<div class="container">
	
		<g:if test="${flash.message}">
			<div class="alert alert-info">${flash.message}</div>
		</g:if>
		
		<div>
			<h2>Admin Login</h2>
			<g:form controller="auth" action="signIn" class="form">
				<div class="form-group">
				  	<label for="username">Username</label>
				  	<input type="text" name="username" class="form-control" id="username" placeholder="">
				</div>
				<div class="form-group">
				  	<label for="password">Password</label>
				  	<input type="password" name="password" class="form-control" id="password" placeholder="***">
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

