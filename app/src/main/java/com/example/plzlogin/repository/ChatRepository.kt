package com.example.plzlogin.repository

import com.example.plzlogin.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ChatRepository { // 리포지토리는 어떤 db를 쓰는지를, ex)firebase, 파라미터를 뷰모델로 넘겨줘야, 파이어베이스에 만들라고 얘기만, addValueEventListener아니고 늘 listener, valuechangelistener

// 뷰모델에서 라이브 데이터를 파라미터로 넘겨줌, 파이어베이스가 수정되면 뷰모델, 레포지토리에 알려줌, 뷰모델이 레포지토리에 넘겨준 파라미터 읽음, 뷰가 뷰모델 옵저브 // 화면 체인지 잘 되기위해
    private val mAuth: FirebaseAuth = Firebase.auth
    private val mDbref: DatabaseReference = Firebase.database.reference

    fun sendMessage(teamCode: String, userList: List<String>, message: String, currentUserName: String) {
        val currentUid = mAuth.currentUser?.uid
        if (currentUid != null && message.isNotBlank()) {
            val timestamp = System.currentTimeMillis()
            val messageObject = Message(message, currentUid, currentUserName, timestamp)

            // 그룹 대화방 설정
            val groupId = teamCode
            val groupChatRoom = "group_$groupId"

            // 데이터 저장
            mDbref.child("Chats").child(groupChatRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    for (memberUid in userList) {
                        mDbref.child("Chats").child(groupChatRoom)
                            .child("user_$memberUid")
                            .child("messages").push().setValue(messageObject)
                    }
                }
        }
    }

    fun loadMessages(teamCode: String, callback: (List<Message>) -> Unit) {
        val groupId = teamCode
        val groupChatRoom = "group_$groupId"

        // 메세지 가져오기
        mDbref.child("Chats").child(groupChatRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<Message>()

                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        message?.let {
                            messages.add(it)
                        }
                    }

                    // 콜백으로 데이터 전달
                    callback(messages)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 실패 처리
                }
            })
    }

}
