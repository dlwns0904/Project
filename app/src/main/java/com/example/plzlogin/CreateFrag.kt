package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentCreateBinding
import com.example.plzlogin.repository.TeamRepository


class CreateFrag : Fragment() {

    lateinit var binding: FragmentCreateBinding
    private val teamRepository = TeamRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        binding = FragmentCreateBinding.bind(view) // 이 부분을 수정

        val teamCode = teamRepository.RandomNumber()
        // 팀 코드 보여주고
        binding.Teamnum.text = teamCode

        // 팀생성
        binding.btnTeamCreate.setOnClickListener {

            teamRepository.createTeam(binding,teamCode)
            teamRepository.addTeam(binding,teamCode)


            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            Frag.remove(this) // this@CreateFrag
            Frag.commit()
        }
        return view
    }
    data class User( var name : String?, var uid : String)
}
