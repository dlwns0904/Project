package com.example.plzlogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plzlogin.Team
import com.example.plzlogin.repository.TeamRepository
import kotlin.random.Random

class TeamViewModel : ViewModel() {

    private val teamRepository : TeamRepository = TeamRepository()
    private val _teamlist = MutableLiveData<List<Team>>()
    val teamlist: LiveData<List<Team>> get() = _teamlist
    init{
        loadteam()
    }
    fun loadteam(){
        teamRepository.observeTeam { teams ->
            _teamlist.value = teams
        }
    }

    fun createTeam( teamName : String, teamCode : String ){
        teamRepository.createTeam( teamName, teamCode )
        teamRepository.addTeam( teamName, teamCode )
    }

    fun joinTeam( teamCode : String ){
        teamRepository.joinTeam( teamCode )
        teamRepository.joinaddTeam( teamCode )
    }

    fun existTeam(teamCode: String, callback: (Int) -> Unit) {
        teamRepository.existTeam(teamCode) { result ->
            callback(result)
        }
    }

    fun reName(teamCode : String?, rename : String){
        teamRepository.reName( teamCode, rename )
    }

    fun removeTeam( teamCode : String ){
        teamRepository.removeTeam( teamCode )
    }

    fun RandomNumber(): String {
        var number = ""
        for (i in 0..5) {
            val addNum = Random.nextInt(10)
            number += addNum.toString()
        }
        return number
    }
}