package com.hk.postphoto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VideoAdapter(
    private val context: Context,
    private val videos: List<String>,
    val listener: (String) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoAdapter.VideoViewHolder, position: Int) {
        val video = videos[position]
        Glide.with(context).load(video).into(holder.image)
        holder.itemView.setOnClickListener {
            if(holder.check.visibility == View.VISIBLE){
                holder.check.visibility = View.INVISIBLE
            }else{
                holder.check.visibility = View.VISIBLE
            }
            listener(video)
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var check: ImageView = itemView.findViewById(R.id.check)
    }
}