package org.greenfield

import org.springframework.dao.DataIntegrityViolationException
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import org.greenfield.BaseController
import java.awt.Graphics2D
import grails.util.Environment
import grails.converters.*


import org.apache.shiro.SecurityUtils
import org.greenfield.log.ProductViewLog
import org.greenfield.log.SearchLog

@Mixin(BaseController)
class ProductController {

    static allowedMethods = [ save: "POST", update: "POST", delete: "POST", save_option: 'POST' ]
	
	def grailsApplication

	
	def index() {
    	redirect(controller: 'store', action: "index")
    }
	
	def search(){		
		if(params.query && params.query.length() >= 4){
			def p1 = Product.findAll("from Product as p where UPPER(p.name) like UPPER('%${params.query}%') AND p.disabled = false AND p.quantity > 0")
			def p2 = Product.findAll("from Product as p where UPPER(p.description) like UPPER('%${params.query}%') AND p.disabled = false AND p.quantity > 0")
			
			p1.collect( { productA ->
			    def productB = p2.find { it.name == productA.name }
				p2.remove(productB)
			})

			def products = p1 + p2
			
			
			def searchLog = new SearchLog()
			searchLog.query = params.query.toLowerCase()
			def subject = SecurityUtils.getSubject();
			if(subject.isAuthenticated()){
				def account = Account.findByUsername(subject.principal)
				if(account){
					searchLog.account = account
				}
			}
			searchLog.save(flush:true)
			
			
			[products : products]
		}else{
			flash.message = "Search query must be at least 4 characters"
		}
	}
	
	
	
	

	def details(Long id){        
		def productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = "Product not found..."
            redirect(controller: 'store', action: "index")
            return
        }
		
		def productOptions = []
		
		productInstance.productOptions.each(){ productOption ->
			
			def option = [:]
			option.id = productOption.id
			option.name = productOption.name
			
			def variants = Variant.findAllByProductOption(productOption)
			option.variants = variants
			
			productOptions.add(option)
			
		}
		
		
		def productViewLog = new ProductViewLog()
		productViewLog.product = productInstance
		productViewLog.save(flush:true)
		
