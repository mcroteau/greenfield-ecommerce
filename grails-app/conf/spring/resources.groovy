// Place your Spring DSL code here
import org.greenfield.exception.BaseException
import org.greenfield.handlers.GreenfieldAuthenticationSuccessHandler

beans = {
	exceptionHandler(org.greenfield.exception.BaseException) {
	    exceptionMappings = ['java.lang.Exception': '/error']
	}
	authenticationSuccessHandler(GreenfieldAuthenticationSuccessHandler) {
        //def conf = SpringSecurityUtils.securityConfig
        requestCache = ref('requestCache')
        redirectStrategy = ref('redirectStrategy')
        //defaultTargetUrl = conf.successHandler.defaultTargetUrl
        //alwaysUseDefaultTargetUrl = conf.successHandler.alwaysUseDefault
        //targetUrlParameter = conf.successHandler.targetUrlParameter
        //useReferer = conf.successHandler.useReferer
	}
}
