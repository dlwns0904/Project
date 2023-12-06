package com.example.plzlogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plzlogin.Message
import com.example.plzlogin.repository.ChatRepository
// 뷰는 ui
class ChatViewModel : ViewModel() { // 자료구조, db유지, 정보, 로직

    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>> get() = _messageList

    private val chatRepository = ChatRepository() // Repository 추가
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
        chatRepository.sendMessage(teamCode, userList, message, currentUserName)
    }

    // 메시지 불러오기 함수
    private fun loadMessages() {
        chatRepository.loadMessages(teamCode) { messages ->
            _messageList.value = messages
        }
    }


}