        [ productInstance: productInstance, productOptions : productOptions ]
	}
	
	
	
    def list(Integer max) {
		authenticatedAdmin { adminAccount ->
			params.max = Math.min(max ?: 10, 100)
    		[productInstanceList: Product.list(params), productInstanceTotal: Product.count()]
		}
    }



    def outofstock(Integer max) {
		authenticatedAdmin { adminAccount ->
			params.max = Math.min(max ?: 10, 100)
    		request.productInstanceList = Product.findAllByQuantity(0)
			request.productInstanceTotal = Product.countByQuantity(0)
			render(view : 'list')
		}
    }
	

	def admin_search(){
		authenticatedAdmin { adminAccount ->

			if(params.query && params.query.length() >= 3){
				def p1 = Product.findAll("from Product as p where UPPER(p.name) like UPPER('%${params.query}%') AND p.disabled = false")
				def p2 = Product.findAll("from Product as p where UPPER(p.description) like UPPER('%${params.query}%') AND p.disabled = false")
				
				
				p1.collect( { productA ->
				    def productB = p2.find { it.name == productA.name }
					p2.remove(productB)
				})
        	
				def productInstanceList = p1 + p2
				request.productInstanceList = productInstanceList
				request.productInstanceTotal = productInstanceList.size()
				render(view:'list')
			}else{
				flash.message = "Search term must be at least 3 characters long"
				redirect(action:'list')
			}
		}
	}
	
	
    def create() {
		authenticatedAdmin { adminAccount ->
			[productInstance: new Product(params)]
    	}
	}	
	
	
	def resizeImage(originalImage, type, height, width){
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
 
		return resizedImage;
	}


    def save() {
		authenticatedAdmin { adminAccount ->
		    
			def productInstance = new Product(params)
			
	    	if(!productInstance.validate()){
				println "**************************"
				println "***      INVALID       ***"
				println "**************************"
				
				flash.message = "Information is invalid, please make sure name is unique"
				render(view:"create",  model: [productInstance: productInstance])
				return
			}	
				
			def imageFile = request.getFile('image')
			
			BufferedImage originalImage = null;
			def fullFileName = imageFile.getOriginalFilename()
			
			String[] nameSplit = fullFileName.toString().split("\\.")
			def fileName = nameSplit[0]
			
			fileName = fileName.replaceAll("[^\\w\\s]","")
			fileName = fileName.replaceAll(" ", "_")
			
			def productName = productInstance.name
			
			productName = productName.replaceAll("[^\\w\\s]",""); 
			productName = productName.replaceAll(" ", "_")
		    
			try {
	
				
				originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
				if(originalImage){
				
		 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
 
					
					//def baseUrl = "images/products/${productName}/"
					def baseUrl = "images/"
					
					def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
					absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
					//def baseDirectory = "${absolutePath}products/${productName}/"
					def baseDirectory = "${absolutePath}"
					
					
					new File(baseDirectory).mkdirs();
					
					productInstance.detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
 					BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
				
					def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
					ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
					
					
					def imageUrl = "${baseUrl}${fileName}.jpg"
					productInstance.imageUrl = imageUrl
			
					def imageLocation = "${baseDirectory}${fileName}.jpg"
					ImageIO.write(originalImage, "jpg",new File(imageLocation));
				
				}else{
					flash.message = "please provide product image"
		       	 	render(view: "create", model: [productInstance: productInstance])
				}
				
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
			
		    if (!productInstance.save(flush: true)) {
		        render(view: "create", model: [productInstance: productInstance])
		        return
		    }
		
		    flash.message = "Successfully created product"
		    redirect(action: "show", id: productInstance.id)		
		}	
    }
	


    def show(Long id) {
		authenticatedAdminProduct { adminAccount, productInstance ->
    	    [productInstance: productInstance]
		}
    }
	
	



    def edit(Long id) {
		authenticatedAdminProduct { adminAccount, productInstance ->
    	    [productInstance: productInstance]
		}
    }
	

	

    def update(Long id, Long version) {
		authenticatedAdminProduct { adminAccount, productInstance ->
		
			def imageFile = request.getFile('image')
			BufferedImage originalImage = null;
			def fullFileName = imageFile.getOriginalFilename()
			
			String[] nameSplit = fullFileName.toString().split("\\.")
			def fileName = nameSplit[0]
			
			fileName = fileName.replaceAll("[^\\w\\s]","")
			fileName = fileName.replaceAll(" ", "_")
			
			def productName = productInstance.name
			
			productName = productName.replaceAll("[^\\w\\s]",""); 
			productName = productName.replaceAll(" ", "_")
			
			
			try {
	
				originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
				if(originalImage){
				
		 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					//def baseUrl = "images/products/${productName}/"
					def baseUrl = "images/"
					
					def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
					absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
					//def baseDirectory = "${absolutePath}products/${productName}/"
					def baseDirectory = "${absolutePath}"
					
					
					new File(baseDirectory).mkdirs();
					
					productInstance.detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
 					BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
				
					def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
					ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
					
					
					def imageUrl = "${baseUrl}${fileName}.jpg"
					productInstance.imageUrl = imageUrl
			
					def imageLocation = "${baseDirectory}${fileName}.jpg"
					ImageIO.write(originalImage, "jpg",new File(imageLocation));
				
				}else{
					flash.message = "please provide product image"
		       	 	render(view: "create", model: [productInstance: productInstance])
				}
				
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
			
			productInstance.properties = params
			
    	    if (!productInstance.save(flush: true)) {
				flash.message = "Something went wrong while trying to update. Please try again."
    	        render(view: "edit", model: [productInstance: productInstance])
    	        return
    	    }
    	
    	    flash.message = "Successfully updated product"
    	    redirect(action: "show", id: productInstance.id)
    	}
	}





    def delete(Long id) {
		authenticatedAdminProduct { adminAccount, productInstance ->
       		try {
       		    productInstance.delete(flush: true)
       		    flash.message = "Successfull deleted product"
       		    redirect(action: "list")
       		}catch (DataIntegrityViolationException e) {
       		    flash.message = "Something went wrong when trying to delete.  The item you are trying to delete might exist in a transaction.  Please try again or check Orders for the specific product"
       		    redirect(action: "show", id: id)
        	}
		}
    }
	
	
	
	
	
	
	
	def remove_additional_photo(Long id){
		authenticatedAdmin{ adminAccount ->
			def additionalPhoto = AdditionalPhoto.get(id)
			
			if(additionalPhoto){
				def productInstance = additionalPhoto.product
				additionalPhoto.delete(flush:true)
				flash.message = "Successfully removed photo from product"
				redirect(action:"additional_photos", id: productInstance.id)
			}
		}
	}
	
	
	def add_additional_photo(){
		authenticatedAdminProduct { adminAccount, productInstance ->
		
			def imageFile = request.getFile('image')
			
			def fullFileName = imageFile.getOriginalFilename()
			
			String[] nameSplit = fullFileName.toString().split("\\.")
			def fileName = nameSplit[0]
			
			fileName = fileName.replaceAll("[^\\w\\s]","")
			fileName = fileName.replaceAll(" ", "_")
			
			BufferedImage originalImage = null;
			
			def productName =  productInstance.name
			productName = productName.replaceAll("[^\\w\\s]",""); 
			productName = productName.replaceAll(" ", "_")
			
			
			try {
				
				originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
				if(originalImage){
				
		 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					//def baseUrl = "images/products/${productName}/"
					def baseUrl = "images/"
					
					def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
					absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
					//def baseDirectory = "${absolutePath}products/${productName}/"
					def baseDirectory = "${absolutePath}"
					
					
					new File(baseDirectory).mkdirs();
					
					def detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
					BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
				
					def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
					ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
					
					
					def imageUrl = "${baseUrl}${fileName}.jpg"
					def imageLocation = "${baseDirectory}${fileName}.jpg"
					ImageIO.write(originalImage, "jpg", new File(imageLocation));

	    			def additionalPhoto = new AdditionalPhoto();
					additionalPhoto.name = fileName
					additionalPhoto.imageUrl = imageUrl
					additionalPhoto.detailsImageUrl = detailsImageUrl
					additionalPhoto.product = productInstance
					additionalPhoto.save(flush:true)
					
					flash.message = "Successfully added photo"
	       	 		redirect(action: "additional_photos", id: productInstance.id)
					
				}else{
					flash.message = "please provide product image & image name"
	       	 		redirect(action: "additional_photos", id: productInstance.id)
				}
				
		    } catch (IOException e) {
		    	e.printStackTrace();
				flash.message = "Something went wrong, please try again"
	       	 	redirect(action: "additional_photos", id: productInstance.id)
		    }
		}
	}
	
	
	def additional_photos(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			[productInstance : productInstance]
		}	
	}
	
	
	def product_options(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			[productInstance : productInstance]
		}
	}
	
	
	def add_product_option(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			[productInstance : productInstance]
		}
	}
	
	
	def save_option(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			if(params.name){
				def productOption = new ProductOption()
				productOption.product = productInstance
				productOption.name = params.name
				productOption.save(flush:true)
				
				productInstance.addToProductOptions(productOption)
				productInstance.save(flush:true)

				flash.message = "Successfully created product option.  Now add variants"
				redirect(controller : 'productOption', action: 'edit', id : productOption.id )
				
			}else{
				flash.message = "Name cannot be blank, please specify product option name"
				request.productInstance = productInstance
				render(view : 'add_product_option')
			}
		}
	}
	
	
	
}
