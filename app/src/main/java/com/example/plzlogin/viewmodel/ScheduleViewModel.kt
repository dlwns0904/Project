    package com.example.plzlogin.viewmodel

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.ViewModel
    import com.example.plzlogin.Meet
    import com.example.plzlogin.repository.ScheduleRepository

    class ScheduleViewModel : ViewModel() {

        private val scheduleRepository : ScheduleRepository = ScheduleRepository()

        private val _meetlist = scheduleRepository.meetlist

        val meetlist : LiveData<List<Meet>> get() = _meetlist

        fun observeSchedule(selectedDate: String) {
            scheduleRepository.observeSchedule(selectedDate)
        }
    }