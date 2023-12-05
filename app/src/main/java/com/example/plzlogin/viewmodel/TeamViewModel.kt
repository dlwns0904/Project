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

    // 이거 바꿔야 하고
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