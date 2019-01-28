<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getDefaultHeader("Newsletter Signup"))}



${raw(applicationService.getDefaultFooter())}