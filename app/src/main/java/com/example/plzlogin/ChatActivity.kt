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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
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

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding // private ?
    private lateinit var drawerLayout: DrawerLayout // 앞에 뭐 안 붙어도 되는지
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbref: DatabaseReference
    private lateinit var navView: NavigationView
    private lateinit var teamCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val toolbar = binding.includeToolbar.toolbar
        val btnMenu = binding.includeToolbar.btnMenu
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        mAuth = Firebase.auth
        mDbref = Firebase.database.reference

        val menu: Menu = navView.menu
        val Users = menu.findItem(R.id.subtitle)?.subMenu
        // Log.d("slsls", "group2: $group2")
        // group2에 해당하는 SubMenu를 찾음
        val currentUserId = mAuth.currentUser?.uid

        val intent = intent
        val teamName = intent.getStringExtra("TeamName")
        teamCode = intent.getStringExtra("TeamCode").toString()



        val headerView = navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.tv_nav_userName)
        val userIdTextView = headerView.findViewById<TextView>(R.id.tv_nav_userEmail)

        val user = Firebase.auth.currentUser
        val uid = user!!.uid
        userIdTextView.text = user?.email



        mDbref.child("user").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").getValue(String::class.java)
                    userNameTextView.text = name
                    val navUsersItem = Users?.findItem(R.id.nav_Users)
                    navUsersItem?.title = name
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 실패 처리
            }
        })


        val teamRef = mDbref.child("Team").child(teamCode)
        teamRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val uid = userSnapshot.key
                    if (uid != currentUserId) { // 현재 사용자의 uid는 제외
                        val userName = userSnapshot.child("name").getValue(String::class.java)
                        userName?.let {
                            val itemId = View.generateViewId() // 아이템 ID 동적으로 생성
                            // Log.d("Dldld", "Generated ItemId: $itemId")
                            val menuItem = Users?.add(Menu.NONE, itemId, Menu.NONE, it)
                            menuItem?.setIcon(R.drawable.ic_user) // 원하는 아이콘으로 설정
                        }
                    }
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
/*



        /*(val teamRef = mDbref.child("Team").child(teamCode)
        teamRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val uid = userSnapshot.key
                    if (uid != currentUserId) { // 현재 사용자의 uid는 제외
                        val userName = userSnapshot.child("name").getValue(String::class.java)

                        // 유저 이름이 null이 아니라면 메뉴에 아이템 추가
                        userName?.let {
                            val itemId = View.generateViewId() // 아이템 ID 동적으로 생성
                            val menuItem = group2!!.add(Menu.NONE, itemId, Menu.NONE, it)
                            menuItem.setIcon(R.drawable.ic_user) // 원하는 아이콘으로 설정
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 실패 처리
            }
        })*/



    }










    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)

        // MenuItem을 가져옴
        val menuItem = menu.findItem(R.id.rec_Menu)

        // ActionLayout을 가져옴
        val actionView = MenuItemCompat.getActionView(menuItem)

        // ActionLayout 안에 있는 RecyclerView를 찾음
        val recyclerView: RecyclerView = actionView.findViewById(R.id.recyclerView)

        val teamRef = mDbref.child("Team").child(teamCode)
        userList = ArrayList()

        teamRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()

                for (uidSnapshot in dataSnapshot.children) {
                    val uid = uidSnapshot.key
                    uid?.let {
                        val userRef = teamRef.child(it)
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val name = userSnapshot.child("name").getValue(User::class.java)
                                name?.let {
                                    userList.add(name)

                                    if (userList.size == dataSnapshot.childrenCount.toInt()) {
                                        // 기존 어댑터 업데이트
                                        userAdapter.notifyDataSetChanged()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // 실패 처리
                            }
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 실패 처리
            }
        })

        return true
    }*/

}


// 검색버튼, 채팅 기능, 카메라&포토 기능, 참여자 리스트, 유저 네임 네비에 띄우기

 */