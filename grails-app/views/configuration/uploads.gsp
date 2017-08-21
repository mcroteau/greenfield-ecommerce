<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title>Greenfield : File Uploads</title>
	</head>
	<body>
	
		<div id="file-upload" class="content scaffold-create" role="main">
			
			<h1>File Uploads</h1>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<div style="width:600px;">
			
				<g:uploadForm action="upload" method="post" >
				
					<div class="form-group">
						<label>Select file to upload</label>
						<input type="file" name="file" id="file" />	
					</div>
					
					<div class="form-group" style="margin-top:20px;">	

						<g:link action="index" name="cancel" class="btn btn-default">Cancel</g:link>
						<g:submitButton name="add" class="btn btn-primary" value="Upload File" />
					</div>
					<p class="secondary">File names may change during upload</p>
				</g:uploadForm>
				
				<g:if test="${uploadInstanceList.size() > 0}">
					<table class="table">
						<thead>
							<tr>	
								<th>Id</th>
								<th>File URL</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<g:each in="${uploadInstanceList}" status="i" var="upload">
							<tr>
								<td>${upload?.id}</td>
								<td>
									<a href="/${applicationService.getContextName()}/${upload?.url}">
										/${applicationService.getContextName()}/${upload?.url}
									</a>
								</td>
								<td><g:link action="remove_upload" class="btn btn-default" id="${upload?.id}">Remove</g:link>
								</td>
							</tr>
							</g:each>
						</tbody>
					</table>

					<div class="btn-group">
						<g:paginate total="${uploadInstanceTotal}" />
					</div>
					
				</g:if>
			</div>
		</div>
	</body>
</html>
