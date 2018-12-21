// Place your Spring DSL code here
import org.greenfield.exception.BaseException
import org.greenfield.handlers.GreenfieldAuthenticationSuccessHandler

beans = {
	exceptionHandler(org.greenfield.exception.BaseException) {
	    exceptionMappings = ['java.lang.Exception': '/error']
	}
	authenticationSuccessHandler(GreenfieldAuthenticationSuccessHandler) {
		//https://groggyman.com/2015/04/05/custom-authentication-success-handler-with-grails-and-spring-security/
        requestCache = ref('requestCache')
        redirectStrategy = ref('redirectStrategy')
	}
}
