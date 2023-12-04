package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentJoinBinding
import com.example.plzlogin.viewmodel.TeamViewModel

class JoinFrag : Fragment() {

    private val teamViewModel = TeamViewModel()
    lateinit var binding : FragmentJoinBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_join, container, false)

        binding = FragmentJoinBinding.bind(view)

        binding.btnJoin.setOnClickListener {
            // 입력한 팀 코드
            val teamCode = binding.edtTeamcode.text.toString().trim()

                    // 이걸 한줄로 실행을 하는데 그러면 viewModel에서는 함수에서 조인이랑 에드팀을 같이 실행하게끔 하면 돼
            teamViewModel.joinTeam(teamCode)

            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            Frag.remove(this)
            Frag.commit()
        }
        return view
    }
}