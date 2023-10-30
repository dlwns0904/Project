package com.example.plzlogin

data class User(
    var name : String,
    var id : String,
    var uid : String
){
    constructor() : this("", "", "") // 주생성자로 바꿀 수 있지 않나?
}