package com.example.plzlogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.plzlogin.databinding.ListTeamsBinding
import com.example.plzlogin.viewmodel.TeamViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

// 왜 private val 일까?
class TeamApdater(private val context: Context, private val teamViewModel: TeamViewModel /*,private val Teamlist : ArrayList<Team>*/ )
        : RecyclerView.Adapter<TeamApdater.TeamHolder>() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDbref : DatabaseReference

    // 레이아웃을 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder {

        val binding = ListTeamsBinding.inflate(LayoutInflater.from(parent.context))
        val holder = TeamHolder(binding)

        return holder
    }
    override fun getItemCount(): Int {
        // 사이즈 null헤야해 안하면 오류
        return teamViewModel.teamlist.value?.size ?: 0
    }

    // 데이터 연결
    override fun onBindViewHolder(holder: TeamHolder, position: Int) {

        val team = teamViewModel.teamlist.value?.get(position)
        mAuth = Firebase.auth
        mDbref = Firebase.database.reference

        /*val uid = mAuth.currentUser?.uid!!*/

        val teamcode = team?.teamCode.toString()
        val teamName = team?.teamName

        /*var teamName: String? = null*/

        // 팀 이름이 수정이 되면 모든 팀원 팀 이름이 수정되어야 하기 때문에
        // 팀 이름을 가져 올 때 Team-TemaCode - TeamName에서 가져옴

        // 요거 viewModel로 넘겨줄 수 있지 않나

        holder.TeamnameText.text = team?.teamName

        /*val TeamNameRef = mDbref.child("Team").child(teamcode).child("TeamName")
        TeamNameRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val teamname = dataSnapshot.value as String?
                holder.TeamnameText.text = teamname
                teamName = teamname

            }
            override fun onCancelled(error: DatabaseError) {
                // 오류 처리를 여기에 추가
            }
        }
        )*/

        // 이동액티비티
        holder.itemView.setOnClickListener {
            notifyDataSetChanged()
            val intent = Intent(context,TeamMenuActivity::class.java)

            // 널 체크
            if (teamName != null) intent.putExtra("TeamName",teamName)
            intent.putExtra("TeamCode",teamcode)
            context.startActivity(intent)
        }

        holder.btnDel.setOnClickListener {
            //요거 삭제해도 되는지 보자

            teamViewModel.removeTeam( teamcode )
            notifyDataSetChanged()
            Toast.makeText(context,"팀이 삭제 되었습니다",Toast.LENGTH_SHORT).show()
        }

        // Frag로 팀코드 보내줘야 함
        holder.btnRename.setOnClickListener {

            val ReNameFrag : RenameFrag = RenameFrag()
            val bundle : Bundle = Bundle()
            bundle.putString("TeamCode",teamcode)

            ReNameFrag.arguments = bundle

            val frag = (context as menu).supportFragmentManager.beginTransaction()
            frag.replace(R.id.Menufrag, ReNameFrag).commit()
            notifyDataSetChanged()
        }
    }

    class TeamHolder(private val binding : ListTeamsBinding/*itemView: ListTeamsBinding*/)
        : RecyclerView.ViewHolder(binding.root/*itemView*/){

        val TeamnameText : TextView = binding.txtTeamName1
        val btnDel : Button = binding.btnDelete
        val btnRename : Button = binding.btnRename
    }
}
