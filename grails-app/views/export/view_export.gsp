<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Greenfield : View Export</title>
	</head>
	<body>
		
		<div id="export-data-view" class="content" role="main">
		
			<h2>Export Data</h2>
			<p class="secondary">All Greenfield data can be exported from this screen. No data will be altered, only exported. <br/>You can use this data to import into a new instance of Greenfield or use at your own will.</p>
		
			
			<g:form action="export_data" >
				<g:submitButton name="export-data" class="btn btn-primary" value="Export Data"/>
			</g:form>
		
		</div>
		
	</body>
</html>	