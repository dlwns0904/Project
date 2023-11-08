package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentJoinBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class JoinFrag : Fragment() {

    lateinit var binding : FragmentJoinBinding

    lateinit var mAuth : FirebaseAuth
    lateinit var mDbref : DatabaseReference

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        mAuth = Firebase.auth

        mDbref = Firebase.database.reference

        val view = inflater.inflate(R.layout.fragment_join, container, false)
        binding = FragmentJoinBinding.bind(view)


        // 참가 버튼 눌렀을 때
        binding.btnJoin.setOnClickListener {
                        // 입력한 팀 코드
            val TeamCode = binding.edtTeamcode.text.toString().trim()

            // DB에 일단 팀 코드 있는지 없는지 확인 있으면 함수 실행 없으면 메세지

            val TeamRef = mDbref.child("Team")
            TeamRef.child(TeamCode).get().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val dataSnapshot = task.result
                    // 데이터 있으면 팀 참가해주고
                    if (dataSnapshot.exists())
                    {
                        Join(TeamCode)
                        AddTeam(TeamCode)
                    }
                    else{
                        /*Toast.makeText(requireActivity(),"존재하지 않는 코드입니다",Toast.LENGTH_SHORT).show()*/
                    }
                }
            }

            /*Join(TeamCode)
            AddTeam(TeamCode)*/


            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            // 다시 menu로 돌아가는거
            Frag.remove(this)
            Frag.commit()
        }

        return view
    }


    // 입력한 팀 코드가 DB에 있으면 가입하는 함수를 구현해야해
    private fun Join( TeamCode : String){

        val TeamRef = mDbref.child("Team").child(TeamCode)

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
                // 데이터베이스에서 데이터를 가져오는 중 오류가 발생한 경우 처리할 내용을 추가
                Toast.makeText(requireContext(), "데이터를 가져오는 중 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun AddTeam(TeamCode: String){
        // 팀네임을 가져와야 겠지?
        val TeamRef = mDbref.child("Team").child(TeamCode).child("TeamName")
        TeamRef.addListenerForSingleValueEvent( object : ValueEventListener{override fun onDataChange(dataSnapshot: DataSnapshot) {
            val TeamName = dataSnapshot.value as String?
            val TeamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(TeamCode)
            // 팀 네임 , 팀 코드 DB에 저장
            TeamRef.setValue(Team(TeamName,TeamCode))
        }
            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터베이스에서 데이터를 가져오는 중 오류가 발생한 경우 처리할 내용을 추가
                Toast.makeText(requireContext(), "데이터를 가져오는 중 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }
}