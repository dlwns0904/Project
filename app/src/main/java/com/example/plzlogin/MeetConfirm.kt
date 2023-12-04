package com.example.plzlogin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.plzlogin.databinding.ActivityMeetConfirmBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class MeetConfirm : AppCompatActivity() {

    lateinit var  binding : ActivityMeetConfirmBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var mDbref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMeetConfirmBinding.inflate((layoutInflater))
        setContentView(binding.root)

        mAuth = Firebase.auth
        mDbref = Firebase.database.reference

        val toolbar5 = binding.toolbar5
        val teamCode = intent.getStringExtra("TeamCode").toString()
        val date = intent.getStringExtra("Date").toString()

        //툴바 세팅
        setSupportActionBar(toolbar5)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${date} 회의 일정"

        val meetdateRef = mDbref.child("Meet").child(teamCode).child(date)
        //버튼 눌렀을 때 정보 저장
        binding.btnMeetsave.setOnClickListener {
            val meetTitle = binding.txtMeetTitle.text.toString()
            val meetStart = binding.txtMeetStart.text.toString()
            val meetEnd = binding.txtMeetEnd.text.toString()
            val meetPlace = binding.txtMeetPlace.text.toString()



            // 저장하는 방식 살짝 수정
            meetdateRef.setValue(Meet(meetTitle,meetStart,meetEnd,meetPlace))
            /*meetdateRef.child(meetTitle).setValue(Meet(meetStart, meetEnd, meetPlace))*/
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}