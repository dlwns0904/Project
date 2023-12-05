package com.example.plzlogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plzlogin.Time
import com.example.plzlogin.repository.TimeSelectRepository


class TimeSelectViewModel : ViewModel() {

    private val timeSelectRepository : TimeSelectRepository = TimeSelectRepository()

    private val _timeList = MutableLiveData<List<Time>>()
    val timeList: LiveData<List<Time>> get() = _timeList
    fun loadTimes(teamCode: String, date: String) {
        timeSelectRepository.loadTimes(teamCode, date) { timeList ->
            _timeList.value = timeList
        }
    }

    fun saveTimes(teamCode: String, date: String, times: List<Time>) {
        timeSelectRepository.saveTimes(teamCode, date, times)
    }
}