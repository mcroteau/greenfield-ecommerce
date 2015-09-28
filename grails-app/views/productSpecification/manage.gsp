<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Add Product Specification</title>
	</head>
	<body>

		<div class="content">
		
			<h2>Manage Product Specifications
				<g:link controller="product" action="edit" name="list" class="btn btn-default pull-right" id="${productInstance.id}">Back to Product</g:link>
			</h2>

		    <p class="information">You may select one from each specification.  Each selection will be filterable via catalog selection.</p>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


			<div id="create-specification-container">

                <div class="form-row">
                    <span class="form-label secondary">Product</span>
                    <span class="input-container">
                        <span class="label label-default">${productInstance?.name}</span>
                    </span>
                    <br class="clear"/>
                </div>


                <g:if test="${availableSpecifications}">

                    <g:each in="${availableSpecifications}" var="specification">

                        <g:if test="${specification.specificationOptions}">

                            <div class="form-row">
                                <h3>${specification.name}</h3>
                                <table style="border:none">
                                <g:each in="${specification.specificationOptions}" var="specificationOption">
                                    <tr>
                                        <td style="width:300px">${specificationOption.name}</td>
                                        <td>
                                            <%
                                            def selected = false
                                            productInstance.productSpecifications.each(){ productSpecification ->
                                                if(productSpecification.specificationOption.id == specificationOption.id){
                                                    selected = true
                                                }
                                            }
                                            %>
                                            <g:if test="${selected}">
                                                <g:form action="remove" method="post" id="${productInstance.id}">
                                                    <input type="hidden" name="optionId" value="${specificationOption.id}"/>
                                                    <g:actionSubmit class="btn btn-danger" action="remove" value="Remove Option" id="${productInstance.id}" formnovalidate="" onclick="return confirm('Are you sure?');" />
                                                </g:form>
                                            </g:if>
                                            <g:else>
                                                <g:form action="add" method="post" id="${productInstance.id}">
                                                    <input type="hidden" name="optionId" value="${specificationOption.id}"/>
                                                    <g:actionSubmit class="btn btn-default" action="add" value="Select Option" id="${productInstance.id}"/>
                                                </g:form>
                                            </g:else>
                                        </td>
                                    </tr>
                                </g:each>
                                </table>
                                <br/>
                            </div>
                        </g:if>
                    </g:each>
                </g:if>

			</div>
			
			<div class="clear"></div>
				
		</div>

    <!-- TODO: move to generic javascript file -->
    <script type="text/javascript" src="${resource(dir:'js/product_catalogs.js')}"></script>

    <script type="text/javascript">

        var catalogIds = [];
        <g:if test="${catalogIdsArray}">
            catalogIds = ${catalogIdsArray};
        </g:if>
        var catalogIdsString = catalogIds.join();

    </script>

	</body>
</html>
