package com.example.plzlogin

// 데이터 클래스 살짝 수정했어
data class Meet (val meetTitle : String, val startTime: String, val endTime: String, val meetPlace: String) {
    constructor() : this("","", "", "")
}