package org.greenfield



import grails.test.mixin.*
import org.junit.*
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Variant)
class VariantSpec extends Specification  {

    void "Test name is blank and price is null"() {
        when: 'the name is blank'
        def v = new Variant(name: '', price : null)

        then: 'variant validation should fail'
        !v.validate()
    }
}
