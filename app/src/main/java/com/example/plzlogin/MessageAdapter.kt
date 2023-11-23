package com.example.plzlogin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MessageAdapter(private var context: Context, private var messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val receive = 1 // 받는 타입
    private val send = 2 // 보내는 타입
    private val date = 3 // 날짜 표시 뷰 타입
    private var searchKeyword: String = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            receive -> { // 받는 화면
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
                ReceiveViewHolder(view)
            }

            send -> { // 보내는 화면
                val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
                SendViewHolder(view)
            }

            else -> { // 날짜 표시 뷰
                val view: View = LayoutInflater.from(context).inflate(R.layout.date, parent, false)
                DateViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position] // 현재 메세지
        //Log.d("FilterDebug", "Search keyword: $searchKeyword, Message: ${currentMessage.message}")


        if (holder.javaClass == DateViewHolder::class.java) {
            Log.d("DateDebug", "holder is DateViewHolder")

            val dateViewHolder = holder as DateViewHolder
            dateViewHolder.dateTextView.text = getDate(currentMessage.timestamp)
            Log.d("DateDebug", "포지션 ${position}에 날짜 바인딩 중")

        } else {
            // 보내는 데이터

            if (holder.javaClass == SendViewHolder::class.java) {
                val viewHolder = holder as SendViewHolder
                viewHolder.sendMessage.text = currentMessage.message
                viewHolder.sendTimestamp.text = getTime(currentMessage.timestamp)
            } else { // 받는 데이터
                val viewHolder = holder as ReceiveViewHolder
                viewHolder.receiveMessage.text = currentMessage.message
                holder.receiverName.text = currentMessage.senderName
                viewHolder.receiveTimestamp.text = getTime(currentMessage.timestamp)
            }
        }
    }


    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position] // 메세지 값
        return if (isDateChanged(position)) {
            date
        } else {
            if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
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
        val dateChanged = !isSameDate(cal1, cal2)

        Log.d("DateDebug", "날짜 변경됨: $dateChanged")

        return !isSameDate(cal1, cal2)
    }

    private fun isSameDate(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.MONTH] == cal2[Calendar.MONTH]
                && cal1[Calendar.DATE] == cal2[Calendar.DATE]
    }

    // 보낸 쪽
    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage: TextView = itemView.findViewById(R.id.tv_senderMessage)
        val sendTimestamp: TextView =
            itemView.findViewById(R.id.tv_senderTime) // 시간을 표시할 TextView 추가
    }

    // 받는 쪽
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.tv_receiverMessage)
        val receiverName: TextView = itemView.findViewById(R.id.tv_receiver)
        val receiveTimestamp: TextView =
            itemView.findViewById(R.id.tv_receiverTime) // 시간을 표시할 TextView 추가
    }

    // 날짜 표시 뷰홀더
    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
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


}
