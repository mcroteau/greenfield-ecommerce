class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"{ 
			controller = "store"
			action = "index"
		}
		
		"500"(view:'/error')

		
		/**"/catalog/$name/products?"(controller:"catalog", action:"catalog_products")**/
		/**"/catalog/$id/products?"(controller:"catalog", action:"products")**/
		"/page/store_view/$title?"(controller:"page", action:"store_view")
	}
}
