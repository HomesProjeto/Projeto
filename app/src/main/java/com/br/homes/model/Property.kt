package com.br.homes.model

class Property {

    var uid: String? = null
    var phone: String? = null
    var price: String? = null
    var cep: String? = null
    var address: String? = null
    var complement: String? = null
    var burgh: String? = null
    var description: String? = null
    var option: String? = null
    var type: String? = null
    var url: String? = null
    var random: String? = null
    var number: String? = null

    constructor() {}

    constructor(
        uid: String,
        phone: String,
        price: String,
        cep: String,
        address: String,
        complement: String,
        burgh: String,
        description: String,
        option: String,
        type: String,
        url: String,
        random: String,
        numver: String

    ) {
        this.uid = uid
        this.phone = phone
        this.price = price
        this.cep = cep
        this.address = address
        this.complement = complement
        this.burgh = burgh
        this.description = description
        this.option = option
        this.type = type
        this.url = url
        this.random = random
        this.number = number
    }
}