package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.plzlogin.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class signupActivity : AppCompatActivity() {


    lateinit var binding : ActivitySignupBinding

    lateinit var mAuth : FirebaseAuth

    private lateinit var mDbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 초기화
        mAuth = Firebase.auth


        // db 초기화
        mDbref = Firebase.database.reference

        // 버튼
        binding.btnSignup.setOnClickListener {

            val name = binding.edtName.text.toString().trim()
            val id = binding.edtId.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            signup(name,id,password)
        }
    }

    private fun signup(name : String, id : String, password : String) {
        mAuth.createUserWithEmailAndPassword(id, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 성공
                    Toast.makeText(this,"회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()

                    // 로그인 성공하면 이제 다른 화면으로 넘어가기 설정
                    val intent : Intent = Intent(this@signupActivity,LoginActivity::class.java)
                    startActivity(intent)

                    // db에 저장하는 기능 추가
                    addusertodatabase(name,id,mAuth.currentUser?.uid!!) // 왜 ? 랑 !! 붙이는지


                } else {
                    // 실패
                    Toast.makeText(this,"회원가입 실패", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun addusertodatabase(name : String, id : String, uid : String){
        mDbref.child("user").child(uid).setValue(User(name, id, uid))
    }

}



