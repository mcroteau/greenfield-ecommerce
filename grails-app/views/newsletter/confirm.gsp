<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getDefaultHeader("Newsletter Opt Out"))}

	<g:if test="${flash.message}">
		<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
	</g:if>
	
	<h3>${account.email} : Opt Out</h3>
	<p>Please confirm that you would like to opt out of news &amp; updates</p>

	<form action="/${applicationService.getContextName()}/newsletter/opt_out">
		<input type="hidden" name="id" value="${account.id}"/>
		<input type="submit" class="btn btn-default" value="Opt In"/>
	</form>

${raw(applicationService.getDefaultFooter())}