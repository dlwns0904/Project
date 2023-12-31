package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentRenameBinding
import com.example.plzlogin.viewmodel.TeamViewModel

class RenameFrag : Fragment() {

    private val teamViewModel = TeamViewModel()
    lateinit var binding : FragmentRenameBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRenameBinding.inflate(inflater, container, false)
        val view = binding.root

        // 수정누르면 이름 변경 USER - uid - TeamCode - teamName Team - Teamcode - teamName

        binding.btnReName.setOnClickListener {

            val teamCode = arguments?.getString("TeamCode")
            val rename = binding.edtReTeamName.text.toString().trim()

            teamViewModel.reName(teamCode, rename)
            Toast.makeText(context,"수정이 완료되었습니다",Toast.LENGTH_SHORT).show()

            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            Frag.remove(this)
            Frag.commit()
        }
        return view
    }
}