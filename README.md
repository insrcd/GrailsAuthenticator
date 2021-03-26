GrailsAuthenticator
===================

Google Authenticator implementation in grails.

This is super-alpha code and it requires a lot of documentation and tests, but I thought I would share it for now.  The setup is simple.  
    
    authenticator.hostname = "somewhere.com" // the that will be appended if an email is not passed in getUser
    authenticator.sessionVariableName = "authenticator"
    authenticator.issuerName = "SomeIssuer"
    authenticator.enabled = true // if the authentiator is enabled or not
    authenticator.useSession = true // expire the authenticator with the session, if this is not set 
    authenticator.expireDays = 30   // the user will have to re-authenticate in this many days
    authenticator.excludeControllers = ['someController','login','logout'] // authenticator/* is added automatically
    
    // example of getUser, hacky
    authenticator.getUser = {
        def grailsApplication = new User().domainClass.grailsApplication
        def ctx = grailsApplication.mainContext
        ctx.springSecurityService?.currentUser?.email
    }
