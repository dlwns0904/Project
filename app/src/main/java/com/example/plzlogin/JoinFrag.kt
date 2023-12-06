package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentJoinBinding
import com.example.plzlogin.viewmodel.TeamViewModel
class JoinFrag : Fragment() {

    private val teamViewModel = TeamViewModel()
    lateinit var binding: FragmentJoinBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_join, container, false)
        binding = FragmentJoinBinding.bind(view)

        binding.btnJoin.setOnClickListener {
            val teamCode = binding.edtTeamcode.text.toString().trim()

            // context에 연결되어 있는지 확인해봐
            context?.let {
                teamViewModel.existTeam(teamCode) { result ->
                    when (result) {
                        0 -> Toast.makeText(it, "6자리 숫자를 입력해주세요", Toast.LENGTH_SHORT).show()
                        1 -> teamViewModel.joinTeam(teamCode)
                        2 -> Toast.makeText(it, "팀이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
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