package greenfield

import greenfield.common.BaseController

import org.greenfield.ProductSpecification
import grails.plugin.springsecurity.annotation.Secured
import org.greenfield.Specification


@Mixin(BaseController)
class ProductSpecificationController {


    @Secured(['ROLE_ADMIN'])
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

            def removable = []
            if(productInstance.productSpecifications){
                productInstance.productSpecifications.each { productSpecification ->
                    if (productSpecification.specificationOption.id == specificationOption.id) {
                        removable.add(productSpecification)
                    }
                }
            }
            
            if(removable.size() > 0){
                removable.each { removableProductSpecification ->
                    productInstance.removeFromProductSpecifications(removableProductSpecification)
                    removableProductSpecification.delete(flush:true)
                }
            }


            flash.message = "Successfully removed specification from product"
            redirect(action: 'manage', id: productInstance.id)
        }
    }


}