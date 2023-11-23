package com.example.plzlogin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plzlogin.CreateFrag
import com.example.plzlogin.Team
import com.example.plzlogin.databinding.FragmentCreateBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// firebase를 통해서 가져오겠지
class TeamRepository {
    // 팀 코드 중복처리 해야하고


    private var mAuth : FirebaseAuth = Firebase.auth
    private var mDbref : DatabaseReference = Firebase.database.reference

    private val _teamlist = MutableLiveData<List<Team>>()
    val teamList: LiveData<List<Team>> get() = _teamlist

    fun observeTeam(){

        val uid = mAuth.currentUser?.uid!!
        val teamref = mDbref.child("USER").child(uid)
        teamref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val teams = mutableListOf<Team>()
                for (postsnapshot in snapshot.children) {
                    val team = postsnapshot.getValue(Team::class.java)
                    team?.let {
                        teams.add(it) }
                }
                _teamlist.value = teams
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    // 요거 다르게 수정해야하고
    fun RandomNumber(): String {
        var Number = ""
        val Range = 0..9

        for (i in 0..5) {
            val Addnum = Range.random()
            Number += Addnum.toString()
        }
        return Number
    }


    // 팀 생성
    fun createTeam(binding: FragmentCreateBinding, TeamCode: String) {

        val TeamName = binding.edtTeam.text.toString().trim()
        val TeamRef = mDbref.child("Team").child(TeamCode)

        TeamRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                TeamRef.setValue(TeamCode)
                TeamRef.child("TeamName").setValue(TeamName)

                // DB에서 user에 저장된 name을 가져와
                val UsernameRef = mDbref.child("user").child(mAuth.currentUser?.uid!!).child("name")
                UsernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val Username = dataSnapshot.value as String?
                        TeamRef.child(mAuth.currentUser?.uid!!).setValue(
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
            else {
                // 안되면 다시 지우고
                // 지금 추가한게 TeamRef.get().addOnCompleteListener요기 부분
                // 팀 코드 만약에 중복된 게 있으면 메시지 출력
                /*Toast.makeText(context,"이미 존재하는 팀 코드 입니다", Toast.LENGTH_SHORT).show()*/
            }
        }
    }
    // 내 uid에 팀 코드랑 팀 이름 추가
    fun addTeam(binding: FragmentCreateBinding, TeamCode: String){

        val TeamName = binding.edtTeam.text.toString().trim()
        val TeamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(TeamCode)

        TeamRef.setValue(Team(TeamName,TeamCode))
    }

    fun joinTeam(TeamCode : String){

        val TeamRef = mDbref.child("Team").child(TeamCode)
        // 이름 가지고 오기
        val UsernameRef = mDbref.child("user").child(mAuth.currentUser?.uid!!).child("name")

        UsernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val Username = dataSnapshot.value as String?
                TeamRef.child(mAuth.currentUser?.uid!!).setValue(
                    CreateFrag.User(
                        Username,
                        mAuth.currentUser?.uid!!
                    )
                )

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                /*Toast.makeText(requireContext(), "데이터를 가져오는 중 오류 발생", Toast.LENGTH_SHORT).show()*/
            }
        })
    }


    fun addTeam(TeamCode: String){
        // 팀 네임을 가져 와야 겠지?
        val TeamRef = mDbref.child("Team").child(TeamCode).child("TeamName")

        TeamRef.addListenerForSingleValueEvent( object : ValueEventListener{override fun onDataChange(dataSnapshot: DataSnapshot) {

            val TeamName = dataSnapshot.value as String?
            val TeamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(TeamCode)
            // 팀 네임 , 팀 코드 DB에 저장
            TeamRef.setValue(Team(TeamName,TeamCode))
        }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                /*Toast.makeText(requireContext(), "데이터를 가져오는 중 오류 발생", Toast.LENGTH_SHORT).show()*/
            }
        })
    }

    // 이름 수정
    fun reName(teamCode : String?, rename : String){
        if (teamCode != null) { // 널 체크
            val uid = mAuth.currentUser?.uid!!
            mDbref.child("USER").child(uid).child(teamCode).child("teamName").setValue(rename)
            mDbref.child("Team").child(teamCode).child("TeamName").setValue(rename)
        }
        else{
            /*Toast.makeText(context,"숫자없음", Toast.LENGTH_SHORT).show()*/
        }
    }
}