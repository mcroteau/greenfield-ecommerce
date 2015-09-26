<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Edit Specification</title>
	</head>
	<body>

		<div class="content">

			<h2>Edit Specification</h2>

			<br class="clear"/>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


			<div style="width:355px;float:left">

				<g:form action="update" method="post">

					<input type="hidden" name="id" value="${specificationInstance.id}"/>
					<input type="hidden" name="catalogId" value="${catalogInstance.id}"/>


					<div class="form-row">
						<span class="form-label secondary">Name</span>
						<input type="text" name="name" class="form-control" value="${specificationInstance?.name}" style="width:150px;margin-left:15px;display:inline-block"/>

						<g:if test="${catalogInstance.subcatalogs}">
							<span class="form-label secondary">Apply to subcatalogs</span>
							<span class="input-container">
								<input type="checkbox" ${specificationInstance?.applySubcatalogs} name="applySubcatalogs" id="applySubcatalogs"/>
								<br/>
								<g:each in="${catalogInstance.subcatalogs}" var="subcatalog">
									<span class="label label-default">${subcatalog.name}</span>
								</g:each>
							</span>
						</g:if>

						<br class="clear"/>
					</div>

					<g:submitButton name="update" class="btn btn-primary" value="Update Specification Settings" style="margin-bottom:20px;"/>

					<br class="clear"/>
				</g:form>


				<g:if test="${specificationInstance.specificationOptions?.size() > 0}">

					<h4 style="margin-top:20px">Current Options</h4>

					<table class="table table-condensed">
						<thead>
							<tr>
								<th>Name</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						<g:each in="${specificationInstance.specificationOptions}" status="i" var="option">
							<tr id="variant_${option.id}">
								<td>${option.name}</td>
								<td>
									<g:link class="btn btn-default" action="edit_option" id="${option.id}" params="[ catalogId : catalogInstance.id ]">Edit</g:link>
									<g:form action="delete_option" method="post" id="${option.id}" style="display:inline-block;">
										<input type="hidden" name="catalogId" value="${catalogInstance.id}"/>
										<g:actionSubmit class="btn btn-default" controller="specification" action="delete_option" value="Delete" id="${option.id}" formnovalidate="" onclick="return confirm('Are you sure?');" />
									</g:form>
								</td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</g:if>
				<g:else>
					<div class="alert alert-info">No options added yet</div>
				</g:else>

			</div>



			<g:form controller="specification" action="add_option" method="post" >

				<div style="width:350px; float:left; margin-left:40px; border:solid 1px #ddd; padding:15px; background:#f8f8f8">

					<g:if test="${flash.optionMessage}">
						<div class="alert alert-info" role="status">${flash.optionMessage}</div>
					</g:if>

					<h3 style="margin:0px auto 20px; text-align:center">Add Option</h3>

					<input type="hidden" name="id" value="${specificationInstance.id}"/>
					<input type="hidden" name="catalogId" value="${catalogInstance.id}"/>

					<div class="form-row">
						<span class="form-label minimum secondary">Name</span>
						<span class="input-container">
							<g:field class="form-control" name="name" value="${name}" style="width:175px;"/>
						</span>
						<br class="clear"/>
					</div>

            		<g:submitButton name="add" class="btn btn-primary pull-right" value="Add Option" />

				</div>

			</g:form>



			<div class="clear"></div>


		</div>


	</body>
</html>
