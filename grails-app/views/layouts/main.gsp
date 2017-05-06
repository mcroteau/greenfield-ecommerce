<!DOCTYPE html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	
	<g:layoutHead/>
	
</head>
<body>

	<div class="container">
	
		<div class="row">
		
			<div class="span12">
			
				<div class="navbar navbar-default" role="navigation">
					<div class="navbar-header">
					
						<g:link controller="content" action="home" class="${home_active}" params="[activeLink: 'home']">Home</g:link>
						<g:link controller="content" action="about" class="${about_active}" params="[activeLink: 'about']">About</g:link>
						
						<g:if test="${shiro?.principal()}">
							Welcome back 
							<g:link controller="account" action="profile"><sec:username/></g:link>&nbsp;&nbsp;|&nbsp;&nbsp;<g:link controller="auth" action="signOut" class="navbar-brand">Logout</g:link>
						</g:if>
						
						<g:else>
							<g:link controller="auth" action="login" class="navbar-brand">Login</g:link>
						</g:else>
						
					</div>
				</div>
				
			</div>
			
		</div>
	
	
		<g:layoutBody/>
		
		
		<footer class="footer" style="background:#efefef; padding:20px">
		</footer>
		
	</div>
	
</body>
</html>
