<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getDefaultHeader("Newsletter Signup"))}

	<g:if test="${flash.message}">
		<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
	</g:if>
	
	<style type="text/css">	
		#signup{
			width:350px !important;
		}
		input, span{
			float:left;
			display:inline-block
		}
	</style>


	<form action="/${applicationService.getContextName()}/newsletter/signup" method="post">
		<span>Sign up for news &amp; updates</span><br/>
		<input type="text" name="email" id="signup" class="form-control" value="" placeholder="enter your email"/>
		<input type="submit" class="btn btn-default" value="Sign Up"/>
		<br class="clear"/>
	</form>

${raw(applicationService.getDefaultFooter())}