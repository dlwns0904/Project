package com.example.plzlogin

data class Meet (val startTime: String, val endTime: String, val meetPlace: String) {
    constructor() : this("", "", "")
}