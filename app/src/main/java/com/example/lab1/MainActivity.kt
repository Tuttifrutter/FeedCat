package com.example.lab1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
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
    var fonMediaPlayer: MediaPlayer? = null

    companion object{
        var countString:String = "0"
        var userMainName:String = "Anon"
        var mMediaPlayer: MediaPlayer? = null
        private var mAnimationDrawable: AnimationDrawable? = null
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun countMe (textView: TextView, imageView:ImageView, context: Context) {
            MainActivity.countString = textView.text.toString()
            var count: Int = Integer.parseInt(countString)
            count++
            textView.text = count.toString();
            mAnimationDrawable?.stop()
            stopSound()
            if(count%15 == 0)
            {
                imageView.setBackgroundResource(R.drawable.catanimation)
                startSound(context, R.raw.cool)
            }
            else if(count%100 == 0)
            {
                imageView.setBackgroundResource(R.drawable.rigunaction)
                startSound(context,R.raw.rig)
            }
            else
            {
                imageView.setBackgroundResource(R.drawable.loveanimation)
                startSound(context,R.raw.meow3)
            }
            mAnimationDrawable = imageView.background as AnimationDrawable
            mAnimationDrawable?.start()

        }
        fun startSound(context: Context, resId: Int) {
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(context,resId)
                mMediaPlayer!!.isLooping = false
                mMediaPlayer!!.start()
            } else mMediaPlayer!!.start()
        }
        private fun stopSound() {
            if (mMediaPlayer != null) {
                mMediaPlayer!!.stop()
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
        }
    }

    private lateinit var textView: TextView
    private val myDbManager = MyDbManager(this)


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
        countString = "0"
        val buttonView = findViewById<View>(R.id.button)
        buttonView.setOnClickListener(View.OnClickListener {
            countMe(textView, imageView, this)
        })
        if (fonMediaPlayer == null) {
            fonMediaPlayer = MediaPlayer.create(this,R.raw.rock)
            fonMediaPlayer!!.isLooping = true

            fonMediaPlayer!!.start()
        } else fonMediaPlayer!!.start()
        textView.text= countString
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_records, R.id.nav_settings
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

    override fun onDestroy() {
        super.onDestroy()
        saveResultToDb()
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveResultToDb(){
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val formattedDate = formatter.format(date)
        myDbManager.openDb()

        if(countString!="0")
            myDbManager.insertToDbIfHigher(userMainName, Integer.parseInt(countString)+1, formattedDate.toString())
        myDbManager.closeDb()
    }

    override fun onStop() {
        super.onStop()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        if (fonMediaPlayer != null) {
            fonMediaPlayer!!.release()
            fonMediaPlayer = null
        }
    }
}