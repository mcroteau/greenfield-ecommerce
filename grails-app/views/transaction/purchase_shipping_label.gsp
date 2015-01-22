
<%@ page import="org.greenfield.Transaction" %>
<%@ page import="org.greenfield.ApplicationService" %>
<%@ page import="org.greenfield.common.OrderStatus" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'transaction.label', default: 'Transaction')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
	
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" style="margin-top:20px;">${flash.message}</div>
	</g:if>
	
	
	<div style="background:#efefef; border:solid 1px #ddd; padding:30px; margin-top:20px;width:400px;">
		<h2>Success</h2>
		
		<p>Successfully purchased shipping label.  Would you like to print the shipping label?</p>
		
		<g:link controller="transaction" action="print_shipping_label" id="${transactionInstance.id}" target="_blank" style="margin:auto; display:block;">
			<img src="${transactionInstance.postageUrl}" height="150" width="150" style="margin:15px auto; display:block; border:solid 1px #ddd;"/>
		</g:link>
		
		<div style="text-align:center">
			
			<g:link controller="transaction" action="show" id="${transactionInstance.id}" class="btn btn-default" style="margin:auto">Back to Order</g:link>	
			
			&nbsp;
			
			<g:link controller="transaction" 
			action="print_shipping_label" 
			id="${transactionInstance.id}"
			class="btn btn-primary" 
			target="_blank"
			style="margin:auto">Print Label</g:link>
		</div>
		
	</div>
	
</body>
</html>
