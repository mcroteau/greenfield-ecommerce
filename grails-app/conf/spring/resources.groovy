// Place your Spring DSL code here
import org.greenfield.exception.NotifyException

beans = {
	exceptionHandler(NotifyException) {
	    exceptionMappings = ['java.lang.Exception': '/error']
	}
}
