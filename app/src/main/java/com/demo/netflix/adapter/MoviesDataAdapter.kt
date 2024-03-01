package com.demo.netflix.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.netflix.R
import com.demo.netflix.TitleDetailPage
import com.demo.netflix.modelclasses.MovieDataClass
import com.google.gson.Gson

class MoviesDataAdapter(private val movieDataClass:ArrayList<MovieDataClass>,private val context: Context):
    RecyclerView.Adapter<MoviesDataAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movielist_design,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return if (movieDataClass.size < 5){
        movieDataClass.size
        } else {
            5
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = movieDataClass[position]
        holder.movieTitle.text = i.title
        Glide.with(context).load("https://img.youtube.com/vi/${i.videoId}/maxresdefault.jpg").placeholder(R.drawable.baseline_ondemand_video_24)
            .placeholder(R.drawable.baseline_play_circle_outline_24).into(holder.imageThumbnail)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,TitleDetailPage::class.java)
            intent.putExtra("selectedcategorytitle",Gson().toJson(i))
            context.startActivity(intent)
        }
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val movieTitle:TextView = itemView.findViewById(R.id.fragmentmovietitle)
        val imageThumbnail:ImageView = itemView.findViewById(R.id.movieposter)
    }
}