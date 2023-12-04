package com.example.plzlogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.plzlogin.Team
import com.example.plzlogin.repository.TeamRepository

class TeamViewModel : ViewModel() {

    private val teamRepository : TeamRepository = TeamRepository()


    // ober 할 떄 그냥 파라미터로 넘겨서
    private val _teamlist = teamRepository.teamList
    val teamlist: LiveData<List<Team>> get() = _teamlist
    init{
        loadteam()
    }
    fun loadteam(){
        teamRepository.observeTeam()
        // teamlist를 viewModel에서 가져와야지
        // 어떻게 view를 바꾸는지에 대한 코드
    }

    fun createTeam(TeamName : String, TeamCode : String){
        teamRepository.createTeam(TeamName,TeamCode)
        teamRepository.addTeam(TeamName,TeamCode)
    }

    fun joinTeam(TeamCode : String){
        teamRepository.joinTeam(TeamCode)
        teamRepository.joinaddTeam(TeamCode)
    }

    fun existTeam(teamCode : String){
        teamRepository.existTeam(teamCode)
    }
    fun RandomNumber(): String {
        var Number = ""
        val Range = 0..9

        for (i in 0..5) {
            val Addnum = Range.random()
            Number += Addnum.toString()
        }
        return Number
    }
}