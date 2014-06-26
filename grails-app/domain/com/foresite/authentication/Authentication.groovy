package com.foresite.authentication

class Authentication {
    
    String id
    
    String userId
    
    Date dateCreated
    Date lastUpdated

    static constraints = {
    }
    
    static mapping = {
        id generator:"uuid"
    }
}
