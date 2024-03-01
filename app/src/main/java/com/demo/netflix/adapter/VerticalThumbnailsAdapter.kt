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
import com.demo.netflix.OpeningToMovieOrShow
import com.demo.netflix.R
import com.demo.netflix.TitleDetailPage
import com.demo.netflix.modelclasses.MovieDataClass
import com.demo.netflix.modelclasses.MovieListData
import com.demo.netflix.modelclasses.VerticalViewData
import com.google.gson.Gson

class VerticalThumbnailsAdapter(private val movieListVerticalView:ArrayList<MovieDataClass>, private val context: Context):
    RecyclerView.Adapter<VerticalThumbnailsAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_common_verticalthumbnails,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return movieListVerticalView.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = movieListVerticalView[position]
        Glide.with(context).load("https://img.youtube.com/vi/${i.videoId}/maxresdefault.jpg").placeholder(R.drawable.baseline_ondemand_video_24)
            .placeholder(R.drawable.baseline_play_circle_outline_24).into(holder.moviePoster)
        holder.movieName.text = i.title
        holder.cast.text = i.desc
//        holder.moviePoster.setImageResource(item.moviePoster!!)
//        holder.topTen.text = item.topTen.replace(" ","\n")
//        holder.recentlyAdded.text = item.recentlyAdded
//        if (!item.topTen.isEmpty())
//        {
//            holder.topTen.visibility = View.VISIBLE
//        }
//        else
//        {
//            holder.topTen.visibility = View.GONE
//        }
//        if (!item.recentlyAdded.isEmpty())
//        {
//            holder.recentlyAdded.visibility = View.VISIBLE
//        }
//        else
//        {
//            holder.recentlyAdded.visibility = View.GONE
//        }
//        holder.movieName.text = item.movieName
//        holder.duration.text = item.duration
//        holder.cast.text = item.cast
//        holder.director.text = item.director
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TitleDetailPage::class.java)
            intent.putExtra("selectedcategorytitle",Gson().toJson(i))
            context.startActivity(intent)
        }
    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val moviePoster:ImageView = itemView.findViewById(R.id.verticalviewmovieposter)
        val topTen:TextView = itemView.findViewById(R.id.verticalviewtoptenlabel)
        val recentlyAdded:TextView = itemView.findViewById(R.id.verticalviewrecentlyaddedtext)
        val movieName:TextView = itemView.findViewById(R.id.moviename)
        val duration:TextView = itemView.findViewById(R.id.movieorshowtime)
        val cast:TextView = itemView.findViewById(R.id.moviedesc)
        val director:TextView = itemView.findViewById(R.id.verticalviewmoviedirector)
    }
}