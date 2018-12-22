
var baseUrl = ""
var $stateSelectRow = $('#stateSelectRow');
var $stateSelect = $('#stateSelect');
var $countrySelect = $('#countrySelect');

function countryStatesInit(contextName, state){
	$stateSelect.find("option").remove()
	baseUrl = "/" + contextName + "/data/states?"
	getStates($countrySelect.val()).then(renderStates).then(setState(state));
	$countrySelect.change(getStatesAction)
}

function setState(state){
	return function(){
		//console.log(${shipping_settings?.state})
		if(state != 1000){
			$stateSelect.val(state)
		}
	}
}

function getStatesAction(event){
	//console.log(event)
	var country = $countrySelect.val()
	getStates(country).then(renderStates);
}

function getStates(country){
	var countryParam = "country=" + country;
	var url = baseUrl + countryParam
	return $.ajax({
		url : url,
		type : 'get',
		dataType : 'json',
		contentType : 'application/json'
	})
}
	

function renderStates(data, response){
	$stateSelect.find("option").remove()
	//console.log($stateSelect)
	//console.log(data)
	$(data).each(function(index){
		//console.log(index)
		//console.log(this)
		$stateSelect.append("<option value=\"" + this.id + "\">" + this.name + "</option>");
	})
	if($stateSelect.val() == null || $stateSelect.val() == "")$stateSelectRow.hide()
}