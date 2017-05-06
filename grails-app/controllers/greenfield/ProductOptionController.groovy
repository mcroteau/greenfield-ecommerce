package greenfield

import org.springframework.dao.DataIntegrityViolationException
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import greenfield.common.BaseController
import java.awt.Graphics2D
import grails.util.Environment
import grails.converters.*

import org.greenfield.Variant

import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class ProductOptionController {


	@Secured(['ROLE_ADMIN'])
	def edit(Long id){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			def variants = Variant.findAllByProductOption(productOptionInstance)
			[ productInstance : productOptionInstance.product, productOptionInstance : productOptionInstance, variants : variants ]
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def update(){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			if(params.name){
				productOptionInstance.name = params.name
				
				if(productOptionInstance.save(flush:true)){
					flash.message = "Successfully updated Name"
					redirect(action:'edit', id: productOptionInstance.id)
				}else{
					flash.message = "Something went wrong, please try again...."
					redirect(action:'edit', id: productOptionInstance.id)
				}
				
			}else{
				flash.message = "Name cannot be blank"
				redirect(action:'edit', id: productOptionInstance.id)
			}
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def add_variant(Long id){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			
			if(!params.name){
				flash.variantMessage = "Variant Name must be specified"
				request.productInstance = productOptionInstance.product
				request.productOptionInstance = productOptionInstance
				request.variants = productOptionInstance.variants
				render(view : 'edit')
				return
			}
			
			if(!params.price || !params.price.isDouble()){
				flash.variantMessage = "Variant Price must be a valid dollar amount"
				request.name = params.name
				request.productInstance = productOptionInstance.product
				request.productOptionInstance = productOptionInstance
				request.variants = productOptionInstance.variants
				render(view : 'edit')
				return
			}
				
			
			def variant = new Variant()
			variant.name = params.name
			variant.price = params.price.toDouble()
			variant.productOption = productOptionInstance
			
			def imageFile = request.getFile('image')
			
			def fullFileName = imageFile.getOriginalFilename()
			
			String[] nameSplit = fullFileName.toString().split("\\.")
			def fileName = nameSplit[0]
			
			fileName = fileName.replaceAll("[^\\w\\s]","")
			fileName = fileName.replaceAll(" ", "_")
			
			
			BufferedImage originalImage = null;
			
			try {
				
				originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
				if(originalImage){
				
		 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					def baseUrl = "images/"
					
					def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
					absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
					def baseDirectory = "${absolutePath}"
					new File(baseDirectory).mkdirs();
					
					def imageUrl = "${baseUrl}${fileName}.jpg"
					def imageLocation = "${baseDirectory}${fileName}.jpg"
					ImageIO.write(originalImage, "jpg", new File(imageLocation));
		   
		   			variant.imageUrl = imageUrl
				}
		
		
				if(variant.save(flush:true)){
					productOptionInstance.addToVariants(variant)
					productOptionInstance.save(flush:true)
					flash.variantMessage = "Successfully added variant"
				}else{
					request.price = params.price
					flash.variantMessage = "Something went wrong, please try again"
				}
					
				redirect(action : 'edit', id : productOptionInstance.id)
		   
		    } catch (IOException e) {
		    	e.printStackTrace();
				flash.message = "Something went wrong..."
	       	 	redirect(controller:'product', action: "list")
		    }		
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def remove_variant(Long id){
		authenticatedAdmin{ account ->
			if(params.id){
				def variant = Variant.get(params.id)
				if(variant){
					def productOption = variant.productOption
					variant.delete(flush:true)
					productOption.removeFromVariants(variant)
					productOption.save(flush:true)
					
					redirect(action : 'edit', id : productOption.id)
					
				}else{
					flash.message = "Unable to find variant"
					redirect(controller : 'product', action: 'list')
				}
			}else{
				flash.message = "Unabel to find variant"
				redirect(controller: 'product', action : 'list')
			}
		}	
	}
	
	
	
	
	@Secured(['ROLE_ADMIN'])
	def edit_variant(Long id){
		authenticatedAdmin{ account ->
			if(params.id){
				def variant = Variant.get(params.id)
				if(variant){				
					[ productOptionInstance : variant.productOption, variant : variant ]
				}else{
					flash.message = "Unable to find variant"
					redirect(controller : 'product', action: 'list')
				}
			}else{
				flash.message = "Unabel to find variant"
				redirect(controller: 'product', action : 'list')
			}
		}	
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def update_variant(){
		authenticatedAdmin{ account ->
			if(params.id){
				def variant = Variant.get(params.id)
				if(variant){			
						
					if(!params.name){
						flash.message = "Variant Name must be specified"
						request.productOptionInstance = variant.productOption
						request.variant = variant
						render(view : 'edit_variant')
						return
					}
			
					if(!params.price || !params.price.isDouble()){
						flash.message = "Variant Price must be a valid dollar amount"
						request.name = params.name
						request.productOptionInstance = variant.productOption
						request.variant = variant
						render(view : 'edit_variant')
						return
					}
					
					def imageFile = request.getFile('image')
					def fullFileName = imageFile.getOriginalFilename()
			
					String[] nameSplit = fullFileName.toString().split("\\.")
					def fileName = nameSplit[0]
			
					fileName = fileName.replaceAll("[^\\w\\s]","")
					fileName = fileName.replaceAll(" ", "_")
			
					BufferedImage originalImage = null;
			
					try {
				
						originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
						if(originalImage){
				
				 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

							def baseUrl = "images/"
					
							def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
							absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
							def baseDirectory = "${absolutePath}"
							new File(baseDirectory).mkdirs();
					
							def imageUrl = "${baseUrl}${fileName}.jpg"
							def imageLocation = "${baseDirectory}${fileName}.jpg"
							ImageIO.write(originalImage, "jpg", new File(imageLocation));
		   
				   			variant.imageUrl = imageUrl
						}
					
					
						variant.name = params.name
						variant.price = params.price.toDouble()
						
						
						if(variant.save(flush:true)){
							flash.message = "Successfully updated variant"
							request.productOptionInstance = variant.productOption
							request.variant = variant
							render(view : 'edit_variant')
							return
						}else{
							flash.message = "Something went wrong, please try again"
							request.productOptionInstance = variant.productOption
							request.variant = variant
							render(view : 'edit_variant')
							return
						}
					
					
					
				    } catch (IOException e) {
				    	e.printStackTrace();
						flash.message = "Something went wrong, please try again"
						request.productOptionInstance = variant.productOptionInstance
						request.variant = variant
						render(view : 'edit_variant')
						return
				    }
					
				
				}else{
					flash.message = "Unable to find variant"
					redirect(controller : 'product', action: 'list')
				}
			}else{
				flash.message = "Unabel to find variant"
				redirect(controller: 'product', action : 'list')
			}
		}
	}
	

	
	@Secured(['ROLE_ADMIN'])
	def edit_variant_positions(Long id){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			def variants = Variant.findAllByProductOption(productOptionInstance)
			[ productOptionInstance : productOptionInstance, variants : variants ]
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def update_variant_positions(Long id){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			if(!params.positions){
				flash.message = "Something went wrong while saving positions ..."
				redirect(action:'edit_variant_positions', id : id)
				return
			}
			
			def positions = params.positions.split(',').collect{it as int}
			
			if(!positions){
				flash.message = "Something went wrong while saving positions ..."
				redirect(action:'edit_variant_positions', id : id)
				return
			}
			
			positions.eachWithIndex(){ variantId, position ->
				def variant = Variant.get(variantId)
				variant.position = position
				variant.save(flush:true)
			}
			
			flash.message = "Successfully updated variant positions"
			redirect(action : 'edit_variant_positions', id : id)
			return
			
		}
	}
	
}