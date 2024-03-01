package com.demo.netflix
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.demo.netflix.adapter.MovieListAdapter
import com.demo.netflix.adapter.SlideShowAdapter
import com.demo.netflix.modelclasses.CategoryData
import com.demo.netflix.modelclasses.MovieDataClass
import com.demo.netflix.modelclasses.MovieListData
import com.demo.netflix.modelclasses.SlideshowData
import com.demo.netflix.modelclasses.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlin.math.abs
class DemoNetflixHomePage : AppCompatActivity() {
    lateinit var movieList:RecyclerView
    val movieListData:ArrayList<MovieListData> = ArrayList()
    lateinit var movieListAdapter: MovieListAdapter
    lateinit var trendingMovieList:RecyclerView
    val trendingMovieListData:ArrayList<MovieListData> = ArrayList()
    lateinit var movieHistorySuggestionList:RecyclerView
    val movieHistorySuggestionListData:ArrayList<MovieListData> = ArrayList()
    lateinit var onlyOnNetflixList:RecyclerView
    lateinit var viewMoreWeThinkYouLoveThese:TextView
    lateinit var viewMoreTrendingNow:TextView
    lateinit var viewMoreBecauseYouSaw:TextView
    lateinit var viewMoreOnlyOnNetflix:TextView
    var handler: Handler? = null
    val onlyOnNetflixListData:ArrayList<MovieListData> = ArrayList()
    val categoryList:ArrayList<CategoryData> = ArrayList()
    val newMoviesData:ArrayList<MovieDataClass> = ArrayList()
    lateinit var slideShowAdapter: SlideShowAdapter
    lateinit var slideShowComponent:ViewPager2
    lateinit var signOut:TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var progressBar:RelativeLayout
    var movieCategoryId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_netflix_home_page)
        handler = Handler(Looper.getMainLooper())
        movieList = findViewById(R.id.movielistsuggestions)
        viewMoreWeThinkYouLoveThese = findViewById(R.id.favouritesuggestionsviewmoretext)
        viewMoreTrendingNow = findViewById(R.id.trendingmovielistviewmoretext)
        viewMoreBecauseYouSaw = findViewById(R.id.becauseyousawviewmore)
        viewMoreOnlyOnNetflix = findViewById(R.id.onlyonnetflixviewmore)
        slideShowComponent = findViewById(R.id.bannerslideshowcomponent)
        signOut = findViewById(R.id.signouttext)
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        var userList:String = sharedPreferences.getString("signinuser","")!!
        val gson = Gson()
        val json = gson.fromJson(userList,UserData::class.java)
        movieList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        movieList.setHasFixedSize(true)
        trendingMovieList = findViewById(R.id.trendingmovielist)
        trendingMovieList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        trendingMovieList.setHasFixedSize(true)
        onlyOnNetflixList = findViewById(R.id.onlyonnetflixlist)
        onlyOnNetflixList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        onlyOnNetflixList.setHasFixedSize(true)
        movieHistorySuggestionList = findViewById(R.id.becauseyousawmoviesuggestionlist)
        progressBar = findViewById(R.id.progressloader)
        movieHistorySuggestionList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        movieHistorySuggestionList.setHasFixedSize(true)
        movieListData.add(MovieListData(R.drawable.coverokja,"Recently Added","Top 10"))
        movieListData.add(MovieListData(R.drawable.covertopgunmaverick,"Recently Added",""))
        movieListData.add(MovieListData(R.drawable.covernowayhome,"",""))
        movieListData.add(MovieListData(R.drawable.covermifour,"",""))
        movieListData.add(MovieListData(R.drawable.coverglassonion,"",""))
        movieListData.add(MovieListData(R.drawable.coverthebatmannew,"","Top 10"))
        movieListData.add(MovieListData(R.drawable.covergodfatherone,"",""))
        movieListData.add(MovieListData(R.drawable.coverelvis,"Recently Added","Top 10"))
        movieListAdapter = MovieListAdapter(movieListData,this@DemoNetflixHomePage)
        movieList.adapter = movieListAdapter
        trendingMovieListData.add(MovieListData(R.drawable.coverduneone,"Recently Added",""))
        trendingMovieListData.add(MovieListData(R.drawable.covertopgun,"",""))
        trendingMovieListData.add(MovieListData(R.drawable.dunkicover,"Recently Added","Top 10"))
        trendingMovieListData.add(MovieListData(R.drawable.coverjaawan,"","Top 10"))
        trendingMovieListData.add(MovieListData(R.drawable.coverelvis,"Recently Added","Top 10"))
        movieListAdapter = MovieListAdapter(trendingMovieListData,this@DemoNetflixHomePage)
        trendingMovieList.adapter = movieListAdapter
        movieHistorySuggestionListData.add(MovieListData(R.drawable.coveramazingspiderman,"",""))
        movieHistorySuggestionListData.add(MovieListData(R.drawable.theflashimage,"",""))
        movieHistorySuggestionListData.add(MovieListData(R.drawable.manofsteelimage,"",""))
        movieHistorySuggestionListData.add(MovieListData(R.drawable.spidermanhomecomingposter,"",""))
        movieHistorySuggestionListData.add(MovieListData(R.drawable.incrediblehulkimage, "",""))
        movieHistorySuggestionListData.add(MovieListData(R.drawable.thedarkknightimage,"",""))
        movieHistorySuggestionListData.add(MovieListData(R.drawable.zodiaccover,"",""))
        movieHistorySuggestionListData.add(MovieListData(R.drawable.spidermanspiderverseone,"",""))
        movieListAdapter = MovieListAdapter(movieHistorySuggestionListData,this@DemoNetflixHomePage)
        movieHistorySuggestionList.adapter = movieListAdapter
        val databaseLink = Firebase.firestore

        databaseLink.collection("appData").addSnapshotListener { value, error ->
            if (error!=null)
            {
                Log.d("categorydataerror",error.toString())
                return@addSnapshotListener
            }
            if (value!=null)
            {
                for (dataSnapshot in value!!.documents)
                {
                    if (dataSnapshot!=null)
                    {
                        var moviesData:MovieDataClass = dataSnapshot.toObject(MovieDataClass::class.java)!!
                        moviesData.movieId == dataSnapshot.id
                        newMoviesData.add(moviesData)
                    }
                }
            }
        }
        onlyOnNetflixListData.add(MovieListData(R.drawable.coverglassonion,"",""))
        onlyOnNetflixListData.add(MovieListData(R.drawable.strangerthingsposter,"",""))
        onlyOnNetflixListData.add(MovieListData(R.drawable.moneyheistposter,"",""))
        onlyOnNetflixListData.add(MovieListData(R.drawable.houseofcardsposter,"",""))
        onlyOnNetflixListData.add(MovieListData(R.drawable.coverpeakyblinders,"",""))
        onlyOnNetflixListData.add(MovieListData(R.drawable.extractioncover,"",""))
        onlyOnNetflixListData.add(MovieListData(R.drawable.extractiontwo,"",""))
        movieListAdapter = MovieListAdapter(onlyOnNetflixListData,this@DemoNetflixHomePage)
        onlyOnNetflixList.adapter = movieListAdapter
        slideShowComponent.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        slideShowComponent.adapter = slideShowAdapter
        viewMoreWeThinkYouLoveThese.setOnClickListener {
            val intent = Intent(this@DemoNetflixHomePage, NetflixHistorySuggestions::class.java)
            startActivity(intent)
        }
        viewMoreTrendingNow.setOnClickListener {
            val intent = Intent(this@DemoNetflixHomePage, NetflixTrendingListViewMore::class.java)
            startActivity(intent)
        }
        viewMoreBecauseYouSaw.setOnClickListener {
            val intent = Intent(this@DemoNetflixHomePage, BecauseYouSawVerticalView::class.java)
            startActivity(intent)
        }
        viewMoreOnlyOnNetflix.setOnClickListener {
            val intent = Intent(this@DemoNetflixHomePage, OnlyOnNetflixViewMore::class.java)
            startActivity(intent)
        }
        setUpTransformer()
        slideShowComponent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler!!.removeCallbacks(runnable)
                handler!!.postDelayed(runnable,3000)
            }
        })
//        signOut.setOnClickListener {
//            firebaseAuth.signOut()
//            val editor:SharedPreferences.Editor = sharedPreferences.edit()
//            editor.putString("signinuser","")
//            editor.putBoolean("userloggedin",false)
//            editor.apply()
//            val intent = Intent(this@DemoNetflixHomePage,LoginScreen::class.java)
//            startActivity(intent)
//        }
    }
    override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(runnable)
    }
    override fun onRestart() {
        super.onRestart()
        handler!!.postDelayed(runnable,3000)
    }
    private val runnable = Runnable {
        slideShowComponent.currentItem = slideShowComponent.currentItem+1

    }
    private fun setUpTransformer() {
       val transformer = CompositePageTransformer()
       transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r + 0.14f
        }
        slideShowComponent.setPageTransformer(transformer)
    }
    public fun getData(){
    }
}