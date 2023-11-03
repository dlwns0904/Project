package com.example.plzlogin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plzlogin.databinding.ListTeamsBinding

// 왜 private val 일까?
class TeamApdater (private val context : Context, private val Teamlist : ArrayList<Team> )
        : RecyclerView.Adapter<TeamApdater.TeamHolder>() {


    // 레이아웃을 연결하는
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder {
        val binding = ListTeamsBinding.inflate(LayoutInflater.from(parent.context))


        val holder = TeamHolder(binding)
        return holder
        /*val view : View = LayoutInflater.from(context).
        inflate(R.layout.list_teams, parent, false)
        return TeamHolder(view)*/

    }

    // 실제 리스트의 개수를 반환해야
    override fun getItemCount(): Int {
        return Teamlist.size
    }

    // 데이터 연결
    override fun onBindViewHolder(holder: TeamHolder, position: Int) {
        val Teamname = Teamlist[position]
        // 아니지 팀네임 넣어야지
        // createfrag가서 수정해와

        // 팀네임 넣고
        holder.TeamnameText.text = Teamname.teamName

        // 이동액티비티
        holder.itemView.setOnClickListener {
            val intent = Intent(context,TeamMenuActivity::class.java)



            // 팀 이름을 넘겨줘 팀 메뉴 액티비티로
            // 팀 메뉴는 성훈이가 해줘
            intent.putExtra("TeamName",Teamname.teamName)
            context.startActivity(intent)

        }
    }

    class TeamHolder(private val binding : ListTeamsBinding/*itemView: ListTeamsBinding*/)
        : RecyclerView.ViewHolder(binding.root/*itemView*/){

        val TeamnameText : TextView = binding.txtTeamName1

        /*val Teamnametext : TextView = itemView.findViewById()*/
    }
}