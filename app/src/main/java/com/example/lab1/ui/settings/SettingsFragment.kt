package com.example.lab1.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab1.MainActivity
import com.example.lab1.R
import com.example.lab1.databinding.FragmentSettingsBinding
import db.MyDbManager


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView =binding.textSettings
        val buttonView : Button = binding.button2
        val editText : EditText = binding.editText1
        val myDbManager = MyDbManager(requireActivity().applicationContext)
        buttonView.setOnClickListener(View.OnClickListener {
            saveIfNotExist(myDbManager, editText)
        })
        val spin: Spinner = binding.spinnerSettings
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        initializeSpin(myDbManager, spin)

        return root
    }

    private fun initializeSpin(myDbManager: MyDbManager, spin: Spinner) {
        val dataList = onDbRead(myDbManager)
        val aa = ArrayAdapter(requireActivity().applicationContext, R.layout.item, dataList)
        aa.setDropDownViewResource(R.layout.item)
        with(spin)
        {
            adapter = aa
            setSelection(0, false)
            prompt = "Select your username:"
        }

        spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                (parent?.getChildAt(0) as TextView).setTextColor(Color.RED)
                val toast = Toast.makeText(
                    requireActivity().applicationContext,
                    "Ваш выбор: " + dataList[selectedItemPosition], Toast.LENGTH_SHORT
                )
                MainActivity.userMainName=dataList[selectedItemPosition]
                toast.show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun saveIfNotExist(myDbManager: MyDbManager, editText: EditText) {
        if(editText.text.toString()!="") {
            myDbManager.openDb()
            val res = myDbManager.readDbUserData()
            for (item in res) {
                if (item == editText.text.toString()) {
                    val toast = Toast.makeText(
                        requireActivity().applicationContext,
                        "User already exists: " + item, Toast.LENGTH_SHORT
                    )
                    toast.show()
                    myDbManager.closeDb()
                    return
                }
            }
            myDbManager.insertToUsersDb(editText.text.toString())
            initializeSpin(myDbManager,spin = binding.spinnerSettings)
            myDbManager.closeDb()
        }
    }


    private fun onDbRead(dbManager: MyDbManager ): ArrayList<String> {
        dbManager.openDb()
        val res= dbManager.readDbUserData()
        dbManager.closeDb()
        return res
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}