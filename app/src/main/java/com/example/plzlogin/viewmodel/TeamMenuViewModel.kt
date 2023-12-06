package com.example.plzlogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plzlogin.Date
import com.example.plzlogin.repository.TeamMenuRepository

class TeamMenuViewModel : ViewModel(){
    private val teamMenuRepository : TeamMenuRepository = TeamMenuRepository()

    private val _dates = MutableLiveData<List<Date>>()
    val dates: LiveData<List<Date>> get() = _dates

    fun loadDates(teamCode: String, date: String) {
        teamMenuRepository.loadDates(teamCode, date) {datesList ->
            _dates.value = emptyList()
            _dates.value = datesList
        }
    }
}