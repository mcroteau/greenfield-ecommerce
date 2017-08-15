<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : View Import</title>
	</head>
	<body>
		
		<div id="export-data-view" class="content" role="main">
		
			<h2>Import Data</h2>
			<p class="secondary">New Greenfield JSON Data can be loaded here.</p>
		

			<g:uploadForm action="import_data" class="form-horizontal" >
				<input type="file" name="json-data" id="json-data" />
				<br/>
				<g:submitButton name="import" class="btn btn-primary" value="Import Data"/>
			</g:uploadForm>
		
		</div>
		
	</body>
</html>	