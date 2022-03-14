package com.example.mensajesos.entidades

class Contactos {
    private lateinit var phone:String
    private lateinit var message:String

    fun getphone():String{
        return phone
    }
    fun setphone(phone:String) {
        this.phone = phone
    }
    fun getmessage():String{
        return message
    }
    fun setmessage(message: String){
        this.message = message
    }
}