package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentCreateBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class CreateFrag : Fragment() {



    lateinit var binding: FragmentCreateBinding

    private lateinit var mDbref : DatabaseReference

    lateinit var mAuth : FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        binding = FragmentCreateBinding.bind(view) // 이 부분을 수정

        // 팀 이름

        // 팀 코드는 랜덤한 숫자 6자리로?
        val Teamcode = Randomnumber()


        mAuth = Firebase.auth

        mDbref = Firebase.database.reference


        // 팀 코드 보여주고
        binding.Teamnum.text = Teamcode

        // 팀생성하기 누르면
        binding.btnTeamcreate.setOnClickListener {
            CreateTeam(Teamcode)
            AddTeam(Teamcode)


            // 다시 메뉴로 돌아가기
            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            Frag.remove(this) // this@CreateFrag
            Frag.commit()

        }
        return view
    }


    // 팀 생성
    private fun CreateTeam(TeamCode: String) {

        val TeamName = binding.edtTeam.text.toString().trim()

        val TeamRef = mDbref.child("Team").child(TeamCode)

        TeamRef.setValue(TeamCode)
        TeamRef.child("TeamName").setValue(TeamName)

        // Uesr에 새로운 uid 넣고 팀네임
        // 아니지 팀네임 말고 다른 걸 해야하나
        // 일단 팀코드 넣고 한번 해볼까


        // DB에서 user에 저장된 name을 가져와
        val UsernameRef = mDbref.child("user").child(mAuth.currentUser?.uid!!).child("name")
        UsernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val Username = dataSnapshot.value as String?
                TeamRef.child(mAuth.currentUser?.uid!!).setValue(User( Username, mAuth.currentUser?.uid!! ))

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터베이스에서 데이터를 가져오는 중 오류가 발생한 경우 처리할 내용을 추가
                Toast.makeText(requireContext(), "데이터를 가져오는 중 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 내 uid에 팀 코드랑 팀이름 추가
    private fun AddTeam(TeamCode: String){
        val TeamName = binding.edtTeam.text.toString().trim()
        val TeamRef = mDbref.child("USER").child(mAuth.currentUser?.uid!!).child(TeamCode)
        /*val newTeamRef = TeamRef.push()*/
        TeamRef.setValue(Team(TeamName,TeamCode))
    }


    data class User(
        var name : String?,
        var uid : String
    ){
        constructor() : this( "", "" ) // 주생성자로 바꿀 수 있지 않나?
    }


    // 랜덤한 팀 코드 숫자 생성
    private fun Randomnumber(): String {
        var Number = ""
        val Range = 0..9

        for (i in 0..5) {
            val Addnum = Range.random()
            Number += Addnum.toString()
        }
        return Number
    }
}



/*
class CreateFrag : Fragment() {

    lateinit var binding : FragmentCreateBinding
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_create, container, false)

        binding = FragmentCreateBinding.inflate(layoutInflater)

        // 팀 이름
        val Teamname = binding.edtTeam.text.toString().trim()

        // 팀코드는 랜덤한 숫자 6자리로?
        val Teamcode = Randomnumber()

        // 팀 코드 보여주고
        binding.Teamnum.setText(Teamcode)

        // 팀생성하기 누르면
        binding.btnTeamcreate.setOnClickListener{
            CreateTeam( Teamname , Teamcode)
        }

    return view
    }


    private fun CreateTeam( Teamname : String, Teamcode : String){

    }

    // 랜덤한 팀 코드 숫자 생성
    private fun Randomnumber() : String {

        var Number = ""
        val Range = 0..9

        for ( i in 0..5){
            val Addnum = Range.random()
            Number += Addnum.toString()
        }
        return Number
    }
}*/
