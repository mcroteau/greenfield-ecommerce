package org.greenfield

import grails.converters.*

class DevelopmentController {

	def createTestCatalogs(){
		/**
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
		
		try{
		
			def books = Catalog.findByName("Books")
			println "*********************************"
			if(!books){
				println "!Books"
				createTopLevelCatalogs()
				books = Catalog.findByName("Books")
			}
			println "*********************************"
			
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
		
		
		def catalogs = Catalog.list()
		render catalogs as JSON
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
	
	
}