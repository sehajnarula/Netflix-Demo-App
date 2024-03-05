package com.demo.netflix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.netflix.adapter.VerticalThumbnailsAdapter
import com.demo.netflix.modelclasses.MovieDataClass
import com.google.api.Logging
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
class CategoryMovieListVView : AppCompatActivity() {
    lateinit var categoryMovieDisplay:RecyclerView
    lateinit var verticalThumbnailsAdapter: VerticalThumbnailsAdapter
    val moviesList:ArrayList<MovieDataClass> = ArrayList()
    lateinit var progressBar:RelativeLayout
    lateinit var screenName:TextView
    lateinit var backArrow:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_movie_list_vview)
        categoryMovieDisplay = findViewById(R.id.categorymoviedisplay)
        screenName = findViewById(R.id.screenname)
        backArrow = findViewById(R.id.backarrow)
        progressBar = findViewById(R.id.progressloader)
        categoryMovieDisplay.layoutManager = LinearLayoutManager(this@CategoryMovieListVView,LinearLayoutManager.VERTICAL,false)
        val categoryId:String = intent.getStringExtra("categoryclicked")!!
        val categoryName:String = intent.getStringExtra("categoryclickedname")!!
        val databaseLink = Firebase.firestore
        screenName.setText(categoryName)
        databaseLink.collection("appData").addSnapshotListener { value, error ->
            progressBar.visibility = View.VISIBLE
            moviesList.clear()
            if (value!=null)
            {
                for(dataSnapshot in value.documents)
                {
                    if (dataSnapshot!=null)
                    {
                        val movieDataClass:MovieDataClass = dataSnapshot.toObject(MovieDataClass::class.java)!!
                        movieDataClass.movieId = dataSnapshot.id
                        if (movieDataClass.catId==categoryId)
                        {
                            moviesList.add(movieDataClass)
                        }
                    }
                }
            }
            progressBar.visibility = View.GONE
            verticalThumbnailsAdapter = VerticalThumbnailsAdapter(moviesList,this@CategoryMovieListVView)
            categoryMovieDisplay.adapter = verticalThumbnailsAdapter
            if (error!=null)
            {
                progressBar.visibility = View.GONE
                Log.d("categorymoviesclick",error.toString())
                return@addSnapshotListener
            }
        }
        backArrow.setOnClickListener {
            finish()
        }
    }
}