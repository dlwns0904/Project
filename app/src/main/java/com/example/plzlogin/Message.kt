package com.example.plzlogin

data class Message(
    var message: String?,
    var currentId: String?,
    var userName: String?
){
    constructor():this("","","")
}
