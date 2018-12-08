// Place your Spring DSL code here
import org.greenfield.exception.BaseException

beans = {
	exceptionHandler(org.greenfield.exception.BaseException) {
	    exceptionMappings = ['java.lang.Exception': '/error']
	}
}
