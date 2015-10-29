<%@ page import="org.greenfield.Catalog" %>


<div class="form-group">
	<label for="name">
		<g:message code="catalog.name.label" default="Name" />
		
	</label>
	<g:textField class="form-control" name="name" value="${catalogInstance?.name}"/>
</div>

<div class="form-group">
	<label for="description">
		<g:message code="catalog.description.label" default="Description" />
		
	</label>
	<g:textArea class="form-control" name="description" id="description" cols="40" rows="15" maxlength="65535" value="${catalogInstance?.description}"/>
	
</div>

<script type="text/javascript">
    CKEDITOR.replace( 'description' );
</script>