package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.CalendarView
import com.example.plzlogin.databinding.ActivityMenuBinding
import com.example.plzlogin.databinding.ActivityTeamMenuBinding

class TeamMenuActivity : AppCompatActivity() {


    // 해야 될 것
    // 달력 추가해야하고
    // 채팅 눌렀을 때, 게시판 눌렀을 때 화면 어떻게 나올지
    // 기능 구현은 나중에 하고 일단 화면 구성만


    lateinit var binding : ActivityTeamMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val teamName = intent.getStringExtra("TeamName")
        val teamCode = intent.getStringExtra("TeamCode")
        val toolbar3 = binding.toolbar3


        // 채팅 눌렀을 때
        // 추가해줘 >> 완료
        binding.btnChat.setOnClickListener {
            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("TeamName", teamName)
            intent.putExtra("TeamCode", teamCode)
            startActivity(intent)
        }
        // 게시판 눌렀을 때
        // 추가해줘
        binding.btnBoard.setOnClickListener {
            val intent: Intent = Intent(this@TeamMenuActivity,Vote::class.java)
            startActivity(intent)
        }

        //날짜 선택
        var cal : CalendarView = findViewById(R.id.Cal)
        cal.minDate = System.currentTimeMillis()
        cal.maxDate = System.currentTimeMillis() + 864000000
        cal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var date = "${year}년${month + 1}월${dayOfMonth}일"
            binding.CalTxt.text = "${date} 시간 선택"
            binding.btnMeet.text = "${date} 회의 일정"
            //시간 선택
            binding.CalTxt.setOnClickListener {
                val intent : Intent = Intent(this@TeamMenuActivity,TimeSelect::class.java)
                intent.putExtra("Date", date)
                intent.putExtra("TeamCode", teamCode)
                startActivity(intent)
            }

            binding.btnMeet.setOnClickListener {
                val intent : Intent = Intent(this@TeamMenuActivity,MeetConfirm::class.java)
                intent.putExtra("Date", date)
                intent.putExtra("TeamCode", teamCode)
                startActivity(intent)
            }
        }


        setSupportActionBar(toolbar3)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = teamName
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