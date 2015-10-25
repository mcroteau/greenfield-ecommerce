package org.greenfield

import org.greenfield.BaseController

@Mixin(BaseController)
class ProductSpecificationController {

    //TODO:remove
    def test(){
        render(ProductSpecification.count())
    }


    //TODO:remove
    def generate(){

        def allOptions = [
            [
                "name" : "Brand",
                "filterName" : "brand",
                "position" : 3,
                "options" : [
                    [ "name" : "Brybelly" ],
                    [ "name" : "Rush Creek" ],
                    [ "name" : "Giantex" ]
                ]
            ],
            [
                "name" : "Size",
                "filterName" : "size",
                "position" : 1,
                "options" : [
                    [ "name" : "Small" ],
                    [ "name" : "Medium" ],
                    [ "name" : "Large" ]
                ]
            ],
            [
                "name" : "Color",
                "filterName" : "color",
                "position" : 2,
                "options" : [
                    [ "name" : "Red" ],
                    [ "name" : "Blue" ],
                    [ "name" : "Yellow" ]
                ]
            ]
        ]

        def product1 = Product.get(6)
        def catalogs = product1.catalogs


        allOptions.each { option ->
            def specification = new Specification()
            specification.name = option.name
            specification.filterName = option.filterName
            specification.position = option.position
            specification.save(flush:true)

            catalogs.each { catalog ->
                specification.addToCatalogs(catalog)
                specification.save(flush:true)
            }

            option.options.each {
                def specficationOption = new SpecificationOption()
                specficationOption.name = it.name
                specficationOption.position = 0
                specficationOption.specification = specification
                specficationOption.save(flush:true)

                specification.addToSpecificationOptions(specficationOption)
                specification.save(flush:true)
            }
            println "new after:" + Specification.count() + " : " + SpecificationOption.count()
        }

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