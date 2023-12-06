package com.example.plzlogin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plzlogin.databinding.ActivityFileBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileBinding
    private lateinit var fileAdapter: FileAdapter

    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val teamName = intent.getStringExtra("TeamName")
        val teamCode = intent.getStringExtra("TeamCode").toString()

        val toolbar = binding.includeToolbar.toolbar
        val btnUp = binding.includeToolbar.btnMenu2
        val btnDown = binding.includeToolbar.btnMenu

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = teamName
        btnUp.setImageResource(R.drawable.ic_up)
        btnDown.setImageResource(R.drawable.ic_down)

        btnUp.setOnClickListener {
            if (fileAdapter.itemCount > 0) {
                binding.recyclerFile.smoothScrollToPosition(0)
            }
        }
        btnDown.setOnClickListener {
            val itemCount = fileAdapter.itemCount
            if (itemCount > 0) {
                binding.recyclerFile.smoothScrollToPosition(itemCount - 1)
            }
        }


        fileAdapter = FileAdapter()
        binding.recyclerFile.layoutManager = LinearLayoutManager(this)
        binding.recyclerFile.adapter = fileAdapter

        binding.btnUploadFile.setOnClickListener {
            launchFilePicker()
        }

        loadData(teamCode)



    }


    private val fileSelectionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { fileUri ->
                    uploadFile(fileUri)
                }
            }
        }

    private fun launchFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        fileSelectionLauncher.launch(intent)
    }


    // 프로그래스바 보이기
    private fun showProgressBar() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Uploading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        blockLayoutTouch()
    }

    // 프로그래스바 숨기기
    private fun hideProgressBar() {
        progressDialog?.dismiss()
        progressDialog = null
        clearBlockLayoutTouch()
    }

    // 화면 터치 막기
    private fun blockLayoutTouch() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // 화면 터치 풀기
    private fun clearBlockLayoutTouch() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private fun uploadFile(fileUri: Uri) {
        showProgressBar()
        val fileName = getFileName(fileUri)
        val teamCode = intent.getStringExtra("TeamCode").toString()
        val teamCodePath = "File/$teamCode"

        val fileRef = storageRef.child(fileName)
        fileRef.putFile(fileUri)
            .addOnSuccessListener { _ ->
                fileRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val file = File(fileName, uri.toString())

                        // 팀 코드에 해당하는 경로에도 파일 정보 저장
                        FirebaseDatabase.getInstance().getReference(teamCodePath).push()
                            .setValue(file)

                        Toast.makeText(this, "파일 업로드 성공", Toast.LENGTH_SHORT).show()

                        // 파일 업로드 성공 후 리사이클러뷰를 맨 아래로 스크롤
                        hideProgressBar()
                        binding.recyclerFile.postDelayed({
                            binding.recyclerFile.scrollToPosition(fileAdapter.itemCount - 1)
                        }, 500)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "다운로드 URL 획득 실패", Toast.LENGTH_SHORT).show()
                        hideProgressBar()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "파일 업로드 실패", Toast.LENGTH_SHORT).show()
                hideProgressBar()
            }
    }

    private fun loadData(teamCode: String) {
        val teamCodePath = "File/$teamCode"

        FirebaseDatabase.getInstance().getReference(teamCodePath)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val filesList = mutableListOf<File>()

                    for (data in snapshot.children) {
                        val fileName = data.child("fileName").getValue(String::class.java)
                        val fileUri = data.child("fileUri").getValue(String::class.java)

                        if (fileName != null && fileUri != null) {
                            val file = File(fileName, fileUri)
                            filesList.add(file)
                        }
                    }

                    fileAdapter.setFiles(filesList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@FileActivity, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }


    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String {
        return contentResolver.query(uri, null, null, null, null)?.use {
            it.moveToFirst()
            val displayName = it.getString(it.getColumnIndex("_display_name"))
            it.close()
            displayName ?: "Unknown_File"
        } ?: "Unknown_File"
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
