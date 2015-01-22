
<%@ page import="org.greenfield.State" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${applicationService.getHeader("Account Info")}




<h1>Reset Successful</h1>

<p>Successfully reset password.  Please login with new password to continue</p>



${applicationService.getFooter()}

