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

// firebase를 통해서 가져오겠지
class TeamRepository {
    // 팀 코드 중복처리 해야하고


    private var mAuth : FirebaseAuth = Firebase.auth
    private var mDbref : DatabaseReference = Firebase.database.reference

    private val _teamlist = MutableLiveData<List<Team>>() //여기서 가지고 있으면 안되
    // viewModel로 옮기고

    // 어떤 db에 의해서 바뀌는 db?
    // res는 데이터 베이스가
    // 화면에
    val teamList: LiveData<List<Team>> get() = _teamlist

    // 팀리스트를 파라미터로 옮겨야 한다
    fun observeTeam(){
        // addValue말고 child가 change되면
        // add가 성공했습니다
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

    // 팀 생성
    // 요거 바인딩 없애고
    fun createTeam( TeamName : String, TeamCode: String) {
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
        }
    }
    // 파이어베이스에 만들라고 얘기를 하고
    // 팀 생성해서 이제 리스트가 바뀌면 알려주는 것?
    //




    // 내 uid에 팀 코드랑 팀 이름 추가
    fun addTeam( TeamName: String, TeamCode: String){

        val TeamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(TeamCode)
        TeamRef.setValue(Team(TeamName,TeamCode))
    }

    // context 받아서 그냥 바로 Toast메세지 실행시키게 함
    // 일반 반환 불가 addOnCompleteListener가 비동기적이라 callback 사용해서 해야해
    fun existTeam(teamCode : String) {
        if (teamCode.length != 6) {

            /*Toast.makeText(context, "6자리 숫자를 입력해주세요", Toast.LENGTH_SHORT).show()*/
        }

        val teamRef = mDbref.child("Team")
        teamRef.child(teamCode).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result

                // 데이터 있으면 팀 참가해주고
                /*if (dataSnapshot.exists())*/
            }
        }
    }

    fun joinTeam(TeamCode : String){

        val TeamRef = mDbref.child("Team").child(TeamCode)
        // 이름 가지고 오기
        val UsernameRef = mDbref.child("user").child(mAuth.currentUser?.uid!!).child("name")

        UsernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val Username = dataSnapshot.value as String?
                TeamRef.child(mAuth.currentUser?.uid!!).setValue(
                    CreateFrag.User(Username, mAuth.currentUser?.uid!!)
                )
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                /*Toast.makeText(requireContext(), "데이터를 가져오는 중 오류 발생", Toast.LENGTH_SHORT).show()*/
            }
        })
    }


    fun joinaddTeam(TeamCode: String){
        // 팀 네임을 가져 와야 겠지?
        val TeamRef = mDbref.child("Team").child(TeamCode).child("TeamName")

        TeamRef.addListenerForSingleValueEvent( object : ValueEventListener{override fun onDataChange(dataSnapshot: DataSnapshot) {

            val TeamName = dataSnapshot.value as String?
            val TeamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(TeamCode)

            // 팀 네임 , 팀 코드 DB에 저장
            TeamRef.setValue(Team(TeamName,TeamCode))
        }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    // 이름 수정
    // 널 체크 왜? ReNameFrag에서 TeamApdater의 agrument로 teamcode가져오는데 이게 널일 수도 있으니까 string? 으로 받고 널체크
    fun reName(teamCode : String?, rename : String){
        if (teamCode != null) {
            // 널 체크
            val uid = mAuth.currentUser?.uid!!
            mDbref.child("USER").child(uid).child(teamCode).child("teamName").setValue(rename)
            mDbref.child("Team").child(teamCode).child("TeamName").setValue(rename)
        }
    }
}