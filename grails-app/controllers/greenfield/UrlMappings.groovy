package greenfield

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"{ 
            controller = "page"
            action = "home"
        }

        "/shopping_cart"{
            controller = "shoppingCart"
        }

		"/store/index"{
			controller = "page"
			action = "home"
		}

        "500"(view:'/error')
        "404"(view:'/notFound')

        "/page/store_view/$title?"(controller:"page", action:"store_view")        
    }
}
