<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Add Specification</title>
	</head>
	<body>

        <div id="catalog-selection-backdrop"></div>

        <div id="catalog-selection-modal">
            <h3>Product Catalogs</h3>
            <p class="information secondary">Selecting a Subcatalog will automatically select all parent Catalogs up to the top level Catalog.</p>
            <div id="catalog-selection-container">
                ${catalogIdSelectionList}
            </div>
            <a href="javascript:" class="btn btn-default pull-right" style="margin-top:15px;" id="close-catalogs-select-modal">Accept &amp; Close</a>
            <br class="clear"/>
        </div>


		<div class="content">
		
			<h2>Add Specification
				<g:link controller="specification" action="list" name="list" class="btn btn-default pull-right">Back to Specifications</g:link>
			</h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


			<div id="create-specification-container">
			
				<g:form action="save" method="post">

					<div class="form-row">
						<span class="form-label secondary">Name</span>
						<span class="input-container">
							<input type="text" name="name" class="form-control" value="${name}" style="width:200px;display:inline-block"/>
						</span>
						<br class="clear"/>
					</div>
					
					<div class="form-row">
						<span class="form-label secondary">Catalogs<br/>
							<a href="javascript:" id="catalog-selection-modal-link">Add/Remove Catalogs</a>
						</span>
						<span class="input-container threefifty" id="selected-catalogs-span">
							<g:each in="${specification?.catalogs}" var="catalog">
								<span class="label label-default">${catalog.name}</span>
							</g:each>
						</span>
						<input type="hidden" value="" id="catalogIds" name="catalogIds"/>
						<br class="clear"/>
					</div>
					
					<g:submitButton name="save" class="btn btn-primary" value="Save Specification" />
					
				</g:form>
				
			</div>
			
			<div class="clear"></div>
				
		</div>

    <!-- TODO: move to generic javascript file -->
    <script type="text/javascript" src="${resource(dir:'js/specification_catalogs.js')}"></script>

    <script type="text/javascript">

        var catalogIds = [];
        <g:if test="${catalogIdsArray}">
            catalogIds = ${catalogIdsArray};
        </g:if>
        var catalogIdsString = catalogIds.join();

    </script>

	</body>
</html>
