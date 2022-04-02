package com.example.lab1.ui.home

import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab1.databinding.FragmentHomeBinding
import com.example.lab1.MainActivity
import com.example.lab1.R
import com.example.lab1.databinding.ActivityMainBinding
import db.MyDbManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var mAnimationDrawable: AnimationDrawable? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textView3
        val imageView: ImageView = binding.imageView2
        val buttonView : Button = binding.button
        buttonView.setOnClickListener(View.OnClickListener {
            MainActivity.countMe(textView, imageView, requireActivity().applicationContext)
        })
        return root
    }

    override fun onStart() {
        super.onStart()
        val textView: TextView = binding.textView3
        textView.text = (Integer.parseInt(MainActivity.countString)+1).toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}