package com.example.plzlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView

class MyCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_calendar)

        val calendarView = findViewById<CalendarView>(R.id.Mycalendar)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // 선택한 날짜의 년, 월, 일을 처리
            val selectedDate = "${year}년 ${month + 1}월 ${dayOfMonth}일"

            // TextView에 날짜 표시
            val textView = findViewById<TextView>(R.id.tv_day)
            textView.text = selectedDate
        }

        // 추가적인 기능으로는 DB에 저장된 내 일정들을 불러와서 달력 커스텀이 가능해야하고
        // 아래 여백에는 다른 기능 추가할 거 더 고민해봐야 할 것 같아

    }
}