package com.example.plzlogin.repository

import com.example.plzlogin.Date
import com.example.plzlogin.Time
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

class TeamMenuRepository {
    private val mAuth: FirebaseAuth = Firebase.auth
    private val mDbref: DatabaseReference = Firebase.database.reference


    fun loadDates(teamCode: String, date: String, callback: (List<Date>) -> Unit) {
        val dateRef = mDbref.child("Date").child(teamCode).child(date)

        dateRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dates = mutableListOf<Date>()

                for (uidSnapshot in snapshot.children) {
                    val uid = uidSnapshot.key

                    if (uid != null) {
                        mDbref.child("user").child(uid).child("name")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(nameSnapshot: DataSnapshot) {
                                    val name = nameSnapshot.value?.toString() ?: ""

                                    val times = mutableListOf<Time>()
                                    for (timeSnapshot in uidSnapshot.children) {
                                        val time = timeSnapshot.getValue(Time::class.java)
                                        time?.let { times.add(it) }
                                    }

                                    val date = Date(name = name, times = times)
                                    dates.add(date)

                                    callback(dates)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // 실패 처리
                                }
                            })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 실패 처리
            }
        })
    }
}

