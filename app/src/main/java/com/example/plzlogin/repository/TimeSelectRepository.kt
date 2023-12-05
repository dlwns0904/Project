package com.example.plzlogin.repository

import androidx.lifecycle.MutableLiveData
import com.example.plzlogin.Time
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class TimeSelectRepository {
    private val mAuth: FirebaseAuth = Firebase.auth
    private val mDbref: DatabaseReference = Firebase.database.reference
    private val uid = mAuth.currentUser?.uid!!

    fun loadTimes(teamCode: String, date: String, callback: (List<Time>) -> Unit) {
        val dateRef = mDbref.child("Date").child(teamCode).child(date).child(uid)

        dateRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timeList: MutableList<Time> = mutableListOf()
                if(snapshot.exists()) {
                    for(timeSnapshot in snapshot.children) {
                        val dateTime = timeSnapshot.getValue(Time::class.java)
                        dateTime?.let { timeList.add(it)}
                    }
                }
                else {
                    timeList.addAll(createTimes())
                }
                callback(timeList)
            }

            override fun onCancelled(error: DatabaseError) {
                //실패 처리
            }
        })

    }

    fun saveTimes(teamCode: String, date: String, times: List<Time>) {
        mDbref.child("Date").child(teamCode).child(date).child(uid).setValue(times)
    }
    private fun createTimes(): MutableList<Time> {
        return MutableList(14) {
            Time(time = "${it + 8}시 - ${it + 9}시")
        }
    }

}