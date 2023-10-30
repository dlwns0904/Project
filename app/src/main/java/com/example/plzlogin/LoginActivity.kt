package com.example.plzlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.plzlogin.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding

    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 초기화
        mAuth = Firebase.auth

        // 로그인 버튼
        binding.btnLogin.setOnClickListener {
            val id = binding.edtId.text.toString().trim() // 트림붙여서 한번 해봐
            val password = binding.edtPassword.text.toString().trim()

            login(id,password)
        }



        // 회원가입
        // 액티비티 이동은 intent로 만들어
        // intent에 들어갈 것 -> 현재와 이동할 액티비티


        binding.btnSingup.setOnClickListener {
            val intent : Intent = Intent(this@LoginActivity, signupActivity::class.java)
            startActivity(intent)
            // signup 액티비티로 이동한다
        }
    }


    // 로그인
    private fun login(id : String, password : String){
        mAuth.signInWithEmailAndPassword(id, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 성공했을 때
                    val intent : Intent = Intent(this@LoginActivity, menu::class.java)
                    startActivity(intent)

                    Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
                    finish() // 액티비티 종료 함수

                } else {
                    // 실패?
                    Toast.makeText(this,"로그인이 실패하였습니다. 다시 시도해주세요",Toast.LENGTH_SHORT).show()
                    /*Log.d("Loing","Error :  ${task.exception}")*/

                }
            }

    }
}