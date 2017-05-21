package greenfield

import org.greenfield.Product
import org.greenfield.Catalog

import grails.converters.*
import grails.plugin.springsecurity.annotation.Secured


class DevelopmentController {
	/***
		Books
			- Computers & Technology
				- Networking
				- Certification
				- Programming
					- Web Programming
					- Algorithms
					- Apple Programming
		Clothing, Shoes & Jewelry
		Electronics & Computers
		Sports & Outdoors
	**/

	@Secured(['ROLE_ADMIN'])
	def generate_development_data(){
		createTopLevelCatalogs()
		createSubCatalogs()
		createProducts()
		def products = Product.list()
		render products as JSON	
	}

	
	def createTopLevelCatalogs(){
		def books = new Catalog()
		books.name = "Books"
		books.toplevel = true
		books.save(flush:true)
		
		def clothing = new Catalog()
		clothing.name = "Clothing, Shoes & Jewelry"
		clothing.toplevel = true
		clothing.save(flush:true)
		
		def electronics = new Catalog()
		electronics.name = "Electronics & Computers"
		electronics.toplevel = true
		electronics.save(flush:true)
		
		def sports = new Catalog()
		sports.name = "Sports & Outdoors"
		sports.toplevel = true
		sports.save(flush:true)
	}
	

	def createSubCatalogs(){

		try{
		
			def books = Catalog.findByName("Books")

			if(!books){
				println "!Books"
				createTopLevelCatalogs()
				books = Catalog.findByName("Books")
			}
			
			def computers = new Catalog()
			computers.name = "Computers & Technology"
			computers.parentCatalog = books
			computers.save(flush:true)
			
			books.addToSubcatalogs(computers)
			books.save(flush:true)
			
			
			def networking = new Catalog()
			networking.name = "Networking"
			networking.parentCatalog = computers
			networking.save(flush:true)
			computers.addToSubcatalogs(networking)
			computers.save(flush:true)
			
			
			def certification = new Catalog()
			certification.name = "Certification"
			certification.parentCatalog = computers
			certification.save(flush:true)
			computers.addToSubcatalogs(certification)
			computers.save(flush:true)
			
			
			def programming = new Catalog()
			programming.name = "Programming"
			programming.parentCatalog = computers
			programming.save(flush:true)
			computers.addToSubcatalogs(programming)
			computers.save(flush:true)
			
			def web = new Catalog()
			web.name = "Web Programming"
			web.parentCatalog = programming
			web.save(flush:true)
			
			programming.addToSubcatalogs(web)
			programming.save(flush:true)
			
			
			def algorithms = new Catalog()
			algorithms.name = "Algorithms"
			algorithms.parentCatalog = programming
			algorithms.save(flush:true)
			
			programming.addToSubcatalogs(algorithms)
			programming.save(flush:true)
			
			
			def apple = new Catalog()
			apple.name = "Apple Programming"
			apple.parentCatalog = programming
			apple.save(flush:true)
			
			programming.addToSubcatalogs(apple)
			programming.save(flush:true)
		
		
		}catch (Exception e){
			println "Error"
			e.printStackTrace()
		}
		
		
	}


	def createProducts(){
		def books = Catalog.findByName("Books")
		def computers = Catalog.findByName("Computers & Technology")
		def programming = Catalog.findByName("Programming")
		def certification = Catalog.findByName("Certification")
		def web = Catalog.findByName("Web Programming")
		def algorithms = Catalog.findByName("Algorithms")
		def apple = Catalog.findByName("Apple Programming")
		def clothing = Catalog.findByName("Clothing, Shoes & Jewelry")
		def electronics = Catalog.findByName("Electronics & Computers")
		def sports = Catalog.findByName("Sports & Outdoors")

		
		//web programming books
		(1..30).each{ i ->
			def webbook = new Product()
			webbook.name = "Web Programming ${i}"
			webbook.price = i * 10
			webbook.quantity = i * 3
			webbook.weight = 16
			webbook.save(flush:true)
			
			webbook.addToCatalogs(books)
			webbook.addToCatalogs(computers)
			webbook.addToCatalogs(programming)
			webbook.addToCatalogs(web)
			webbook.save(flush:true)
		}
		
		
		(1..45).each{ i ->	
			def algobook = new Product()
			algobook.name = "Algorithms ${i}"
			algobook.price = i * 10
			algobook.quantity = i * 3
			algobook.weight = 16
			algobook.save(flush:true)
			
			algobook.addToCatalogs(books)
			algobook.addToCatalogs(computers)
			algobook.addToCatalogs(programming)
			algobook.addToCatalogs(algorithms)
			algobook.save(flush:true)
		}
		
		
		(1..30).each{ i ->	
			def applebook = new Product()
			applebook.name = "Apple Programming ${i}"
			applebook.price = i * 10
			applebook.quantity = i * 3
			applebook.weight = 16
			applebook.save(flush:true)
			
			applebook.addToCatalogs(books)
			applebook.addToCatalogs(computers)
			applebook.addToCatalogs(programming)
			applebook.addToCatalogs(apple)
			applebook.save(flush:true)
		}
		
		
		(1..30).each{ i ->	
			def certbook = new Product()
			certbook.name = "Certification ${i}"
			certbook.price = i * 10
			certbook.quantity = i * 3
			certbook.weight = 16
			certbook.save(flush:true)
			
			certbook.addToCatalogs(books)
			certbook.addToCatalogs(computers)
			certbook.addToCatalogs(certification)
			certbook.save(flush:true)
		}

		
		(1..34).each{ i ->	
			def clothingProduct = new Product()
			clothingProduct.name = "Clothing, Shoes & Jewelry ${i}"
			clothingProduct.price = i * 10
			clothingProduct.quantity = i * 3
			clothingProduct.weight = 16
			clothingProduct.save(flush:true)
			
			clothingProduct.addToCatalogs(clothing)
			clothingProduct.save(flush:true)
		}
		(1..13).each{ i ->	
			def electronicProduct = new Product()
			electronicProduct.name = "Electronics & Computers ${i}"
			electronicProduct.price = i * 10
			electronicProduct.quantity = i * 3
			electronicProduct.weight = 16
			electronicProduct.save(flush:true)
			
			electronicProduct.addToCatalogs(electronics)
			electronicProduct.save(flush:true)
		}
		(1..24).each{ i ->	
			def sportsProduct = new Product()
			sportsProduct.name = "Sports & Outdoors ${i}"
			sportsProduct.price = i * 10
			sportsProduct.quantity = i * 3
			sportsProduct.weight = 16
			sportsProduct.save(flush:true)
			
			sportsProduct.addToCatalogs(sports)
			sportsProduct.save(flush:true)
		}
	}
	
}