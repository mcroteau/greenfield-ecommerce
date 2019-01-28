package greenfield

import grails.plugin.springsecurity.annotation.Secured

class NewsletterController {

    @Secured(['permitAll'])
    def signup(){
    	
    }

}