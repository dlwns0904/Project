package com.example.plzlogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plzlogin.Message
import com.example.plzlogin.repository.MessageRepository

class MessageViewModel : ViewModel() {

    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>> get() = _messageList

    private val messageRepository = MessageRepository() // Repository 추가
    private lateinit var teamCode: String
    private lateinit var receiverUid: String
    private lateinit var userList: MutableList<String>

    // 초기화 함수
    fun init(teamCode: String, receiverUid: String, userList: MutableList<String>) {
        this.teamCode = teamCode
        this.receiverUid = receiverUid
        this.userList = userList
        loadMessages()
    }

    // 메시지 전송 함수
    fun sendMessage(message: String, currentUserName: String) {
        messageRepository.sendMessage(teamCode, userList, message, currentUserName)
    }

    // 메시지 불러오기 함수
    private fun loadMessages() {
        messageRepository.loadMessages(teamCode) { messages ->
            _messageList.value = messages
        }
    }


}
