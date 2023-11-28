package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plzlogin.databinding.ActivityTimeSelectBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class TimeSelect : AppCompatActivity() {
    lateinit var binding: ActivityTimeSelectBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var mDbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        mDbref = Firebase.database.reference

        val toolbar4 = binding.toolbar4
        val uid = mAuth.currentUser?.uid!!
        val teamCode = intent.getStringExtra("TeamCode").toString()
        val date = intent.getStringExtra("Date").toString()

        var times: MutableList<Time> = mutableListOf()
        val timesAdapater = TimesAdapater(times)
        binding.recTimes.layoutManager = LinearLayoutManager(this)
        binding.recTimes.adapter = timesAdapater
        // 시간 데이터를 가져올려고 하는데 오류 발생
        val dateRef = mDbref.child("Date").child(teamCode).child(date).child(uid)
        dateRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                        for(timeSnapshot in snapshot.children) {
                            val dateTime = timeSnapshot.getValue(Time::class.java)
                            times.add(dateTime!!)
                        }
                    }
                else {
                    times = createTimes()
                }

                timesAdapater.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //실패 처리
            }
        })

        //툴바에 클릭한 날짜 표시
        setSupportActionBar(toolbar4)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${date} 시간 선택"

        binding.btnSave.setOnClickListener {
            //날짜에 시간 저장
            mDbref.child("Date").child(teamCode).child(date).child(uid).setValue(times)
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

    //시간 Array 생성
    private fun createTimes(): MutableList<Time> {
        return MutableList<Time>(24) {
            Time(time = "${it}시 - ${it+1}시")
        }
    }

}