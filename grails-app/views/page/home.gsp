<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>
${raw(applicationService.getPageHeader(pageInstance))}
	
<g:if test="${flash.message}">
	<div class="alert alert-info" role="status">${flash.message}</div>
</g:if>

${raw(pageInstance?.content)}	

${raw(applicationService.getPageFooter(pageInstance))}