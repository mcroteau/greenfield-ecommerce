grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

grails.project.war.file = "target/${appName}.war"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

//grails.war.exploded=true
//grails.project.war.exploded.dir = "target/${appName}"


// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]


grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        //runtime 'mysql:mysql-connector-java:5.1.22'
		
		//compile "cglib:cglib:3.1" //hibernate3 dependency
		
        //compile "postgresql:postgresql:9.1-901.jdbc4"
		compile "com.stripe:stripe-java:1.23.0"
		compile "javax.mail:mail:1.4"
		compile "com.easypost:easypost-java:2.0.9"
		compile "com.google.code.gson:gson:2.3.1"
    }

    plugins {
	
        build ":tomcat:7.0.55"
		
    	compile ":scaffolding:2.1.2"
    	compile ':cache:1.1.8'
    	compile ":asset-pipeline:1.9.9"
	
    	runtime ":hibernate4:4.3.6.1"
		//compile ":postgresql-extensions:4.4.0"
		
		//runtime ":hibernate:3.6.10.18"
    	
		runtime ":database-migration:1.4.0"
		
		compile ":shiro:1.2.1"
	
    }
}
