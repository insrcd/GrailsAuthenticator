
/**
 *
 * @author ashaw
 */
class AuthenticatorFilters {
    
    def grailsApplication
    
    def filters = {
        adminCheck(controller: '*', action: '*') {
            before = {          
                if (grailsApplication.config.authenticator.getUser()){
                    def authenticatorSessionVarName = grailsApplication.config.authenticator.sessionVariableName ?: "authenticator"
               
                    def expireDays = grailsApplication.config.authenticator.expireDays ?: 30
                    def excludeControllers = grailsApplication.config.authenticator.excludeControllers ?: []
                
                    def authenticator 
                    if ( grailsApplication.config.authenticator.useSession){
                        authenticator = com.foresite.authentication.Authenticator.get(session[authenticatorSessionVarName])              
                    } else {
                        authenticator = com.foresite.authentication.Authenticator.findByUsername(grailsApplication.config.authenticator.getUser())
                    }    
                    // make sure the last authentiation was within the threshhold
                    def authenticated = authenticator ? authenticator.lastAuthentication?.after(new Date()-expireDays) : false
                
                    if (controllerName != 'authenticator' 
                        && controllerName != 'assets' 
                        && controllerName != 'login' 
                        && !excludeControllers.contains(controllerName)
                        && !authenticated
                        && grailsApplication.config.authenticator.enabled){
                    
                   
                        redirect(uri:"/authenticator/authenticate")
                        
                        return false
                    
                    }
                }
                
                return true
            }
        }
    }
}

