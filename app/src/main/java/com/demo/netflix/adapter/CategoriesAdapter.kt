package com.demo.netflix.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.netflix.CategoryMovieListVView
import com.demo.netflix.R
import com.demo.netflix.modelclasses.CategoryData

class CategoriesAdapter(private val categoryList:ArrayList<CategoryData>,private val context: Context):
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_layout,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return categoryList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = categoryList[position]
        holder.categoryName.text = i.catName
        holder.itemView.setOnClickListener {
            val intent = Intent(context,CategoryMovieListVView::class.java)
            intent.putExtra("categoryclicked",i.catDocId)
            intent.putExtra("categoryclickedname",i.catName)
            context.startActivity(intent)
        }
    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val categoryName:TextView = itemView.findViewById(R.id.categoryname)
    }
}