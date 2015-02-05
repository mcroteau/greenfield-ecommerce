<%@ page import="org.greenfield.Page" %>


<g:if test="${pageInstance.title != "Home"}">
	<div class="form-group">
		<label for="title">
			<g:message code="page.title.label" default="Title" />		
		</label>
		<g:textField class="form-control" name="title" value="${pageInstance?.title}"/>
	</div>
</g:if>
<g:else>
	<div class="form-group">
		<label for="title">
			<g:message code="page.title.label" default="Title" />		
		</label>
		<span>${pageInstance?.title}</span>
		<p>Cannot change homepage title</p>
	</div>
</g:else>	
	
<div class="form-group">
	<label for="content">
		<g:message code="page.content.label" default="Content" />
	</label>
	<g:textArea class="form-control" name="content" class="ckeditor" cols="40" rows="15" maxlength="65535" value="${pageInstance?.content}"/>
	
</div>

<script type="text/javascript">

</script>