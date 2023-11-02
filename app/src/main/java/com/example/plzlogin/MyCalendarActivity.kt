package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import com.example.plzlogin.databinding.ActivityMyCalendarBinding

class MyCalendarActivity : AppCompatActivity() {

    lateinit var binding : ActivityMyCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.Mycalendar.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            val selectedDate = "${year}년 ${month + 1}월 ${dayOfMonth}일"
            binding.tvDay.text = selectedDate // TextView에 날짜 표시
        }


        // 돌아가기 임시로
        binding.btnBack.setOnClickListener {
            val intent : Intent = Intent(this@MyCalendarActivity, menu::class.java)
            startActivity(intent)

        }


        // 추가적인 기능으로는 DB에 저장된 내 일정들을 불러와서 달력 커스텀이 가능해야하고
        // 아래 여백에는 다른 기능 추가할 거 더 고민해봐야 할 것 같아

    }
}