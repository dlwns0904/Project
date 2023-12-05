package com.example.plzlogin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.CalendarView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.plzlogin.databinding.ActivityTeamMenuBinding
import com.example.plzlogin.viewmodel.TeamMenuViewModel
import androidx.recyclerview.widget.LinearLayoutManager

class TeamMenuActivity : AppCompatActivity() {


    // 해야 될 것
    // 달력 추가해야하고
    // 채팅 눌렀을 때, 게시판 눌렀을 때 화면 어떻게 나올지
    // 기능 구현은 나중에 하고 일단 화면 구성만


    lateinit var binding : ActivityTeamMenuBinding
    private val viewModel: TeamMenuViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val teamName = intent.getStringExtra("TeamName")
        val teamCode = intent.getStringExtra("TeamCode")
        val toolbar3 = binding.toolbar3

        val datesAdapater = DateAdapter(listOf())
        binding.recDates.layoutManager = LinearLayoutManager(this)
        binding.recDates.adapter = datesAdapater

        // 채팅 눌렀을 때
        binding.btnChat.setOnClickListener {
            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("TeamName", teamName)
            intent.putExtra("TeamCode", teamCode)
            startActivity(intent)
        }
        // 파일 눌렀을 때
        binding.btnFile.setOnClickListener {
            val intent: Intent = Intent(this@TeamMenuActivity,FileActivity::class.java)
            startActivity(intent)
        }

        //날짜 선택
        var cal : CalendarView = binding.Cal
        cal.minDate = System.currentTimeMillis()
        cal.maxDate = System.currentTimeMillis() + 864000000
        cal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = "${year}년${month + 1}월${dayOfMonth}일"
            viewModel.loadDates(teamCode!!, date)
            viewModel.dates.observe(this) {dates ->
                datesAdapater.setDate(dates)
            }
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
                intent.putExtra("Date", "${year}${month+1}${dayOfMonth}")
                intent.putExtra("TeamCode", teamCode)
                startActivity(intent)

            }

        }

        viewModel.dates.observe(this) {dates ->
            datesAdapater.setDate(dates)
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