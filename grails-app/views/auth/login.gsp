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
		padding:50px 50px 50px 50px;
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

	.btn.btn-primary{
		border:solid 1px #2068eb;
		background: #357bfc;
		background: -moz-linear-gradient(top,  #357bfc 0%, #226df8 100%); 
		background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#357bfc), color-stop(100%,#226df8)); 
		background: -webkit-linear-gradient(top,  #357bfc 0%,#226df8 100%);
		background: -o-linear-gradient(top,  #357bfc 0%,#226df8 100%); 
		background: -ms-linear-gradient(top,  #357bfc 0%,#226df8 100%); 
		background: linear-gradient(to bottom,  #357bfc 0%,#226df8 100%); 
		filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#357bfc', endColorstr='#226df8',GradientType=0 );	
	}
	
	h1.maintenance-header{
		font-size:20px;
		margin:0px auto;
		text-transform: uppercase;
		color:rgba(0,0,0,0.54) !important;
	}
	
	a{
		font-weight:normal;
		font-size:13px;
	}
	.clear{
		clear:both;
	}
</style>
	
	<div class="container">
	
		<g:if test="${flash.message}">
			<div class="alert alert-info">${flash.message}</div>
		</g:if>
		
		<div>
			<h1 class="maintenance-header">Administration Login</h1>
			<br class="clear"/>
			
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

