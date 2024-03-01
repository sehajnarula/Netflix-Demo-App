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
import com.demo.netflix.modelclasses.MovieListData
import com.google.gson.Gson

class MoreLikeThisAdapter(private val moreLikeThisData:ArrayList<MovieDataClass>, private val context: Context):
    RecyclerView.Adapter<MoreLikeThisAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.morelikethis_design,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return moreLikeThisData.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = moreLikeThisData[position]
        Glide.with(context).load("https://img.youtube.com/vi/${item.videoId}/maxresdefault.jpg").placeholder(R.drawable.baseline_ondemand_video_24)
            .placeholder(R.drawable.baseline_play_circle_outline_24).into(holder.moviePoster)
        holder.recentlyAdded.text = item.title
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
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TitleDetailPage::class.java)
            intent.putExtra("selectedcategorytitle", Gson().toJson(item))
            context.startActivity(intent)
        }
    }
    class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview)
    {
        val moviePoster:ImageView = itemview.findViewById(R.id.morelikethismovieposter)
        val topTen:TextView = itemview.findViewById(R.id.morelikethistoptenlabel)
        val recentlyAdded:TextView = itemview.findViewById(R.id.morelikethisrecentlyaddedtext)
    }
}