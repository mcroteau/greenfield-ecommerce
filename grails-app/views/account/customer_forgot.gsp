
<%@ page import="org.greenfield.State" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Account Info"))}




<h1>Begin Password Reset Process</h1>

<g:if test="${flash.message}">
	<div class="alert alert-info" id="">${flash.message}</div>		
</g:if>


<g:hasErrors bean="${accountInstance}">
	<div class="message error" id="">
		<span class="header">Something Went wrong</span>
		<span class="messageDetails">
	    	<g:renderErrors bean="${accountInstance}" as="list" />
		</span>
	</div>
</g:hasErrors>

<div class="resetWrapper notes">

	<g:form action="customer_send_reset_email" method="post" >

		
		<style type="text/css">
			.form-group em{
				font-size:18px;
				font-weight:bold;
				font-style:normal;
			}
			.form-group.inactive em{
				color:#777;
				font-style:italic;
			}
		</style>
		
		<div class="form-group">
			<em class="highlight">Step One : Enter Email</em><br/>
			<span class="small">An email will be sent to this address with instructions on how to continue reset process</span>
			<input type="email" value="" id="email" name="email" class="form-control" style="width:250px;"/>
		</div>
		
		<div class="form-group inactive">
			<em>Step Two : Verify Email</em><br/>
			<span class="small">An email will be sent to this address with instructions on how to continue reset process</span>
		</div>
		
		<div class="form-group inactive">
			<em>Step Three : Reset Password</em><br/>
		</div>
    	
		
		<input type="submit" value="Start Reset Process" class="btn btn-primary"/>
		
	</g:form>

</div>

<br class="clear"/>


${raw(applicationService.getFooter())}

