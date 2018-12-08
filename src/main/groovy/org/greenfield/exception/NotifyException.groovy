package org.greenfield.exception

import org.grails.web.errors.GrailsExceptionResolver
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.*
import java.lang.Override
import org.grails.web.servlet.mvc.exceptions.*
import org.codehaus.groovy.runtime.InvokerInvocationException
import org.grails.web.util.WebUtils
import grails.util.Environment
import grails.util.Holders

class NotifyException extends GrailsExceptionResolver {
	
	def applicationService
	def grailsApplication
	
	NotifyException(){
		if(!grailsApplication){
			grailsApplication = Holders.grailsApplication
		}
		if(!applicationService){
		    applicationService = grailsApplication.classLoader.loadClass("org.greenfield.ApplicationService").newInstance()
		}
	}
	//https://searchcode.com/file/94890774/grails-web-mvc/src/main/groovy/org/codehaus/groovy/grails/web/errors/GrailsExceptionResolver.java
    @Override
    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    	try{
			
			println "overriding resolveException"
			def path = request.forwardURI
			println path
			
			def e = findWrappedException(ex)
			Throwable cause = getRootCause(e);
			def message = cause.getMessage()
			println message
			
			
			//def o = "http://104.207.157.132:8080/nod/q/t?uri='" + uri + "'&m=" + message
			//def o = "http://localhost:9462/nod/q/t?uri='" + uri + "'&q=" + message
			def pathEncodedParams = "uri=" + URLEncoder.encode(path, "UTF-8")
			def errorEncodedParams = "&q=" + URLEncoder.encode(message, "UTF-8")
			
			def o = "http://localhost:8080/nod/q/t?" + pathEncodedParams + errorEncodedParams
			//URI uri = new URI(o);
			//URL url = uri.toURL();
			
			//println url
			
			println o
			
			new URL(o).text
			
		}catch(Exception e){
			println "************************************"
			println "***      Monitoring is down      ***"
			println "************************************"
			e.printStackTrace()
		}
			
    	return super.resolveException(request, response, handler, ex)
   	}
	
    protected Exception findWrappedException(Exception e) {
        if ((e instanceof InvokerInvocationException)||(e instanceof GrailsMVCException)) {
            Throwable t = getRootCause(e);
            if (t instanceof Exception) {
                e = (Exception) t;
            }
        }
        return e;
    }
	
}