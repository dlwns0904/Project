    package com.example.plzlogin.viewmodel

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.ViewModel
    import com.example.plzlogin.Meet
    import com.example.plzlogin.repository.ScheduleRepository

    class ScheduleViewModel : ViewModel() {

        private val scheduleRepository : ScheduleRepository = ScheduleRepository()

        private val _schedulelist = scheduleRepository.schedulelist

        val schedulelist : LiveData<List<Meet>> get() = _schedulelist
        init{
            loadschedule("20231130")
        }

        fun loadschedule(date : String){
            scheduleRepository.obverseSchedule(date)
        }
    }