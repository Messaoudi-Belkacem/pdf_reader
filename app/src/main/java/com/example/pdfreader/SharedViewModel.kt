package com.example.pdfreader

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _pdfUri = MutableLiveData<Uri?>()
    val pdfUri: LiveData<Uri?> = _pdfUri
    fun setPdfUri(uri: Uri?) {
        _pdfUri.value = uri
    }
    // navigation
    private val _navigateTo = MutableLiveData<String?>()
    val navigateTo: LiveData<String?> = _navigateTo
    fun setNavigateTo(route: String?) {
        _navigateTo.value = route
    }
}