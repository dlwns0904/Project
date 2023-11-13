package com.example.plzlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.plzlogin.databinding.ActivityChatBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        val toolbar = binding.includeToolbar.toolbar
        val btnMenu = binding.includeToolbar.btnMenu
        mAuth = Firebase.auth
        mDbref = Firebase.database.reference

        val intent = intent
        val teamName = intent.getStringExtra("TeamName")
        Toast.makeText(this@ChatActivity,teamName,Toast.LENGTH_SHORT).show()


        val headerView = binding.navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.tv_nav_userName)
        val userIdTextView = headerView.findViewById<TextView>(R.id.tv_nav_userEmail)

        val user = Firebase.auth.currentUser
        val uid = user!!.uid
        userIdTextView.text = user!!.email

        mDbref.child("user").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").getValue(String::class.java)
                    userNameTextView.text = name
                    //val myMenuItem = findViewById<View>(R.id.nav_userName)
                    //myMenuItem.text = name
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 실패 처리
            }
        })

        // 툴바
        setSupportActionBar(toolbar) // toolbar를 액티비티의 액션바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.title = teamName

        /*mDbref.child("Team").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (teamSnapshot in dataSnapshot.children) {
                        val teamCode = teamSnapshot.key // 팀 코드 (랜덤 숫자)
                        val teamName = teamSnapshot.child("TeamName").getValue(String::class.java)

                        if (teamCode != null && uid != null) {
                            // 이제 teamCode와 userId를 사용하여 해당 팀 및 유저의 정보를 가져올 수 있습니다.
                            val userSnapshot = teamSnapshot.child(uid)
                            val userName = userSnapshot.child("name").getValue(String::class.java)


                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ChatActivity,"fail",Toast.LENGTH_SHORT).show()
            }
        })*/



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


// 검색버튼, 채팅 기능, 카메라&포토 기능, 참여자 리스트, 유저 네임 네비에 띄우기