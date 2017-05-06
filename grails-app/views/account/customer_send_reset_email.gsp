<%@ page import="org.greenfield.State" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Account Info"))}

<div class="row">

	<div class="span12">	
	
		<h1>Email Confirmation Sent</h1>

		<g:if test="${flash.message}">
			<div class="alert alert-info" id="">${flash.message}</div>		
		</g:if>


		<style type="text/css">
			.large em{
				font-size:18px;
				font-weight:bold;
				font-style:normal;
			}
			.large.inactive em{
				color:#777;
				font-style:italic;
			}
		</style>
		
	
		<div class="resetWrapper notes">
	
			<div class="resetForm">
				<p class="large inactive">
					<em class="highight">Step One : Enter Email</em><br/>
				</p>
				<p class="large">
					<em>Step Two : Verify Email</em><br/>
					<span class="small">
					Successfully sent reset password email confirmation. Please check the email address entered for instructions on how to continue the password reset process.</span>
				</p>
				<p class="large inactive">
					<em>Step Three : Reset Password</em><br/>
				</p>
			</div>

	
		</div>
		
		<br class="clear"/>
	</div>
</div>
	
${raw(applicationService.getFooter())}
