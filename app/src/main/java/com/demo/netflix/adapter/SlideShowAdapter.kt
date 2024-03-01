package com.demo.netflix.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.netflix.CategoryMovieListVView
import com.demo.netflix.OpeningToMovieOrShow
import com.demo.netflix.R
import com.demo.netflix.TitleDetailPage
import com.demo.netflix.modelclasses.MovieDataClass
import com.demo.netflix.modelclasses.SlideshowData
import com.google.gson.Gson

class SlideShowAdapter(private val slideShowList:ArrayList<MovieDataClass>, private val context: Context):
    RecyclerView.Adapter<SlideShowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slideshowlayout,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return slideShowList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = slideShowList[position]
        Glide.with(context).load("https://img.youtube.com/vi/${i.videoId}/sddefault.jpg").placeholder(R.drawable.baseline_ondemand_video_24)
            .placeholder(R.drawable.baseline_play_circle_outline_24).into(holder.moviePoster)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,TitleDetailPage::class.java)
            intent.putExtra("selectedcategorytitle",Gson().toJson(i))
            context.startActivity(intent)
        }
    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val moviePoster:ImageView = itemView.findViewById(R.id.slideshowimage)
    }
}