package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plzlogin.databinding.ActivityMenuBinding
import com.example.plzlogin.databinding.ActivityTeamMenuBinding

class TeamMenuActivity : AppCompatActivity() {


    // 해야 될 것
    // 달력 추가해야하고
    // 채팅 눌렀을 때, 게시판 눌렀을 때 화면 어떻게 나올지
    // 기능 구현은 나중에 하고 일단 화면 구성만

    lateinit var binding : ActivityTeamMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)




        // 채팅 눌렀을 때
        // 추가해줘 >> 완료
        binding.btnChat.setOnClickListener {
            val intent = Intent(this,ChatActivity::class.java)
            startActivity(intent)
        }


        // 게시판 눌렀을 때
        // 추가해줘
        binding.btnBoard.setOnClickListener {

        }
    }
}