<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getDefaultHeader("Newsletter Signup"))}

	<g:if test="${flash.message}">
		<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
	</g:if>
	
	<h3>${account.email} : Opt In</h3>
	<p>We found an account for the email entered, however you have not opted in for news and updates</p>

	<form action="/${applicationService.getContextName()}/newsletter/opt_in">
		<input type="hidden" name="id" value="${account.id}"/>
		<input type="submit" class="btn btn-default" value="Opt In"/>
	</form>

${raw(applicationService.getDefaultFooter())}