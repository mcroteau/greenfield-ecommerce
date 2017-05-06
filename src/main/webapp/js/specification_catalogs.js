$(document).ready(function(){

	var $modal = $('#catalog-selection-modal'),
		$backdrop = $('#catalog-selection-backdrop');

	var $catalogsIdsInput = $('#catalogIds'),
		$catalogSelectionModalBtn = $('#catalog-selection-modal-link'),
		$catalogSelectionDiv = $('#catalog-selection-container'),
		$selectedCatalogsSpan = $('#selected-catalogs-span'),
		$closeCatalogSelectionModalBtn = $('#close-catalogs-select-modal')
		$catalogCheckboxes = $catalogSelectionDiv.find('.catalog_checkbox');
	

	var NO_CATALOGS_SELECTED = "<span class=\"information secondary\" style=\"display:block\">No Catalogs selected.  Please select a Catalog to continue</span>"	
	
	var ADD_CATALOGS_BUTTON = "<a href=\"javascript:\" id=\"add-catalogs-btn\" class=\"btn btn-default\">Add Catalogs</a>" 	
	
	
	if(catalogIds.length == 0){
		$selectedCatalogsSpan.append(ADD_CATALOGS_BUTTON)
		$selectedCatalogsSpan.append(NO_CATALOGS_SELECTED)
		$("#add-catalogs-btn").click(showModal)
	}


	$catalogsIdsInput.val(catalogIdsString);
	$catalogCheckboxes.click(selectCheckbox);
	$catalogSelectionModalBtn.click(showModal);
	$closeCatalogSelectionModalBtn.click(hideModal);
	$backdrop.click(hideModal);



	function selectCheckbox(event){
		clearSetSelectedCatalogs()
	}



	function clearSetSelectedCatalogs(){
		var idsArray = []
		$catalogsIdsInput.val("");
		$selectedCatalogsSpan.empty();
		$catalogCheckboxes.each(function(index, checkbox){
			var $checkbox = $(checkbox)
			if($checkbox.is(':checked')){
				idsArray.push($checkbox.data('id'))
				var spanHtml = "<span class=\"label label-default\">" + $checkbox.data('name') + "</span>";
				$selectedCatalogsSpan.append(spanHtml);
			}
		})
		$catalogsIdsInput.val(idsArray.join())
	
		if(idsArray.length == 0){
			$selectedCatalogsSpan.append(ADD_CATALOGS_BUTTON)
			$selectedCatalogsSpan.append(NO_CATALOGS_SELECTED)
			$("#add-catalogs-btn").click(showModal)
		}
	}


	function showModal(){
		$backdrop.animate({
			"opacity" : 1,
			"z-index" : 1000
		}, 100, function(){
			$modal.animate({
				"opacity" : 1,
				"z-index" : 1010
			}, 50, function(){
			});
		});	
	};
		

	function hideModal(){
		$modal.animate({
			"opacity" : 0,
			"z-index" : -1
		}, 0, function(){
			$backdrop.animate({
				"opacity" : 0,
				"z-index" : -2
			}, 0, function(){
			});	
		}); 
	};


})