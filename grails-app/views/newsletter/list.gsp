
<%@ 
page import="org.greenfield.Account" 
page import="org.greenfield.log.CatalogViewLog"
page import="org.greenfield.log.ProductViewLog"
page import="org.greenfield.log.PageViewLog"
page import="org.greenfield.log.SearchLog"
page import="org.greenfield.Transaction"
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title>Greenfield : Accounts</title>

		<style type="text/css">
			#newsletter-signups-link{
				text-align:right;
			}
		</style>
	</head>
	<body>

		<div id="list-account" class="content scaffold-list" role="main">
		

			<g:if test="${admin}">
				<h2 class="pull-left">Admin Accounts</h2>
			</g:if>
			<g:else>
				<h2 class="pull-left">Customer Accounts</h2>
			</g:else>
			

			<g:if test="${admin}">
				<div style="float:right; width:470px; text-align:right ">
					<g:form action="admin_list" class="form-horizontal">
						<input type="hidden" name="admin" value="true"/>
						<input type="text" name="query" id="searchbox" class="form-control" style="width:250px;" placeholder="search name, username or email" value="${query}"/>
						<g:submitButton name="submit" value="Search" id="search" class="btn btn-info"/>
					</g:form>
				</div>
			</g:if>
			<g:else>
				<div style="float:right; width:470px; text-align:right ">
					<g:form action="admin_list" class="form-horizontal">
						<input type="hidden" name="admin" value="false"/>
						<input type="text" name="query" id="searchbox" class="form-control" style="width:250px;" placeholder="search name, username or email" value="${query}"/>
						<g:submitButton name="submit" value="Search" id="search" class="btn btn-info"/>
					</g:form>
				</div>
			</g:else>
		
			<br class="clear"/>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			
			
			<g:if test="${accountsList}">
			
			
				<table class="table">
					<thead>
						<tr>
							<g:sortableColumn property="email" title="Email"/>
							
							<th></th>
						</tr>
					</thead>
					<tbody>
					<g:each in="${accountsList}" status="i" var="accountInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						
							<td>${accountInstance.email}
							</td>

							<td>
								<g:form action="opt_out" method="post" id="${accountInstance.id}">
									<input type="submit" value="Opt Out" class="btn btn-default"/>
								</g:form>
							</td>
						
						</tr>
					</g:each>
					</tbody>
				</table>
				
				<div class="btn-group">
					<g:paginate total="${accountsTotal}" 
						 	params="${[query : params.query ]}"/>
				</div>

				<div id="newsletter-signups-link">
					<g:link controller="newsletter" action="list">Newsletter Signups</g:link>
				</div>
			</g:if>
			<g:else>
				<br/>
				<p style="color:#333;padding:0px 40px;">No email signups found...</p>
			</g:else>
		</div>
	</body>
</html>
