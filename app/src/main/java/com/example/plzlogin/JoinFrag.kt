package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentJoinBinding
import com.example.plzlogin.repository.TeamRepository
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class JoinFrag : Fragment() {


    private val teamRepository = TeamRepository()
    lateinit var binding : FragmentJoinBinding

    lateinit var mDbref : DatabaseReference

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_join, container, false)
        mDbref = Firebase.database.reference

        binding = FragmentJoinBinding.bind(view)

        // 참가 버튼
        binding.btnJoin.setOnClickListener {
            // 입력한 팀 코드
            val teamCode = binding.edtTeamcode.text.toString().trim()

            // DB에 일단 팀 코드 있는지 없는지 확인 있으면 함수 실행 없으면 메세지
            if (teamCode.length != 6){
                Toast.makeText(requireContext(),"6자리 숫자를 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else{

                val TeamRef = mDbref.child("Team")
                TeamRef.child(teamCode).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val dataSnapshot = task.result

                        // 데이터 있으면 팀 참가해주고
                        if (dataSnapshot.exists()) {

                            teamRepository.joinTeam(teamCode)
                            teamRepository.addTeam(teamCode)

                        } else {
                            Toast.makeText(context,"존재하지 않는 팀코드입니다",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            Frag.remove(this)
            Frag.commit()
        }
        return view
    }
}