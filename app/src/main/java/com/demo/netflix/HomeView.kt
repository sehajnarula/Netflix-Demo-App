package com.demo.netflix
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.demo.netflix.adapter.MoviesDataAdapter
import com.demo.netflix.adapter.SlideShowAdapter
import com.demo.netflix.modelclasses.MovieDataClass
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.math.abs
class HomeView : Fragment() {
    lateinit var kidsMovieList: RecyclerView
    lateinit var kidsMoviesDataAdapter: MoviesDataAdapter
    lateinit var moviesDataAdapter: MoviesDataAdapter
    lateinit var newsDataAdapter: MoviesDataAdapter
    lateinit var musicDataAdapter: MoviesDataAdapter
    lateinit var movieList: RecyclerView
    val movieGenreListData:ArrayList<MovieDataClass> = ArrayList()
    lateinit var newsList: RecyclerView
    val kidsMovieData:ArrayList<MovieDataClass> = ArrayList()
    lateinit var musicList: RecyclerView
    lateinit var viewMoreKidsSuggestion: TextView
    lateinit var viewMoreMovies: TextView
    lateinit var viewMoreNews: TextView
    lateinit var viewMoreMusic: TextView
    var handler: Handler? = null
    val musicGenreData:ArrayList<MovieDataClass> = ArrayList()
    val slideShowList:ArrayList<MovieDataClass> = ArrayList()
    val newsGenreList:ArrayList<MovieDataClass> = ArrayList()
    val kidsGenre:String = "3wsuOge0vAXVyp1ZJtVm"
    val moviesGenre:String = "um0pU5eFrzoVVAe5XNA2"
    val newsGenre:String = "r8GEFWIkFSLqITeOIwf5"
    val musicGenre:String = "vrx0t2we3Q8Glo5gZyiL"
    lateinit var slideShowAdapter: SlideShowAdapter
    lateinit var slideShowComponent: ViewPager2
    lateinit var progressBar:RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_view, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = Handler(Looper.getMainLooper())
        kidsMovieList = view.findViewById(R.id.kidssuggestions)
        viewMoreKidsSuggestion = view.findViewById(R.id.kidssuggestionsviewmore)
        viewMoreMovies = view.findViewById(R.id.movielistviewmoretext)
        viewMoreNews = view.findViewById(R.id.newsviewmore)
        viewMoreMusic = view.findViewById(R.id.musicviewmore)
        slideShowComponent = view.findViewById(R.id.bannerslideshowcomponent)
        progressBar = view.findViewById(R.id.progressloader)
        kidsMovieList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        kidsMovieList.setHasFixedSize(true)
        movieList = view.findViewById(R.id.movielist)
        movieList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false)
        movieList.setHasFixedSize(true)
        musicList = view.findViewById(R.id.musiclist)
        musicList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false)
        musicList.setHasFixedSize(true)
        newsList = view.findViewById(R.id.newslist)
        newsList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false)
        newsList.setHasFixedSize(true)
        slideShowComponent.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val databaseLink = Firebase.firestore
        databaseLink.collection("appData").addSnapshotListener { value, error ->
            kidsMovieData.clear()
            movieGenreListData.clear()
            newsGenreList.clear()
            musicGenreData.clear()
            progressBar.visibility = View.VISIBLE
            if (value!=null)
            {
                for (dataSnapshot in value.documents)
                {
                    if (dataSnapshot!=null)
                    {
                        val movieDataClass:MovieDataClass = dataSnapshot.toObject(MovieDataClass::class.java)!!
                        movieDataClass.movieId = dataSnapshot.id
                        if (movieDataClass.catId == kidsGenre)
                        {
                            kidsMovieData.add(movieDataClass)
                            if (kidsMovieData.size<5)
                            {
                                viewMoreKidsSuggestion.visibility = View.GONE
                            }
                            else
                            {
                                viewMoreKidsSuggestion.visibility = View.VISIBLE
                            }
                        }
                        else if (movieDataClass.catId == moviesGenre)
                        {
                            movieGenreListData.add(movieDataClass)
                            if (movieGenreListData.size<5)
                            {
                                viewMoreMovies.visibility = View.GONE
                            }
                            else
                            {
                                viewMoreMovies.visibility = View.VISIBLE
                            }
                        }
                        else if(movieDataClass.catId == newsGenre)
                        {
                            newsGenreList.add(movieDataClass)
                            if (newsGenreList.size<5)
                            {
                                viewMoreNews.visibility = View.GONE
                            }
                            else
                            {
                                viewMoreNews.visibility = View.VISIBLE
                            }
                        }
                        else if(movieDataClass.catId == musicGenre)
                        {
                            musicGenreData.add(movieDataClass)
                            if (musicGenreData.size<5)
                            {
                                viewMoreMusic.visibility = View.GONE
                            }
                            else
                            {
                                viewMoreMusic.visibility = View.VISIBLE
                            }
                        }
                        if (movieDataClass.trending)
                        {
                            slideShowList.add(movieDataClass)
                        }
                    }
                }
            }
            slideShowAdapter = SlideShowAdapter(slideShowList,requireContext())
            slideShowComponent.adapter = slideShowAdapter
            kidsMoviesDataAdapter = MoviesDataAdapter(kidsMovieData, requireContext())
            kidsMovieList.adapter = kidsMoviesDataAdapter
            moviesDataAdapter = MoviesDataAdapter(movieGenreListData,requireContext())
            movieList.adapter = moviesDataAdapter
            newsDataAdapter = MoviesDataAdapter(newsGenreList,requireContext())
            newsList.adapter = newsDataAdapter
            musicDataAdapter = MoviesDataAdapter(musicGenreData,requireContext())
            musicList.adapter = musicDataAdapter
            progressBar.visibility = View.GONE
            if (error!=null)
            {
                progressBar.visibility = View.GONE
                Log.d("moviefetcherror",error.toString())
            }
        }
        setUpTransformer()
        slideShowComponent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler!!.removeCallbacks(runnable)
                handler!!.postDelayed(runnable,3000)
            }
        })
    }
    override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(runnable)
    }
    override fun onResume() {
        super.onResume()
        handler!!.postDelayed(runnable,3000)
    }
    private val runnable = Runnable {
        //slideShowComponent.currentItem = slideShowComponent.currentItem+1
        val newPage: Int = if ((slideShowComponent.currentItem + 1)< slideShowList.size){
            slideShowComponent.currentItem+1
        } else {
            0
        }
        slideShowComponent.currentItem = newPage
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
}