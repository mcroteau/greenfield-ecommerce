<%@ page import="org.greenfield.ShoppingCart" %>



<div class="fieldcontain ${hasErrors(bean: shoppingCartInstance, field: 'status', 'error')} ">
	<label for="status">
		<g:message code="shoppingCart.status.label" default="Status" />
	</label>
	<g:textField class="form-control" style="width:120px;" name="status" value="${shoppingCartInstance?.status}"/>
</div>

<p>Possible Status Codes : <em>ACTIVE, SHIPPED</em></p>
