package com.example.plzlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plzlogin.databinding.FragmentRenameBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class RenameFrag : Fragment() {

    lateinit var binding : FragmentRenameBinding

    lateinit var mAuth : FirebaseAuth
    lateinit var mDbref : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRenameBinding.inflate(inflater, container, false)
        val view = binding.root


        // 입력된 이름 가져오고


        mAuth = Firebase.auth
        mDbref = Firebase.database.reference

        val uid = mAuth.currentUser?.uid!!

        val TeamCode = arguments?.getString("TeamCode")

        // 수정누르면 이름 변경
        // USER - uid - TeamCode - teamName
        // Team - Teamcode - teamName
        binding.btnReName.setOnClickListener {
            val rename = binding.edtReTeamName.text.toString().trim()
            // 널 체크
            if (TeamCode != null) {
                mDbref.child("USER").child(uid).child(TeamCode).child("teamName").setValue(rename)
                mDbref.child("Team").child(TeamCode).child("TeamName").setValue(rename)
            }
            else{
                Toast.makeText(context,"숫자없음",Toast.LENGTH_SHORT).show()
            }


            Toast.makeText(context,"수정이 완료되었습니다",Toast.LENGTH_SHORT).show()

            val Frag = requireActivity().supportFragmentManager.beginTransaction()
            Frag.remove(this)
            Frag.commit()
        }


        return view
    }
}