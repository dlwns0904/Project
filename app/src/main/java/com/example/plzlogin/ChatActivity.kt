package com.example.plzlogin

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Highlights
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plzlogin.databinding.ActivityChatBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding // private ?
    private lateinit var drawerLayout: DrawerLayout // 앞에 뭐 안 붙어도 되는지
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbref: DatabaseReference
    private lateinit var navView: NavigationView
    private lateinit var teamCode: String
    private lateinit var senderUid: String // 고민
    private lateinit var receiverUid: String
    private lateinit var currentRoom: String // 보낸 대화방
    private lateinit var receiverRoom: String // 받는 대화방
    private lateinit var messageList: ArrayList<Message>
    private var currentUserName: String = ""
    private var searchKeyword: String = ""
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)


        // recyclerView
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerChat.adapter = messageAdapter


        // 인스턴스 초기화
        mAuth = Firebase.auth
        mDbref = Firebase.database.reference

        // 이전화면에서 가져오기
        val intent = intent
        val teamName = intent.getStringExtra("TeamName")
        teamCode = intent.getStringExtra("TeamCode").toString()


        val toolbar = binding.includeToolbar.toolbar
        val btnMenu = binding.includeToolbar.btnMenu
        val btnSearch = binding.includeToolbar.btnSearch
        btnSearch.setOnClickListener {
            // 검색 다이얼로그 또는 검색 뷰를 표시
            showSearchDialog()
        }
        drawerLayout = binding.drawerLayout
        navView = binding.navView


        val menu: Menu = navView.menu
        val Users = menu.findItem(R.id.subtitle)?.subMenu
        // Log.d("slsls", "group2: $group2")
        // subtitle에 해당하는 SubMenu를 찾음


        val headerView = navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.tv_nav_userName)
        val userIdTextView = headerView.findViewById<TextView>(R.id.tv_nav_userEmail)

        val userList = mutableListOf<String>()
        val currentUser = mAuth.currentUser
        val currentUid = currentUser?.uid ?: "" // 현재 user가 null일 경우가 생길 수가 있나 ?
        userIdTextView.text = currentUser?.email


        // 드로어 헤더에 현재 유저 정보
        mDbref.child("user").child(currentUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val name = dataSnapshot.child("name").getValue(String::class.java)
                        userNameTextView.text = name
                        val navUsersItem = Users?.findItem(R.id.nav_Users)
                        navUsersItem?.title = name
                        currentUserName = name.toString()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 실패 처리
                }
            })


        // 드로어에 채팅방 참여자 리스트
        val teamRef = mDbref.child("Team").child(teamCode)
        teamRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val uid = userSnapshot.key //.also, apply
                    uid?.let {
                        if (it != "TeamName") { // "TeamName"은 제외
                            receiverUid = it
                            Log.d("flflf", "receiver Uid: $receiverUid")
                            userList.add(receiverUid)
                        }
                    }
                    if (uid != currentUid) { // 현재 사용자의 uid는 제외
                        val usersName = userSnapshot.child("name").getValue(String::class.java)
                        usersName?.let {
                            val itemId = View.generateViewId() // 아이템 ID 동적으로 생성
                            // Log.d("Dldld", "Generated ItemId: $itemId")
                            val menuItem = Users?.add(Menu.NONE, itemId, Menu.NONE, it)
                            menuItem?.setIcon(R.drawable.ic_user) // 원하는 아이콘으로 설정
                        }
                    }
                }
                // userList = mutableListOf(receiverUid, currentUid)
                userList.sort()
                Log.d("rlrlr", "user list: $userList")

                // 그룹 대화방 설정
                val groupId = teamCode
                val groupChatRoom = "group_$groupId"

                // 그룹 채팅 메시지 전송
                binding.btnSend.setOnClickListener {
                    val message = binding.edtMessage.text.toString()
                    if (message.trim().isNotEmpty()) { // 공백만 보내기는 안되게
                        val timestamp = System.currentTimeMillis() // 현재 시간을 밀리초로 얻음
                        val messageObject = Message(message, currentUid, currentUserName, timestamp)

                        // 데이터 저장
                        mDbref.child("Chats").child(groupChatRoom).child("messages").push()
                            .setValue(messageObject).addOnSuccessListener {
                                // 저장 성공시
                                for (memberUid in userList) {
                                    mDbref.child("Chats").child(groupChatRoom)
                                        .child("user_$memberUid")
                                        .child("messages").push().setValue(messageObject)
                                }

                            }
                        binding.edtMessage.setText("") // 입력값 초기화
                    }
                }

                // 메세지 가져오기
                mDbref.child("Chats").child(groupChatRoom).child("messages")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            messageList.clear()

                            for (postSnapshot in snapshot.children) {
                                val message = postSnapshot.getValue(Message::class.java)
                                messageList.add(message!!)
                            }
                            // 적용
                            messageAdapter.notifyDataSetChanged()
                            binding.recyclerChat.scrollToPosition(messageList.size - 1)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // 실패 처리
                        }

                    })

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        // 툴바
        setSupportActionBar(toolbar) // toolbar를 액티비티의 액션바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.title = teamName


        // 툴바 메뉴 버튼
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END) // 드로어를 오른쪽에서 열기
        }

        // 네비 드로어
        binding.navView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_camera -> Toast.makeText(this, "카메라", Toast.LENGTH_SHORT).show()
                R.id.nav_photo -> Toast.makeText(this, "사진", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener false // true = 강조 남음
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }


    // 툴바 뒤로 가기 버튼 실행
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 에뮬레이터의 뒤로가기 버튼 누르면 드로어 닫힘
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) drawerLayout.closeDrawers()
            else finish()
        }
    }



    private fun showSearchDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("검색")

        val editText = EditText(this)
        builder.setView(editText)

        builder.setPositiveButton("확인") { dialog, which ->
            // 확인 버튼을 눌렀을 때의 처리
            val searchKeyword = editText.text.toString()
            //messageAdapter.filterByKeyword(searchKeyword)
            if (!searchKeyword.isEmpty() && messageList.isNotEmpty()) {
                val firstMatchingPosition = findFirstMatchingPosition(searchKeyword)
                if (firstMatchingPosition != -1) {
                    binding.recyclerChat.scrollToPosition(firstMatchingPosition)
                    messageAdapter.notifyDataSetChanged()
                }
            }
        }

        builder.setNegativeButton("취소") { dialog, which ->
            // 취소 버튼을 눌렀을 때의 처리
        }

        builder.show()
    }
    private fun findFirstMatchingPosition(keyword: String): Int {
        for ((index, message) in messageList.withIndex()) {
            if (message.message?.contains(keyword) == true) {
                return index
            }
        }
        return -1
    }




}


// senderUid? currentUid? 객체이름 다시 생각해보기, 샌더와 리시버 나누기 ? 안 나누면 ?