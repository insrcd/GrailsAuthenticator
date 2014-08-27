package com.foresite.authentication

import grails.converters.*
import java.util.concurrent.*

class AuthenticatorController {

    def authenticatorService
    def grailsApplication
    
    def index(){
        []
    }
    
    def check(){
        def key = session['authenticator.key']
        
        def timeUnits = new Date().getTime() / TimeUnit.SECONDS.toMillis(30) as Long
        
        return render([result:authenticatorService.checkCode(key, params.code as Long, timeUnits)] as JSON)
    }
    
    def authenticate(){
        
        def authenticatorSessionVarName = grailsApplication.config.authenticator.sessionVariableName ?: "authenticator"
        
        def authenticator = Authenticator.findByUser(grailsApplication.config.authenticator.getUser())
             
        if (!authenticator){
            return redirect(uri:"/authenticator/register")
        }
        
        def key = authenticator.secretKey
        
        if (!params.code){
            return render(view:"authenticate", model:[message:"Authenticate with this service.",authenticator:authenticator])
        }
        
        def timeUnits = new Date().getTime() / TimeUnit.SECONDS.toMillis(30) as Long
                  
        if (authenticatorService.checkCode(key, params.code as Long, timeUnits)){
            flash.message = "Authentication successful"
          
            authenticator.lastAuthentication = new Date()
                    
            authenticator.save(flush:true)
            
            session[authenticatorSessionVarName] = authenticator.id
            
            return redirect(uri:"/")
        } else {
            authenticator.failedAuthentications += 1
            authenticator.save()
            
            return [error:"Incorrect code."]
        }
    }
    
    def register(){
        def authenticatorSessionVarName = grailsApplication.config.authenticator.sessionVariableName ?: "authenticator"
        def hostname = grailsApplication.config.authenticator.hostname ?: "example.com"
        def issuerName = grailsApplication.config.authenticator.issuerName ?: "Default Issuer"
        
        def authenticator = Authenticator.findByUser(grailsApplication.config.authenticator.getUser())
        
        if (authenticator){
            return render(view:"error", model:[message:"An authenticator is already registered to your user, you cannot register another one."])
        }
                   
        // use the getUser closure to get the username that we will associate with this.
        
        def username = grailsApplication.config.authenticator.getUser()
        
        if (!params.code){
            // create a key temporarily with the authenticator - stored in the session, probably can be more secure with this
                  
            def key = session['authenticator.key'] ?: authenticatorService.generateKey()
        
            session['authenticator.key'] = key
            
            def email =  username.split("@")
            
            def url = authenticatorService.generateQRCodeURL(email[0], email.length > 1 ? username.split("@")[1] : hostname, key, issuerName)
        
            return render(view:"register", model:[key:key, url:url])
        } else {
            def key = session['authenticator.key']
                                  
            def timeUnits = new Date().getTime() / TimeUnit.SECONDS.toMillis(30) as Long
            
            if (!authenticatorService.checkCode(key, params.code as Long, timeUnits)){
                return render(view:"register", model:[message:"register", error:"Code doesn't match"])
            }
                        
            authenticator = new Authenticator(secretKey:key, lastAuthentication:new Date(), user:username)                     
                  
            if (authenticator.save()){
                if (grailsApplication.config.authenticator.useSession){
                    session[authenticatorSessionVarName] = authenticator.id
                    session['authenticator.key'] = null; // clear the authenticator key.
                }
                
                return render(view:"success", model:[message:"You have successfully setup two factor authentication."])
            } else {
                println authenticator.error
            }
        }
        
        
    }
}
