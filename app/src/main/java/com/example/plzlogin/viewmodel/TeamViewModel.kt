package com.example.plzlogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.plzlogin.Team
import com.example.plzlogin.repository.TeamRepository

class TeamViewModel : ViewModel() {

    private val teamRepository : TeamRepository = TeamRepository()

    private val _teamlist = teamRepository.teamList
    val teamlist: LiveData<List<Team>> get() = _teamlist
    init{
        loadteam()
    }
    fun loadteam(){
        teamRepository.observeTeam()
    }
}