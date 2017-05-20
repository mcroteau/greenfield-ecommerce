
<%@ page import="org.greenfield.Account" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title>Greenfield : Page Activity</title>

	</head>
	<body>

		<div id="list-account" class="content scaffold-list" role="main">
		

			<g:link controller="account" action="account_activity" id="${accountInstance.id}" class="btn btn-default pull-right" name="Back to Accounts">Back to Activity</g:link>

			<h2>
				<g:if test="${accountInstance.name}">
					${accountInstance?.name}: 
				</g:if>
				${accountInstance.username}
			</h2>


			<p class="information secondary" style="margin-bottom:0px;">${accountInstance.username}'s overall page viewing activity</p>

			<br class="clear" style="display:inline-block;line-height:1.0em;padding:0px;margin:0px;"/>
			

			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


			<g:if test="${pageViewStats}">

				<div id="activity-stats-wrapper" class="pull-left">
					<table class="table" style="width:400px">
						<tr>
							<th>Page</th>
							<th>Date/Time</th>
						</tr>
						<g:each in="${pageViewLogs}" var="pageViewLog">
							<tr>
								<td>${pageViewLog.page.title}</td>
								<td><g:formatDate format="hh:mm z - dd MMM yyyy " date="${pageViewLog.dateCreated}"/></td>
							</tr>
						</g:each>
					</table>
				</div>


				<div class="activity-stats pull-right">
					<h4 class="secondary">Pages Viewed</h4>

					<g:if test="${pageViewStats}">
						<g:each in="${pageViewStats}" var="statistic" status="i">
							<div class="activity-stat-row">
								<span class="activity-stat-title">${statistic.value.page}</span>
								<span class="activity-stat-value">${statistic.value.count}</span>
								<br class="clear"/>
							</div>
						</g:each>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							No Activity Data Available
						</div>
					</g:else>
				</div>
			</g:if>
			<g:else>
				<div style="margin:30px auto 40px auto" class="hint">
					No Activity Data Available
				</div>
			</g:else>	
		</div>
	</body>
</html>
