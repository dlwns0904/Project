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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// 왜 private val 일까?
class TeamApdater (private val context : Context, private val Teamlist : ArrayList<Team> )
        : RecyclerView.Adapter<TeamApdater.TeamHolder>() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDbref : DatabaseReference

    // 레이아웃을 연결하는
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder {
        val binding = ListTeamsBinding.inflate(LayoutInflater.from(parent.context))


        val holder = TeamHolder(binding)
        return holder


    }

    // 실제 리스트의 개수를 반환해야
    override fun getItemCount(): Int {
        return Teamlist.size
    }

    // 데이터 연결
    override fun onBindViewHolder(holder: TeamHolder, position: Int) {
        val Team = Teamlist[position]
        // 아니지 팀네임 넣어야지
        // createfrag가서 수정해와

        mAuth = Firebase.auth

        mDbref = Firebase.database.reference

        val uid = mAuth.currentUser?.uid!!


        val TeamCode = Team.teamCode.toString()
        var TeamName : String? = null


        // 팀 이름이 수정이 되면 모든 팀원 팀 이름이 수정되어야 하기 때문에
        // 팀 이름을 가져 올 때 Team-TemaCode - TeamName에서 가져옴

        val TeamNameRef = mDbref.child("Team").child(TeamCode).child("TeamName")
        TeamNameRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                TeamName = dataSnapshot.value as String?
                holder.TeamnameText.text = TeamName

            }
            override fun onCancelled(error: DatabaseError) {
                // 오류 처리를 여기에 추가
            }
        }
        )

        // 팀네임 넣고
        /*holder.TeamnameText.text = */

        // 이동액티비티
        holder.itemView.setOnClickListener {
            notifyDataSetChanged()
            val intent = Intent(context,TeamMenuActivity::class.java)



            // 팀 이름이랑 팀네임 넘겨주기 을 넘겨줘 팀 메뉴 액티비티로
            // 팀 메뉴는 성훈이가 해줘

            // 널 체크
            if (TeamName != null) intent.putExtra("TeamName",TeamName)
            intent.putExtra("TeamCode",TeamCode)
            context.startActivity(intent)
        }




        // 삭제 누르면 팀 삭제
        // 이미지 추가 해야해

        holder.btnDel.setOnClickListener {

            mDbref.child("USER").child(uid).child(TeamCode).removeValue()
            mDbref.child("Team").child(TeamCode).child(uid).removeValue()

            notifyDataSetChanged()
            Toast.makeText(context,"팀이 삭제 되었습니다",Toast.LENGTH_SHORT).show()
        }

        // 이름 수정
        // Frag로 팀코드 보내줘야 함
        holder.btnRename.setOnClickListener {

            // 숫자를 왜 안가지고 오니?
            val ReNameFrag : RenameFrag = RenameFrag()
            val bundle : Bundle = Bundle()
            bundle.putString("TeamCode",TeamCode)


            ReNameFrag.arguments = bundle

            val frag = (context as menu).supportFragmentManager.beginTransaction()
            frag.replace(R.id.Menufrag, ReNameFrag).commit()


            notifyDataSetChanged()
            // 토스트 메세지도 추가?ㅅ
        }
    }

    class TeamHolder(private val binding : ListTeamsBinding/*itemView: ListTeamsBinding*/)
        : RecyclerView.ViewHolder(binding.root/*itemView*/){

        val TeamnameText : TextView = binding.txtTeamName1
        val btnDel : Button = binding.btnDelete
        val btnRename : Button = binding.btnRename
    }
}