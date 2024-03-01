package com.demo.netflix.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.netflix.OpeningToMovieOrShow
import com.demo.netflix.R
import com.demo.netflix.modelclasses.MovieListData

class MovieListAdapter(private val movieList:ArrayList<MovieListData>, private val context: Context):
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movielist_design,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return movieList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = movieList[position]
        holder.moviePoster.setImageResource(item.moviePoster!!)
        holder.topTen.text = item.topTen.replace(" ","\n")
        holder.recentlyAdded.text = item.recentlyAdded
        if (!item.topTen.isEmpty())
        {
            holder.topTen.visibility = View.VISIBLE
        }
        else
        {
            holder.topTen.visibility = View.GONE
        }
        if (!item.recentlyAdded.isEmpty())
        {
            holder.recentlyAdded.visibility = View.VISIBLE
        }
        else
        {
            holder.recentlyAdded.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, OpeningToMovieOrShow::class.java)
            context.startActivity(intent)
        }
    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val moviePoster:ImageView = itemView.findViewById(R.id.movieposter)
        val topTen:TextView = itemView.findViewById(R.id.toptenlabel)
        val recentlyAdded:TextView = itemView.findViewById(R.id.fragmentmovietitle)
    }
}