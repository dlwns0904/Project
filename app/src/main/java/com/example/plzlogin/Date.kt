package com.example.plzlogin

data class Date (var name: String, val times: List<Time>) {
    constructor() : this("", listOf())
}