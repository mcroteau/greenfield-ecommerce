<style type="text/css">
	body,html{
		padding:0px;
		margin:0px;
	}
	h3,p{
		font:Arial;
		color:#333;
	}
	#wrapper{
		padding:20px;
	}
	h3{
		padding:0px;
		margin:0px auto 20px auto;
	}
</style>

<div id="wrapper">
	<h3>You do not have access to the resource requested</h3>

	<p><g:link controller="store" action="index">Back to the store</g:link></p>
</div>

<script type="text/javascript">
setInterval(redirect, 5000)
function redirect(){
	window.location.href = '<g:createLink controller="store" action="index"/>';
}
</script>