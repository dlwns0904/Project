package com.example.plzlogin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plzlogin.databinding.ListTimesBinding

class TimesAdapater(val times: List<Time>): RecyclerView.Adapter<TimesAdapater.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListTimesBinding.inflate(LayoutInflater.from(parent.context))
        val holder = Holder(binding)
        return holder
    }

    override fun getItemCount() = times.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(times[position])
    }
    class Holder(private val binding: ListTimesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(time: Time) {
            binding.checkTime.text = time.time

            binding.checkTime.isChecked = time.isSelected
            binding.checkTime.setOnCheckedChangeListener { _, isChecked -> time.isSelected = isChecked
            }
        }
    }
}