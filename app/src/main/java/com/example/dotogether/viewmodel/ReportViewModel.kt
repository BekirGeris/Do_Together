package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.request.ReportRequest
import com.example.dotogether.model.response.ReportResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor() : BaseViewModel() {

    private val _sendReport = MutableLiveData<Resource<ReportResponse>>()
    val sendReport: MutableLiveData<Resource<ReportResponse>> = _sendReport

    fun sendReport(targetId: Int, reportRequest: ReportRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.sendReport(targetId, reportRequest).collect {
                sendReport.value = it
            }
        }
    }
}