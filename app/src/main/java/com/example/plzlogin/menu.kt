package com.example.plzlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plzlogin.databinding.ActivityMenuBinding
import com.example.plzlogin.viewmodel.TeamViewModel


class menu : AppCompatActivity() {

    lateinit var binding : ActivityMenuBinding
    lateinit var adapter : TeamApdater

    private val teamViewModel = TeamViewModel()
    private lateinit var TeamList : ArrayList<Team>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리스트 초기화
        TeamList = ArrayList()
        adapter = TeamApdater(this, teamViewModel)

        binding.recTeam.layoutManager = LinearLayoutManager(this)
        binding.recTeam.adapter = adapter


        teamViewModel.teamlist.observe(this) { teams ->
            TeamList.clear()
            TeamList.addAll(teams)
            adapter.notifyDataSetChanged()
        }

        binding.btnMycalendar.setOnClickListener{
            val intent = Intent(this, MyCalendarActivity::class.java)
            startActivity(intent)
        }

        binding.btnCreate.setOnClickListener {
            goFrag(0)
        }

        binding.btnJoin.setOnClickListener {
            goFrag(1)
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            Toast.makeText(this,"로그아웃",Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

    }

    private fun goFrag(num : Int){
        val frag = supportFragmentManager.beginTransaction()

        when (num){
            0 -> frag.replace(R.id.Menufrag, CreateFrag()).commit()
            1 -> frag.replace(R.id.Menufrag, JoinFrag()).commit()
        }
    }
}