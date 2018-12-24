<%@ page import="org.greenfield.Catalog" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : Store Layout</title>
		
		<script type="text/javascript" src="${resource(dir:'js/allow-tab.js')}"></script>
		
	</head>
	<body>

		<g:if test="${flash.message}">
			<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
		</g:if>
		
		<g:if test="${flash.error}">
			<div class="alert alert-danger" style="margin-top:10px;">${flash.error}</div>
		</g:if>

				
		<div style="margin-bottom:20px">
			
			<g:form controller="layout" action="apply_catalogs" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Catalogs" class="btn btn-default pull-right" style="display:inline-block;"/>
			</g:form>
			
			<g:form controller="layout" action="apply_products" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Products" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
			<g:form controller="layout" action="apply_pages" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Pages" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
		</div>
		<br class="clear"/>
		
		<form name="update-layout" action="/${applicationService.getContextName()}/layout/update" method="post" id="layoutForm" class="form-horizontal">
		
			<g:hiddenField name="id" value="${layoutInstance?.id}" />
			<g:hiddenField name="version" value="${layoutInstance?.version}" />
			
			
			<div class="form-group" style="margin-top:30px">
				<g:actionSubmit class="btn btn-primary pull-right" action="update" value="Update Store Layout" />
				
				<g:link class="btn btn-default pull-right" controller="layout" action="index" style="display:inline-block;margin-right:10px;">Back to Layouts</g:link>
				<br class="clear"/>
			</div>
			
			
			<div class="form-row">
				<span class="form-label twohundred secondary">Name 
					<span class="information secondary block">Name must be unique</span>
				</span>
				<span class="input-container">
					<input name="name" type="text" class="form-control threefifty" value="${layoutInstance?.name}" id="name"/>
				</span>
				<br class="clear"/>
			</div>
			<div class="form-row">
				<span class="form-label twohundred secondary">Default 
					<span class="information secondary block">Selecting default will automatically unassign the other</span>
				</span>
				<span class="input-container">
					<g:checkBox name="defaultLayout" value="${layoutInstance?.defaultLayout}" checked="${layoutInstance?.defaultLayout}"/>
				</span>
				<br class="clear"/>
			</div>
			
			<hr/>
			
			<h3>Layout HTML</h3>
			
			<p class="instructions">Layout code will automatically be wrapped by necessary wrapper html that includes everything necessary for your store front. <g:link controller="layout" action="edit_wrapper">Edit Main HTML Wrapper</g:link>. All that is needed here is all html within your <strong>body</strong> tag. Libraries included jQuery, jQuery UI &amp; Bootstrap CSS.</p>
			
			<p class="instructions">Place all layout code below. Be sure to include <strong>[[CONTENT]]</strong> tag.  <g:link controller="layout" action="tags">View All Available Tags</g:link>.</p>  
			
			<p class="instructions">[[CONTENT]] tag is where all store front product, catalog, page and other content is displayed automatically</p>
			
			<p class="instructions"><g:link controller="layout" action="how">How the Layout Engine works</g:link></p>
			

				
			<div style="border:solid 1px #ddd; background:#333;background:#384248">
				<span class="layout-code">&lt;html&gt;</span>
				<span class="layout-code">&nbsp;&nbsp;&nbsp;&lt;body&gt;</span>
				<textarea id="layout-textarea"
						name="content" 
						class="form-control">${layoutInstance?.content}</textarea>
				<span class="layout-code">&nbsp;&nbsp;&nbsp;&lt;/body&gt;</span>
				<span class="layout-code">&lt;/html&gt;</span>
			</div>

			
			<h3>Layout CSS</h3>
			<p class="instructions">Much of the storefront has css code, however your layout will need its own  supporting css code below</p>
			
			<div id="layout-code-wrapper" class="css">
				<span class="layout-code">&lt;style&gt;</span>
				<textarea id="css-textarea" 
						name="css" 
						class="form-control">${layoutInstance?.css}</textarea>
				<span class="layout-code">&lt;/style&gt;</span>
			</div>
			
			<br class="clear"/>
			
			
			<h3>Layout Javascript</h3>
			
			<p class="instructions">This section is for such things as home page carousels, sliding panes or any other Javascript goodness to add to your storefront</p>
			
			<p>Please use single quotes for javascript variables</p>
			
			<p class="instructions">This will be added at the bottom of your page layout. Warning, there are additional scripts on a per page basis that may conflict. Please check variable names on page load to ensure no duplicates or interference with base store front.</p>
			
			
			<div id="layout-code-wrapper" class="javascript">
				<span class="layout-code">&lt;script&gt;</span>
				<textarea id="javascript-textarea" 
						name="javascript" 
						class="form-control">${layoutInstance?.javascript}</textarea>
				<span class="layout-code">&lt;/script&gt;</span>
			</div>
					
			
			<div class="form-group" style="margin-top:30px">
				<g:actionSubmit class="btn btn-primary pull-right" action="update" value="Update Store Layout" />
				
				<g:actionSubmit class="btn btn-danger pull-right" action="delete" value="Delete" formnovalidate="" onclick="return confirm('Are you sure you want to delete this Layout?');" style="display:inline-block; margin-right:10px;"/>
			</div>
			
		</form>
				
		<br class="clear"/>
				
		<div style="margin-top:20px">
			
			<g:form controller="layout" action="apply_catalogs" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Catalogs" class="btn btn-default pull-right" style="display:inline-block;"/>
			</g:form>
			
			<g:form controller="layout" action="apply_products" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Products" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
			<g:form controller="layout" action="apply_pages" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Pages" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
		</div>
	
		<br class="clear"/>
		

<script type="text/javascript">
$(document).ready(function(){
	var $layoutForm = $("#layoutForm");
	
	$("#layout-textarea").allowTabChar();
	$("#css-textarea").allowTabChar();
	$("#javascript-textarea").allowTabChar();
	
	$(window).keypress(function(event) {
		/**
		console.log(event)
		console.log(event.which, event.ctrlKey)
		
        if((event.ctrlKey || event.metaKey) && event.which == 83) {
            // Save Function
            event.preventDefault();
            return false;
        }
		**/
	});
	
	$(window).keydown(function(event){
        if((event.ctrlKey || event.metaKey) && event.which == 83) {
			console.log("save...")
            event.preventDefault();
            $layoutForm.submit()
            return false;
        }
	})
	
});
</script>		
		
	</body>
</html>
