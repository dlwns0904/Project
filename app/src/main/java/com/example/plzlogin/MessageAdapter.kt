package com.example.plzlogin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plzlogin.databinding.DateReceiveBinding
import com.example.plzlogin.databinding.DateSendBinding
import com.example.plzlogin.databinding.ReceiveBinding
import com.example.plzlogin.databinding.SendBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MessageAdapter(private var context: Context, private var messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val receive = 1 // 받는 타입
    private val send = 2 // 보내는 타입
    private val dateSend = 3 // 날짜 표시 + 보내는 타입
    private val dateReceive = 4 // 날짜 표시 + 받는 타입

    private val currentUid = FirebaseAuth.getInstance().currentUser?.uid

    class SendViewHolder(binding: SendBinding) : RecyclerView.ViewHolder(binding.root) {
        val sendMessage: TextView = binding.tvSenderMessage
        val sendTimestamp: TextView = binding.tvSenderTime
    }
    class ReceiveViewHolder(binding: ReceiveBinding) : RecyclerView.ViewHolder(binding.root) {
        val receiveMessage: TextView = binding.tvReceiverMessage
        val receiverName: TextView = binding.tvReceiver
        val receiveTimestamp: TextView = binding.tvReceiverTime
    }

    class DateSendViewHolder(binding: DateSendBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateSendTextView: TextView = binding.tvDateS
        val dateSenderMessage: TextView = binding.tvDateSenderMessage
        val dateSenderTime: TextView = binding.tvDateSenderTime
    }

    class DateReceiveViewHolder(binding: DateReceiveBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateReceiveTextView: TextView = binding.tvDateR
        val dateReceiveMessage: TextView = binding.tvDateReceiverMessage
        val dateReceiverName: TextView = binding.tvDateReceiver
        val dateReceiveTimestamp: TextView = binding.tvDateReceiverTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            receive -> ReceiveViewHolder(ReceiveBinding.inflate(inflater, parent, false))
            send -> SendViewHolder(SendBinding.inflate(inflater, parent, false))
            dateSend -> DateSendViewHolder(DateSendBinding.inflate(inflater, parent, false))
            dateReceive -> DateReceiveViewHolder(DateReceiveBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when (holder) {
            is DateSendViewHolder -> {
                holder.dateSendTextView.text = getDate(currentMessage.timestamp)
                holder.dateSenderMessage.text = currentMessage.message
                holder.dateSenderTime.text = getTime(currentMessage.timestamp)
            }
            is SendViewHolder -> {
                holder.sendMessage.text = currentMessage.message
                holder.sendTimestamp.text = getTime(currentMessage.timestamp)
            }
            is DateReceiveViewHolder -> {
                holder.dateReceiveTextView.text = getDate(currentMessage.timestamp)
                holder.dateReceiveMessage.text = currentMessage.message
                holder.dateReceiverName.text = currentMessage.senderName
                holder.dateReceiveTimestamp.text = getTime(currentMessage.timestamp)
            }
            is ReceiveViewHolder -> {
                holder.receiveMessage.text = currentMessage.message
                holder.receiverName.text = currentMessage.senderName
                holder.receiveTimestamp.text = getTime(currentMessage.timestamp)
            }
        }
    }


    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position] // 메세지 값
        return if (isDateChanged(position)) {
            if (currentUid == currentMessage.senderId) {
                dateSend
            } else {
                dateReceive
            }
        } else {
            if (currentUid == (currentMessage.senderId)) {
                send
            } else {
                receive
            }
        }
    }


    private fun isDateChanged(position: Int): Boolean {
        if (position == 0) { // 첫 번째 메세지는 항상 날짜가 바뀌는 것으로 처리
            Log.d("DateDebug", "첫 번째 메시지, 항상 날짜 변경.")
            return true
        }
        val currentMessage = messageList[position]
        val previousMessage = messageList[position - 1]

        val cal1 = Calendar.getInstance().apply {
            timeInMillis = currentMessage.timestamp ?: 0
        }

        val cal2 = Calendar.getInstance().apply {
            timeInMillis = previousMessage.timestamp ?: 0
        }

        return !isSameDate(cal1, cal2)
    }

    private fun isSameDate(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.MONTH] == cal2[Calendar.MONTH]
                && cal1[Calendar.DATE] == cal2[Calendar.DATE]
    }



    private fun getTime(timestamp: Long?): String {
        if (timestamp == null) return ""

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        val amPm: String = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "오전" else "오후"
        val hour: Int = calendar.get(Calendar.HOUR)
        val minute: String = String.format("%02d", calendar.get(Calendar.MINUTE))

        return "$amPm $hour:$minute"
    }

    private fun getDate(timestamp: Long?): String {
        if (timestamp == null) return ""

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        return sdf.format(calendar.time)
    }



    fun updateMessages(newMessages: List<Message>) {
        messageList.clear()
        messageList.addAll(newMessages)
        notifyDataSetChanged()
    }


}
