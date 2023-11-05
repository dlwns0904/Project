package com.example.plzlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import com.example.plzlogin.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    // lateinit var binding : DrawerLayout
    lateinit var binding : ActivityChatBinding
    private lateinit  var receiverName : String
    private lateinit  var receiverUid : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.includeToolbar.toolbar // findViewById 대신 사용
        setSupportActionBar(toolbar) // 액션바에 toolbar 세팅 >> 노액션바인데 왜 액션바가 나왔지 ?
        supportActionBar?.title = "채팅 화면" // 채팅 화면이 아닌 팀네임 받아오기
        binding.includeToolbar.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END) // 드로어를 오른쪽에서 왼쪽으로 엽니다.
        }

        val actionbar: ActionBar? = supportActionBar // 액션바 생성
        actionbar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        // actionbar?.setHomeAsUpIndicator(R.drawable.ic_menu) // 뒤로가기 버튼 이미지 >> 메뉴 아이콘으로 교체

        binding.navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_camera -> {
                    item.isChecked = true
                    Toast.makeText(this, "카메라", Toast.LENGTH_SHORT).show()
                    binding.drawerLayout.closeDrawers()
                    return@setNavigationItemSelectedListener true // 그냥 true 로 해도 되는지?
                }
                R.id.nav_photo -> {
                    item.isChecked = true
                    Toast.makeText(this, "사진", Toast.LENGTH_SHORT).show()
                    binding.drawerLayout.closeDrawers()
                    return@setNavigationItemSelectedListener true

                }
            }
            return@setNavigationItemSelectedListener true // true false 제대로
        }






        receiverName = intent.getStringExtra("name").toString()
        receiverUid = intent.getStringExtra("uId").toString()
    }

    //뒤로 가기 버튼 실행
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}