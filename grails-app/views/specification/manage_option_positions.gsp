<%@ page import="org.greenfield.Product" %>
<%@ page import="org.greenfield.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('org.greenfield.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Order Options</title>
	
		
		<script type="text/javascript" src="${resource(dir:'js/lib/jquery-ui/1.11.2/jquery-ui.min.js')}"></script>
			
		<style type="text/css">
			#specificationOption{
				padding:0px;
				width:400px;
				float:left;
			}
			#specificationOptions li{
				list-style:none;
				display:block;
				padding:10px 15px;
				margin:3px auto;
				font-size:14px;
				cursor:move;
				height:45px;
				border:dashed 1px #ddd;
				background:#f8f8f8;
			}
			.position-highlight{
				border:dashed 1px #ddd;
				background:#fff !important;
				height:45px;
			}
			.order-number{
				display:inline-block;
				font-weight:bold;
				margin-right:15px;
			}
		</style>
		
	</head>
	<body>
		
		<div class="content">
		
			<h2>Order Options
				<g:link action="edit" name="edit" class="btn btn-default pull-right" id="${specificationInstance.id}">Back to Specification</g:link>
			</h2>
			
            
            
            
			<p class="information secondary">Click, drag and drop option to desired positions.  Click "Update Option Positions" to save changes</p>
			
			<div class="clear" style="margin-top:20px;"></div>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:form action="update_option_positions" method="post">
				
				<input type="hidden" name="positions" id="positions" value="">
				
				<ul id="specificationOptions">
					<g:each in="${specificationOptions}" var="specificationOption">
				  		<li data-id="${specificationOption.id}"><span class="order-number"></span>${specificationOption.name}</li>
					</g:each>
				</ul>
				
				<input type="submit" value="Update Option Positions" class="btn btn-primary" style="float:left; margin-left:30px;"/>
				
			</g:form>
			
			<div class="clear"></div>
			
		</div>


<script type="text/javascript">
$(document).ready(function() {

	var $specificationOptions = $('#specificationOptions'),
		$positionsInput = $('#positions');
	
	var positions = [];
	
	$specificationOptions.sortable({
		placeholder: "position-highlight",
		stop : updatePositions
	});
	
	$specificationOptions.disableSelection();
	
	function updatePositions(){
		positions = [];
		$specificationOptions.find('li').each(function(index, element){
			var $element = $(element);
			if($element.html() != ""){
				var number = index + 1
				$element.find('.order-number').html(number)
				positions.push($element.data("id"));
			}
		})
		$positionsInput.val(positions);
	}
	
	updatePositions();
	
});
</script>

</body>
</html>
