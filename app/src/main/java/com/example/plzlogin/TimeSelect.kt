package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plzlogin.databinding.ActivityTimeSelectBinding

class TimeSelect : AppCompatActivity() {
    lateinit var binding: ActivityTimeSelectBinding
    //시간 리스트
    val times = arrayOf(
        Time("00시 - 01시"),
        Time("01시 - 02시"),
        Time("02시 - 03시"),
        Time("03시 - 04시"),
        Time("04시 - 05시"),
        Time("05시 - 06시"),
        Time("06시 - 07시"),
        Time("07시 - 08시"),
        Time("08시 - 09시"),
        Time("09시 - 10시"),
        Time("10시 - 11시"),
        Time("11시 - 12시"),
        Time("12시 - 13시"),
        Time("13시 - 14시"),
        Time("14시 - 15시"),
        Time("15시 - 16시"),
        Time("16시 - 17시"),
        Time("17시 - 18시"),
        Time("18시 - 19시"),
        Time("19시 - 20시"),
        Time("20시 - 21시"),
        Time("21시 - 22시"),
        Time("22시 - 23시"),
        Time("23시 - 24시")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //adapter 연결
        binding.recTimes.layoutManager = LinearLayoutManager(this)
        binding.recTimes.adapter = TimesAdapater(times)

        val date = intent.getStringExtra("Date")
        binding.TimeSelect.text = "${date} 시간 선택"

        //취소 클릭 시 되돌아가기
        binding.btnCancel.setOnClickListener {
            val intent: Intent = Intent(this@TimeSelect,TeamMenuActivity::class.java)
            startActivity(intent)
        }
    }
}