package com.example.lab1

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lab1.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import db.MyDbManager
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var imageView: ImageView
    private var mAnimationDrawable: AnimationDrawable? = null
    var countString = "0"
    private lateinit var textView: TextView
    private lateinit var recordView: TextView
    private val myDbManager = MyDbManager(this)
    private var usermainname = "Dmitry"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        imageView = findViewById(R.id.imageView2)
        imageView.setBackgroundResource(R.drawable.sadcat)
        textView = findViewById(R.id.textView3)
        binding.appBarMain.fab.setOnClickListener {
           shareBtnClick()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_records, R.id.nav_slideshow
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun countMe (view: View) {
        countString = textView.text.toString()
        var count: Int = Integer.parseInt(countString)
        count++
        textView.text = count.toString();
        if(count%15 == 0)
        {
            imageView.setBackgroundResource(R.drawable.catanimation)
            mAnimationDrawable = imageView.background as AnimationDrawable
            mAnimationDrawable?.start()
        }
        else if(count%100 == 0)
        {
            imageView.setBackgroundResource(R.drawable.rigunaction)
            mAnimationDrawable = imageView.background as AnimationDrawable
            mAnimationDrawable?.start()
        }
        else
        {
            mAnimationDrawable?.stop()
            imageView.setBackgroundResource(R.drawable.loveanimation)
            mAnimationDrawable = imageView.background as AnimationDrawable
            mAnimationDrawable?.start()
        }

    }
    private fun shareBtnClick()
    {
        var intCountStr =Integer.parseInt(countString)
            intCountStr+=1
        val myIntent = Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain")
        val shareBody = "Я набрал $intCountStr в Кот Супермегахорош"
        val shareSub = "Афигеть"
        myIntent.putExtra(EXTRA_SUBJECT, shareSub)
        myIntent.putExtra(EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(myIntent, "Share using"));
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDestroy() {
        super.onDestroy()
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val formattedDate = formatter.format(date)
        myDbManager.openDb()
        if(countString!="0")
        myDbManager.insertToDbIfHigher(usermainname, Integer.parseInt(countString)+1, formattedDate.toString())
        myDbManager.closeDb()
    }


}