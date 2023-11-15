package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plzlogin.databinding.ActivityTimeSelectBinding

class TimeSelect : AppCompatActivity() {
    lateinit var binding: ActivityTimeSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val times = createTimes()
        //adapter 연결
        binding.recTimes.layoutManager = LinearLayoutManager(this)
        binding.recTimes.adapter = TimesAdapater(times)

        //클릭한 날짜 표시
        val date = intent.getStringExtra("Date")
        binding.TimeSelect.text = "${date} 시간 선택"

        binding.btnSave.setOnClickListener {

            val intent: Intent = Intent(this@TimeSelect,TeamMenuActivity::class.java)
            startActivity(intent)
        }
        //취소 클릭 시 되돌아가기
        binding.btnCancel.setOnClickListener {
            val intent: Intent = Intent(this@TimeSelect,TeamMenuActivity::class.java)
            startActivity(intent)
        }
    }

    //시간 Array 생성
    private fun createTimes(): Array<Time> {
        val times = Array<Time>(24) {
            Time(time = "${it}시 - ${it+1}시")
        }
        return times
    }

}