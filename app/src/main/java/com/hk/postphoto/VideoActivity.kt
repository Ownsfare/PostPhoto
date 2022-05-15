package com.hk.postphoto

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hk.postphoto.VideoGallery.listOfVideos
import java.lang.Exception
import java.util.ArrayList

class VideoActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    private lateinit var mediaController: MediaController
    private lateinit var galleryVideo: VideoView
    private var galleryVideoAdapter: VideoAdapter? = null
    var videos: List<String>? = null
    companion object{
        lateinit var folderListVideo: ArrayList<Folder>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        folderListVideo = arrayListOf()
        getAllVideoFolders()

        val button: ImageView = findViewById(R.id.bottomBtn)
        val bottomSheetFragment = BottomSheetFragment(folderListVideo,'b')
        button.setOnClickListener{
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }
        mediaController = MediaController(this)
        recyclerView = findViewById(R.id.videoRv)
        galleryVideo = findViewById(R.id.galleryVideo)
        mediaController.setAnchorView(galleryVideo)
        mediaController.setMediaPlayer(galleryVideo)

        loadVideos()
    }

    private fun loadVideos() {
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = GridLayoutManager(this, 4)
        videos = listOfVideos(this)
        galleryVideoAdapter = VideoAdapter(this,videos as ArrayList<String>){
            val uri: Uri = Uri.parse(it)
            galleryVideo.setMediaController(mediaController)
            galleryVideo.setVideoURI(uri)
            galleryVideo.start()
        }
        recyclerView!!.adapter =galleryVideoAdapter
    }

    @SuppressLint("Range")
    private fun getAllVideoFolders(){
        val tempFolderList = ArrayList<String>()
        val projection = arrayOf(MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.BUCKET_ID)
        val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,null,null,
        MediaStore.Video.Media.DATE_ADDED + " DESC")
        if(cursor != null)
            if(cursor.moveToNext())
                do {
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))

                    try {
                        if(!tempFolderList.contains(folderC)){
                            tempFolderList.add(folderC)
                            folderListVideo.add(Folder(folderIdC,folderC))
                        }
                    }catch (e: Exception){}
                }while (cursor.moveToNext())
                cursor?.close()
    }
}