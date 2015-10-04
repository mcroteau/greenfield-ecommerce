package org.greenfield

import org.greenfield.BaseController

@Mixin(BaseController)
class ProductSpecificationController {

    //TODO:remove
    def add_products(){

        def c = Product.createCriteria()
        def products = c.list{
            catalogs{
                idEq(3.toLong())
            }
        }

        def rand = new Random()

        products.each{ product ->

            def brandSpecification = Specification.get(1)
            def sizeSpecification = Specification.get(2)
            def colorSpecification = Specification.get(3)

            def brands = SpecificationOption.findAllBySpecification(brandSpecification)
            def sizes = SpecificationOption.findAllBySpecification(sizeSpecification)
            def colors = SpecificationOption.findAllBySpecification(colorSpecification)

            def brandIndex = Math.abs(rand.nextInt() % brands.size()) + 0
            def brand = brands.getAt(brandIndex)

            def sizeIndex = Math.abs(rand.nextInt() % sizes.size()) + 0
            def size = sizes.getAt(sizeIndex)

            def colorIndex = Math.abs(rand.nextInt() % colors.size()) + 0
            def color = colors.getAt(colorIndex)


            def brandOption = new ProductSpecification()
            brandOption.specificationOption = brand
            brandOption.specification = brandSpecification
            brandOption.product = product
            brandOption.save(flush:true)

            def sizeOption = new ProductSpecification()
            sizeOption.specificationOption = size
            sizeOption.specification = sizeSpecification
            sizeOption.product = product
            sizeOption.save(flush:true)

            def colorOption = new ProductSpecification()
            colorOption.specificationOption = color
            colorOption.specification = colorSpecification
            colorOption.product = product
            colorOption.save(flush:true)

            product.addToProductSpecifications(brandOption)
            product.addToProductSpecifications(sizeOption)
            product.addToProductSpecifications(colorOption)
            product.save(flush:true)

        }


        render(ProductSpecification.list())

    }


    //TODO:remove
    def generate(){

        def allOptions = [
            [
                "name" : "Brand",
                "searchName" : "brand",
                "options" : [
                    [ "name" : "Brybelly" ],
                    [ "name" : "Rush Creek" ],
                    [ "name" : "Giantex" ]
                ]
            ],
            [
                "name" : "Size",
                "searchName" : "size",
                "options" : [
                    [ "name" : "Small" ],
                    [ "name" : "Medium" ],
                    [ "name" : "Large" ]
                ]
            ],
            [
                "name" : "Color",
                "searchName" : "color",
                "options" : [
                    [ "name" : "Red" ],
                    [ "name" : "Blue" ],
                    [ "name" : "Yellow" ]
                ]
            ]
        ]


        def product1 = Product.get(18)
        def catalogs = product1.catalogs

        allOptions.each { option ->
            def specification = new Specification()
            specification.name = option.name
            specification.searchName = option.searchName
            specification.save(flush:true)

            println "new before:" + Specification.count()
            catalogs.each { catalog ->
                specification.addToCatalogs(catalog)
                specification.save(flush:true)
            }

            println "new after:" + Specification.count()
            option.options.each {
                def specficationOption = new SpecificationOption()
                specficationOption.name = it.name
                specficationOption.specification = specification
                specficationOption.save(flush:true)

                specification.addToSpecificationOptions(specficationOption)
                specification.save(flush:true)
            }
        }

        //TODO:remove belongsTo relationship to Catalog in Specification

    }

    //TODO:remove
    def tmp_delete(){
        println "before : " + Specification.count()
        def specifications = Specification.list()
        specifications.each {
            it.delete(flush:true)
        }
        println "done deleting : " + Specification.count()
    }


    //TODO:remove
    def delete_all(){
        ProductSpecification.executeUpdate('delete from ProductSpecification')
        def specificationOptions = SpecificationOption.list()
        specificationOptions.each { specificationOption ->
            specificationOption.products = null
            specificationOption.products.clear()
            specificationOption.save(flush:true)
        }
    }


    //TODO:remove
    def count(){
        render(ProductSpecification.count())
    }


    def manage(Long id){
        authenticatedAdminProduct { adminAccount, productInstance ->
            def availableSpecifications = []

            productInstance.catalogs.each { catalog ->
                def c = Specification.createCriteria()
                def results = c.list() {
                    catalogs {
                        idEq(catalog.id)
                    }
                }

                results.each(){ specification ->
                    availableSpecifications.push(specification)
                }
            }

            availableSpecifications.unique { a, b ->
                a.id <=> b.id
            }

            [ productInstance: productInstance, availableSpecifications: availableSpecifications ]
        }
    }


    def add(Long id){
        authenticatedAdminProduct { adminAccount, productInstance ->
            def specificationOption = SpecificationOption.get(params.optionId)

            if(!specificationOption){
                flash.message = "Something went wrong while adding the specification to ${productInstance.name}"
                redirect(action: 'manage', id: productInstance.id)
                return
            }

            def specification = specificationOption.specification

            def productSpecificationsRemove = []
            if(productInstance.productSpecifications){
                productInstance.productSpecifications.each { productSpecification ->
                    if (productSpecification.specificationOption.specification.id == specification.id) {
                        productSpecificationsRemove.push(productSpecification)
                    }
                }
            }

            if(productSpecificationsRemove){
                productSpecificationsRemove.each { productSpecification ->
                    productInstance.removeFromProductSpecifications(productSpecification)
                    productSpecification.delete(flush:true)
                }
            }


            def productSpecification = new ProductSpecification()
            productSpecification.specificationOption = specificationOption
            productSpecification.specification = specification
            productSpecification.product = productInstance
            productSpecification.save(flush:true)

            productInstance.addToProductSpecifications(productSpecification)
            productInstance.save(flush:true)

            specificationOption.addToProducts(productInstance)
            specificationOption.save(flush:true)

            println "**********************************"
            println "add products : " + specificationOption.products.size()
            println "**********************************"

            flash.message = "Successfully added specification to product"
            redirect(action: 'manage', id: productInstance.id)

        }
    }



    def remove(Long id){
        authenticatedAdminProduct { adminAccount, productInstance ->
            def specificationOption = SpecificationOption.get(params.optionId)

            if(!specificationOption){
                flash.message = "Something went wrong while adding the specification to ${productInstance.name}"
                redirect(action: 'manage', id: productInstance.id)
                return
            }


            if(productInstance.productSpecifications){
                productInstance.productSpecifications.each { productSpecification ->
                    if (productSpecification.specificationOption.id == specificationOption.id) {
                        productInstance.removeFromProductSpecifications(productSpecification)
                        productSpecification.delete(flush:true)

                        specificationOption.removeFromProducts(productInstance)
                        specificationOption.save(flush:true)
                    }
                }
            }


            println "**********************************"
            println "remove products : " + specificationOption.products.size()
            println "**********************************"


            flash.message = "Successfully removed specification from product"
            redirect(action: 'manage', id: productInstance.id)
        }
    }


}