package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plzlogin.databinding.ActivityTimeSelectBinding
import com.example.plzlogin.viewmodel.TimeSelectViewModel


class TimeSelect : AppCompatActivity() {
    private val viewModel: TimeSelectViewModel by viewModels()
    lateinit var binding: ActivityTimeSelectBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar4 = binding.toolbar4
        val teamCode = intent.getStringExtra("TeamCode").toString()
        val date = intent.getStringExtra("Date").toString()

        val timesAdapater = TimesAdapater(listOf())
        binding.recTimes.layoutManager = LinearLayoutManager(this)
        binding.recTimes.adapter = timesAdapater
        viewModel.loadTimes(teamCode, date)
        viewModel.timeList.observe(this) { times ->
            timesAdapater.setTime(times)
        }


        //툴바에 클릭한 날짜 표시
        setSupportActionBar(toolbar4)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "${date} 시간 선택"


       binding.btnSave.setOnClickListener {
            //날짜에 시간 저장
            viewModel.saveTimes(teamCode, date, viewModel.timeList.value!!)
            finish()
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}