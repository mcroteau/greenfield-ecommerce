<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : Manage Data Access Key</title>
	</head>
	<body>

		<style type="text/css">
			#outer-manage-key-container{
				text-align:center;
			}
			#manage-key-container{
				width:550px;
				margin:81px auto;
				text-align:center;
				border:solid 0px #ddd;
			}
			#key-container{
				margin:30px auto;
				display:block;
				border-radius:1px;
			}
			#key{
				font-size:20px;
				display:block;
				padding:10px 20px;
				background:#f8f8f8;
				border:solid 1px #ddd;
			}
		</style>
		<div id="outer-manage-key-container">	
			<div id="manage-key-container" class="content scaffold-create" role="main">
			
				<h2>Manage Access Key</h2>
				<p class="information secondar">This key is used to access <strong>/data/accounts?k=key</strong></p>
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
				
				<g:form action="generate_key" method="post">
					<div id="key-container">
						<span id="key">${privateKey}</span>
					</div>
					
					
					<input type="submit" value="Generate Key" class="btn btn-primary"/>
					
				</g:form>
				
			</div>
		</div>
	</body>
</html>

						
	
	