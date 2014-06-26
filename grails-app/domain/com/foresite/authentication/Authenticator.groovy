package com.foresite.authentication

class Authenticator {
    
    String id
    
    String user
    String secretKey
    
    Date dateCreated
    Date lastUpdated
    
    Date lastAuthentication
    
    int failedAuthentications   = 0

    static constraints = {
       
    }
    
    static mapping = {
        id generator:"uuid"
    }
}
