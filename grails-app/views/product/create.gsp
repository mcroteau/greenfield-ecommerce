<%@ page import="org.greenfield.Product" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
	
		<div id="create-product" class="content scaffold-create" role="main">
			
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:hasErrors bean="${productInstance}">
				<style type="text/css">
					.errors{
						padding:0px;
						margin-left:0px;
					}
					.errors li{
						list-style:none;
					}
				</style>
				
				<ul class="errors" role="alert">
					<g:eachError bean="${productInstance}" var="error">
					<li class="alert alert-danger"<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>
			
			<div style="width:500px;">
				<g:uploadForm action="save" class="form-horizontal" >
					<g:render template="form"/>
					
					
					<h4>Product Image</h4>
					<div class="form-group">
						<input type="file" name="image" id="image" />	
					</div>
					
						
					<div class="form-group">	
						<g:submitButton name="create" class="btn btn-primary pull-right" value="${message(code: 'default.button.create.label', default: 'Create')}" />
					</div>
				</g:uploadForm>
			</div>
		</div>
	</body>
</html>
