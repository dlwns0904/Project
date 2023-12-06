package com.example.plzlogin.repository

import com.example.plzlogin.CreateFrag
import com.example.plzlogin.Team
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class TeamRepository {

    private var mAuth: FirebaseAuth = Firebase.auth
    private var mDbref: DatabaseReference = Firebase.database.reference

    fun observeTeam( callback : (List<Team>) -> Unit ) {
        val uid = mAuth.currentUser?.uid!!
        val teamref = mDbref.child("USER").child(uid)
        teamref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val teams = mutableListOf<Team>()
                for (postsnapshot in snapshot.children) {
                    val team = postsnapshot.getValue(Team::class.java)
                    team?.let {
                        teams.add(it)
                    }
                }
                callback(teams)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    fun createTeam( teamName: String, teamCode: String ) {
        val teamRef = mDbref.child("Team").child(teamCode)

        teamRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                teamRef.setValue(teamCode)
                teamRef.child("TeamName").setValue(teamName)

                // DB에서 user에 저장된 name을 가져와
                val UsernameRef = mDbref.child("user").child(mAuth.currentUser?.uid!!).child("name")
                UsernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val Username = dataSnapshot.value as String?
                        teamRef.child(mAuth.currentUser?.uid!!).setValue(
                            CreateFrag.User(
                                Username,
                                mAuth.currentUser?.uid!!
                            )
                        )
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
            }
        }
    }

    // 내 uid에 팀 코드랑 팀 이름 추가
    fun addTeam( teamName: String, teamCode: String ) {

        val TeamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(teamCode)
        TeamRef.setValue(Team( teamName, teamCode ))
    }

    fun existTeam( teamCode: String, callback: (Int) -> Unit ) {
        if (teamCode.length != 6) {
            callback(0)
            return
        }
        val teamRef = mDbref.child("Team")
        teamRef.child(teamCode).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result

                if (dataSnapshot.exists()) {
                    callback(1)
                } else {
                    callback(2)
                }
            }
        }
    }

    fun joinTeam( teamCode : String ){

        val teamRef = mDbref.child("Team").child(teamCode)
        val usernameRef = mDbref.child("user").child(mAuth.currentUser?.uid!!).child("name")

        usernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val Username = dataSnapshot.value as String?
                teamRef.child(mAuth.currentUser?.uid!!).setValue(
                    CreateFrag.User(Username, mAuth.currentUser?.uid!!)
                )
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }


    fun joinaddTeam( teamCode : String ){

        val teamRef = mDbref.child("Team").child(teamCode).child("TeamName")

        teamRef.addListenerForSingleValueEvent( object : ValueEventListener{override fun onDataChange(dataSnapshot: DataSnapshot) {

            val teamName = dataSnapshot.value as String?
            val teamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(teamCode)

            teamRef.setValue( Team( teamName,teamCode) )
        }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun reName( teamCode : String?, rename : String ){
        if (teamCode != null) {
            val uid = mAuth.currentUser?.uid!!
            mDbref.child("USER").child(uid).child(teamCode).child("teamName").setValue(rename)
            mDbref.child("Team").child(teamCode).child("TeamName").setValue(rename)
        }
    }

    fun removeTeam( teamCode : String ){
        val uid = mAuth.currentUser?.uid!!
        mDbref.child("USER").child(uid).child(teamCode).removeValue()
        mDbref.child("Team").child(teamCode).child(uid).removeValue()
    }
}