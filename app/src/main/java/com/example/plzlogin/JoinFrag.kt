package com.example.plzlogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plzlogin.databinding.FragmentCreateBinding
import com.example.plzlogin.databinding.FragmentJoinBinding

class JoinFrag : Fragment() {

    lateinit var binding : FragmentJoinBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_join, container, false)
        binding = FragmentJoinBinding.bind(view)


        // 참가 버튼 눌렀을 때
        binding.btnJoin.setOnClickListener {

            // 입력한 팀 코드
            val TeamCode = binding.edtTeamcode.text.toString().trim()



            // 다시 menu로 돌아가는거
            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            Frag.remove(this)
            Frag.commit()
        }

        return view
    }


    // 입력한 팀 코드가 DB에 있으면 가입하는 함수를 구현해야해
    private fun Join( TeamCode : Int){

    }
}