package com.demo.netflix
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class NewVideoActivity : AppCompatActivity() {
    lateinit var videoView : VideoView
    lateinit var landscapeRewind:ImageView
    lateinit var landscapePlay:ImageView
    lateinit var landscapeForward:ImageView
    lateinit var videoControlIconsParent:ConstraintLayout
    lateinit var seekBar: SeekBar
    lateinit var zoomOutButton:ImageView
    lateinit var landscapeCloseIcon:ImageView
    var handler:Handler?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_video)
        videoView = findViewById(R.id.videoView)
        landscapeRewind = findViewById(R.id.landscaperewind)
        landscapePlay = findViewById(R.id.landscapeplay)
        landscapeForward = findViewById(R.id.landscapeforward)
        seekBar = findViewById(R.id.landscapeseekbar)
        zoomOutButton = findViewById(R.id.zoomoutlandscapevideo)
        handler = Handler(Looper.getMainLooper())
        videoControlIconsParent = findViewById(R.id.controliconsparent)
        landscapeCloseIcon = findViewById(R.id.landscapecloseicon)
        val landscapeRequestCode = 200
        var videoPostion = intent.getIntExtra("videoPostion",0)
        val uri:Uri = Uri.parse("android.resource://$packageName//${R.raw.thebatmantrailer}")
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener(object: MediaPlayer.OnPreparedListener{
            override fun onPrepared(mp: MediaPlayer?) {
                videoView.start()
                videoView.seekTo(videoPostion)
                seekBar.setProgress(0)
                seekBar.max = videoView.duration
                handler!!.postDelayed(updateVideoTime,100)
                landscapePlay.contentDescription = "Playing"
            }
        })
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoView.seekTo(seekBar?.progress!!)
            }
        })
        videoView.setOnTouchListener { v, event ->
            //videoView.performClick()
            if (videoControlIconsParent.visibility==View.INVISIBLE) {
                videoControlIconsParent.visibility=View.VISIBLE
                if (landscapePlay.contentDescription=="Playing") {
                    landscapePlay.setImageDrawable(getDrawable(R.drawable.baseline_pause_circle_outline_24))
                } else {
                    landscapePlay.setImageDrawable(getDrawable(R.drawable.baseline_play_circle_outline_24))
                }
            } else {
                videoControlIconsParent.visibility=View.INVISIBLE
            }

            return@setOnTouchListener false
        }
        landscapePlay.setOnClickListener {
            playPause()
        }
        landscapeRewind.setOnClickListener {
            videoView.seekTo(videoView.currentPosition -10000)
        }
        landscapeForward.setOnClickListener {
            videoView.seekTo(videoView.currentPosition +10000)
        }
        zoomOutButton.setOnClickListener {
           backToOpeningScreen()
        }
        landscapeCloseIcon.setOnClickListener {
            backToOpeningScreen()
        }
    }

    fun playPause() {
        if (landscapePlay.contentDescription == "Playing"){
            videoView.pause()
            landscapePlay.contentDescription = "Pause"
            landscapePlay.setImageDrawable(getDrawable(R.drawable.baseline_play_circle_outline_24))
        } else {
            videoView.start()
            landscapePlay.contentDescription = "Playing"
            landscapePlay.setImageDrawable(getDrawable(R.drawable.baseline_pause_circle_outline_24))
        }
    }
    private fun backToOpeningScreen(){
        val returnIntent = Intent()
        returnIntent.putExtra("videoPostion",videoView.currentPosition)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private val updateVideoTime: Runnable = object : Runnable {
        override fun run() {
            seekBar.progress = videoView.currentPosition.toInt()
            handler!!.postDelayed(this, 100)
        }
    }

    override fun onBackPressed() {
        backToOpeningScreen()
        super.onBackPressed()
    }
}