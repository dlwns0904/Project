package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.plzlogin.databinding.ActivityMainBinding
import com.example.plzlogin.databinding.ActivityMenuBinding




class menu : AppCompatActivity() {



    lateinit var binding : ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 일단 팀 만들었다고 가정하고 해보자
        // CreateFrag에서 팀 이름 가져와야하는 거 추가해야해
        binding.Team1.text = "FirstTeam"


        // 팀 생성 누를 때
        binding.btnCreate.setOnClickListener {

            goFrag(0)
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