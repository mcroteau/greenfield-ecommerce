package org.greenfield.handlers

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.core.Authentication
 
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

//TODO: thanks 
//https://groggyman.com/2015/04/05/custom-authentication-success-handler-with-grails-and-spring-security/
class GreenfieldAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	def requestCache
	boolean administrator = false

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
//		println "is admininistrator : " + administrator		
//		def targetUrl = super.determineTargetUrl(request, response)
//		println "target url -> " + targetUrl
		
        if(administrator){
            return "/admin"
		} else {
            return super.determineTargetUrl(request, response)
        }
    }
	
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws ServletException, IOException {
        try {
			checkSetAdministratorUser(authentication)
            handle(request, response, authentication)
            super.clearAuthenticationAttributes(request)
		}catch(Exception e){
			e.printStackTrace()
		} finally {
            // always remove the saved request
            //requestCache.removeRequest(request, response)
        }
		
    }
	
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response)
		
		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl)
            return
        }
 
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
	
	def checkSetAdministratorUser(authentication){
		authentication.authorities.each(){ authority ->
			//println "'" + authority.authority + "'"
			if(authority.authority == "ROLE_ADMIN")administrator = true
		}
	}
}