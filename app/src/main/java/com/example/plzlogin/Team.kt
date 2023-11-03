package com.example.plzlogin


// RT DB에 저장할 데이터 클래스
data class Team(
    var teamName: String? = null, // 필드 추가
    var teamCode: String? = null
) {
    constructor() : this("", "")
}