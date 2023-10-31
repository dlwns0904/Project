package com.example.plzlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plzlogin.databinding.ActivityTimeSelectBinding

class TimeSelect : AppCompatActivity() {
    lateinit var binding: ActivityTimeSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimeSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}