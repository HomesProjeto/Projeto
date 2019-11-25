package com.br.homes.model

class Users {

    var uid: String? = null
    var name: String? = null
    var password: String? = null
    var email: String? = null
    var profileImageUrl: String? = null

    constructor() {}

    constructor(
        uid: String,
        name: String,
        password: String,
        email: String,
        profileImageUrl: String
    ) {
        this.uid = uid
        this.name = name
        this.password = password
        this.email = email
        this.profileImageUrl = profileImageUrl
    }
}
