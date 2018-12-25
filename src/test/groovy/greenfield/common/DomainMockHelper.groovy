package org.greenfield.common

import org.greenfield.Account
import org.greenfield.AdditionalPhoto
import org.greenfield.Country
import org.greenfield.Catalog
import org.greenfield.Layout
import org.greenfield.Page
import org.greenfield.Permission
import org.greenfield.Product
import org.greenfield.ProductOption
import org.greenfield.ProductSpecification
import org.greenfield.ShoppingCart
import org.greenfield.ShoppingCartItem
import org.greenfield.ShoppingCartItemOption
import org.greenfield.Specification
import org.greenfield.SpecificationOption
import org.greenfield.State
import org.greenfield.Transaction
import org.greenfield.Upload
import org.greenfield.Variant

class DomainMockHelper {

	public static final Account getMockAccount(){
		def account = new Account(name: 'Robert Fripp', username: "robert", password: "robert", email: "robert@mail.com")
		return account
	}
	
	
	public static final AdditionalPhoto getMockAdditionalPhoto(product){
		def additionalPhoto = new AdditionalPhoto(name: 'Composite Chips Photo 1', imageUrl : '/image.jpg', detailsImageUrl: '/details.jpg', product: product)
		return additionalPhoto
	}

	
	public static final Catalog getMockCatalog(layout){
		def catalog = new Catalog(name: 'Poker Chips', layout: layout, description: "Poker Chips")
		return catalog
	}
	

	public static final Country getMockCountry(){
		def country = new Country(name: "United States")
		return country
	}
	
	
	public static final Layout getMockLayout(){
		def layout = new Layout(name : "Default Layout", content: "[[CONTENT]]", defaultLayout:true)
		return layout
	}
	
	
	public static final Page getMockPage(layout){
        def page = new Page(title: "Lorem ipsum", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit", layout: layout)
		return page
	}
	
	
	public static final Product getMockProduct(catalog, layout){
        def product = new Product(name: 'Composite Poker Chips', price: 123.00, quantity:10, layout: layout)
	    product.addToCatalogs(catalog)
		return product
	}
	
	
	public static final ProductOption getMockProductOption(product){
        def productOption = new ProductOption(name: 'Graphite center', position: 0, product: product)
		return productOption
	}
	

	public static final ProductSpecification getMockProductSpecification(product, specification, specificationOption){
        def productSpecification = new ProductSpecification(product: product, specification: specification, specificationOption: specificationOption)
		return productSpecification
	}


	public static final Specification getMockSpecification(){
        def specification = new Specification(name: "Type", filterName: "type", position: 0)
		return specification
	}
	
	
	public static final Specification getMockSpecificationOption(specification){
        def specificationOption = new SpecificationOption(name: "Composite", position: 0)
		return specificationOption
	}
	
}



