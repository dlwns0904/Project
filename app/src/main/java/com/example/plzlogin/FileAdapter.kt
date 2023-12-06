package com.example.plzlogin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FileAdapter : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private val fileList = mutableListOf<File>()

    fun setFiles(newFiles: List<File>) {
        fileList.clear()
        fileList.addAll(newFiles)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(fileList[position])


    }
    override fun getItemCount(): Int {
        return fileList.size
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(file: File) {
            itemView.findViewById<TextView>(R.id.tvFileName).text = file.fileName
            Glide.with(itemView.context)
                .load(file.fileUri)
                .into(itemView.findViewById<ImageView>(R.id.imgFileIcon))
        }
    }

}
