package com.example.plzlogin

data class Message(
    var message: String?,
    var sendertId: String?,
    var senderName: String?
){
    constructor():this("","","")
}
