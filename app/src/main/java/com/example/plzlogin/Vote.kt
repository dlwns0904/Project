package com.example.plzlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plzlogin.databinding.ActivityVoteBinding

class Vote : AppCompatActivity() {
    lateinit var binding: ActivityVoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            supportFragmentManager.beginTransaction().run {
                replace(binding.Votefrag.id, VoteFrag())
                commit()
            }
        }
    }
}