<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Edit Option</title>
	</head>
	<body>

		<div class="form-outer-container">


			<div class="form-container">

				<h2>Edit Option
					<g:link controller="specification" action="edit" name="edit" class="btn btn-default pull-right" id="${specificationOption.specification.id}">Back to Specification</g:link>
				</h2>

				<div class="clear" style="margin-top:20px;"></div>


				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>


				<g:form controller="specification" action="update_option" method="post" >

					<div style="width:550px;">

						<input type="hidden" name="id" value="${specificationOption.id}"/>


						<div class="form-row">
							<span class="form-label medium">Specification</span>
							<span class="input-container">
								<span class="label label-default">${specificationOption.specification.name}</span>
							</span>
							<br class="clear"/>
						</div>


						<div class="form-row">
							<span class="form-label medium">Option Name</span>
							<span class="input-container">
								<g:field class="form-control" name="name" value="${specificationOption.name}" style="width:250px;"/>
							</span>
							<br class="clear"/>
						</div>


						<div class="buttons-container">

							<g:link controller="specification" action="edit" name="edit" class="btn btn-default" id="${specificationOption.specification.id}" style="margin-right:10px;">Back</g:link>

            				<g:submitButton name="update" class="btn btn-primary" value="Update Option" />
        				</div>

						<div class="clear"></div>

					</div>

				</g:form>



				<div class="clear"></div>

			</div>

		</div>


	</body>
</html>
