package com.demo.netflix
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.TextView.BufferType
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.netflix.adapter.MoreLikeThisAdapter
import com.demo.netflix.modelclasses.MovieListData
class OpeningToMovieOrShow : AppCompatActivity() {
    lateinit var movieCast:TextView
    lateinit var videoView: VideoView
    lateinit var moreLikeThisRecommendationList:RecyclerView
    lateinit var playButtonVideo:ImageView
    lateinit var likeButton:TextView
    lateinit var backwardTenButton:ImageView
    lateinit var forwardTenButton:ImageView
    lateinit var zoomInButton:ImageView
    lateinit var myListButton:TextView
    lateinit var movieShowDetailsMediaplayerControls:RelativeLayout
    lateinit var seekBar: SeekBar
    lateinit var portraitIntent: Intent
    lateinit var closeIcon:ImageView
    var handler:Handler?=null
    var landscapePosition:Int?=null
    lateinit var playButtonParent:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening_to_movie_or_show)
        handler = Handler(Looper.getMainLooper())
        movieCast = findViewById(R.id.moviecast)
        videoView = findViewById(R.id.movievideo)
        playButtonVideo = findViewById(R.id.playbuttonvideo)
        moreLikeThisRecommendationList = findViewById(R.id.morelikethisrecommendationlist)
        likeButton = findViewById(R.id.likebutton)
        backwardTenButton = findViewById(R.id.backwardtenbutton)
        forwardTenButton = findViewById(R.id.forwardtenbutton)
        zoomInButton = findViewById(R.id.zoominbutton)
        myListButton = findViewById(R.id.mylistbutton)
        movieShowDetailsMediaplayerControls = findViewById(R.id.movieshowdetailsmediaplayercontrols)
        closeIcon = findViewById(R.id.closeicon)
        seekBar = findViewById(R.id.videoseeker)
        playButtonParent = findViewById(R.id.playbuttonparent)
        portraitIntent = intent
        landscapePosition = portraitIntent.getIntExtra("portraitResume",0)
        moreLikeThisRecommendationList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        moreLikeThisRecommendationList.setHasFixedSize(true)
        makeTextViewResizable(movieCast,1,"View More",true)
        val offlineUri:Uri = Uri.parse("android.resource://$packageName/${R.raw.thebatmantrailer}")
        videoView.setVideoURI(offlineUri)
        videoView.requestFocus()
        videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener{
            override fun onPrepared(mp: MediaPlayer?) {
                seekBar.setProgress(0)
                seekBar.max = videoView.duration
                handler!!.postDelayed(updateVideoTime,100)
                videoView.start()
                playButtonVideo.contentDescription = "Playing"
            }
        })
        videoView.setOnTouchListener { v, event ->
            //videoView.performClick()
            if (movieShowDetailsMediaplayerControls.visibility==View.INVISIBLE) {
                movieShowDetailsMediaplayerControls.visibility=View.VISIBLE
                if (playButtonVideo.contentDescription=="Playing") {
                    playButtonVideo.setImageDrawable(getDrawable(R.drawable.baseline_pause_circle_outline_24))
                } else {
                    playButtonVideo.setImageDrawable(getDrawable(R.drawable.baseline_play_circle_outline_24))
                }
            } else {
                movieShowDetailsMediaplayerControls.visibility=View.INVISIBLE
            }

            return@setOnTouchListener false
        }

        seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoView.seekTo(seekBar?.progress!!)
            }
        })
        playButtonVideo.setOnClickListener {
            playPause()
        }
        forwardTenButton.setOnClickListener {
            videoView.seekTo(videoView.currentPosition +10000)
        }
        backwardTenButton.setOnClickListener {
            videoView.seekTo(videoView.currentPosition -10000)
        }
        zoomInButton.setOnClickListener {
            val intent = Intent(this@OpeningToMovieOrShow, NewVideoActivity::class.java)
            intent.putExtra("videoPostion",videoView.currentPosition)
            startActivityForResult(intent,200)
        }
//        moreLikeThisData.add(MovieListData(R.drawable.zodiaccover,"",""))
//        moreLikeThisData.add(MovieListData(R.drawable.thedarkknightimage,"",""))
//        moreLikeThisData.add(MovieListData(R.drawable.theflashimage,"",""))
//        moreLikeThisData.add(MovieListData(R.drawable.coverjaawan,"","Top 10"))
//        moreLikeThisData.add(MovieListData(R.drawable.coveramazingspiderman,"",""))
//        moreLikeThisAdapter = MoreLikeThisAdapter(moreLikeThisData,this@OpeningToMovieOrShow)
//        moreLikeThisRecommendationList.adapter = moreLikeThisAdapter
        likeButton.setOnClickListener {
            likeButton.setCompoundDrawablesWithIntrinsicBounds(0,
                R.drawable.baseline_thumb_up_24,0,0)
        }
        myListButton.setOnClickListener {
            myListButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.baseline_check_24,0,0)
        }
        closeIcon.setOnClickListener {
            finish()
        }
        playButtonParent.setOnClickListener {
            val movieIntent = Intent(this@OpeningToMovieOrShow, YoutubeVideoScreen::class.java)
            startActivity(movieIntent)
        }
    }
    fun makeTextViewResizable(textView: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {
        if (textView.tag == null) {
            textView.tag = textView.text
        }
        val vto = textView.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            @Suppress("deprecation")
            override fun onGlobalLayout() {
                val text: String
                val lineEndIndex: Int
                val obs = textView.viewTreeObserver
                obs.removeOnGlobalLayoutListener(this)
                if (maxLine == 0) {
                    lineEndIndex = textView.layout.getLineEnd(0)
                    text = textView.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                } else if (maxLine > 0 && textView.lineCount >= maxLine) {
                    lineEndIndex = textView.layout.getLineEnd(maxLine - 1)
                    text = textView.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                } else {
                    lineEndIndex = textView.layout.getLineEnd(textView.layout.lineCount - 1)
                    text = textView.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                }
                textView.text = text
                textView.movementMethod = LinkMovementMethod.getInstance()
                textView.setText(
                    addClickablePartTextViewResizable(
                        SpannableString(textView.text.toString()), textView, lineEndIndex, expandText,
                        viewMore
                    ), BufferType.SPANNABLE
                )
            }
        })
    }
    private fun addClickablePartTextViewResizable(
        strSpanned: Spanned, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    tv.layoutParams = tv.layoutParams
                    tv.setText(tv.tag.toString(), BufferType.SPANNABLE)
                    tv.invalidate()
                    if (viewMore)
                    {
                        makeTextViewResizable(tv, -1, "View Less", false)
                    }
                    else
                    {
                        makeTextViewResizable(tv, 3, "View More", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }
    private fun playPause() {
        if (playButtonVideo.contentDescription=="Playing")
        {
            videoView.pause()
            playButtonVideo.contentDescription="Pause"
            playButtonVideo.setImageDrawable(getDrawable(R.drawable.baseline_play_circle_outline_24))
        }
        else{
            videoView.start()
            playButtonVideo.contentDescription="Playing"
            playButtonVideo.setImageDrawable(getDrawable(R.drawable.baseline_pause_circle_outline_24))
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==200 && resultCode==Activity.RESULT_OK && data!=null)
        {
             val pos = data!!.getIntExtra("videoPostion",0)
            videoView.seekTo(pos)
        }
    }
    private val updateVideoTime: Runnable = object : Runnable {
        override fun run() {
            seekBar.progress = videoView.currentPosition.toInt()
            handler!!.postDelayed(this, 100)
        }
    }
}