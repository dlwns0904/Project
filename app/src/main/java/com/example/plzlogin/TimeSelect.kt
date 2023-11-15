package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val uid = mAuth.currentUser?.uid!!
        val teamCode = intent.getStringExtra("TeamCode").toString()
        val date = intent.getStringExtra("Date").toString()
        val teamuserRef = mDbref.child("Team").child(teamCode).child("Date")

        var times: MutableList<Time> = mutableListOf()
        times = createTimes()
        // 시간 데이터를 가져올려고 하는데 오류 발생
        /*
        teamuserRef.child(date).child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                        for(timeSnapshot in snapshot.children) {
                            val timesnap = timeSnapshot.child("time").getValue(String::class.java).toString()
                            val selectedsnap = timeSnapshot.child("selected").getValue(String::class.java).toBoolean()
                            times.add(Time(time = timesnap, isSelected = selectedsnap))
                        }
                    }
                else {
                    times = createTimes()
                }
                //adapter 연결
                val timesAdapater = TimesAdapater(times)
                binding.recTimes.layoutManager = LinearLayoutManager(this@TimeSelect)
                binding.recTimes.adapter = TimesAdapater(times)
                timesAdapater.notifyDataSetChanged()
                }

            override fun onCancelled(error: DatabaseError) {
                //실패 처리
            }
        })
        */
        binding.recTimes.layoutManager = LinearLayoutManager(this@TimeSelect)
        binding.recTimes.adapter = TimesAdapater(times)

        //클릭한 날짜 표시
        binding.TimeSelect.text = "${date} 시간 선택"

        binding.btnSave.setOnClickListener {
            //날짜에 시간 저장

            teamuserRef.child(date).child(uid).setValue(times)
            val intent: Intent = Intent(this@TimeSelect,TeamMenuActivity::class.java)
            startActivity(intent)
        }
        //취소 클릭 시 되돌아가기
        binding.btnCancel.setOnClickListener {
            val intent: Intent = Intent(this@TimeSelect,TeamMenuActivity::class.java)
            startActivity(intent)
        }
    }

    //시간 Array 생성
    private fun createTimes(): MutableList<Time> {
        val times = MutableList<Time>(24) {
            Time(time = "${it}시 - ${it+1}시")
        }

        return times
    }

}