<%@ page import="org.greenfield.ApplicationService" %>
<!-- //TODO: MOVE HOME PAGE OUT OF ApplicationService -->
<%@ page import="org.greenfield.Page" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Home"))}

<g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
</g:if>

<table>
<tr>
	<th></th>
</tr>
<g:each in="${accounts}" var="account">
	<tr style="border:solid 1px #ccc;">
		<td>${account.username}</td>
		<td>
			<g:each in="${account.permissions}" var="permission">
				${permission.permission}<br/>
			</g:each>
		</td>
	</tr>
</g:each>
</table>

${applicationService.getHomePage()}


		
${raw(applicationService.getFooter())}		