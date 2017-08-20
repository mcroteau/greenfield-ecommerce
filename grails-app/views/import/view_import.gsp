<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : View Import</title>
	</head>
	<body>
		
		<div id="export-data-view" class="content" role="main">
		
			<h2>Import Data</h2>
			<p class="secondary">Expecting Greenfield Json formatted data</p>
			<p class="secondary">* Accounts will by default be set with role of Customer only</p>
			<p class="secondary">* Import & Export is meant as an alternate backup mechanism of Greenfield data</p>

			<g:uploadForm action="import_data" class="form-horizontal" >
				<input type="file" name="json-data" id="json-data" />
				<br/>
				<g:submitButton name="import" class="btn btn-primary" value="Import Data"/>
			</g:uploadForm>
		
		</div>
		
	</body>
</html>	