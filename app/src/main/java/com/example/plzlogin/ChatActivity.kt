package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class ChatActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        drawerLayout = findViewById(R.id.drawerLayout)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        // 액션바에 toolbar 세팅
        setSupportActionBar(toolbar)
        supportActionBar?.title = "채팅 화면"

        // 액션바 생성
        val actionbar: ActionBar? = supportActionBar

        // 뒤로가기 버튼 생성
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // 뒤로가기 버튼 이미지 >> 메뉴 아이콘으로 교체
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // 드로워 안에 아이템들이 추가돼야 메뉴 아이콘 클릭 했을 때 드로워가 열리는 게 구현이 가능한 것 같아서
        // 이건 기능 디벨롭 하면서 더 해봐야 할 것 같아
        // 드로워 안에는 카메라, 갤러리 접근이 가능하게 하거나 현재 방에 입장되어있는 참여자 목록이 뜨는 게 좋을 것 같아



    }
}