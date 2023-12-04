package com.example.plzlogin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plzlogin.Meet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ScheduleRepository {
    private var mAuth : FirebaseAuth = Firebase.auth
    private var mDbref : DatabaseReference = Firebase.database.reference

    private val _schedulelist = MutableLiveData<List<Meet>>(emptyList())

    val schedulelist : LiveData<List<Meet>> get() = _schedulelist
    // 내가 들어가있는 팀을 가져오고 데이터 가져오는 것을 log.d로 해서
    fun obverseSchedule( date : String){
        val uid = mAuth.currentUser?.uid!!
        val teamref = mDbref.child("USER").child(uid)
        val scheduleref = mDbref.child("Meet")

        teamref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val schedules = mutableListOf<Meet>()
                for ( postsnapshot in snapshot.children){
                    // USER에 접근해서 postsnapshot이 teamCode를 가져와
                    // 이 가져온 팀코드를 이용해서
                    scheduleref.child(postsnapshot.toString()).child(date).addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (postsnapshot in snapshot.children) {
                            val schedule = postsnapshot.getValue(Meet::class.java)
                            schedule?.let {
                                schedules.add(it)
                            }
                        }
                    }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                _schedulelist.value = schedules
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
   /* fun observeSchedule(date: String) {
        val uid = mAuth.currentUser?.uid!!
        val teamRef = mDbref.child("USER").child(uid).child("teams")
        val scheduleRef = mDbref.child("Meet")

        teamRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(teamSnapshot: DataSnapshot) {
                val schedules = mutableListOf<Meet>()

                for (teamSnapshotChild in teamSnapshot.children) {
                    val teamCode = teamSnapshotChild.key

                    if (teamCode != null) {
                        scheduleRef.child(teamCode).child(date).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(scheduleSnapshot: DataSnapshot) {
                                for (scheduleChildSnapshot in scheduleSnapshot.children) {
                                    val schedule = scheduleChildSnapshot.getValue(Meet::class.java)
                                    schedule?.let {
                                        schedules.add(it)
                                    }
                                }

                                // 데이터를 한 번에 업데이트
                                _schedulelist.setValue(schedules)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // 에러 처리
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        })
    }*/
}
