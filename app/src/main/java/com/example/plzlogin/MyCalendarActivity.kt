package com.example.plzlogin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plzlogin.databinding.ActivityMyCalendarBinding
import com.example.plzlogin.viewmodel.ScheduleViewModel

class MyCalendarActivity : AppCompatActivity() {

    lateinit var adapter2: ScheduleAdapter
    lateinit var binding: ActivityMyCalendarBinding

    private val scheduleViewModel = ScheduleViewModel()
    private lateinit var schedulelist: ArrayList<Meet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        schedulelist = ArrayList()
        adapter2 = ScheduleAdapter(this, scheduleViewModel)

        binding.recSch.layoutManager = LinearLayoutManager(this)
        binding.recSch.adapter = adapter2

        binding.Mycalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "${year}${String.format("%02d", month + 1)}${String.format("%d", dayOfMonth)}"
            scheduleViewModel.observeSchedule(selectedDate)
            adapter2.notifyDataSetChanged()
        }

        binding.btnBack.setOnClickListener {
            val intent: Intent = Intent(this@MyCalendarActivity, menu::class.java)
            startActivity(intent)
        }

        scheduleViewModel.meetlist.observe(this) { meets ->

            schedulelist.clear()
            schedulelist.addAll(meets)
            adapter2.notifyDataSetChanged()
        }
    }
}

