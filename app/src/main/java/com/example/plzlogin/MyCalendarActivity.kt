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
            val selectedDate = "${year}${String.format("%02d", month + 1)}${String.format("%02d", dayOfMonth)}"
            scheduleViewModel.loadschedule(selectedDate)
        }

        binding.btnBack.setOnClickListener {
            val intent: Intent = Intent(this@MyCalendarActivity, menu::class.java)
            startActivity(intent)
        }

        scheduleViewModel.schedulelist.observe(this) { schedules ->
            schedulelist.clear()
            schedulelist.addAll(schedules)
            adapter2.notifyDataSetChanged()
        }
    }
}
/*
class MyCalendarActivity : AppCompatActivity() {

    lateinit var adapter2 : ScheduleAdapter
    lateinit var binding : ActivityMyCalendarBinding

    private val scheduleViewModel = ScheduleViewModel()
    private lateinit var schedulelist : ArrayList<Meet>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        schedulelist = ArrayList()
        adapter2 = ScheduleAdapter(this, scheduleViewModel)


        binding.recSch.layoutManager = LinearLayoutManager(this)
        binding.recSch.adapter = adapter2

        binding.Mycalendar.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            // val selectedDate = "${year}년 ${month + 1}월 ${dayOfMonth}일" // 굳이 var ?
            binding.tvDay.text = "${year}년 ${month + 1}월 ${dayOfMonth}일" // TextView에 날짜 표시
        }

        binding.Mycalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            scheduleViewModel.
        }

        // 돌아가기 임시로
        binding.btnBack.setOnClickListener {
            val intent : Intent = Intent(this@MyCalendarActivity, menu::class.java)
            startActivity(intent)

        }


        scheduleViewModel.schedulelist.observe(this){ schedules ->
                schedulelist.clear()
                schedulelist.addAll(schedules)
                adapter2.notifyDataSetChanged()
        }

        // 추가적인 기능으로는 DB에 저장된 내 일정들을 불러와서 달력 커스텀이 가능해야하고
        // 아래 여백에는 다른 기능 추가할 거 더 고민해봐야 할 것 같아

    }
}*/
