package greenfield

import org.springframework.dao.DataIntegrityViolationException
import greenfield.common.BaseController

@Mixin(BaseController)
class ShoppingCartItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
		authenticatedAdmin{ adminAccount ->
        	redirect(action: "list", params: params)
		}
    }

    def list(Integer max) {
		authenticatedAdmin{ adminAccount ->
       	 	params.max = Math.min(max ?: 10, 100)
        	[shoppingCartItemInstanceList: ShoppingCartItem.list(params), shoppingCartItemInstanceTotal: ShoppingCartItem.count()]
		}
    }

}
