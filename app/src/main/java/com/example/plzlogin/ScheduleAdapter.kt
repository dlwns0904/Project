package com.example.plzlogin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plzlogin.databinding.ListScheduleBinding
import com.example.plzlogin.viewmodel.ScheduleViewModel

class ScheduleAdapter(private val context : Context, private val scheduleViewModel: ScheduleViewModel )
    : RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleHolder {
        val binding = ListScheduleBinding.inflate(LayoutInflater.from(parent.context))
        val holder = ScheduleHolder(binding)

        return holder
    }

    override fun onBindViewHolder(holder: ScheduleHolder, position: Int) {

        val schedule = scheduleViewModel.schedulelist.value?.get(position)


        holder.purpose.text = schedule?.meetTitle
        holder.starttime.text = schedule?.startTime
        holder.endtime.text = schedule?.endTime
        holder.place.text = schedule?.meetPlace
    }

    override fun getItemCount(): Int {
        return scheduleViewModel.schedulelist.value?.size ?: 0
    }


    class ScheduleHolder(private val binding : ListScheduleBinding)
        : RecyclerView.ViewHolder(binding.root){

        val purpose : TextView = binding.txtPurpose
        val starttime : TextView = binding.txtStarttime
        val endtime : TextView = binding.txtEndtime
        val place : TextView = binding.txtPlace

    }
}

