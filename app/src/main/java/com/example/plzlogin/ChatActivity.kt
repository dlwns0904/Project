package com.example.plzlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plzlogin.databinding.ActivityChatBinding
import com.example.plzlogin.viewmodel.ChatViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding // private ?
    private lateinit var drawerLayout: DrawerLayout // 앞에 뭐 안 붙어도 되는지
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbref: DatabaseReference
    private lateinit var navView: NavigationView
    private lateinit var receiverUid: String
    private var currentUserName: String = ""
    private lateinit var messageAdapter: MessageAdapter


    private val viewModel: ChatViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 이전 화면에서 가져오기
        val teamName = intent.getStringExtra("TeamName")
        val teamCode = intent.getStringExtra("TeamCode").toString()
        val userList = mutableListOf<String>()


        // 인스턴스 초기화
        mAuth = Firebase.auth
        mDbref = Firebase.database.reference

        // RecyclerView 설정
        messageAdapter = MessageAdapter(this, ArrayList())
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerChat.adapter = messageAdapter

        // LiveData를 관찰하여 UI 업데이트
        viewModel.messageList.observe(this) { messages ->
            messages?.let {
                messageAdapter.updateMessages(it)
                binding.recyclerChat.scrollToPosition(it.size - 1)
            }
        }

        val toolbar = binding.includeToolbar.toolbar
        val btnMenu = binding.includeToolbar.btnMenu
        drawerLayout = binding.drawerLayout
        navView = binding.navView


        val menu: Menu = navView.menu
        val Users = menu.findItem(R.id.subtitle)?.subMenu


        val headerView = navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.tv_nav_userName)
        val userIdTextView = headerView.findViewById<TextView>(R.id.tv_nav_userEmail)

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

                // ViewModel 초기화
                viewModel.init(teamCode, receiverUid, userList)
                Log.d("ViewModelDebug", "teamcode: $teamCode, receiverUid: $receiverUid, userList: $userList")


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        // 메시지 전송 버튼 설정
        binding.btnSend.setOnClickListener {
            val message = binding.edtMessage.text.toString()
            viewModel.sendMessage(message, currentUserName)
            binding.edtMessage.setText("")
        }

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



}


