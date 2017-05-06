<%@ page import="org.greenfield.State" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Account Info"))}

		
			<h1>Reset Password</h1>
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
					<p class="large inactive">
						<em>Step Two : Verify Email</em><br/>
					</p>
					<p class="large ">
						<em>Step Three : Reset Password</em><br/>
					</p>
					
					<p>Reseting password for <strong>${username}</strong></p>
		            <g:form action="customer_reset_password" method="post" >
						<div class="form-group">
		            		<label for="password">New Password</label>
		            		<input type="password" name="password" id="password" value="" class="form-control" style="width:250px;">
		            	</div>
						<div class="form-group">
			        		<label for="username">Confirm Password</label>
			        		<input type="password" name="confirmPassword" id="confirmPassword" value="" class="form-control" style="width:250px;" >
			        	</div>
						<input type="hidden" value="${username}" name="username"/>
						
						<input type="submit" value="Reset Password" class="btn btn-primary"/>
					</g:form>
					
				</div>
		
			</div>
			

${raw(applicationService.getFooter())}
