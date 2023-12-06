package com.example.plzlogin.repository

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

    fun observeSchedule( selectedDate: String, callback : (List<Meet>) -> Unit ) {
        val uid = mAuth.currentUser?.uid!!
        val teamRef = mDbref.child("USER").child(uid)
        val meetRef = mDbref.child("Meet")

        teamRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val meets = mutableListOf<Meet>()

                for (postSnapshot in snapshot.children) {
                    val teamCode = postSnapshot.key

                    if (teamCode != null) {
                        meetRef.child(teamCode).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(scheduleSnapshot: DataSnapshot) {
                                for (scheduleChildSnapshot in scheduleSnapshot.children) {
                                    val meet = scheduleChildSnapshot.getValue(Meet::class.java)

                                    // meetdate가 선택한 날짜와 같은지 확인
                                    if (meet?.meetdate == selectedDate) {
                                        meet?.let {
                                            meets.add(it)
                                        }
                                    }
                                }
                                callback(meets)
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
    }


    /*fun obverseSchedule() {
        val uid = mAuth.currentUser?.uid!!
        val teamref = mDbref.child("USER").child(uid)
        val meetref = mDbref.child("Meet")

        teamref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val meets = mutableListOf<Meet>()

                for (postsnapshot in snapshot.children) {
                    val teamCode = postsnapshot.key

                    if (teamCode != null) {
                        meetref.child(teamCode).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snap_shot: DataSnapshot) {
                                for (post_snapshot in snap_shot.children) {
                                    val meet = post_snapshot.getValue(Meet::class.java)
                                    meet?.let {
                                        meets.add(it)
                                    }
                                }
                                // 데이터를 한 번에 업데이트
                                _meetlist.value = meets

                            }
                            override fun onCancelled(error: DatabaseError){
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


    /*fun obverseSchedule(){
        val uid = mAuth.currentUser?.uid!!
        val teamref = mDbref.child("USER").child(uid)
        val meetref = mDbref.child("Meet")

        teamref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val meets = mutableListOf<Meet>()
                for ( postsnapshot in snapshot.children){
                    // USER에 접근해서 postsnapshot이 teamCode를 가져와
                    // 이 가져온 팀코드를 이용해서
                    meetref.child(postsnapshot.toString()).addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snap_shot: DataSnapshot) {
                            TODO("Not yet implemented")
                            for ( post_snapshot in snap_shot.children){
                                val meet = post_snapshot.getValue(Meet::class.java)
                                meet?.let{
                                    meets.add(it)
                                }

                            }
                        }
                        _meetlist.value = meets


                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }*/
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
