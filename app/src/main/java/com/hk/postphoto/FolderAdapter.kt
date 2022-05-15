package com.hk.postphoto

import android.content.Context
import android.media.Image
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class FolderAdapter(
    private val context: Context,
    private val folders: List<Folder>,
    val listener : (String,String) -> Unit
) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folders[position].folderName
        holder.folder.text = folder
        holder.itemView.setOnClickListener {
            listener(folders[position].id,folder)
        }
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folder: TextView = itemView.findViewById(R.id.folderTv)
    }
}