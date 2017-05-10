<%@ page import="org.greenfield.ApplicationService" %>
<!-- //TODO: MOVE HOME PAGE OUT OF ApplicationService -->
<%@ page import="org.greenfield.Page" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Home"))}

<g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
</g:if>

${raw(applicationService.getHomePage())}
	
${raw(applicationService.getFooter())}		