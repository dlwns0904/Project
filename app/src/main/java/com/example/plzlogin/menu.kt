package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plzlogin.databinding.ActivityMenuBinding




class menu : AppCompatActivity() {



    lateinit var binding : ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 그리고 기존 텍스트뷰 여러개를 일단 빼고 카드뷰로 변경했고 방 갯수가 많아지면 스크롤이 되는지는 기능 구현하고 보면 될 것 같아

        // Mycalender 버튼 추가했어 누르면 내 일정을 한눈에 볼 수 있는 화면이 나와
        binding.btnMycalendar.setOnClickListener{
            val intent = Intent(this, MyCalendarActivity::class.java)
            startActivity(intent)
        }


        // 일단 팀 만들었다고 가정하고 해보자
        // CreateFrag에서 팀 이름 가져와야하는 거 추가해야해



        // 팀 생성 누를 때
        binding.btnCreate.setOnClickListener {

            goFrag(0)
            binding.Team1.text = "FirstTeam"
        }

        // 팀 참가 누를 때
        binding.btnJoin.setOnClickListener {

            goFrag(1)
        }


        // 일단 임시로 팀 눌렀을 때 팀 메뉴로 가는거 설정
        binding.Team1.setOnClickListener {
            val intent : Intent = Intent(this@menu,TeamMenuActivity::class.java)
            startActivity(intent)
        }


    }

    // 프래그먼트 전환하기
    private fun goFrag(num : Int){
        val frag = supportFragmentManager.beginTransaction()

        when (num){

            // 생성누르면
            0 -> frag.replace(R.id.Menufrag, CreateFrag()).commit()

            // 참여누르면
            1 -> frag.replace(R.id.Menufrag, JoinFrag()).commit()
        }

    }
}