package com.demo.netflix
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.netflix.adapter.CategoriesAdapter
import com.demo.netflix.modelclasses.CategoryData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
class Categories : Fragment() {
    lateinit var categoriesList:RecyclerView
    lateinit var categoriesAdapter: CategoriesAdapter
    var categoriesData:ArrayList<CategoryData> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesList = view.findViewById(R.id.categorieslist)
        categoriesList.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        categoriesList.setHasFixedSize(true)
        val databaseLink = Firebase.firestore
        databaseLink.collection("categories").addSnapshotListener { value, error ->
            categoriesData.clear()
            if (value!=null)
            {
                for (dataSnapshot in value.documents)
                {
                   if (dataSnapshot!=null)
                   {
                       val categoryData:CategoryData = dataSnapshot.toObject(CategoryData::class.java)!!
                       categoryData.catDocId = dataSnapshot.id
                       categoriesData.add(categoryData)
                   }
                }
            }
            categoriesAdapter = CategoriesAdapter(categoriesData,requireContext())
            categoriesList.adapter = categoriesAdapter
            if (error!=null)
            {
                Log.d("categoryfetcherror",error.toString())
                return@addSnapshotListener
            }
        }
    }
}