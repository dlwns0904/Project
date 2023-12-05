package com.example.plzlogin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _teamlist = MutableLiveData<List<Team>>() //여기서 가지고 있으면 안돼
    // viewModel로 옮기고

    // 어떤 db에 의해서 바뀌는 db?
    // res는 데이터 베이스가
    // 화면에
    val teamList: LiveData<List<Team>> get() = _teamlist

    // observeTeam 파라미터로 팀리스트를 넘겨주고 그 팀리스트의 teamlist.value = teams하면 되겠네
    // 팀리스트를 파라미터로 옮겨야 한다
    fun observeTeam() {
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
                _teamlist.value = teams
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
    // 파이어베이스에 만들라고 얘기를 하고
    // 팀 생성해서 이제 리스트가 바뀌면 알려주는 것?
    //


    // 내 uid에 팀 코드랑 팀 이름 추가
    fun addTeam( teamName: String, teamCode: String ) {

        val TeamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(teamCode)
        TeamRef.setValue(Team( teamName, teamCode ))
    }

    // addOnCompleteListener가 비동기적이라 callback 사용해서 해야해

    fun existTeam( teamCode: String, callback: (Int) -> Unit ) {
        if (teamCode.length != 6) {
            // 이걸 어떻게 해야할까?
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

            // 팀 네임 , 팀 코드 DB에 저장
            teamRef.setValue( Team( teamName,teamCode) )
        }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    // 이름 수정
    // 널 체크 왜? ReNameFrag에서 TeamApdater의 agrument로 teamcode가져오는데 이게 널일 수도 있으니까 string? 으로 받고 널체크
    fun reName( teamCode : String?, rename : String ){
        if (teamCode != null) {
            // 널 체크
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