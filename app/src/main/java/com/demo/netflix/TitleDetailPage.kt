package com.demo.netflix
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.netflix.adapter.MoreLikeThisAdapter
import com.demo.netflix.modelclasses.FavoriteModel
import com.demo.netflix.modelclasses.MovieDataClass
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
class TitleDetailPage : AppCompatActivity() {
    val movieListData:ArrayList<MovieDataClass> = ArrayList()
    lateinit var titleName:TextView
    lateinit var titleDesc:TextView
    lateinit var moreLikeThisList:RecyclerView
    lateinit var moreLikeThisAdapter: MoreLikeThisAdapter
    lateinit var addToFavourites:TextView
    lateinit var recommendation:TextView
    lateinit var youtubePlayerView:YouTubePlayerView
    lateinit var selectedTitleName:String
    lateinit var selectedTitleDesc:String
    lateinit var selectedTitleCategory:String
    lateinit var selectedTitleVideoId:String
    lateinit var selectedMovieTitleId:String
    lateinit var movieDataParams:MovieDataClass
    lateinit var sharedPreferences: SharedPreferences
    lateinit var favouritesIconunCheck:ImageView
    lateinit var favouritesIconCheck:ImageView
    var favouriteList:ArrayList<String> = ArrayList()
    lateinit var addtofavouritesbuttonparent:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_detail_page)
        addToFavourites = findViewById(R.id.addtofavouritesbutton)
        titleName = findViewById(R.id.titlenameondetailpage)
        titleDesc = findViewById(R.id.titledesc)
        moreLikeThisList = findViewById(R.id.morelikethisfromcategorylist)
        youtubePlayerView = findViewById(R.id.titlevideoplayer)
        recommendation = findViewById(R.id.dynamicrecommendbutton)
        favouritesIconunCheck = findViewById(R.id.favouritesuncheck)
        favouritesIconCheck = findViewById(R.id.favouritescheck)
        addtofavouritesbuttonparent = findViewById(R.id.addtofavouritesbuttonparent)
        if (intent.getStringExtra("selectedcategorytitle")!=null)
        {
            movieDataParams = Gson().fromJson(intent.getStringExtra("selectedcategorytitle"),MovieDataClass::class.java)
            selectedTitleName = movieDataParams.title!!
            selectedTitleDesc = movieDataParams.desc!!
            selectedTitleCategory = movieDataParams.catId!!
            selectedMovieTitleId = movieDataParams.movieId!!
            selectedTitleVideoId = movieDataParams.videoId!!
            titleName.setText(selectedTitleName)
            titleDesc.setText(selectedTitleDesc)
        }
        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val favouritePref = Gson().fromJson(sharedPreferences.getString("getfavouritetitles",""),FavoriteModel::class.java)     //getting id from model
        if (favouritePref!=null && !favouritePref.favouriteTitleId.isNullOrEmpty()) {
            favouriteList = favouritePref.favouriteTitleId!!        //saving id from model
            if (favouriteList.contains(selectedMovieTitleId)) {
                favouritesIconCheck.visibility = View.VISIBLE
                favouritesIconunCheck.visibility = View.GONE
            } else {
                favouritesIconunCheck.visibility = View.VISIBLE
                favouritesIconCheck.visibility = View.GONE
            }
        }
        else
        {
            favouritesIconunCheck.visibility = View.VISIBLE
            favouritesIconCheck.visibility = View.GONE
        }
        val databaseLink = Firebase.firestore
        moreLikeThisList.layoutManager = GridLayoutManager(this@TitleDetailPage,3,GridLayoutManager.VERTICAL,false)
        lifecycle.addObserver(youtubePlayerView)
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(selectedTitleVideoId,0f)
                super.onReady(youTubePlayer)
            }
            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)             //when video is finished
            }
        })
        makeTextViewResizable(titleDesc,3,"View More",true)
        databaseLink.collection("appData").addSnapshotListener { value, error ->
            if (value!=null)
            {
                for (dataSnapshot in value.documents)
                {
                    val movieDataClass:MovieDataClass = dataSnapshot.toObject(MovieDataClass::class.java)!!
                    movieDataClass.movieId = dataSnapshot.id
                    if (movieDataClass.catId == selectedTitleCategory)
                    {
                        if (movieDataClass.movieId!=selectedMovieTitleId)
                        {
                            movieListData.add(movieDataClass)
                        }
                    }
                }
            }
            moreLikeThisAdapter = MoreLikeThisAdapter(movieListData,this@TitleDetailPage)
            moreLikeThisList.adapter = moreLikeThisAdapter
            if (error!=null)
            {
                Log.d("titlevideoplayererror",error.toString())
                return@addSnapshotListener
            }
        }
        addtofavouritesbuttonparent.setOnClickListener {
            if (favouritesIconCheck.visibility==View.VISIBLE)
            {
                saveFavourite(false,selectedTitleVideoId)
            }
            else if (favouritesIconunCheck.visibility==View.VISIBLE)
            {
                saveFavourite(true,selectedTitleVideoId)
            }
        }
        recommendation.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                var shareTitle:String = "\nLet Me Recommend You This Title\n\n"
                shareTitle = shareTitle + "https://www.youtube.com/watch?v=${selectedTitleVideoId}"
                intent.putExtra(Intent.EXTRA_TEXT,shareTitle)
                startActivity(Intent.createChooser(intent,"Title To Be Shared"))
            }
            catch (e:Exception)
            {
                Log.d("sharetitleerror",e.toString())
            }
        }
    }
    fun makeTextViewResizable(textView: TextView, maxLine: Int, expandText: String, viewMore: Boolean) {
        if (textView.tag == null) {
            textView.tag = textView.text
        }
        val vto = textView.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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
                    ), TextView.BufferType.SPANNABLE
                )
            }
        })
    }
    public fun saveFavourite(isAdd:Boolean, videoId:String)
    {
        if (isAdd)
        {
            if (!favouriteList.contains(videoId))
            {
                favouriteList.add(videoId)      //
            }
            favouritesIconCheck.visibility = View.VISIBLE
            favouritesIconunCheck.visibility = View.GONE
            Toast.makeText(this@TitleDetailPage, "Video added to favourite", Toast.LENGTH_SHORT).show()
        }
        else
        {
            if (favouriteList.contains(videoId))
            {
                favouriteList.remove(videoId)
            }
            favouritesIconunCheck.visibility = View.VISIBLE
            favouritesIconCheck.visibility = View.GONE
            Toast.makeText(this@TitleDetailPage, "Video removed from favourite", Toast.LENGTH_SHORT).show()
        }
        val favouiteModel:FavoriteModel = FavoriteModel(favouriteList)
        val editor:SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("savefavtitle",Gson().toJson(favouiteModel))
        editor.apply()
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
                    tv.setText(tv.tag.toString(), TextView.BufferType.SPANNABLE)
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
}