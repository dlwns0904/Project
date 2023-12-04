package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentCreateBinding
import com.example.plzlogin.viewmodel.TeamViewModel

class CreateFrag : Fragment() {

    lateinit var binding: FragmentCreateBinding
    private val teamViewModel = TeamViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        binding = FragmentCreateBinding.bind(view) // 이 부분을 수정

        val teamCode = teamViewModel.RandomNumber()
        // 팀 코드 보여주고
        binding.Teamnum.text = teamCode

        // 팀생성
        binding.btnTeamCreate.setOnClickListener {
            // ui관련된 코드는 view에 있어야 한다
            // binding넘겨주면 안돼
            // teamVeiwModel.creaTeam() 을 해서 res에서 다시 createTeam()
            val teamName = binding.edtTeam.text.toString().trim()
            teamViewModel.createTeam(teamName, teamCode)


            // viewmodel livedata observe해서 바뀌는 것
            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            Frag.remove(this) // this@CreateFrag
            Frag.commit()
        }
        return view
    }
    data class User( var name : String?, var uid : String)
}
