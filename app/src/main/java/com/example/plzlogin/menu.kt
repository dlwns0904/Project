package com.example.plzlogin

import android.content.Intent
import android.os.Bundle
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
            // 데이터가 변경될 때마다 RecyclerView 업데이트
            adapter.notifyDataSetChanged()
        }

        teamViewModel.teamlist.observe(this) { teams ->
            TeamList.clear()
            TeamList.addAll(teams)
            adapter.notifyDataSetChanged()
        }


        // 요기도 수정
        // USER의 정보를 가지고 오겠다
        /*mDbref.child("USER").child(mAuth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                TeamList.clear()
                for (postSnapshot in snapshot.children){
                    val TeamName = postSnapshot.getValue(Team::class.java)
                    TeamList.add(TeamName!!)
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })*/

        binding.btnMycalendar.setOnClickListener{
            val intent = Intent(this, MyCalendarActivity::class.java)
            startActivity(intent)
        }

        // 팀 생성 누를 때
        binding.btnCreate.setOnClickListener {
            goFrag(0)
        }

        // 팀 참가 누를 때
        binding.btnJoin.setOnClickListener {
            goFrag(1)
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