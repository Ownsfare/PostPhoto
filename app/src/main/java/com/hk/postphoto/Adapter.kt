package com.hk.postphoto

import android.content.Context
import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide


class Adapter(
    private val context: Context,
    private val images: List<String>,
//    private var photoListener: PhotoListener
    val listener: (String) -> Unit

) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        Glide.with(context).load(image).into(holder.image)
        holder.itemView.setOnClickListener {
            if(holder.check.visibility == View.VISIBLE){
                holder.check.visibility = View.INVISIBLE
            }else{
                holder.check.visibility = View.VISIBLE
            }
            listener(image)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var check: ImageView = itemView.findViewById(R.id.check)
    }
}