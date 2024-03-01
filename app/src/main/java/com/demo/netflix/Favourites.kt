package com.demo.netflix
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.netflix.adapter.VerticalThumbnailsAdapter
import com.demo.netflix.modelclasses.FavoriteModel
import com.demo.netflix.modelclasses.MovieDataClass
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
class Favourites : Fragment() {
    lateinit var favouriteTitlesDisplay:RecyclerView
    lateinit var verticalThumbnailsAdapter: VerticalThumbnailsAdapter
    lateinit var sharedPreferences: SharedPreferences
    val favouriteTitlesData:ArrayList<MovieDataClass> = ArrayList()
    var getFavouriteTitles:ArrayList<String> = ArrayList()
    lateinit var movieDataParams:MovieDataClass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteTitlesDisplay = view.findViewById(R.id.favouritetitlesdisplay)
        favouriteTitlesDisplay.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        sharedPreferences = requireActivity().getSharedPreferences(requireContext().packageName,Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharedPreferences.edit()
        val getFavTitle = Gson().fromJson(sharedPreferences.getString("getfavouritetitles",""),FavoriteModel::class.java)
        if (getFavTitle != null && !getFavTitle.favouriteTitleId.isNullOrEmpty())
        {
            getFavouriteTitles = getFavTitle.favouriteTitleId!!
        }
        val databaseLink = Firebase.firestore
        databaseLink.collection("appData").addSnapshotListener { value, error ->
            if (value!=null)
            {
                for (dataSnapshot in value.documents)
                {
                    if (dataSnapshot!=null)
                    {
                        val movieDataClass:MovieDataClass = dataSnapshot.toObject(MovieDataClass::class.java)!!
                        movieDataClass.movieId = dataSnapshot.id
                        favouriteTitlesData.add(movieDataClass)
                    }
                }
            }
            verticalThumbnailsAdapter = VerticalThumbnailsAdapter(favouriteTitlesData,requireContext())
            favouriteTitlesDisplay.adapter = verticalThumbnailsAdapter
            if (error!=null)
            {
                Log.d("favouritetitleserror",error.toString())
            }
        }
    }
}