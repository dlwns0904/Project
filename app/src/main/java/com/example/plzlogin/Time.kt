package com.example.plzlogin

data class Time (var selected: Boolean = false, val time: String) {
    constructor() : this(false, "")
}