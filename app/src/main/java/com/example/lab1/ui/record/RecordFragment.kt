package com.example.lab1.ui.record

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab1.MainActivity
import com.example.lab1.R
import com.example.lab1.databinding.FragmentRecordsBinding
import db.MyDbManager
import java.text.SimpleDateFormat
import java.util.*

class RecordFragment : Fragment() {

    private var _binding: FragmentRecordsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recordViewModel =
            ViewModelProvider(this).get(RecordViewModel::class.java)
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userView: TextView = binding.textRecords
        val myDbManager = MyDbManager(requireActivity().applicationContext)
        onDbRead(myDbManager, userView)
        return root
    }

    private fun onDbRead(dbManager: MyDbManager, view: TextView){
        view.text=""

        dbManager.openDb()
        val dataList = dbManager.readDbData()
        view.append("   USER   |   SCORE  |   DATE  \n")
        for(item in dataList){
            view.append(item.user + " | " + item.score + " | " + item.date )
            view.append("\n")
        }
        dbManager.closeDb()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}