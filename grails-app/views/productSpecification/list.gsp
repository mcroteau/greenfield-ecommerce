<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Product Specifications</title>
	</head>
	<body>

		<div class="content">

			<h2>Product Specifications
				<g:link controller="productSpecification" action="create" name="edit" class="btn btn-primary pull-right" style="margin-left:10px;" id="${productInstance.id}">Add Specification</g:link>
				<g:link action="edit" name="edit" class="btn btn-default pull-right" id="${productInstance.id}">Back to Product</g:link>
			</h2>


			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


			<g:if test="${productInstance?.productSpecifications.size() > 0}">
				<table class="table table-condensed">
                    <thead>
                        <tr>
                            <th>Specification</th>
                            <th>Option</th>
                            <th></th>
                        </tr>
					</thead>
					<tbody>
                        <g:each in="${productInstance?.productSpecifications}" status="i" var="productSpecification">
                            <tr>
                                <td>${productSpecification.specificationOption.specification.name}</td>
                                <td>${productSpecification.specificationOption.name}</td>
                                <td>
                                    <g:form action="delete_specification" method="post" id="${option.id}" style="display:inline-block;">
                                        <g:actionSubmit class="btn btn-default" controller="productSpecification" action="delete_specification" value="Delete" id="${productSpecification.id}" formnovalidate="" onclick="return confirm('Are you sure?');" />
                                    </g:form>
                                </td>
                            </tr>
                        </g:each>
					</tbody>
				</table>
			</g:if>
			<g:else>
				<div class="alert alert-info">No Product Specifications added yet</div>
			</g:else>

		</div>

	</body>
</html>
