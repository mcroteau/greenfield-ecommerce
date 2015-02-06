<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.Catalog" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<style type="text/css">
label{
	text-align:right;
}
</style>

<h4>Basic Info</h4>

<div class="form-group" style="margin-top:10px;">
	<label class="col-sm-4 control-label">Catalog</label>
	<g:select id="catalog" name='catalog.id' value="${productInstance?.catalog?.id}"
	    noSelection="${['null':'Select One...']}"
	    from='${Catalog?.list()}'
	    optionKey="id" optionValue="name"
		class="form-control"
		style="width:150px;"></g:select>
</div>


<div class="form-group">
	<label for="name" class="col-sm-4 control-label">
		<g:message code="product.name.label" default="Name" class="col-sm-3 control-label" />
		<span class="required-indicator">*</span>
		
	</label>
	<g:textField class="form-control" name="name" value="${productInstance?.name}" style="width:150px;"/>
</div>

<div class="form-group">
	<label for="price" class="col-sm-4 control-label">
		<g:message code="product.price.label" default="Price" />
		<span class="required-indicator">*</span>
	</label>
	<g:field class="form-control" name="price" value="${applicationService.formatPrice(productInstance?.price)}" required="" style="width:150px;"/>
</div>


<div class="form-group">
	<label for="quantity" class="col-sm-4 control-label">
		<g:message code="product.quantity.label" default="Quantity" />
		<span class="required-indicator">*</span>
	</label>
	<g:field class="form-control" name="quantity" type="number" value="${productInstance.quantity}" required="" style="width:150px;"/>
</div>

					
<div class="form-group">
	<label for="quantity" class="col-sm-4 control-label">
		Disabled
	</label>
	<g:checkBox name="disabled" value="${productInstance.disabled}" />
</div>

					

<h4>Shipping Info</h4>

<div class="form-group">
	<label for="name" class="col-sm-4 control-label">
		<g:message code="product.weight.label" default="Weight" class="col-sm-3 control-label" />
		<span class="required-indicator">*</span>
		<span style="font-size:11px;color:#777">ounces</span>
	</label>
	<g:textField class="form-control" name="weight" value="${productInstance?.weight}" style="width:75px;"/>
</div>




<div class="form-group">
	<label for="name" class="col-sm-4 control-label">
		<g:message code="product.length.label" default="Length" class="col-sm-3 control-label" />
		<span style="font-size:11px;color:#777">inches</span>
	</label>
	<g:textField class="form-control" name="length" value="${productInstance?.length}" style="width:75px;"/>
</div>


<div class="form-group">
	<label for="name" class="col-sm-4 control-label">
		<g:message code="product.width.label" default="Width" class="col-sm-3 control-label" />
		<span style="font-size:11px;color:#777">inches</span>
	</label>
	<g:textField class="form-control" name="width" value="${productInstance?.width}" style="width:75px;"/>
</div>


<div class="form-group">
	<label for="name" class="col-sm-4 control-label">
		<g:message code="product.height.label" default="Height" class="col-sm-3 control-label" />
		<span style="font-size:11px;color:#777">inches</span>
	</label>
	<g:textField class="form-control" name="height" value="${productInstance?.height}" style="width:75px;"/>
</div>



<h4>Description</h4>

<div class="form-group">
	<textarea class="form-control" name="description" id="description" cols="10" rows="5">${productInstance?.description}</textarea>
</div>

