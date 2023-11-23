package com.example.plzlogin

data class Message(
    var message: String?,
    var senderId: String?,
    var senderName: String?,
    val timestamp: Long?
){
    constructor():this("","","",System.currentTimeMillis())
}
