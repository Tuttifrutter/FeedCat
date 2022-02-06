package com.example.lab1.ui.record

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab1.MainActivity
import db.MyDbManager

class RecordViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {

        value =  "This is records Fragment"
    }

    val text: LiveData<String> = _text
}