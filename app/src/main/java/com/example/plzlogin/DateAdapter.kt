package com.example.plzlogin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plzlogin.databinding.ListDatesBinding

class DateAdapter(private var dates: List<Date>): RecyclerView.Adapter<DateAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListDatesBinding.inflate((LayoutInflater.from(parent.context)))
        val holder = Holder(binding)
        return holder
    }

    override fun getItemCount() = dates.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(dates[position])
    }

    class Holder(private val binding: ListDatesBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(date: Date) {
            for (idx in 0 until 14) {
                when (idx) {
                    0 -> binding.checkbox1.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    1 -> binding.checkbox2.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    2 -> binding.checkbox3.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    3 -> binding.checkbox4.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    4 -> binding.checkbox5.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    5 -> binding.checkbox6.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    6 -> binding.checkbox7.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    7 -> binding.checkbox8.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    8 -> binding.checkbox9.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    9 -> binding.checkbox10.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    10 -> binding.checkbox11.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    11 -> binding.checkbox12.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    12 -> binding.checkbox13.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )

                    13 -> binding.checkbox14.setImageResource(
                        when (date.times[idx].selected) {
                            false -> R.drawable.white_square
                            true -> R.drawable.blue_square
                        }
                    )
                }
            }
            binding.name.text = date.name
        }
    }
    fun setDate(newDates: List<Date>) {
        dates = newDates
        notifyDataSetChanged()

    }
}