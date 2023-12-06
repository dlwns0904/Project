package com.example.plzlogin

data class File(
    val fileName: String,
    val fileUri: String
){
    fun getFileExtension(): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex > 0) {
            fileName.substring(dotIndex + 1)
        } else {
            ""
        }
    }
}
