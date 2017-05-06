package greenfield

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"{ 
            controller = "store"
            action = "index"
        }

        "/shopping_cart"{
            controller = "shoppingCart"
        }

        "500"(view:'/error')
        "404"(view:'/notFound')

        "/page/store_view/$title?"(controller:"page", action:"store_view")        
    }
}
